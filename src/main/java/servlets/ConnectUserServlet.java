package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.json.JSONException;
import org.json.JSONObject;

import database.DBStatic;
import services.ServicesAuthentification;
import utils.HibernateUtil;
import utils.Persist;

/**
 * Servlet implementation class ConnectUserServlet
 */
@WebServlet("/ConnectUserServlet")
public class ConnectUserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConnectUserServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Lecture des paramètres
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		

		PrintWriter writer = response.getWriter();
		response.setContentType("application/json");
		
		try {
			JSONObject json = new JSONObject();
			int rslt = ServicesAuthentification.connectUser(username, password, json);
			json.put("ConnectUserServlet", ""+rslt);
			System.out.println(json);
			json.write(writer);
//			writer.println(json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
