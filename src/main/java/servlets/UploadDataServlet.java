package servlets;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.JSONException;
import org.json.JSONObject;

import database.DBSessionKey;
import database.DBStatic;
import database.DBUserTask;
import hibernate_entity.UserTask;
import mozaik_process.ImageResizer;
import services.ServicesAuthentification;
import services.ServicesMozaikProcessingCompletableFuture;
import utils.FileProcess;
import utils.Persist;
import utils.Tools;


/**
 * Servlet implementation class UploadDataServlet
 */
@WebServlet("/html/UploadDataServlet")
@MultipartConfig
public class UploadDataServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static int NB_IMAGES = 149;

	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadDataServlet() {
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
		String keyword = request.getParameter("userKeyword");
		Part imageFilePart = request.getPart("imageFile");
		
		String originalFileName = Tools.getFileName(imageFilePart);
		
		BufferedImage originalImage = FileProcess.getBufferedImageFromPart(imageFilePart);
		BufferedImage image = ImageResizer.resizeValidDimensions(originalImage, originalImage.getType(), originalImage.getWidth(), originalImage.getHeight());
		
		System.out.println("image resizing OK");

		PrintWriter writer = response.getWriter();
		response.setContentType("text/plain");
		
		JSONObject json = new JSONObject();
		
		System.out.println("SESSIONKEY=" + sessionkey);

		int reset = ServicesAuthentification.resetSessionKey(sessionkey);
		System.out.println("RESET SESSION : " + reset);
		
		if(reset == Persist.RESET_SESSION_KEY_OK) {	
			int rslt = ServicesMozaikProcessingCompletableFuture.verifyParameters(sessionkey, keyword, imageFilePart);
			if(rslt != Persist.SUCCESS) {
				try {
					json.put("UploadDataServlet", "" + rslt);	
					writer.println(json.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
			System.out.println("PARAMETER VERIFICATION OK : " + rslt);
			
			UserTask userTask = DBUserTask.createUserTask(sessionkey);
			json.put("userTaskId", "" + userTask.getId());
			
			System.out.println("USER TASK CREATED : " + userTask.getId());
			
			System.out.println("CompletableFuture call started...");
			rslt = generateMozaik(sessionkey, keyword, image, originalFileName);
			System.out.println("... CompletableFuture call ended");		
			
			try {
				json.put("UploadDataServlet", "" + rslt);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		else {
			try {
				json.put("UploadDataServlet", "" + reset);
				json.put("sessionkey", sessionkey);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Ecriture des donnees
		writer.println(json.toString());
	}
	
	
	private CompletableFuture<ArrayList<String>> getURLs(String sessionkey, String keyword) {
		return CompletableFuture.supplyAsync( () -> ServicesMozaikProcessingCompletableFuture.loadAPIImagesFromKeyword(keyword),
				Executors.newSingleThreadExecutor());
	}

	private CompletableFuture<List<BufferedImage>> saveAndResizeImagesFromURLs(String sessionkey, String keyword) {
		return getURLs(sessionkey, keyword).
			thenCompose(urls -> ServicesMozaikProcessingCompletableFuture.saveImagesFromURLs(urls));
	}

	private int generateMozaik(String sessionkey, String keyword, Image image, String originalFileName) {
		long startTime = System.currentTimeMillis();
		String newFilename = originalFileName;
		
		
		saveAndResizeImagesFromURLs(sessionkey, keyword).
			thenCompose(savedImages -> 
				ServicesMozaikProcessingCompletableFuture.generateMozaik(savedImages, image, originalFileName, newFilename)).
			thenCompose(pairStatusVSStoredFilename -> 
				ServicesMozaikProcessingCompletableFuture.storeMozaik(pairStatusVSStoredFilename.getKey(), sessionkey, originalFileName, pairStatusVSStoredFilename.getValue(), keyword)).
			thenAccept( (status) -> {
				System.out.println(System.currentTimeMillis() - startTime);
				System.out.println("COMPLETED FUTURE - STATUS : " + status);
		});
		return Persist.PROCESS_COMPLETABLE_FUTURE_TASKS_STARTED;
	}

}
