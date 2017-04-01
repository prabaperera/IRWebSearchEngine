package ntu.ir.app;

import java.io.File;
import java.io.StringReader;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import ntu.ir.app.util.CustomStopWordAnalizer;
import ntu.ir.app.util.XMLReader;
import ntu.ir.test.ConfigLoader;

public class FrequentTagIndexer {

	IndexWriter writer = null;
	
	public void indexBody(List<String[]> attList) throws Exception {

		try {
			ConfigLoader configLoader=new ConfigLoader();
			String docLocation=configLoader.getConfig("DOC_LOCATION");


			// specify the directory to store the Lucene index
			Directory indexDir = FSDirectory.open(Paths.get(docLocation+"\\TagBodyIndex.txt"));

			// specify the analyzer used in indexing
				Analyzer analyzer = new CustomStopWordAnalizer();

			IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
			cfg.setOpenMode(OpenMode.CREATE_OR_APPEND);
			// create the IndexWriter
			writer = new IndexWriter(indexDir, cfg);

			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

			for (String[] data : attList) {
				
				String tagWithoutSymbols=data[1].replace(">", ",").replace("<", "");
				
				String tagList[]=tagWithoutSymbols.split(",");
				
				for (String tag : tagList) {
				
				FieldType fieldType=new FieldType();
				fieldType.setStoreTermVectors(true);
				fieldType.setStored(true);
				fieldType.setTokenized(true);
				fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
				Document doc = new Document();
				doc.add(new TextField("tag",tag, Field.Store.YES));
				Field bodyField=new Field("body",data[3], fieldType);
				doc.add(bodyField);
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

	
	
	

	public void buildIndex(){
		
		
		try {
			ConfigLoader configLoader=new ConfigLoader();
			String docLocation=configLoader.getConfig("DOC_LOCATION");
            File folder =new File(docLocation+"//doc");
           
            for (File xmlFile : folder.listFiles()) {
				
			
			XMLReader xmlReader = new XMLReader();
			List<String[]> tagList = xmlReader.readTagData(xmlFile.getPath());

			FrequentTagIndexer frequentTagIndexer=new FrequentTagIndexer();
			frequentTagIndexer.indexBody(tagList);
            }
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
