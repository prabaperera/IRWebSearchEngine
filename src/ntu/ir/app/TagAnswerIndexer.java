package ntu.ir.app;

import java.beans.FeatureDescriptor;
import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.text.DateFormat;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.*;
import org.apache.lucene.analysis.fi.FinnishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.lucene50.Lucene50CompoundFormat;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.function.valuesource.DoubleFieldSource;
import org.apache.lucene.queryparser.flexible.core.nodes.FieldableNode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.apache.lucene.util.packed.PackedInts.Reader;

import ntu.ir.app.util.XMLReader;
import ntu.ir.test.ConfigLoader;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.SortedNumericDocValuesField;
import org.apache.lucene.document.SortedSetDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;

public class TagAnswerIndexer {

	IndexWriter writer = null;

	
	
	public void indexTagData(List<String[]> attList) throws Exception {

		try {
			ConfigLoader configLoader=new ConfigLoader();
			String docLocation=configLoader.getConfig("DOC_LOCATION");

			// specify the directory to store the Lucene index
			Directory indexDir = FSDirectory.open(Paths.get(docLocation+"\\TagIndex.txt"));

			// specify the analyzer used in indexing
			Analyzer analyzer = new StopAnalyzer(StopAnalyzer.ENGLISH_STOP_WORDS_SET);

			IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
			cfg.setOpenMode(OpenMode.CREATE_OR_APPEND);

			// create the IndexWriter
			writer = new IndexWriter(indexDir, cfg);

			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

			for (String[] data : attList) {
				Document doc = new Document();
				doc.add(new TextField("tags", data[1], Field.Store.YES));
				Field f=new Field("answerID", data[0], TextField.TYPE_STORED);
				doc.add(f);
				SortedDocValuesField answerFeild=new SortedDocValuesField("answerID", new BytesRef(data[0]));
				System.out.println("dfd "+data[0]);
				doc.add(answerFeild);
				//doc.add(new TextField("acceptedAnswerId",data[0],Field.Store.YES));
				doc.add(new TextField("answerCount", data[2], Field.Store.YES));

				writer.addDocument(doc);
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

	
	public void indexAnswerData(List<String[]> attList) throws Exception {

		try {
			ConfigLoader configLoader=new ConfigLoader();
			String docLocation=configLoader.getConfig("DOC_LOCATION");

			// specify the directory to store the Lucene index
			Directory indexDir = FSDirectory.open(Paths.get(docLocation+"\\AnswerIndex.txt"));

			// specify the analyzer used in indexing
			Analyzer analyzer = new StandardAnalyzer();

			IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
			cfg.setOpenMode(OpenMode.CREATE_OR_APPEND);

			// create the IndexWriter
			writer = new IndexWriter(indexDir, cfg);

			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

			for (String[] data : attList) {
				Document doc = new Document();
				Field f=new Field("answerID", data[0], TextField.TYPE_STORED);
				SortedDocValuesField answerFeild=new SortedDocValuesField("answerID", new BytesRef(data[0]));
				System.out.println(new BytesRef(data[0]));
				doc.add(f);
				doc.add(answerFeild);
				SortedDocValuesField ownerId=new SortedDocValuesField("ownerId", new BytesRef(data[1]));
				doc.add(ownerId);
				
				doc.add(new TextField("ownerId", data[1], Field.Store.YES));
				
				writer.addDocument(doc);
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

			TagAnswerIndexer indexer = new TagAnswerIndexer();

			indexer.indexTagData(tagList);

			
			List<String[]> answerList = xmlReader.readAnswerData("J:\\MSC\\IR\\data\\Posts1.XML");

			indexer.indexAnswerData(answerList);

			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void buildIndex(){
		try {
			ConfigLoader configLoader=new ConfigLoader();
			String docLocation=configLoader.getConfig("DOC_LOCATION");
          System.out.println(docLocation);
			File folder =new File(docLocation+"//doc");
           
            for (File xmlFile : folder.listFiles()) {
				
			
			XMLReader xmlReader = new XMLReader();
			List<String[]> tagList = xmlReader.readTagData(xmlFile.getPath());

			TagAnswerIndexer indexer = new TagAnswerIndexer();

			indexer.indexTagData(tagList);

			
			List<String[]> answerList = xmlReader.readAnswerData(xmlFile.getPath());

			indexer.indexAnswerData(answerList);

			
            }
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
	}

}
