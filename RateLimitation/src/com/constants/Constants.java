//$Id$
package com.constants;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Constants {

	public enum constants{
		LOGIN("login"),
		WELCOME("welcome");

		private String value = null;
		private constants(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}

	public enum tags{
		SECONDCOUNT("second-count"),
		MINUTECOUNT("minute-count");

		private String value = null;
		private tags(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
	}

	private static int  secondCount = 0;
	private static int  minuteCount = 0;
	private static Map<String, HashMap<String,Integer>> constants = new HashMap<>();

	public HashMap<String, Integer> getConstants(String url){
		return constants.get(url);
	}

	static{
		String location = "/home/local/ZOHOCORP/gokul-13991/MyData/DevelopMent/Ithu oru Project/WebContent/WEB-INF/conf";
		File file = new File(location+"/"+"constants.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		HashMap<String, Integer> xmlvalues;
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();  
			Document doc = db.parse(file);  
			doc.getDocumentElement().normalize();  
			NodeList nodeList = doc.getElementsByTagName("url-based-constants"); 
			for(int i=0; i<nodeList.getLength(); i++) {
				xmlvalues = new HashMap<>();
				Node node = nodeList.item(i);
				String nodeName;
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;  
					//getting child node name
					NodeList childNodes = node.getChildNodes();
					Node childNode = childNodes.item(1);
					nodeName = childNode.getNodeName();
					//iterating parant node 
					secondCount =  Integer.valueOf(element.getElementsByTagName(tags.SECONDCOUNT.getValue()).item(0).getTextContent());
					minuteCount = Integer.valueOf(element.getElementsByTagName(tags.MINUTECOUNT.getValue()).item(0).getTextContent());  
					xmlvalues.put(tags.SECONDCOUNT.getValue(), secondCount);
					xmlvalues.put(tags.MINUTECOUNT.getValue(), minuteCount);
					if(!nodeName.isEmpty()) {
						constants.put(nodeName, xmlvalues);
					}
				}
			}			 
		}catch(Exception ex) {
			System.out.println("Exception while parshing XML"+ex);
		}
	}
}