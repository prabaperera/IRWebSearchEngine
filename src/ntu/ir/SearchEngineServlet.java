package ntu.ir;

import java.io.IOException;
import java.io.PrintWriter;
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
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		if("search".equals(requestType))
		{
			String searchQuery =  request.getParameter("searchQuery");
			 
			 try {
				 List<SearchResult> resultDocs = TestLucene.search(searchQuery, 10);
				
				response.getWriter().println((new Gson()).toJson(resultDocs));

			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		else if("document".equals(requestType))
		{
			String documentId = request.getParameter("documentId");
			
		}
		else
		{
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<body>");
			out.println("<h1>Hello Servlet Get</h1>");
			out.println("</body>");
			out.println("</html>");
		}
	}
}
