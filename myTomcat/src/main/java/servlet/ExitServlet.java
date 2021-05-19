package servlet;

import core.RequestObject;
import core.ResponseObject;

import java.io.PrintWriter;

public class ExitServlet implements Servlet{

	@Override
	public void service(RequestObject request,ResponseObject response) {
		System.out.println("exit");
	}
}
