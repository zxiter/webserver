/**
 * 
 */
package webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Description:TODO
 * 
 * @author liufeihua
 * @date 2015年7月22日上午12:05:54
 * @version 1.0
 * 
 */
public class Processor extends Thread {

	private Socket socket;
	private InputStream in;
	private PrintStream out;

	public final static String WEB_ROOT = "F:\\html";

	public Processor(Socket socket) {
		this.socket = socket;
		try {
			in = socket.getInputStream();
			out = new PrintStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		String fileName = parse(in);
		sendFile(fileName);
	}

	public String parse(InputStream in) {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String fileName = null;
		try {
			String httpMessage = br.readLine();
			String[] content = httpMessage.split(" ");

			if (content.length != 3) {
				sendErrorMessage(400, "请求出错!");
				return null;
			}

			System.out.println("code:" + content[0] + "fileName:" + content[1]
					+ "http version:" + content[2]);

			fileName = content[1];
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}

	public void sendErrorMessage(int errorCode, String errorMessage) {
		out.println("HTTP/1.0 " + errorCode + " " + errorMessage);
		System.out.println("errorCode:" + errorCode + "errorMessage:"
				+ errorMessage);
		out.println("content-type: text/html");
		out.println();
		out.println("<html>");
		out.println("<title>Error Message");
		out.println("<title>");
		out.println("<body>");
		out.println("<h1>ErrorCode:" + errorCode + ",Message" + errorMessage
				+ "<h1>");
		out.println("</body>");
		out.println("</html>");
		out.flush();
		out.close();
	}

	public void sendFile(String fileName) {
		File file = new File(WEB_ROOT + fileName);
		if (!file.exists()) {
			sendErrorMessage(404, "请求文件不存在!");
			return;
		}

		try {
			InputStream inputStream = new FileInputStream(file);
			byte[] content = new byte[(int) file.length()];
			inputStream.read(content);

			out.println("HTTP/1.0 200 请求成功");
			out.println("content-lenght" + content.length);
			out.println();
			out.write(content);
			out.flush();
			out.close();

		} catch (FileNotFoundException e) {
			System.out.println("严重:文件不存在!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("严重:输出出错了!");
			e.printStackTrace();
		}
	}
}
