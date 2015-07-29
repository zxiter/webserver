/**
 * 
 */
package webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Description:TODO
 * 
 * @author liufeihua
 * @date 2015年7月22日上午12:05:26
 * @version 1.0
 * 
 */
public class WebServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 默认端口是80
		int port = 80;
		if (args.length == 1) {
			port = Integer.parseInt(args[1]);
		}

		new WebServer().serverStart(port);

	}

	public void serverStart(int port) {
		try {
			ServerSocket serverSocket = new ServerSocket(port);

			while (true) {
				Socket socket = serverSocket.accept();
				new Processor(socket).start();
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

}
