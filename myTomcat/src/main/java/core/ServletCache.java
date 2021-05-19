package core;

import servlet.Servlet;

import java.util.HashMap;
import java.util.Map;

/**
 * Servlet对象缓存池
 */
public class ServletCache {
	private static Map<String, Servlet> servletMap = new HashMap<>();

	public static void put(String className, Servlet servlet){
		servletMap.put(className,servlet);
	}

	public static Servlet get(String className){
		return servletMap.get(className);
	}

	public static boolean containsServlet(String className){
		return servletMap.containsKey(className);
	}
}
