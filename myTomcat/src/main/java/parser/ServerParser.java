package parser;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.net.URISyntaxException;

/**
 * 解析server.xml配置文件
 *
 */
public class ServerParser {
	/**
	 * 获取服务器端口号
	 * @return int port
	 */
	public static int getPort(){
		//默认
		int port = 8080;
		try {
			SAXReader saxReader = new SAXReader();
			//读取xml为document对象树
			//因为绝对路径中含有空格，特殊处理
			String basePath = ServerParser.class.getResource("/").toURI().getPath();
			Document document = saxReader.read(new File(basePath+"config/server.xml"));
			Element connectionEle = (Element)document.selectSingleNode("//connector");
			port = Integer.parseInt(connectionEle.attributeValue("port"));
		} catch (DocumentException | URISyntaxException e) {
			e.printStackTrace();
		}
		return port;
	}
}
