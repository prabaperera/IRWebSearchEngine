package ntu.ir.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.AttributeFactory;

public class JavaCodeTokenizer extends CharTokenizer {
	  
	private static final String pattern = "[ |.|(|)|{|}|,|<|>]";

    // Create a Pattern object
    
	  /**
	   * Construct a new LetterTokenizer.
	   */
	  public JavaCodeTokenizer() {
	  }
	  
	  /**
	   * Construct a new LetterTokenizer using a given
	   * {@link org.apache.lucene.util.AttributeFactory}.
	   * 
	   * @param factory
	   *          the attribute factory to use for this {@link Tokenizer}
	   */
	  public JavaCodeTokenizer(AttributeFactory factory) {
	    super(factory);
	  }
	  
	  /** Collects only characters which satisfy
	   * {@link Character#isLetter(int)}.*/
	  @Override
	  protected boolean isTokenChar(int c) 
	  {
	    if( Character.isWhitespace(c))
	    {
	    	return false;
	    }
	    
	    String val = new String(Character.toChars(c));
	    // Create a Pattern object
	    Pattern r = Pattern.compile(pattern);

	    // Now create matcher object.
	    Matcher m = r.matcher(val);
	    return !m.find( );
	    
	  }
	  
//	  
//	  public static void main(String[] args) {
//		  String line = "thisis(";
//	      // Create a Pattern object
//	      Pattern r = Pattern.compile(pattern);
//
//	      // Now create matcher object.
//	      Matcher m = r.matcher(line);
//	      System.out.println( m.find( ));
//	}
	}


