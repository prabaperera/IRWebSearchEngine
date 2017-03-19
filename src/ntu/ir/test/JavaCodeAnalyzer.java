package ntu.ir.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;

public class JavaCodeAnalyzer extends Analyzer{

	@Override
	   protected TokenStreamComponents createComponents(String fieldName) {
	     final Tokenizer source = new JavaCodeTokenizer();
	     TokenStream result = new LengthFilter(source, 1, Integer.MAX_VALUE);
	     TokenStream stream = new LowerCaseFilter(result);
	     return new TokenStreamComponents(source, stream);
	   }

}
