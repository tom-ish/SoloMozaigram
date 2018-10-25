package mozaik_process;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import utils.Persist;

public class ImageResizer {
	public static void resizeMultipleImages(File dir_from, String to) {
		int nbResizedImages = 0;
		File dir_to = new File(to);
		if(!dir_to.exists())
			dir_to.mkdir();
		for(File f : dir_from.listFiles()) {
			if(!f.isDirectory()) {
				BufferedImage originalImage = null;
				try {
					originalImage = ImageIO.read(f);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("IOException from resizeMultipleImages on ImageIO.read() in file : " + f.toString());
					e.printStackTrace();
					break;
				}
				
				int type = originalImage.getType() == BufferedImage.TYPE_CUSTOM? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
				
				BufferedImage resizeImageJpg = resizeImage(originalImage, type);
				try {
					ImageIO.write(resizeImageJpg, "jpg", new File(dir_to + File.separator + f.getName()));
					nbResizedImages++;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("IOException from resizeMultipleImages on ImageIO.write");
					e.printStackTrace();
				}
			}
		}
		System.out.println("NB_RESIZED_IMAGE : " + nbResizedImages);
	}
	
	public static BufferedImage resizeImage(BufferedImage originalImage, int type){
		return resizeImage(originalImage, type, Persist.IMG_WIDTH, Persist.IMG_HEIGHT);
	}
	
	public static BufferedImage resizeImage(BufferedImage originalImage, int type, int imgWidth, int imgHeight) {
		BufferedImage resizedImage = new BufferedImage(imgWidth, imgHeight, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, imgWidth, imgHeight, null);
		g.dispose();

		return resizedImage;
	}
	
	public static BufferedImage resizeValidDimensions(BufferedImage originalImage, int type, int imgWidth, int imgHeight) {
		int finalWidth = Math.floorDiv(imgWidth, Persist.GRAIN) * Persist.GRAIN;
		int finalHeight = Math.floorDiv(imgHeight, Persist.GRAIN) * Persist.GRAIN;
		System.out.println("imgWidth : " + imgWidth + ", imgWidth/GRAIN : " + Math.floorDiv(imgWidth, Persist.GRAIN) + ", GRAIN : " + Persist.GRAIN);
		System.out.println("imgHeight : " + imgHeight+ ", imgHeight/GRAIN : " + Math.floorDiv(imgHeight, Persist.GRAIN) + ", GRAIN : " + Persist.GRAIN);
		return resizeImage(originalImage, type, finalWidth, finalHeight);		
	}
}
