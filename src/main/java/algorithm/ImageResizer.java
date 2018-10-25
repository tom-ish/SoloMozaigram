package algorithm;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
 
import javax.imageio.ImageIO;
 
/**
 * This program demonstrates how to resize an image.
 *
 * @author www.codejava.net
 *
 */
public class ImageResizer {
 
    /**
     * Resizes an image to a absolute width and height (the image may not be
     * proportional)
     * @param inputImagePath Path of the original image
     * @param outputImagePath Path to save the resized image
     * @param scaledWidth absolute width in pixels
     * @param scaledHeight absolute height in pixels
     * @throws IOException
     */
    public static void resize(String inputImagePath,
            String outputImagePath, int scaledWidth, int scaledHeight) {
    	System.out.println("Resizing from " + inputImagePath + " on file " + outputImagePath);
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = null;
		try {
			inputImage = ImageIO.read(inputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException on resize of file : " + inputImagePath);
			e.printStackTrace();
		}
        
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());
        
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
       
        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);

        try {
	        // writes to output file
	        File outputFile = new File(outputImagePath);	        
        	if(outputFile != null)
        		ImageIO.write(outputImage, formatName, outputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException on write on " + outputImagePath);
			e.printStackTrace();
		}
    }
 
    /**
     * Resizes an image by a percentage of original size (proportional).
     * @param inputImagePath Path of the original image
     * @param outputImagePath Path to save the resized image
     * @param percent a double number specifies percentage of the output image
     * over the input image.
     * @throws IOException
     */
    public static void resize(String inputImagePath,
            String outputImagePath, double percent) throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);
        resize(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
    }
    
    /**
     * Resizes an image to a absolute width and height (the image may not be
     * proportional)
     * @param inputImage the original BufferedImage image
     * @param scaledWidth absolute width in pixels
     * @param scaledHeight absolute height in pixels
     * @throws IOException
     */
    public static BufferedImage resize(BufferedImage inputImage, int scaledWidth, int scaledHeight) {
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());
        
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
       
        return inputImage;
    }
    
}
