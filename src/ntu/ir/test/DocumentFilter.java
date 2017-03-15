package ntu.ir.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

public class DocumentFilter 
{
	
	private static final TreeMap<String, Integer> qIdMap = new TreeMap<String, Integer>();
	
	private static TreeMap<String, Integer> IdMap = new TreeMap<String, Integer>();
	
	public static void main(String[] args) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
		
		//Get Java related records from the large data set.
		
		Path newFile = Paths.get(ConfigLoader.getConfig(ConfigLoader.DOC_LOCATION) , "javaQandA.xml");
		
		try(Scanner sc = new Scanner(Paths.get(ConfigLoader.getConfig(ConfigLoader.ORIGINAL_DOC_LOCATION))))
		{
			while(sc.hasNextLine())
			{
				String line =  sc.nextLine();
				line = line.trim();
				if(!line.startsWith("<row"))
				{
					continue;
				}
				DoumentUtil.RowData rowData = null;
				try
				{
					rowData = DoumentUtil.extractRowData(line);
				}
				catch(XPathExpressionException| ParserConfigurationException| SAXException e1)
				{
					System.out.println( e1);
					continue;
				}
				
				if(!rowData.isAnAnswer())
				{
					if(rowData.isJavaRelated())
					{
						qIdMap.put(rowData.id, 0);
						IdMap.put(rowData.id , 1);
					}
				}
				else
				{
					if(!qIdMap.containsKey(rowData.parentId))
					{
						continue;
					}
					IdMap.put(rowData.id , 1);
				}
			}
		}
		
		//Write Data to File
		
		try(Scanner sc = new Scanner(Paths.get(ConfigLoader.getConfig(ConfigLoader.ORIGINAL_DOC_LOCATION))))
		{
			while(sc.hasNextLine())
			{
				String line =  sc.nextLine();
				line = line.trim();
				if(!line.startsWith("<row"))
				{
					continue;
				}
				String rowId = null;
				try
				{
					rowId = DoumentUtil.extractRowId(line);
				}
				catch(XPathExpressionException| ParserConfigurationException| SAXException e1)
				{
					System.out.println( e1);
					continue;
				}
				if(IdMap.containsKey(rowId))
				{
					line += "\n";
					Files.write(newFile, line.getBytes(), StandardOpenOption.APPEND);
				}
			}
		}
		
	}
}
