package ntu.ir.test;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import ntu.ir.test.TestLucene.RowDetail;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.xml.sax.SAXException;

public class CombineDocumentIndexer implements DocumentIndexer {

	@Override
	public void addCompoundDocToIndex(IndexWriter w, List<RowDetail> rowDetails)
			throws IOException, ParserConfigurationException, SAXException {
		
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

}
