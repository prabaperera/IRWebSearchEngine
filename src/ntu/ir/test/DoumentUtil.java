package ntu.ir.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DoumentUtil {

	public static RowData extractRowData(String input2) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException 
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		org.w3c.dom.Document doc = builder.parse(new InputSource(new ByteArrayInputStream(input2.getBytes("utf-8"))));
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression idTag = xpath.compile("//row/@Id");
		XPathExpression postTypeTag = xpath.compile("//row/@PostTypeId");
		XPathExpression parentIdTag = xpath.compile("//row/@ParentId");
		XPathExpression tagsTag = xpath.compile("//row/@Tags");
		
		Object idObj = idTag.evaluate(doc, XPathConstants.STRING);
		Object postTypeObj = postTypeTag.evaluate(doc, XPathConstants.STRING);
		Object parentIdObj = parentIdTag.evaluate(doc, XPathConstants.STRING);
		Object tagsObj = tagsTag.evaluate(doc, XPathConstants.STRING);
		
		
		return new RowData((idObj != null ? idObj.toString() : null) ,
				(postTypeObj != null ? postTypeObj.toString() : null) , 
						(parentIdObj != null ? parentIdObj.toString() : null),
								(tagsObj != null ? tagsObj.toString() : null));
		
	}
	
	public static String extractRowId(String input2) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException 
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		org.w3c.dom.Document doc = builder.parse(new InputSource(new ByteArrayInputStream(input2.getBytes("utf-8"))));
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression idTag = xpath.compile("//row/@Id");
		
		Object idObj = idTag.evaluate(doc, XPathConstants.STRING);
		
		
		return (idObj != null ? idObj.toString() : null);
		
	}
	
	public static String[] getTitleAndBody(String input2) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException 
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		org.w3c.dom.Document doc = builder.parse(new InputSource(new ByteArrayInputStream(input2.getBytes("utf-8"))));
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		XPathExpression exprId = xpath.compile("//row/@Id");
		XPathExpression exprBody = xpath.compile("//row/@Body");
		XPathExpression exprTitle = xpath.compile("//row/@Title");
		
		Object vId = exprId.evaluate(doc, XPathConstants.STRING);
		Object vTitle = exprTitle.evaluate(doc, XPathConstants.STRING);
		Object vBody = exprBody.evaluate(doc, XPathConstants.STRING);
		
		String[] results = {vId.toString(), vTitle.toString() , vBody.toString()};
		return results;
	}
	
	static class RowData
	{
		String id;
		String postTypeId;
		String parentId;
		String tags;
		
		public RowData(String id,String postTypeId,String parentId,String tags)
		{
			this.id = id;
			this.postTypeId = postTypeId;
			this.parentId = parentId;
			this.tags = tags;
		}
		
		boolean isAnAnswer()
		{
			return "2".equals(postTypeId);
		}
		
		boolean isJavaRelated()
		{
			return tags != null && tags.contains("<java>");
		}
		
	}
}
