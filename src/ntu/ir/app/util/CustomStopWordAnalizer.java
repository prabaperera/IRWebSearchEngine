package ntu.ir.app.util;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;

import ntu.ir.test.JavaCodeTokenizer;

public class CustomStopWordAnalizer extends Analyzer{

	static CharArraySet stopwords ;
	
	static
	{
		//stopwords = new CharArraySet(StopAnalyzer.ENGLISH_STOP_WORDS_SET , Boolean.TRUE);
		List<String> stopWordList = new ArrayList<String>();
		stopWordList.add("<p>");
//		stopWordList.add("</p>");
		stopWordList.add("p");
		stopWordList.add("/p");
		stopWordList.add("/li");
		stopWordList.add("i");
		stopWordList.add("us");
		stopWordList.add("/code");
		stopWordList.add(";");
		stopWordList.add("=");
		stopWordList.add("/pre");
		stopWordList.add("code>");
		stopWordList.add("// Code");
		stopWordList.add("Code");
		stopWordList.add("I'm");
		stopWordList.add("us");
		stopWordList.add("my");
		stopWordList.add("/us");
		stopwords = new CharArraySet(stopWordList , Boolean.TRUE);
		stopwords.addAll(StopAnalyzer.ENGLISH_STOP_WORDS_SET);
		
	}
	
	
	@Override
	   protected TokenStreamComponents createComponents(String fieldName) {
	     final Tokenizer source = new JavaCodeTokenizer();
	     TokenStream result = new StandardFilter(source);
	     result = new EnglishPossessiveFilter(result);
	     result = new LowerCaseFilter(result);
	     result = new StopFilter(result, stopwords);
	     result = new PorterStemFilter(result);
	     return new TokenStreamComponents(source, result);
	   }


}
