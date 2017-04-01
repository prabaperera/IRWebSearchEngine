package ntu.ir.app;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import ntu.ir.app.model.Question;

public class RelavantQuestionFinder {


public List<Question> searchQuestions(String queryStr){
		
		 
		 IndexReader iReader;
		 
		 
		List<Question> questionList=new ArrayList<Question>();
			
		try {
			
		System.out.println("Searching");
		
			
		
		DirectoryReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("J:\\MSC\\IR\\data\\TagBodyIndex.txt")));
	    PostingsEnum de = MultiFields.getTermDocsEnum(reader, "body", new BytesRef(queryStr));
	    		
	    int doc;
	    
	    while((doc = de.nextDoc()) != PostingsEnum.NO_MORE_DOCS) { 
	    	Question question=new Question();
	    		Document aDoc = reader.document( de.docID() );
	    	String docIdString = aDoc.get("body");
	    	
	    	question.setBody(docIdString);
	    	question.setTagFrequency(de.freq());
		    question.setDocId(""+de.docID());
		    questionList.add(question);
		    System.out.println(  de.docID() +" - "+ docIdString+" "+  de.freq());
	    }
		
	    Collections.sort(questionList,
				(left, right) -> left.getTagFrequency() - right.getTagFrequency());
		Collections.reverse(questionList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
		return questionList;
		
		
		
		
	}
	

	
	
	public static void main(String args[]){
		try {
			//search index
			RelavantQuestionFinder searcher=new RelavantQuestionFinder();
			
			
			searcher.searchQuestions("java");
			
			


			
		} catch (Exception e) {
			
			e.printStackTrace();
			// TODO: handle exception
		}
		
		
		
		
	} 
}
