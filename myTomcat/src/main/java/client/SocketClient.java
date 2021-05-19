package client;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 实际中此为浏览器，仅测试用
 */
public class SocketClient {

	public static void main(String[] args) {
		Socket clientSocket = null;
		PrintWriter out = null;
		try {
			clientSocket = new Socket("localhost",8080);

			out = new PrintWriter(clientSocket.getOutputStream());
			String msg = "hello world";
			out.print(msg);
		}catch (Exception e ){
			e.printStackTrace();
		}finally {
			closeResource(out);
			closeResource(clientSocket);
		}
	}
	private static void closeResource(Closeable resource){
		if (resource != null){
			try {
				resource.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
