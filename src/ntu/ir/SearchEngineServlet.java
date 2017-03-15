package ntu.ir;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryparser.classic.ParseException;

import com.google.gson.Gson;

import ntu.ir.test.SearchResult;
import ntu.ir.test.TestLucene;

@WebServlet("/SearchEngineServlet")
public class SearchEngineServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4293517489243789198L;

	public SearchEngineServlet()
	{
		super();
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		String requestType = request.getParameter("requestType");
		response.setCharacterEncoding("UTF-8");
		
		if("search".equals(requestType))
		{
			String searchQuery =  request.getParameter("searchQuery");
			String resultsPerPage = request.getParameter("resultsPerPage");
			int resPerPage = Integer.parseInt(resultsPerPage);
			response.setContentType("application/json");
			 try 
			 {
				 List<SearchResult> resultDocs = TestLucene.search(searchQuery, resPerPage);
				
				response.getWriter().println((new Gson()).toJson(resultDocs));
	
			 }catch (ParseException e) {
				throw new ServletException(e);
			 }
		}
		else if("document".equals(requestType))
		{
			response.setContentType("text/html");
			String documentId = request.getParameter("documentId");
			try {
				List<String[]> documents =  TestLucene.findDocument(documentId);
				System.out.println(documents);
				String docAsHtml = HtmlResponseBuilder.buildSingleDocumentPage(documents);
				response.getWriter().println(docAsHtml);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
	}
}
