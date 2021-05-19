package core;

import java.io.PrintWriter;

/**
 * 封装响应对象
 */
public class ResponseObject {
	private PrintWriter out;

	public void setWriter(PrintWriter out){
		this.out = out;
	}

	public PrintWriter getWriter(){
		return out;
	}
}
