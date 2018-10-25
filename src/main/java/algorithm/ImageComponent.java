package algorithm;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

public class ImageComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	private Image image;
	private int[][][] pixels;
	
	public ImageComponent(Image image){
		this.image = image;
		pixels = new int[image.getWidth(null)][image.getHeight(null)][3];
	}

	public BufferedImage toBufferedImage(Image img) {
		if(img instanceof BufferedImage)
			return (BufferedImage) img;
		else {
			BufferedImage rslt = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics2D bGr = rslt.createGraphics();
			bGr.drawImage(img, 0, 0, null);
			bGr.dispose();
			return rslt;
		}
	}

	public int[][][] getPixel() {
		BufferedImage bi = toBufferedImage(image);
		long startTime = System.currentTimeMillis();
		for(int x = 0; x < bi.getWidth(); x++) {
			for(int y = 0; y < bi.getHeight(); y++) {
				int clr=  bi.getRGB(x,y); 
				int  red   = (clr & 0x00ff0000) >> 16;
				int  green = (clr & 0x0000ff00) >> 8;
				int  blue  =  clr & 0x000000ff;
				pixels[x][y][0] = red;
				pixels[x][y][1] = green;
				pixels[x][y][2] = blue;
				//System.out.println("["+x+"]["+y+"] - R: "+ red + " G: " + green + " B: " + blue);				
			}
		}
		return pixels;
	}
	
	public int[] getAveragePixel(){
		int[][][] pixels = this.getPixel();
		
		int [] averPix= new int[3];
		
		averPix[0]=0;
		averPix[1]=0;
		averPix[2]=0;
		
		for (int i=0;i<pixels.length;i++){
			for (int j=0;j<pixels[0].length;j++){
				averPix[0]+=pixels[i][j][0];
				averPix[1]+=pixels[i][j][1];
				averPix[2]+=pixels[i][j][2];
			}
		}
		
		averPix[0]=averPix[0]/(pixels.length*pixels[0].length);
		averPix[1]=averPix[1]/(pixels.length*pixels[0].length);
		averPix[2]=averPix[2]/(pixels.length*pixels[0].length);
		
		//System.out.println("R: "+averPix[0]+" G: "+averPix[1]+" B: "+averPix[2]);
		return averPix;
	}


	@Override
	public void paintComponent (Graphics g){
		if(image == null) return;
		int imageWidth = image.getWidth(this);
		int imageHeight = image.getHeight(this);

		g.drawImage(image, 50, 50, this);

		for (int i = 0; i*imageWidth <= getWidth(); i++)
			for(int j = 0; j*imageHeight <= getHeight();j++)
				if(i+j>0) g.copyArea(0, 0, imageWidth, imageHeight, i*imageWidth, j*imageHeight);
		
	}

}
