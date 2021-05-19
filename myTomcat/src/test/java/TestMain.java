import core.RequestHandler;
import core.RequestObject;
import org.junit.Test;
import parser.WebXMLParser;
import util.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class TestMain {
	@Test
	public void testLogger(){
		Logger.log("test tester");
	}
	@Test
	public void filePathTest() throws URISyntaxException {
		String url = this.getClass().getClassLoader().getResource("").toURI().getPath();
		assert url != null;
		File file = new File(url);
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	@Test
	public void parseTest(){
		WebXMLParser.parseWebApp();
	}

	@Test
	public void requestTest(){
		RequestObject request = new RequestObject("http://localhost:63342/user/save?username=%E5%93%A5&gender=1&interest=sport&interest=food");
		String username = request.getParameterValue("username");
		assert username.equals("%E5%93%A5");
		List<String> interest = request.getParameterValueList("interest");
		assert interest.size()==2;

	}
}
