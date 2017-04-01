package ntu.ir;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.gson.Gson;

import ntu.ir.app.ExpertUserFinder;
import ntu.ir.app.RelavantQuestionFinder;
import ntu.ir.app.model.Question;
import ntu.ir.app.model.User;

/**
 * Servlet implementation class IRApplicationServlet
 */
@WebServlet("/IRApplicationServlet")
public class IRApplicationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IRApplicationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestType = request.getParameter("requestType");
		response.setCharacterEncoding("UTF-8");
		
		if("expertUserSearch".equals(requestType))
		{
			String tagName =  request.getParameter("tagName");
			
			ExpertUserFinder expertUserFinder=new ExpertUserFinder();
			
			
			response.setContentType("application/json");
			 try 
			 {
				 
				 List<User> userList = expertUserFinder.searchTopUsers(tagName);
				response.getWriter().println((new Gson()).toJson(userList));
	
			 }catch (Exception e) {
				throw new ServletException(e);
			 }
		}	else if("tagRelevanceSearch".equals(requestType))
		{
			String tagName =  request.getParameter("tagName");
			
			RelavantQuestionFinder relavantQuestionFinder=new RelavantQuestionFinder();
			System.out.println("Q :"+tagName);
			
			response.setContentType("application/json");
			 try 
			 {
				 
				 List<Question> questions = relavantQuestionFinder.searchQuestions(tagName);
				response.getWriter().println((new Gson()).toJson(questions));
	
			 }catch (Exception e) {
				throw new ServletException(e);
			 }
		}	
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doRequest(request, response);
		//doGet(request, response);
	}

	protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		String requestType = request.getParameter("requestType");
		response.setCharacterEncoding("UTF-8");
		
		if("expertUserSearch".equals(requestType))
		{
			String tagName =  request.getParameter("tagName");
			
			ExpertUserFinder expertUserFinder=new ExpertUserFinder();
			
			
			response.setContentType("application/json");
			 try 
			 {
				 
				 List<User> userList = expertUserFinder.searchTopUsers(tagName);
				response.getWriter().println((new Gson()).toJson(userList));
	
			 }catch (Exception e) {
				throw new ServletException(e);
			 }
		}
	}
}
