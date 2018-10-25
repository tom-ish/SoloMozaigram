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

import database.DBStatic;
import services.ServicesAuthentification;
import utils.Persist;

/**
 * Servlet implementation class CreateUserServlet
 */
@WebServlet("/CreateUserServlet")
public class CreateUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateUserServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		// Lecture des parametres
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		String email = request.getParameter("email");
			
		JSONObject json = new JSONObject();
		int rslt = ServicesAuthentification.createNewUser(username, password, password2, email);
		
		try {
			// Traitement des donnees
			json.put("CreateUserServlet", ""+rslt);
			json.put("username", ""+username);
			json.put("password", ""+password);
			json.put("password2", ""+password2);
			json.put("email", ""+email);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		// Ecriture des donnees
		PrintWriter writer = response.getWriter();
		response.setContentType("text/plain");
		writer.println(json.toString());
	}

}
