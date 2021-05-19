package core;

import parser.WebXMLParser;
import servlet.LoginServlet;
import servlet.Servlet;
import sun.rmi.runtime.Log;
import util.Logger;

import javax.xml.bind.helpers.AbstractUnmarshallerImpl;
import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * 处理客户端请求
 *
 */
public class RequestHandler implements Runnable{
	public Socket clientSocket;
	public RequestHandler(Socket clientSocket){
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		BufferedReader br =null;
		PrintWriter out = null;
		Logger.log("httpserver thread: "+ Thread.currentThread().getName());
		try {
			//接收客户端消息
			br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String requestLine = br.readLine();
			Logger.log("request received: "+requestLine);
			out = new PrintWriter(clientSocket.getOutputStream());
			//获取资源定位符
			String requestURI = requestLine.split(" ")[1];
			//判断是否为静态页面
			if (requestURI.endsWith(".html")||requestURI.endsWith("htm")){
				respondStaticPage(requestURI,out);
			}else{
				//动态请求，寻找对应的servlet
				//提取应用名
				String webAppName= requestURI.split("[/]")[1];
				Map<String, String> servletMap = WebXMLParser.servletMaps.get(webAppName);
				//根据url取对应的servlet
				String urlPatternWithParam = requestURI.substring(1+webAppName.length());
				String urlPattern = urlPatternWithParam.split("[?]")[0];
				String servletClassName = servletMap.get(urlPattern);
				Logger.log(servletClassName);
				if (servletClassName!=null){
					//封装响应对象
					ResponseObject responseObject = new ResponseObject();
					responseObject.setWriter(out);
					Servlet servlet;
					//去容器中查找，如没有则创建
					if (ServletCache.containsServlet(servletClassName)){
						servlet = ServletCache.get(servletClassName);
					}else {
						Class<?> c = Class.forName(servletClassName);
						Object obj = c.newInstance();
						servlet = (Servlet) obj;
						ServletCache.put(servletClassName,servlet);
					}
					servlet.service(new RequestObject(urlPatternWithParam),responseObject);
				}else {
					respondErrorPage(out);
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		} finally {
			closeResource(out);
			closeResource(br);
			closeResource(clientSocket);
		}
	}

	private void respondErrorPage(PrintWriter out) {
		out.print(getErrorPageAsString());
		out.flush();
	}

	/**
	 * 返回静态资源
	 * @param requestURI
	 * @param out
	 */
	private void respondStaticPage(String requestURI, PrintWriter out) {
		String htmlPath = requestURI.substring(1);
		BufferedReader br = null;
		try {
			//因为绝对路径中含有空格，特殊处理
			String basePath = this.getClass().getResource("/").toURI().getPath();
			//读取页面
			System.out.println(basePath);
			br = new BufferedReader(new FileReader(basePath+htmlPath));
			StringBuilder html = new StringBuilder();
			//响应体
			html.append("HTTP/1.1 200 OK\n");
			html.append("Content-Type:text/html;charset=utf-8\n\n");
			String temp = null;
			while ((temp = br.readLine())!= null){
				html.append(temp);
			}
			out.print(html);
			out.flush();
		} catch (FileNotFoundException e) {
			//404,找不到资源
			out.print(getErrorPageAsString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private String getErrorPageAsString() {
		//404,找不到资源
		String html = "HTTP/1.1 200 NotFound\n" +
				"Content-Type:text/html;charset=utf-8\n\n" +
				"<!DOCTYPE html>\n" +
				"<html lang=\"en\">\n" +
				"<head>\n" +
				"    <meta charset=\"UTF-8\">\n" +
				"    <title>Title</title>\n" +
				"</head>\n" +
				"<body>\n" +
				"404\n" +
				"</body>\n" +
				"</html>";
		return html;
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
