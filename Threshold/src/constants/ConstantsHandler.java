//$Id$
package constants;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConstantsHandler {

	public static String getValue() throws Exception {
		String location = "/home/local/ZOHOCORP/gokul-13991/MyData/DevelopMent/ThresholdManagement/Threshold/WebContent/WEB-INF/conf";
		File file = new File(location+"/"+"constants.xml");  
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
		DocumentBuilder db = dbf.newDocumentBuilder();  
		Document doc = db.parse(file);  
		doc.getDocumentElement().normalize();  
		NodeList nodeList = doc.getElementsByTagName("leakybucket");  
		Node node = nodeList.item(0);
		Element eElement = (Element) node;  
		String reqLimit = eElement.getElementsByTagName("request-limit").item(0).getTextContent();  
		String timeInterval = eElement.getElementsByTagName("time-lap").item(0).getTextContent(); 
		return reqLimit;
	}

}
