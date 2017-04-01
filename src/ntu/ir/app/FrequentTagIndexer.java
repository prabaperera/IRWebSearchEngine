package ntu.ir.app;

import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import ntu.ir.app.util.XMLReader;

public class FrequentTagIndexer {

	IndexWriter writer = null;
	
	public void indexBody(List<String[]> attList) throws Exception {

		try {

			// specify the directory to store the Lucene index
			Directory indexDir = FSDirectory.open(Paths.get("J:\\MSC\\IR\\data\\TagBodyIndex.txt"));

			// specify the analyzer used in indexing
			Analyzer analyzer = new StopAnalyzer(StopAnalyzer.ENGLISH_STOP_WORDS_SET);

			IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
			cfg.setOpenMode(OpenMode.CREATE_OR_APPEND);

			// create the IndexWriter
			writer = new IndexWriter(indexDir, cfg);

			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

			for (String[] data : attList) {
				
				String tagWithoutSymbols=data[1].replace(">", ",").replace("<", "");
				
				String tagList[]=tagWithoutSymbols.split(",");
				
				for (String tag : tagList) {
				
					System.out.println("tag :"+tag);
						
				Document doc = new Document();
				doc.add(new TextField("tag",tag, Field.Store.YES));
				doc.add(new TextField("body",data[3], Field.Store.YES));
				writer.addDocument(doc);
				}
				
				
				
			}

			System.out.println("Total number of documents indexed: " + writer.maxDoc());

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			// close the index writer.
			if (null != writer)
				writer.close();
		}

	}

	
	
	public static void main(String args[]) {
		try {

			XMLReader xmlReader = new XMLReader();
			List<String[]> tagList = xmlReader.readTagData("J:\\MSC\\IR\\data\\Posts1.XML");

			FrequentTagIndexer frequentTagIndexer=new FrequentTagIndexer();
			frequentTagIndexer.indexBody(tagList);

			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	
	
}
