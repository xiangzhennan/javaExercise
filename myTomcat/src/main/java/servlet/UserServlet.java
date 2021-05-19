package servlet;

import core.RequestObject;
import core.ResponseObject;

import java.io.PrintWriter;

public class UserServlet implements Servlet{

	@Override
	public void service(RequestObject request, ResponseObject response) {
		String username = request.getParameterValue("username");
		String password = request.getParameterValue("gender");
		PrintWriter out = response.getWriter();
		String html = "HTTP/1.1 200 NotFound\n" +
				"Content-Type:text/html;charset=utf-8\n\n" +
				"<!DOCTYPE html>\n" +
				"<html lang=\"en\">\n" +
				"<head>\n" +
				"    <meta charset=\"UTF-8\">\n" +
				"    <title>Title</title>\n" +
				"</head>\n" +
				"<body>\n" +username+password+
				"登录验证中\n" +
				"</body>\n" +
				"</html>";
		out.write(html);
		out.flush();
	}
}
