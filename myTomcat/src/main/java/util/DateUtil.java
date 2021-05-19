package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	private DateUtil(){}

	/**
	 * 获取当前时间
	 * @return msg
	 */
	public static String getCurrentTime(){
		return dateFormat.format(new Date());
	}
}
