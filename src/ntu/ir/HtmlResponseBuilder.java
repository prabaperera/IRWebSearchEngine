package ntu.ir;

import java.util.List;

public class HtmlResponseBuilder {

	public static String buildSingleDocumentPage(List<String[]> documents)
	{
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<html>");
		boolean questionCreated = Boolean.FALSE;
		
		for(String[] docDetail : documents)
		{
			String title = docDetail[1];
			String body = docDetail[2];
			if(!questionCreated)
			{
				htmlBuilder.append("<h3>").append(title).append("</h3>");
				htmlBuilder.append("<p>").append(body).append("</p>");
				questionCreated = Boolean.TRUE;
				continue;
			}
			htmlBuilder.append("<p>").append(title).append("<p>").append(body).append("</p>").append("</p><hr/>");
		}
		htmlBuilder.append("</html>");
		return htmlBuilder.toString();
	}
	
	public static String getDoumentAccessURL(String documentId, String title)
	{
		String titleUrl = "<a href=\"/IRWebSearchEngine/SearchEngineServlet?requestType=document&documentId="+documentId+"\" target=\"_blank\" >"+title+"</a>";
        return titleUrl;
	}
}
