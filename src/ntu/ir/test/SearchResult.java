package ntu.ir.test;

import java.io.Serializable;

public class SearchResult implements Serializable{

	private static final long serialVersionUID = 1913307038382816106L;
	
	String documentId;
	String title;
	
	public SearchResult(String documentId , String title)
	{
		this.documentId = documentId;
		this.title = title;
	}
	
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
