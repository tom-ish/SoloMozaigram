package servlets;

import java.awt.Image;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import database.DBSessionKey;
import database.DBStatic;
import database.DBUserTask;
import services.ServicesAuthentification;
import services.ServicesImage;
import services.ServicesUserTask;
import utils.HibernateUtil;
import utils.Persist;

/**
 * Servlet implementation class IsMozaikGeneratedServlet
 */
@WebServlet("/html/IsMozaikGeneratedServlet")
public class IsMozaikGeneratedServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public IsMozaikGeneratedServlet() {
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
		// TODO Auto-generated method stub
		String sessionkey = request.getParameter("sessionkey");
		response.setContentType("text/plain");
		
		JSONObject json = new JSONObject();

		int reset = ServicesAuthentification.resetSessionKey(sessionkey);
		if(reset == Persist.RESET_SESSION_KEY_OK) {
			try {
				Integer rslt = ServicesUserTask.getImgPath(sessionkey, json);
				json.put("IsMozaikGeneratedServlet", ""+ rslt);
			} catch (JSONException e) {
				e.printStackTrace();
			}			
		}
		else {
			try {
				json.put("IsMozaikGeneratedServlet", ""+reset);
				json.put("sessionkey", ""+sessionkey);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		// Ecriture des donnees
		PrintWriter writer = response.getWriter();
		writer.println(json.toString());
	}
}
