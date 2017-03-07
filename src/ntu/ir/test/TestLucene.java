package ntu.ir.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import ntu.ir.test.DoumentUtil.RowData;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Field;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TestLucene 
{
	private static final String DOC_LOCATION = "/Users/praba/Documents/workspace/Algo/IRSearchEngine/resource/documents/java";
	private static final String INDEX_LOCATION = "/Users/praba/Documents/workspace/Algo/IRSearchEngine/resource/index/lucene.index";
	
	public static void buildIndex() throws IOException, XPathExpressionException, ParserConfigurationException, SAXException
	{
		//Build relation Map
		Map<String, LinkedList<RelationDetail>> rowRelationMap = buildRelationMap();
		
		JavaCodeAnalyzer analyzer = new JavaCodeAnalyzer();
		//Directory index = new RAMDirectory();
		FileUtils.cleanDirectory(Paths.get(INDEX_LOCATION).toFile()); 
		
		FSDirectory index = FSDirectory.open(Paths.get(INDEX_LOCATION));
		
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		
		System.out.println("Start building Indexes");
		//try(IndexWriter w = new IndexWriter(index, config); Stream<Path> files = Files.list(Paths.get("./resource/documents"));)
		try(IndexWriter w = new IndexWriter(index, config);)
		{
			Iterator<String> rdKeyIterator = rowRelationMap.keySet().iterator();
			
			while(rdKeyIterator.hasNext())
			{
				String key = rdKeyIterator.next();
				LinkedList<RelationDetail> rdList = rowRelationMap.get(key);
				
				List<RowDetail> rowDetails = extractRowDetail(rdList);
				
				addDocToIndex(w, rowDetails);
			}
		}
		
		System.out.println("Index generation completed.");
	}
	
	public static List<SearchResult> search(String searchQuery , int hitsPerPage) throws IOException, ParseException
	{
		JavaCodeAnalyzer analyzer = new JavaCodeAnalyzer();
		FSDirectory index = FSDirectory.open(Paths.get(INDEX_LOCATION));
		
		// 2. query
        String querystr = searchQuery;

        // the "title" arg specifies the default field to use
        // when no field is explicitly specified in the query.
        
        MultiFieldQueryParser  queryParser = new MultiFieldQueryParser(
                new String[] {"body", "title"},
                analyzer);
        Query q = queryParser.parse(querystr);
        
        // 3. search
        
        List<SearchResult> resultDocIds = new ArrayList<SearchResult>(hitsPerPage);
        
        try(IndexReader reader = DirectoryReader.open(index);)
        {
	        IndexSearcher searcher = new IndexSearcher(reader);
	        
	        TopDocs docs = searcher.search(q, hitsPerPage);
	        ScoreDoc[] hits = docs.scoreDocs;
	
	        // 4. display results
	        System.out.println("Found " + hits.length + " hits.");
	        for(int i=0;i<hits.length;++i) {
	            int docId = hits[i].doc;
	            Document d = searcher.doc(docId);
	            String title = d.get("title");
	            String documentId = d.get("root");
	            String titleUrl = "<a href=\"/SearchEngineServlet?requestType=\"document\"&documentId="+documentId+"\">"+title+"</a>";
	            resultDocIds.add(new SearchResult(documentId, titleUrl));
	            System.out.println((i + 1) + ". " + documentId +" "+title);
	        }
	        System.out.println("-----------------------------------------\n");
	        // reader can only be closed when there
	        // is no need to access the documents any more.
        }
		
		return resultDocIds;
	}
	
//	public static void main(String[] args) throws IOException, ParseException, XPathExpressionException, ParserConfigurationException, SAXException {
//		
//		buildIndex();
//		
//		JavaCodeAnalyzer analyzer = new JavaCodeAnalyzer();
//		FSDirectory index = FSDirectory.open(Paths.get(INDEX_LOCATION));
//		
//		try(Scanner sc = new Scanner(System.in))
//		{
//			while(true)
//			{
//				String keywords = sc.nextLine();
//				
//				if(keywords == null || keywords.isEmpty())
//				{
//					continue;
//				}
//				
//				if("exit".equals(keywords))
//				{
//					break;
//				}
//
//				// 2. query
//		        String querystr = keywords;
//
//		        // the "title" arg specifies the default field to use
//		        // when no field is explicitly specified in the query.
//		        
//		        MultiFieldQueryParser  queryParser = new MultiFieldQueryParser(
//		                new String[] {"body", "title"},
//		                analyzer);
//		        Query q = queryParser.parse(querystr);
//		        
//		        // 3. search
//		        int hitsPerPage = 10;
//		        try(IndexReader reader = DirectoryReader.open(index);)
//		        {
//			        IndexSearcher searcher = new IndexSearcher(reader);
//			        
//			        TopDocs docs = searcher.search(q, hitsPerPage);
//			        ScoreDoc[] hits = docs.scoreDocs;
//			
//			        // 4. display results
//			        System.out.println("Found " + hits.length + " hits.");
//			        for(int i=0;i<hits.length;++i) {
//			            int docId = hits[i].doc;
//			            Document d = searcher.doc(docId);
//			            System.out.println((i + 1) + ". " + d.get("root") +" "+d.get("title"));
//			        }
//			        System.out.println("-----------------------------------------\n");
//			        // reader can only be closed when there
//			        // is no need to access the documents any more.
//		        }
//			}
//		}
//		
//		System.out.println("Search engine terminated successfully..");
//	}

	private static List<RowDetail> extractRowDetail(LinkedList<RelationDetail> rdList) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException 
	{
		Iterator<RelationDetail> rIterator = rdList.iterator();
		List<RowDetail> rowdetailList = new ArrayList<TestLucene.RowDetail>(rdList.size());
		while(rIterator.hasNext())
		{
			RelationDetail rDetail = rIterator.next();
			String row = extractRowData(rDetail.file , rDetail.rowNumber);
			String[] data =  DoumentUtil.getTitleAndBody(row);
			RowDetail rData = new RowDetail(data[0] , data[1],data[2]);
			rowdetailList.add(rData);
		}
		return rowdetailList;
	}

	private static String extractRowData(Path file, int rowNumber) throws IOException 
	{
		try (Stream<String> lines = Files.lines(file , Charset.forName("utf-8"))) {
		    String line = lines.skip(rowNumber-1).findFirst().get();
		    return line;
		}
	}

	private static Map<String, LinkedList<RelationDetail>> buildRelationMap() throws XPathExpressionException,
			ParserConfigurationException, SAXException, IOException {
		
		Map<String, LinkedList<RelationDetail>> rowRelationMap = new TreeMap<String, LinkedList<RelationDetail>>();
		
		try(Stream<Path> files = Files.list(Paths.get(DOC_LOCATION));)
		{
			Iterator<Path> pi = files.iterator();
			
			while(pi.hasNext())
			{
				Path file = pi.next();
				int rowNumber = 0;
				try(Scanner sc = new Scanner(file))
				{
					while(sc.hasNextLine())
					{
						String  xml = sc.nextLine();
						rowNumber++;
						xml = xml.trim();
						if(!xml.startsWith("<row"))
						{
							continue;
						}
						
						RowData rd = DoumentUtil.extractRowData(xml);
						
						if(!rd.isAnAnswer())
						{
							if(!rd.isJavaRelated())
							{
								continue;
							}
							LinkedList<RelationDetail> valList = new LinkedList<RelationDetail>();
							valList.add(new RelationDetail(file, rowNumber, rd.id));
							rowRelationMap.put(rd.id, valList);
						}
						else
						{
							if(!rowRelationMap.containsKey(rd.parentId))
							{
								continue;
							}
							LinkedList<RelationDetail> valList = rowRelationMap.get(rd.parentId);
							valList.add(new RelationDetail(file, rowNumber, rd.id));
						}
					}
				}
			}
		}
		return rowRelationMap;
	}
	
//	private static void addDoc(IndexWriter w, String title, String body ) throws IOException {
//		  Document doc = new Document();
//		  doc.add(new TextField("title", title, Field.Store.YES));
//		  doc.add(new TextField("body", body, Field.Store.YES));
//		  w.addDocument(doc);
//		}
	
	private static void addDocToIndex(IndexWriter w, List<RowDetail> rowDetails) throws IOException, ParserConfigurationException, SAXException {
		Document doc = new Document();
		  
		RowDetail rd = rowDetails.get(0);
		
		for(RowDetail rowd : rowDetails)
		{
			doc.add( new TextField("title", rowd.title, Field.Store.YES));
			doc.add( new TextField("body", rowd.body, Field.Store.YES));
		}
		doc.add(new StoredField("root", rd.id));
		w.addDocument(doc);
	}
	
	private static void addDocToIndex(IndexWriter w, String relationDocument) throws IOException, ParserConfigurationException, SAXException {
		Document doc = new Document();
		  
	  	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		org.w3c.dom.Document xmlDoc = builder.parse(new InputSource(new ByteArrayInputStream(relationDocument.getBytes("utf-8"))));
	  
		doc.add(new StoredField("completeDoc", relationDocument));
		
		String[] tags = {"title","body"};
		
		for(String tag : tags)
		{
			NodeList titles = xmlDoc.getElementsByTagName(tag);
			for(int cnt = 0 ; cnt < titles.getLength() ; cnt++)
			{
				String title = titles.item(cnt).getTextContent();
				doc.add( new TextField(tag, title, Field.Store.YES));
			}
		}
		w.addDocument(doc);
	}
	
//	private static String getCompactDocument(List<RowDetail> data)
//	{
//		StringBuilder xmlBuilder = new StringBuilder( );
//		Iterator<RowDetail> rowDataIterator = data.iterator();
//		
//		RowDetail rd = rowDataIterator.next();
//		
//		xmlBuilder.append("<parent>");
//		xmlBuilder.append("<title>").append(rd.getTitleAsString()).append("<\\title>");
//		xmlBuilder.append("<body>").append(rd.getBodyAsString()).append("<\\body>");
//		while(rowDataIterator.hasNext())
//		{
//			RowDetail rdc = rowDataIterator.next();
//			xmlBuilder.append("<child>")
//			.append("<title>").append(rdc.getTitleAsString()).append("<\\title>")
//			.append("<body>").append(rdc.getBodyAsString()).append("<\\body>")
//			.append("</child>");
//		}
//		xmlBuilder.append("</parent>");
//		return xmlBuilder.toString();
//	}
	
	private static class RelationDetail
	{
		String id;
		int rowNumber;
		Path file;
		
		public RelationDetail(Path file,int rowNumber,String id )
		{
			this.file = file;
			this.rowNumber = rowNumber;
			this.id = id;
		}
	}
	
	private static class RowDetail
	{
		String title;
		String body;
		String id;
		
		public String getTitleAsString()
		{
			return "\""+title+"\"";
		}
		
		public String getBodyAsString()
		{
			return "\""+body+"\"";
		}
		
		public RowDetail(String id, String title,String body)
		{
			this.id = id;
			this.title = title;
			this.body = body;
		}
	}
}
