package ntu.ir.test;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import ntu.ir.test.TestLucene.RowDetail;

import org.apache.lucene.index.IndexWriter;
import org.xml.sax.SAXException;

public interface DocumentIndexer {

	public void addCompoundDocToIndex(IndexWriter w, List<RowDetail> rowDetails) throws IOException, ParserConfigurationException, SAXException;
}
