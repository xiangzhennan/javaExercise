package core;

import java.util.*;

/**
 * 封装请求
 */
public class RequestObject {
	private Map<String, List<String>> parameterMap = new HashMap<>();

	public Map<String, List<String>> getParameterMap() {
		return parameterMap;
	}

	public List<String> getParameterValueList(String key){
		return parameterMap.get(key);
	}
	public String getParameterValue(String key){
		List<String> values = parameterMap.get(key);
		if (values.size()==1){
			return values.get(0);
		}else {
			return null;
		}
	}

	public RequestObject(String requestURI){
		//请求可能为无参，多参，某个参数为多值
		if (requestURI.contains("?")){
			String[] uriAndData = requestURI.split("[?]");
			//是否为多参数
			if (uriAndData.length>1){
				String data = uriAndData[1];
				if (data.contains("&")){
					String[] dataArray = data.split("&");
					for (String parameterAndValue : dataArray) {
						String[] split  = parameterAndValue.split("=");
						if (parameterMap.containsKey(split[0])){
							parameterMap.get(split[0]).add(split[1]);
						}else {
							ArrayList<String> valueList = new ArrayList<>();
							valueList.add(split[1]);
							parameterMap.put(split[0],valueList);
						}
					}
				}else {
					String[] nameAndValue = data.split("=");
					ArrayList<String> value = new ArrayList<>();
					if (nameAndValue.length>1){
						value.add(nameAndValue[1]);
						parameterMap.put(nameAndValue[0],value);
					}else {
						value.add("");
						parameterMap.put(nameAndValue[0],value);
					}
				}
			}
		}
	}
}
