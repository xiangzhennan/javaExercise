import com.sun.org.apache.xml.internal.resolver.helpers.BootstrapResolver;
import core.RequestHandler;
import parser.ServerParser;
import parser.WebXMLParser;
import util.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 入口方法
 */
public class BootStrap {
	public static void main(String[] args) {
		new BootStrap().start();
	}

	private void start() {
		WebXMLParser.parseWebApp();
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		BufferedReader br = null;
		try {
			Logger.log("httpserver start");
			long start = System.currentTimeMillis();
			int port = ServerParser.getPort();
			Logger.log("httpserver port: "+ port);
			//创建套接字，绑定端口
			serverSocket = new ServerSocket(port);
			long end = System.currentTimeMillis();
			Logger.log("httpserver start in "+(end - start)+" ms");
			while (true){
				//接收客户端消息
				clientSocket = serverSocket.accept();
				new Thread(new RequestHandler(clientSocket)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			closeResource(serverSocket);
		}
	}

	private void closeResource(Closeable resource){
		if (resource != null){
			try {
				resource.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
