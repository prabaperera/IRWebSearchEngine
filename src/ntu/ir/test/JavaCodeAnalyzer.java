package ntu.ir.test;

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
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;

public class JavaCodeAnalyzer extends Analyzer{

	static CharArraySet stopwords ;
	
	static
	{
		stopwords = new CharArraySet(StandardAnalyzer.STOP_WORDS_SET , Boolean.TRUE);
		
		try(Scanner sc = new Scanner(Paths.get(ConfigLoader.getConfig(ConfigLoader.STOP_WORD_LIST))))
		{
			List<String> stopWordList = new ArrayList<String>();
			
			String word ;
			while(sc.hasNextLine())
			{
				word = sc.nextLine();
				stopWordList.add(word);
			}
			stopwords = new CharArraySet(stopWordList , Boolean.TRUE);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
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
