package parser;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析web。xml配置文件
 */
public class WebXMLParser {

	public static Map<String,Map<String,String>> servletMaps = new HashMap<>();

	/**
	 * 解析所有webapp目录下的项目
	 */
	public static void parseWebApp() {
		try {
			//因为绝对路径中含有空格，特殊处理
			String basePath = WebXMLParser.class.getResource("/").toURI().getPath() + "webapp/";
			File webapp = new File(basePath);
			if (webapp.exists()&&webapp.isDirectory()){
				File[] files = webapp.listFiles();
				for (File webApp : files) {
				    servletMaps.put(webApp.getName(),parseSingleApp(basePath+webApp.getName()));
				}
			}
		} catch (URISyntaxException | DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param webappName
	 * @return
	 */
	private static Map<String,String> parseSingleApp(String webappName) throws URISyntaxException, DocumentException {
		//获取xml路径
		String webPath = webappName + "/WEB-INF/web.xml";
		//解析
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(new File(webPath));
		//获取servlet节点元素
		List<Element> servletNodes = document.selectNodes("/web-app/servlet");
		//servlet名和servlet class名 map
		HashMap<String, String> servletInfoMap = new HashMap<>();
		for (Element servletNode : servletNodes) {
		    //获取servlet-name节点元素
			Element servletNameEle = (Element) servletNode.selectSingleNode("servlet-name");
			String servletName = servletNameEle.getStringValue();
			//获取servlet-class节点元素
			Element servletClassNameEle = (Element) servletNode.selectSingleNode("servlet-class");
			String servletClassName = servletClassNameEle.getStringValue();
			servletInfoMap.put(servletName,servletClassName);
		}
		//获取servlet-mapping节点元素
		List<Element> servletMappingNodes = document.selectNodes("/web-app/servlet-mapping");
		//servlet名和servlet url pattern名 map
		HashMap<String, String> servletMappingInfoMap = new HashMap<>();
		for (Element servletMappingNode : servletMappingNodes) {
			//获取servlet-name节点元素
			Element servletNameEle = (Element) servletMappingNode.selectSingleNode("servlet-name");
			String servletName = servletNameEle.getStringValue();
			//获取url-pattern节点元素
			Element servletUrlEle = (Element) servletMappingNode.selectSingleNode("url-pattern");
			String servletUrl = servletUrlEle.getStringValue();
			servletMappingInfoMap.put(servletName,servletUrl);
		}
		//合成url和servlet class的map
		HashMap<String, String> urlServletClassNameMap = new HashMap<>();
		for (String servletName : servletInfoMap.keySet()) {
		    urlServletClassNameMap.put(servletMappingInfoMap.get(servletName),servletInfoMap.get(servletName));
		}
		return urlServletClassNameMap;
	}
}
