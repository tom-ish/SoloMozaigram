package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import services.ServicesAuthentification;
import services.ServicesComment;
import utils.Persist;

/**
 * Servlet implementation class AddCommentServlet
 */
@WebServlet("/html/AddCommentServlet")
public class AddCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddCommentServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String sessionkey = request.getParameter("sessionkey");
		String comment = request.getParameter("text");
		int imgid = Integer.valueOf(request.getParameter("imgid"));
		
		System.out.println("Commentaire : ");
		System.out.println(comment);
		
		PrintWriter writer = response.getWriter();
		response.setContentType("text/plain");
		JSONObject json = new JSONObject();
		

		int reset = ServicesAuthentification.resetSessionKey(sessionkey);
		if(reset == Persist.RESET_SESSION_KEY_OK) {			
			Integer rslt = ServicesComment.addComment(sessionkey,comment,imgid,json);
			try {
				// Traitement des donnees
				json.put("AddCommentServlet", ""+rslt);
				json.put("sessionkey", sessionkey);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			try {
				// Traitement des donnees
				json.put("AddCommentServlet", ""+reset);
				json.put("sessionkey", sessionkey);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Ecriture des donnees
		writer.println(json.toString());
	}


}