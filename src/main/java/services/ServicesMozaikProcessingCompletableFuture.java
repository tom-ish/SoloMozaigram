package services;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.Part;

import mozaik_process.ImageResizer;
import mozaik_process.ImageSaver;
import mozaik_process.MozaikGenerator;
import utils.Persist;
import utils.Tools;

public class ServicesMozaikProcessingCompletableFuture {
	

	private static int NB_THREAD = Runtime.getRuntime().availableProcessors();
	private static int NB_IMAGES = 149;
	private static String PIXABAY_API_KEY = System.getenv(Persist.PIXABAY_API_KEY);
	
	public static File FROM_REPOSITORY = new File("images_save");
	
	public static int verifyParameters(String sessionkey, String keyword, Part imagePartFile) {
		if(Tools.isNullParameter(sessionkey) || Tools.isNullParameter(keyword) || imagePartFile.equals(null))
			return Persist.ERROR_NULL_PARAMETER;

		//		if(DBSessionKey.isSessionKeyExpired(userId))		
		String imageFileName = Paths.get(imagePartFile.getSubmittedFileName()).getFileName().toString();
		if(Tools.isNullParameter(imageFileName) || imageFileName.length() == 0)
			return Persist.ERROR_FILE_NOT_FOUND;
		return Persist.SUCCESS;
	}
	
	public static ArrayList<String> loadAPIImagesFromKeyword(String keyword) {
//		String url = "https://api.qwant.com/api/search/images?count="+NB_IMAGES+"&offset=1&q="+keyword;
		String url = "https://pixabay.com/api/?key="+PIXABAY_API_KEY+"&q="+keyword+"&image_type=photo";
		return Tools.getURLsfromJSON(Tools.readJsonFromUrl(url));
	}
	
	
	public static CompletableFuture<List<BufferedImage>> saveImagesFromURLs(ArrayList<String> urls) {
		ExecutorService executorService = Executors.newFixedThreadPool(NB_THREAD);


		// Separation  des taches dans les differents threads
		/*
		CompletionService<Integer> completion = new ExecutorCompletionService<Integer>(executorService);
		int nb_images_per_pool = urls.size()/NB_THREAD;
		int i = 0;
		for(i = 0; i < NB_THREAD-1; i++) {
			completion.submit(new ImageSaver(new ArrayList<String>(urls.subList(i*nb_images_per_pool, (i+1)*(nb_images_per_pool)-1))));
		}
		completion.submit(new ImageSaver(new ArrayList<String>(urls.subList(i*nb_images_per_pool, urls.size()-1))));
		*/

		CompletionService<List<BufferedImage>> imagesCompletion = new ExecutorCompletionService<List<BufferedImage>>(executorService);
		int nb_images_per_pool = urls.size()/NB_THREAD;
		int i = 0;
		for(i = 0; i < NB_THREAD-1; i++) {
			imagesCompletion.submit(new ImageSaver(new ArrayList<String>(urls.subList(i*nb_images_per_pool, (i+1)*(nb_images_per_pool)-1))));
		}
		imagesCompletion.submit(new ImageSaver(new ArrayList<String>(urls.subList(i*nb_images_per_pool, urls.size()-1))));
		
		
		i = 0;
		List<BufferedImage> savedImages = new ArrayList<BufferedImage>();
		Integer nbImagesSaved = 0;
		for(i = 0; i < NB_THREAD; i++) {
			try {
				savedImages.addAll(imagesCompletion.take().get());
//				nbImagesSaved += completion.take().get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		executorService.shutdown();
		nbImagesSaved = savedImages.size();
		System.out.println("NB_IMAGE_SAVED : " + nbImagesSaved);
		
		return CompletableFuture.completedFuture(savedImages);
	}
	
	public static CompletableFuture<Integer> resizeImages(Integer previousReturnCode) {
		if(previousReturnCode == Persist.SUCCESS) {
			ImageResizer.resizeMultipleImages(FROM_REPOSITORY, "images_save" + File.separator + "rescale");
			return CompletableFuture.completedFuture(Persist.SUCCESS);
		}
		else
			return CompletableFuture.completedFuture(previousReturnCode);
	}
	
	public static CompletableFuture<Entry<Integer, String>> generateMozaik(List<BufferedImage> savedImages, Image image, String originalFileName, String storedFilename) {
		if(savedImages != null)
			return CompletableFuture.completedFuture(MozaikGenerator.generate(savedImages, image, originalFileName, storedFilename));
		return CompletableFuture.completedFuture(new AbstractMap.SimpleEntry<Integer, String>(Persist.ERROR, ""));
	}
	
	public static CompletableFuture<Integer> storeMozaik(Integer previousReturnCode, String sessionkey, String originalFilename, String mozaikFilePath, String keyword) {
		//String mozaikFilePath = Persist.DEST_MOZAIK_REPOSITORY_PATH + File.separator + originalFileName;
		System.out.println("previousReturnCode from MOZAIKGENERATION = " + previousReturnCode);
		if(previousReturnCode == Persist.SUCCESS)
			return CompletableFuture.completedFuture(ServicesImage.addImage(sessionkey, originalFilename, mozaikFilePath, keyword));
		return CompletableFuture.completedFuture(previousReturnCode);
	}
	
	
}
