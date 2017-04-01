package ntu.ir.app;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.xml.builders.TermQueryBuilder;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.QueryBuilder;

import ntu.ir.app.model.FrequentTerm;
import ntu.ir.app.util.CustomStopWordAnalizer;

public class FrequentWordFinder {

	
	
	
	
	private ScoreDoc[] getDocumentsForTag(String tagName){
	 int MAX_SEARCH_HITS=1000;
		 IndexReader iReader;
		 ScoreDoc[] docarray=null;
		 try {
				Analyzer analyzer = new CustomStopWordAnalizer();

				
				System.out.println("Searching");
				iReader = DirectoryReader.open(FSDirectory.open(Paths.get("J:\\MSC\\IR\\data\\TagBodyIndex.txt")));
				
				TopScoreDocCollector collector = TopScoreDocCollector.create(MAX_SEARCH_HITS);
				QueryBuilder builder = new QueryBuilder(analyzer);
				Query termQuery = builder.createPhraseQuery("tag", tagName);
				
				//TermQueryBuilder builder=new TermQueryBuilder();
			//	TermQuery termQuery=builder.new TermQuery(new Term("tag",tagName));
				
				 IndexSearcher searcher = new IndexSearcher(iReader);
					
				searcher.search(termQuery, collector);
				
				
				docarray=collector.topDocs().scoreDocs;
				
		 }catch(Exception e){
			 
			 
		 }
		
		
		return docarray;
		
		
		
	}
	public static void main(String args[]){
		
		FrequentWordFinder fwf=new FrequentWordFinder();

		List<FrequentTerm> list=fwf.getFrequentWordList("java");
		for (FrequentTerm frequentTerm : list) {
			System.out.println(frequentTerm.getTerm() +"-"+frequentTerm.getFrequency());
		}
		
	}
	
	public List<FrequentTerm> getFrequentWordList(String tagname){
		
		 ScoreDoc[] docs= getDocumentsForTag(tagname);
		  
		
		
		Map<String,Integer> terms=new HashMap<String,Integer>();
		List<FrequentTerm> frequentTermList=new ArrayList<>();
		try {
			
			
			IndexReader iReader = DirectoryReader.open(FSDirectory.open(Paths.get("J:\\MSC\\IR\\data\\TagBodyIndex.txt")));
				
			
			for (ScoreDoc scoreDoc : docs) {
				int docNum=scoreDoc.doc;
				
				Terms termVector = iReader.getTermVector(docNum, "body");
				        TermsEnum itr = termVector.iterator();
				        BytesRef term = null;
				         PostingsEnum postings = null;
				         while((term = itr.next()) != null){
				              try{
				                  String termText = term.utf8ToString();
				                    postings = itr.postings(postings, PostingsEnum.FREQS);
				                    postings.nextDoc();
					                int freq = postings.freq();
				
				 
					                 if (null!= terms.get(termText.toLowerCase())){
					                	  terms.put(termText.toLowerCase(), freq+terms.get(termText.toLowerCase()) ) ;
					                	 
					                 }else{
					                	 terms.put(termText.toLowerCase(), freq)  ;
					                	 		 
					                 }
					                
				            } catch(Exception e){
				                  e.printStackTrace();
				            }
				        }
			}
				
			
			terms=sortByValue(terms);
			
			int i=0;
			
			
			for (Map.Entry<String, Integer> entry : terms.entrySet())
			{
				
				i=i+1;
				FrequentTerm frqTerm=new FrequentTerm();
				frqTerm.setTerm(entry.getKey());
				frqTerm.setFrequency(entry.getValue());
				frequentTermList.add(frqTerm);
				if(i==10)
				break;
			}
			
			
				
			
			
			
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return frequentTermList;
		
		
		
		
	}
	
	 private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap) {

	        List<Map.Entry<String, Integer>> list =
	                new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

	        
	        
	        Collections.sort(list, new Comparator<Map.Entry<String,Integer>>() {
		        		            public int compare(Map.Entry<String, Integer> o1,
		                                    Map.Entry<String, Integer> o2) {
		            
		                return (o2.getValue()).compareTo( o1.getValue() );
		            }
			
		        		            });

	        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
	        for (Map.Entry<String, Integer> entry : list) {
	            sortedMap.put(entry.getKey(), entry.getValue());
	        }

	        

	        return sortedMap;
	    }

	
	
}
