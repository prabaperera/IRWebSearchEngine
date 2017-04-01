package ntu.ir.app.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLReader {

	
	public List<String[]> readTagData(String path){
		List<String[]> resultList=new ArrayList<String[]>();
		
		  try {
			    

				File fXmlFile = new File(path);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);

				//optional, but recommended
				//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();

				System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

				NodeList nList = doc.getElementsByTagName("row");

				System.out.println("----------------------------");

				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);

					//System.out.println("\nCurrent Element :" + nNode.getNodeName());

					if (nNode.getNodeType() == Node.ELEMENT_NODE && !"posts".equalsIgnoreCase(nNode.getNodeName())) {

					
					
					
					
					NamedNodeMap map= nNode.getAttributes();
					
					String [] resarray=null;
					
					if( map.getNamedItem("PostTypeId").getNodeValue().equalsIgnoreCase("1") && null!=map.getNamedItem("AcceptedAnswerId")){
					 resarray=new String [4];
					
					
					
					if(null!=map.getNamedItem("AcceptedAnswerId"))
					resarray [0]= map.getNamedItem("AcceptedAnswerId").getNodeValue();
					else
					resarray [0]= "null";
						
					if(null!=map.getNamedItem("Tags"))
					resarray [1]= map.getNamedItem("Tags").getNodeValue();
					else
					resarray [1]="null";
					
					if(null!=map.getNamedItem("AnswerCount"))
						resarray [2]= map.getNamedItem("AnswerCount").getNodeValue();
						else
						resarray [2]="0";
					if(null!=map.getNamedItem("Body"))
						resarray [3]= map.getNamedItem("Body").getNodeValue();
						else
						resarray [3]="null";
				//	System.out.println(resarray[0] +" - "+resarray[1]);
					
					resultList.add(resarray)	;	
					}
					}
				}
			    } catch (Exception e) {
				e.printStackTrace();
			    }
		return resultList;
	}
	
	public List<String[]> readAnswerData(String path){
		List<String[]> resultList=new ArrayList<String[]>();
		
		  try {
			    

				File fXmlFile = new File(path);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);

				//optional, but recommended
				//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();

				System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

				NodeList nList = doc.getElementsByTagName("row");

				//System.out.println("----------------------------");

				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);

				//	System.out.println("\nCurrent Element :" + nNode.getNodeName());

					if (nNode.getNodeType() == Node.ELEMENT_NODE && !"posts".equalsIgnoreCase(nNode.getNodeName())) {

					
					
					
					
					NamedNodeMap map= nNode.getAttributes();
					
					String [] resarray=null;
					
					if( map.getNamedItem("PostTypeId").getNodeValue().equalsIgnoreCase("2")){
					 resarray=new String [3];
					
					
					
					if(null!=map.getNamedItem("Id"))
					resarray [0]= map.getNamedItem("Id").getNodeValue();
					else
					resarray [0]= "";
						
					if(null!=map.getNamedItem("OwnerUserId"))
					resarray [1]= map.getNamedItem("OwnerUserId").getNodeValue();
					else
					resarray [1]="";
					
					//System.out.println(resarray[0] +" - "+resarray[1]);
					
					resultList.add(resarray)	;	
					}
					}
				}
			    } catch (Exception e) {
				e.printStackTrace();
			    }
		return resultList;
	}
	
}
