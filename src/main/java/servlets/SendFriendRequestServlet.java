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

import database.DBSessionKey;
import database.DBStatic;
import services.ServicesAuthentification;
import services.ServicesFriendship;
import utils.Persist;

/**
 * Servlet implementation class AddFriendServlet
 */
@WebServlet("/html/SendFriendRequestServlet")
public class SendFriendRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SendFriendRequestServlet() {
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
		String sessionkey =  request.getParameter("sessionkey");
		String friendname = request.getParameter("friendname")	;

		JSONObject json = new JSONObject();
		PrintWriter writer = response.getWriter();
		response.setContentType("text/plain");

		int reset = ServicesAuthentification.resetSessionKey(sessionkey);
		if(reset == Persist.RESET_SESSION_KEY_OK) {
			try {
				// Traitement des donnees
				int rslt = ServicesFriendship.addFriend(sessionkey, friendname, json);
				json.put("SendFriendRequestServlet", ""+rslt);
				json.put("sessionkey", ""+sessionkey);
				json.put("friendId", ""+friendname);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		else {
			try {
				json.put("SendFriendRequestServlet", ""+reset);
				json.put("sessionkey", ""+sessionkey);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		writer.println(json.toString());
	}


}
