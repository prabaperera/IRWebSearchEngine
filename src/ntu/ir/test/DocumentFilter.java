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
	private static final String DOC_LOCATION = "/Users/praba/Documents/NTU/IR/project/keka/xaa.xml";
	
	private static final TreeMap<String, Integer> qIdMap = new TreeMap<String, Integer>();
	
	private static TreeMap<String, Integer> IdMap = new TreeMap<String, Integer>();
	
	public static void main(String[] args) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
		
		//Get Java related records from the large data set.
		
		Path newFile = Paths.get("./resource/documents/javaQandA.xml");
		
		try(Scanner sc = new Scanner(Paths.get(DOC_LOCATION)))
		{
			while(sc.hasNextLine())
			{
				String line =  sc.nextLine();
				line = line.trim();
				if(!line.startsWith("<row"))
				{
					continue;
				}
				DoumentUtil.RowData rowData = DoumentUtil.extractRowData(line);
				
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
		
		try(Scanner sc = new Scanner(Paths.get(DOC_LOCATION)))
		{
			while(sc.hasNextLine())
			{
				String line =  sc.nextLine();
				line = line.trim();
				if(!line.startsWith("<row"))
				{
					continue;
				}
				
				String rowId = DoumentUtil.extractRowId(line);
				if(IdMap.containsKey(rowId))
				{
					line += "\n";
					Files.write(newFile, line.getBytes(), StandardOpenOption.APPEND);
				}
			}
		}
		
	}
}
