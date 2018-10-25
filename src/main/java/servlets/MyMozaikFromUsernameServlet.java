package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import services.ServicesAuthentification;
import services.ServicesMyMozaik;
import utils.Persist;

/**
 * Servlet implementation class MyMozaikFromUsernameServlet
 */
@WebServlet("/MyMozaikFromUsernameServlet")
public class MyMozaikFromUsernameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyMozaikFromUsernameServlet() {
        super();
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
		System.out.println("MyMozaikFromUsernameServlet called");
		String sessionkey = request.getParameter("sessionkey");
		String searchRsltUsername = request.getParameter("username");
		System.out.println(searchRsltUsername);
		
		
		PrintWriter writer = response.getWriter();
		response.setContentType("text/plain");
		
		JSONObject json = new JSONObject();
		
		int reset = ServicesAuthentification.resetSessionKey(sessionkey);
		if(reset == Persist.RESET_SESSION_KEY_OK) {
			try {
				JSONArray jsonArray = ServicesMyMozaik.buildMozaikThumnails(sessionkey, searchRsltUsername);
				System.out.println(jsonArray);
				json.put("MyMozaikServlet", ""+Persist.SUCCESS);
				json.put("myMozaikRslt", jsonArray);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		else {
			try {
				json.put("MyMozaikServlet", ""+reset);
				json.put("sessionkey", ""+sessionkey);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		writer.println(json.toString());
	}

}
