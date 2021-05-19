package util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义日志工具
 */
public class Logger {

	private Logger(){}

	public static void log(String msg){
		System.out.println("[INFO]" + DateUtil.getCurrentTime() + " " + msg);
	}
}
