package ntu.ir.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import ntu.ir.test.TestLucene.RowDetail;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.xml.sax.SAXException;

public class BlockDocumentIndexer implements DocumentIndexer{

	@Override
	public void addCompoundDocToIndex(IndexWriter w, List<RowDetail> rowDetails)
			throws IOException, ParserConfigurationException, SAXException {
		
		List<Document> docList = new ArrayList<Document>(rowDetails.size());
		RowDetail rd = rowDetails.remove(0);
		Document maindoc = new Document();
		maindoc.add( new TextField("title", rd.title, Field.Store.YES));
		maindoc.add( new TextField("body", rd.body, Field.Store.YES));
		
		for(RowDetail rowd : rowDetails)
		{
			Document doc = new Document();
			doc.add( new TextField("title", rowd.title, Field.Store.YES));
			doc.add( new TextField("body", rowd.body, Field.Store.YES));
			docList.add(doc);
		}
		maindoc.add(new StoredField("root", rd.id));
		docList.add(maindoc);
		
		w.addDocuments(docList);
		
	}

}
