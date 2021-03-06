package ntu.ir.test;

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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import ntu.ir.HtmlResponseBuilder;
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
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.xml.sax.SAXException;

public class TestLucene 
{
	
	public static void buildIndex() throws IOException, XPathExpressionException, ParserConfigurationException, SAXException
	{
		//Build relation Map
		Map<String, LinkedList<RelationDetail>> rowRelationMap = buildRelationMap();
		
		JavaCodeAnalyzer analyzer = new JavaCodeAnalyzer();
		//Directory index = new RAMDirectory();
		FileUtils.cleanDirectory(Paths.get(ConfigLoader.getConfig(ConfigLoader.INDEX_LOCATION_OF_MERGED_DOCUMENT)).toFile()); 
		
		FSDirectory index = FSDirectory.open(Paths.get(ConfigLoader.getConfig(ConfigLoader.INDEX_LOCATION_OF_MERGED_DOCUMENT)));
		
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		
		System.out.println("Start building Indexes");
		DocumentIndexer docIndexer = DocumentIndexerFactory.getDocumentIndexer(DocumentIndexerFactory.COMPOUND_INDEX);
		//try(IndexWriter w = new IndexWriter(index, config); Stream<Path> files = Files.list(Paths.get("./resource/documents"));)
		try(IndexWriter w = new IndexWriter(index, config);)
		{
			Iterator<String> rdKeyIterator = rowRelationMap.keySet().iterator();
			int docs = 0;
			while(rdKeyIterator.hasNext())
			{
				String key = rdKeyIterator.next();
				LinkedList<RelationDetail> rdList = rowRelationMap.get(key);
				
				List<RowDetail> rowDetails = extractRowDetail(rdList);
				docIndexer.addCompoundDocToIndex(w, rowDetails);
				System.out.println(docs++);
			}
		}
		
		System.out.println("Index generation completed.");
	}
	
	public static void buildDocumentIndex() throws IOException, XPathExpressionException, ParserConfigurationException, SAXException
	{
		
		JavaCodeAnalyzer analyzer = new JavaCodeAnalyzer();
		//Directory index = new RAMDirectory();
		FileUtils.cleanDirectory(Paths.get(ConfigLoader.getConfig(ConfigLoader.INDEX_LOCATION_OF_INDIVIDUAL_DOCUMENT)).toFile()); 
		
		FSDirectory index = FSDirectory.open(Paths.get(ConfigLoader.getConfig(ConfigLoader.INDEX_LOCATION_OF_INDIVIDUAL_DOCUMENT)));
		
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		
		System.out.println("Start building individual file indexes");
		//try(IndexWriter w = new IndexWriter(index, config); Stream<Path> files = Files.list(Paths.get("./resource/documents"));)
		try(IndexWriter w = new IndexWriter(index, config);)
		{
			int row = 0;
			try(Stream<Path> files = Files.list(Paths.get(ConfigLoader.getConfig(ConfigLoader.DOC_LOCATION)));)
			{
				Iterator<Path> pi = files.iterator();
				
				while(pi.hasNext())
				{
					Path file = pi.next();
					try(Scanner sc = new Scanner(file))
					{
						while(sc.hasNextLine())
						{
							System.out.println(row++);
							String  xml = sc.nextLine();
							try
							{
								RowData rd =  DoumentUtil.extractRowData(xml);
								addIndividualDocToIndex(w ,xml , rd.id , rd.parentId);
							}
							catch(Throwable e)
							{
								System.out.println(xml);
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		
		System.out.println("Index generation completed.");
	}
	
	public static List<String[]> findDocumentById(String documentId) throws IOException, ParseException, XPathExpressionException, ParserConfigurationException, SAXException
	{
		JavaCodeAnalyzer analyzer = new JavaCodeAnalyzer();
		FSDirectory index = FSDirectory.open(Paths.get(ConfigLoader.getConfig(ConfigLoader.INDEX_LOCATION_OF_INDIVIDUAL_DOCUMENT)));
		
		// 2. query
        String querystr = documentId;

        // the "title" arg specifies the default field to use
        // when no field is explicitly specified in the query.
        
        MultiFieldQueryParser  queryParser = new MultiFieldQueryParser(
                new String[] {"id", "parentId"},
                analyzer);
        Query q = queryParser.parse(querystr);
        
        // 3. search
        
        List<String[]> resultDocs = new ArrayList<String[]>();
        
        try(IndexReader reader = DirectoryReader.open(index);)
        {
	        IndexSearcher searcher = new IndexSearcher(reader);
	        
	        TopDocs docs = searcher.search(q , 20);
	        ScoreDoc[] hits = docs.scoreDocs;
	
	        // 4. display results
	        System.out.println("Found " + hits.length + " hits.");
	        for(int i=0;i<hits.length;++i) {
	            int docId = hits[i].doc;
	            Document d = searcher.doc(docId);
	            String document = d.get("document");
	            String[] details = DoumentUtil.getTitleAndBody(document);
	            resultDocs.add(details);
	        }
	        System.out.println("-----------------------------------------\n");
	        // reader can only be closed when there
	        // is no need to access the documents any more.
        }
		
		return resultDocs;
	}
	
	public static List<SearchResult> excecuteQuery(String searchQuery , int hitsPerPage , String searchIn) throws IOException, ParseException
	{
		String [] searchInArray = searchIn.split(",");
		JavaCodeAnalyzer analyzer = new JavaCodeAnalyzer();
		FSDirectory index = FSDirectory.open(Paths.get(ConfigLoader.getConfig(ConfigLoader.INDEX_LOCATION_OF_MERGED_DOCUMENT)));
		
		// 2. query
        String querystr = searchQuery;

        // the "title" arg specifies the default field to use
        // when no field is explicitly specified in the query.
        
        MultiFieldQueryParser  queryParser = new MultiFieldQueryParser(
        		searchInArray,
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
	        	ScoreDoc doc = hits[i];
	            int docId = doc.doc;
	            Document d = searcher.doc(docId);
	            String title = d.get("title");
	            String documentId = d.get("root");
	            String titleUrl = HtmlResponseBuilder.getDoumentAccessURL(documentId , title);
	            resultDocIds.add(new SearchResult(documentId, titleUrl , i+1, doc.score));
	            System.out.println((i + 1) + ". " + documentId +" "+title);
	        }
	        System.out.println("-----------------------------------------\n");
	        // reader can only be closed when there
	        // is no need to access the documents any more.
        }
		
		return resultDocIds;
	}
	
	/**
	 * Advance search
	 * @param hitsPerPage
	 * @param queryMap
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public static List<SearchResult> excecuteQuery(int hitsPerPage , Map<String, String> queryMap) 
			throws IOException, ParseException{
		String[] searchInArray = new String[queryMap.keySet().size()];
		
		int cnt = 0;
		for(String key : queryMap.keySet()){
			searchInArray[cnt++] = key;
		}
		
		JavaCodeAnalyzer analyzer = new JavaCodeAnalyzer();
		FSDirectory index = FSDirectory.open(Paths.get(ConfigLoader.
				getConfig(ConfigLoader.INDEX_LOCATION_OF_MERGED_DOCUMENT)));
		
		BooleanQuery.Builder bQueryBuilder = new BooleanQuery.Builder();
        for(String searchIn : searchInArray){
            //create the term query object
            Query q = MultiFieldQueryParser.parse(
            		queryMap.get(searchIn) , new String[]{searchIn} ,new Occur[]{Occur.MUST}, analyzer);
            bQueryBuilder.add(q , Occur.MUST);
        }
        List<SearchResult> resultDocIds = new ArrayList<SearchResult>(hitsPerPage);
        
        try(IndexReader reader = DirectoryReader.open(index);){
	        IndexSearcher searcher = new IndexSearcher(reader);
	        
	        TopDocs docs = searcher.search(bQueryBuilder.build(), hitsPerPage);
	        ScoreDoc[] hits = docs.scoreDocs;
	        for(int i=0;i<hits.length;++i) {
	        	ScoreDoc doc = hits[i];
	            int docId = doc.doc;
	            Document d = searcher.doc(docId);
	            String title = d.get("title");
	            String documentId = d.get("root");
	            String titleUrl = HtmlResponseBuilder.getDoumentAccessURL(documentId , title);
	            resultDocIds.add(new SearchResult(documentId, titleUrl , i+1, doc.score));
	        }
        }
		return resultDocIds;
	}

	private static List<RowDetail> extractRowDetail(LinkedList<RelationDetail> rdList) 
			throws XPathExpressionException, ParserConfigurationException, SAXException, IOException 
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
		/**
		 * This map maintain Question and its answers for the Question
		 * RelationDetail - contains row number inside the file and the details of the subfile which record contains
		 */
		Map<String, LinkedList<RelationDetail>> rowRelationMap = new TreeMap<String, LinkedList<RelationDetail>>();
		
		try(Stream<Path> files = Files.list(Paths.get(ConfigLoader.getConfig(ConfigLoader.DOC_LOCATION)));)
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

	private static void addIndividualDocToIndex(IndexWriter w, String document, String id, String parentId) throws IOException, ParserConfigurationException, SAXException {
		Document doc = new Document();
		  
		doc.add( new TextField("id", id, Field.Store.YES));
		if(parentId != null)
		{
			doc.add( new TextField("parentId", parentId , Field.Store.YES));
		}
		doc.add(new StoredField("document", document));
		
		w.addDocument(doc);
	}
	
	private static class RelationDetail
	{
		int rowNumber;
		Path file;
		
		public RelationDetail(Path file,int rowNumber,String id )
		{
			this.file = file;
			this.rowNumber = rowNumber;
		}
	}
	
	public static class RowDetail
	{
		String title;
		String body;
		String id;
		
		public RowDetail(String id, String title,String body)
		{
			this.id = id;
			this.title = title;
			this.body = body;
		}
	}

}
