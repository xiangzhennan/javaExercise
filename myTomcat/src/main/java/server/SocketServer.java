package server;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class SocketServer {
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		BufferedReader br = null;
		try {
			//创建套接字，绑定端口
			serverSocket = new ServerSocket(8080);
			//接收客户端消息
			clientSocket = serverSocket.accept();
			br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String temp = null;
			while ((temp = br.readLine())!=null){
				System.out.println(temp);
			}
		}catch (Exception e ){
			e.printStackTrace();
		}finally {
			closeResource(br);
			closeResource(clientSocket);
			closeResource(serverSocket);
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
