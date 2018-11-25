package servlets;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.JSONException;
import org.json.JSONObject;

import database.DBStatic;
import mozaik_process.ImageResizer;
import services.ServicesMozaikProcessingCompletableFuture;
import utils.FileProcess;
import utils.Persist;
import utils.Tools;

/**
 * Servlet implementation class AsyncUploadDataServlet
 */
@WebServlet(urlPatterns= {"/html/AsyncUploadDataServlet"}, asyncSupported=true)
public class AsyncUploadDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AsyncUploadDataServlet() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	public static AsyncContext asyncContext;
	private static int NB_IMAGES = 149;

	public static HashMap<String, SimpleEntry<Integer, String>> userTasksMapper;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// Initialization of the asyncContext
		asyncContext = request.startAsync();

		String sessionkey = request.getParameter("sessionkey");
		String keyword = request.getParameter("userKeyword");
		Part imageFilePart = request.getPart("imageFile");

		String originalFileName = Tools.getFileName(imageFilePart);

		BufferedImage originalImage = FileProcess.getBufferedImageFromPart(imageFilePart);
		BufferedImage image = ImageResizer.resizeValidDimensions(originalImage, originalImage.getType(), originalImage.getWidth(), originalImage.getHeight());


		JSONObject json = new JSONObject();

		int rslt = ServicesMozaikProcessingCompletableFuture.verifyParameters(sessionkey, keyword, imageFilePart);
		if(rslt != Persist.SUCCESS) {
			try {
				json.put("AsyncUploadDataServlet", "" + rslt);				
				PrintWriter writer = response.getWriter();
				response.setContentType("text/plain");
				writer.println(json.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		rslt = generateMozaik(sessionkey, keyword, image, originalFileName);


		try {
			json.put("AsyncUploadDataServlet", "" + rslt);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Ecriture des donnees
		PrintWriter writer = response.getWriter();
		response.setContentType("text/plain");
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
		asyncContext.start(() -> {
			System.out.println("CompletableFuture call started...");

			/*
			saveAndResizeImagesFromURLs(sessionkey, keyword).
			thenCompose(savedImages -> ServicesMozaikProcessingCompletableFuture.generateMozaik(savedImages, image, originalFileName)).
			thenCompose(status -> ServicesMozaikProcessingCompletableFuture.storeMozaik(status, sessionkey, originalFileName)).
			thenAccept( (statusImgIdSimpleEntry) -> {					
				System.out.println(System.currentTimeMillis() - startTime);
				System.out.println("COMPLETED FUTURE - STATUS : " + statusImgIdSimpleEntry*//* + " imgPath : " + statusImgIdSimpleEntry.getValue()*//*);

				JSONObject json = new JSONObject();
				PrintWriter asyncWriter = null;
				try {
					asyncWriter = asyncContext.getResponse().getWriter();
					asyncContext.getResponse().setContentType("text/plain");
					json.put("AsyncUploadDataServlet", ""+Persist.SUCCESS);
					json.put("imgPath", ""+statusImgIdSimpleEntry);
					asyncWriter.println(json.toString());
					asyncContext.complete();
					System.out.println("... CompletableFuture call ended");							
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			*/
		});

		return Persist.PROCESS_COMPLETABLE_FUTURE_TASKS_STARTED;
	}


}
