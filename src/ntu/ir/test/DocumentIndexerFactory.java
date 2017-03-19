package ntu.ir.test;

public class DocumentIndexerFactory {

	public static final String BLOCK_INDEX = "BLOCK_INDEX";
	public static final String COMPOUND_INDEX = "COMPOUND_INDEX";
	
	public static DocumentIndexer getDocumentIndexer(String indexType)
	{
		if(BLOCK_INDEX.equals(indexType))
		{
			return new BlockDocumentIndexer();
		}
		else if(COMPOUND_INDEX.equals(indexType))
		{
			return new CombineDocumentIndexer();
		}
		
		throw new RuntimeException(indexType + " is not supported by DocumentIndexerFactory");
	}
}
