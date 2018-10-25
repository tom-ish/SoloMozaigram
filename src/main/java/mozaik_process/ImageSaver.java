package mozaik_process;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import utils.FileProcess;

public class ImageSaver implements Callable</*Integer*/List<BufferedImage>> {
	
	ArrayList<String> urls;
	
	public ImageSaver(ArrayList<String> urls) {
		this.urls = urls;
	}

	/*
	@Override
	public Integer call() throws Exception {
		// TODO Auto-generated method stub
		return FileProcess.saveImagesFromURLs(urls);
	}
	*/
	
	@Override
	public List<BufferedImage> call() throws Exception {
		// TODO Auto-generated method stub
		return FileProcess.saveImagesFromURLsAsList(urls);
	}

}
