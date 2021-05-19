package servlet;

import core.RequestObject;
import core.ResponseObject;

import java.io.PrintWriter;

/**
 * 每一个servlet默认是单例存在于容器中，
 * 所以涉及到线程安全问题
 */
public interface Servlet {
	public void service(RequestObject request,ResponseObject response);
}
