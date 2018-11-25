package mozaik_process;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import algorithm.ImageComponent;
import amazon.AmazonUtilities;
import utils.Persist;
import utils.Tools;

public class ImageFrame {
	
	private ImageComponent component;
	private ArrayList<String> library;
	private ArrayList<ArrayList<ImageComponent>> mozaik;
	private File f;
	private List<BufferedImage> savedImages;
	
	private String originalFileName, storedFilename;
	

	public ImageFrame (List<BufferedImage> savedImages, Image file, String originalFileName, String storedFilename) {
		this.component = new ImageComponent(file);
		this.savedImages = savedImages;
		this.originalFileName = originalFileName;
		this.storedFilename = storedFilename;
	}
	
	public int compute(int grain) {
		long startTime = System.currentTimeMillis();
		// la matrice RGB de tous les pixels de l'image contenue dans component
		int [][][] pixels=component.getPixel();
		// la matrice RGB de toutes les tuiles de l'image contenue dans component
		int [][][] weighedpixels=new int[pixels.length/grain][pixels[0].length/grain][3];
		
		for (int i=0;i<weighedpixels.length;i++){
			for (int j=0;j<weighedpixels[0].length;j++){
				weighedpixels[i][j][0]=0;
				weighedpixels[i][j][1]=0;
				weighedpixels[i][j][2]=0;
				for (int k=0;k<grain;k++){
					for (int l=0;l<grain;l++){
						weighedpixels[i][j][0]+=pixels[grain*i+k][grain*j+l][0];
						weighedpixels[i][j][1]+=pixels[grain*i+k][grain*j+l][1];
						weighedpixels[i][j][2]+=pixels[grain*i+k][grain*j+l][2];
					}
				}
				weighedpixels[i][j][0]=weighedpixels[i][j][0]/(grain*grain);
				weighedpixels[i][j][1]=weighedpixels[i][j][1]/(grain*grain);
				weighedpixels[i][j][2]=weighedpixels[i][j][2]/(grain*grain);
			}
		}
		System.out.print("INITIALIZATION : ");
		System.out.println(System.currentTimeMillis() - startTime);
		
		loadLibrary(grain);

		System.out.println("LIBRARY LOADING : ");
		System.out.println(System.currentTimeMillis() - startTime);
		
		HashMap<String, Integer[]> collection=new HashMap<String,Integer[]>();
		for (String p:library){
			ImageComponent c = null;
			try {
				c = new ImageComponent(ImageIO.read(new File(p)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("IOException... cannot read file : " + p);
				return Persist.ERROR_FILE_NOT_FOUND;
			}
			int[] averPix=c.getAveragePixel();
			Integer[] a=new Integer[3];
			a[0]=averPix[0];
			a[1]=averPix[1];
			a[2]=averPix[2];
			collection.put(p, a);
		}
		
		System.out.println("MOZAIKS");
		System.out.print("RESCALED IMAGES COLLECTION LOADING : ");
		System.out.println(System.currentTimeMillis() - startTime);
		ArrayList<String> mozaikS=new ArrayList<String>();
		
		//HashMap<String,Integer> iter=new HashMap<String, Integer>();
		/*
		for (String c:library){
			iter.put(c,0);
		}
		*/
			
			
		System.out.println("COLLECTION.SIZE() : " + collection.size());
		
		System.out.println("PIXELS.length : " + pixels.length + " PIXELS[0].LENGTH : " + pixels[0].length);
		System.out.println("WEIGHTEDPIXELS.length : " + weighedpixels.length + " WEIGHTEDPIXELS[0].LENGTH : " + weighedpixels[0].length);
		for (int i=0; i<weighedpixels[0].length;i++){
			for (int j=0;j<weighedpixels.length;j++){
				int distance=Integer.MAX_VALUE;
				//int itofclos=0;
				String closer=null;
				//System.out.println("< ================ ["+i+"/"+weighedpixels.length+"]["+j+"/"+weighedpixels[0].length+"] ================ >");
				for (String c:library){
					int d=(weighedpixels[j][i][0]-collection.get(c)[0])*(weighedpixels[j][i][0]-collection.get(c)[0])+(weighedpixels[j][i][1]-collection.get(c)[1])*(weighedpixels[j][i][1]-collection.get(c)[1])+(weighedpixels[j][i][2]-collection.get(c)[2])*(weighedpixels[j][i][2]-collection.get(c)[2]);
					//System.out.println("from library : " + c + " d="+d);
					if (d<distance){
						distance=d;
						closer=c;
						//itofclos=iter.get(c);
					}
				}
				//System.out.println("STEP");
/*				if (closer==null){
					for (String c:library){
						int d=(weighedpixels[j][i][0]-collection.get(c)[0])*(weighedpixels[j][i][0]-collection.get(c)[0])+(weighedpixels[j][i][1]-collection.get(c)[1])*(weighedpixels[j][i][1]-collection.get(c)[1])+(weighedpixels[j][i][2]-collection.get(c)[2])*(weighedpixels[j][i][2]-collection.get(c)[2]);
						if (d<distance){
							System.out.println("from library : "+c);
							distance=d;
							closer=c;
						}
					}
				}
*/
			//	corCol.add(new Color(red,green,blue));
				mozaikS.add(closer);
				//System.out.println(" ................................... ");
			}
			//System.out.println("++++++++++++++++++++++++++++++++++");
		}

		System.out.print("CLOSEST COLOR IMAGES LOADING : ");
		System.out.println(System.currentTimeMillis() - startTime);
		
		System.out.println("KO");
		System.out.println("Component.width : " + pixels.length + " Height : " + pixels[0].length);
		
		BufferedImage rslt= new BufferedImage(pixels.length, pixels[0].length, BufferedImage.TYPE_INT_RGB);
		System.out.println("OK");

		Graphics g = rslt.getGraphics();
		int x = 0;
		int y = 0;
		int count=0;
		
		for (String c:mozaikS){
			//System.out.println("from mozaikS : " +c);
			BufferedImage bi = null;
			try {
				bi = ImageIO.read(new File(c));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Persist.ERROR_FILE_NOT_FOUND;
			}
		 	/*
			for (int i=0;i<bi.getWidth();i++){
				for (int j=0;j<bi.getHeight();j++){
						bi.setRGB(i, j, bi.getRGB(i, j)+ corCol.get(count).getRGB());
				}
			}
			*/
			g.drawImage(bi, x, y, grain, grain, null);
			x+=grain;
			
			if (x>=rslt.getWidth()){
				y+=grain;
				x=0;
			}
			count++;
		}
		
		System.out.println("GRAPHICS");
		System.out.print("DRAW OPERATION : ");
		System.out.println(System.currentTimeMillis() - startTime);
		try {
			if(!Persist.DEST_MOZAIK_REPOSITORY.exists() || !Persist.DEST_MOZAIK_REPOSITORY.isDirectory())
				Persist.DEST_MOZAIK_REPOSITORY.mkdir();
			String rsltFileName = Persist.DEST_MOZAIK_REPOSITORY_PATH + File.separator + originalFileName;
			System.out.println("OUTPUT FILEPATH : " + rsltFileName);
			ImageIO.write(rslt, "jpg", new File(rsltFileName));
			
			emptyLibrary();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("IOException... cannot write on output.jpg file...");
			return Persist.ERROR_FILE_NOT_FOUND;
		}
		System.out.println("Mozaik generated!");
		return Persist.SUCCESS;
	}
	
	
	public int generateMozaik(int grain) {
		long startTime = System.currentTimeMillis();
		// la matrice RGB de tous les pixels de l'image contenue dans component
		int [][][] pixels=component.getPixel();
		// la matrice RGB de toutes les tuiles de l'image contenue dans component
		int [][][] weighedpixels=new int[pixels.length/grain][pixels[0].length/grain][3];
		
		for (int i=0;i<weighedpixels.length;i++){
			for (int j=0;j<weighedpixels[0].length;j++){
				weighedpixels[i][j][0]=0;
				weighedpixels[i][j][1]=0;
				weighedpixels[i][j][2]=0;
				for (int k=0;k<grain;k++){
					for (int l=0;l<grain;l++){
						weighedpixels[i][j][0]+=pixels[grain*i+k][grain*j+l][0];
						weighedpixels[i][j][1]+=pixels[grain*i+k][grain*j+l][1];
						weighedpixels[i][j][2]+=pixels[grain*i+k][grain*j+l][2];
					}
				}
				weighedpixels[i][j][0]=weighedpixels[i][j][0]/(grain*grain);
				weighedpixels[i][j][1]=weighedpixels[i][j][1]/(grain*grain);
				weighedpixels[i][j][2]=weighedpixels[i][j][2]/(grain*grain);
			}
		}
		System.out.print("INITIALIZATION : ");
		System.out.println(System.currentTimeMillis() - startTime);

		HashMap<BufferedImage, Integer[]> collection=new HashMap<BufferedImage,Integer[]>();
		for (BufferedImage savedImg : savedImages){
			ImageComponent c = null;
			c = new ImageComponent(savedImg);
			int[] averPix=c.getAveragePixel();
			Integer[] a=new Integer[3];
			a[0]=averPix[0];
			a[1]=averPix[1];
			a[2]=averPix[2];
			collection.put(savedImg, a);
		}
		
		System.out.println("MOZAIKS");
		System.out.print("RESCALED IMAGES COLLECTION LOADING : ");
		System.out.println(System.currentTimeMillis() - startTime);
		ArrayList<BufferedImage> mozaikS=new ArrayList<BufferedImage>();
		
		//HashMap<String,Integer> iter=new HashMap<String, Integer>();
		/*
		for (String c:library){
			iter.put(c,0);
		}
		*/
			
			
		System.out.println("COLLECTION.SIZE() : " + collection.size());
		
		System.out.println("PIXELS.length : " + pixels.length + " PIXELS[0].LENGTH : " + pixels[0].length);
		System.out.println("WEIGHTEDPIXELS.length : " + weighedpixels.length + " WEIGHTEDPIXELS[0].LENGTH : " + weighedpixels[0].length);
		for (int i=0; i<weighedpixels[0].length;i++){
			for (int j=0;j<weighedpixels.length;j++){
				int distance=Integer.MAX_VALUE;
				//int itofclos=0;
				BufferedImage closer=null;
				//System.out.println("< ================ ["+i+"/"+weighedpixels.length+"]["+j+"/"+weighedpixels[0].length+"] ================ >");
				for (BufferedImage c:collection.keySet()){
					int d=(weighedpixels[j][i][0]-collection.get(c)[0])*(weighedpixels[j][i][0]-collection.get(c)[0])+(weighedpixels[j][i][1]-collection.get(c)[1])*(weighedpixels[j][i][1]-collection.get(c)[1])+(weighedpixels[j][i][2]-collection.get(c)[2])*(weighedpixels[j][i][2]-collection.get(c)[2]);
					//System.out.println("from library : " + c + " d="+d);
					if (d<distance){
						distance=d;
						closer=c;
						//itofclos=iter.get(c);
					}
				}
				//System.out.println("STEP");
/*				if (closer==null){
					for (String c:library){
						int d=(weighedpixels[j][i][0]-collection.get(c)[0])*(weighedpixels[j][i][0]-collection.get(c)[0])+(weighedpixels[j][i][1]-collection.get(c)[1])*(weighedpixels[j][i][1]-collection.get(c)[1])+(weighedpixels[j][i][2]-collection.get(c)[2])*(weighedpixels[j][i][2]-collection.get(c)[2]);
						if (d<distance){
							System.out.println("from library : "+c);
							distance=d;
							closer=c;
						}
					}
				}
*/
			//	corCol.add(new Color(red,green,blue));
				mozaikS.add(closer);
				//System.out.println(" ................................... ");
			}
			//System.out.println("++++++++++++++++++++++++++++++++++");
		}

		System.out.print("CLOSEST COLOR IMAGES LOADING : ");
		System.out.println(System.currentTimeMillis() - startTime);
		
		System.out.println("KO");
		System.out.println("Component.width : " + pixels.length + " Height : " + pixels[0].length);
		
		BufferedImage rslt= new BufferedImage(pixels.length, pixels[0].length, BufferedImage.TYPE_INT_RGB);
		System.out.println("OK");

		Graphics g = rslt.getGraphics();
		int x = 0;
		int y = 0;
		int count=0;
		
		for (BufferedImage bi:mozaikS){
			//System.out.println("from mozaikS : " +c);
			g.drawImage(bi, x, y, grain, grain, null);
			x+=grain;
			
			if (x>=rslt.getWidth()){
				y+=grain;
				x=0;
			}
			count++;
		}
		
		System.out.println("GRAPHICS");
		System.out.print("DRAW OPERATION : ");
		System.out.println(System.currentTimeMillis() - startTime + " ms");
		System.out.println("now is : " + System.currentTimeMillis());
		
		this.storedFilename = Tools.addCurrentTimeMillis(this.originalFileName);
		File output = new File(this.storedFilename);
//        File output = Paths.get(originalFileName).toFile();
        System.out.println("output file created");
		System.out.println(output.getAbsolutePath());
		
		try {
			if(ImageIO.write(rslt, "jpg", output) == false)
				System.out.println("ERROR ON ImageIO.write");
			Persist.RESIZED_IMAGES.clear();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Persist.ERROR_FILE_NOT_FOUND;
		}
		
		/*
		try {
			if(!Persist.DEST_MOZAIK_REPOSITORY.exists() || !Persist.DEST_MOZAIK_REPOSITORY.isDirectory())
				Persist.DEST_MOZAIK_REPOSITORY.mkdir();
			String rsltFileName = Persist.DEST_MOZAIK_REPOSITORY_PATH + File.separator + originalFileName;
			System.out.println("OUTPUT FILEPATH : " + rsltFileName);
			ImageIO.write(rslt, "jpg", new File(rsltFileName));
			
			emptyLibrary();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("IOException... cannot write on output.jpg file...");
			return Persist.ERROR_FILE_NOT_FOUND;
		}
		*/
		
		
		int finalRslt = AmazonUtilities.uploadImagesAmazonAPI(output);
		if(finalRslt == Persist.SUCCESS)
			System.out.println("Mozaik generated!");
		return finalRslt;
	}
	
	private void loadLibrary(int grain) {
		library=new ArrayList<String>();
		for(File f : Persist.FROM_REPOSITORY.listFiles()){
			if (f.length() != 0 && f.isFile()){
				//System.out.println("loaded path(file) : " + f.getPath());
				//ImageResizer.resize(f.getPath(), "images_save" + File.separator + "rescale" + File.separator + f.getName(), grain, grain);
				library.add("images_save" + File.separator + "rescale" + File.separator + f.getName());
			}
		}
		//System.out.println("NB_IMAGES_LOADED : " + library.size());
	}
	
	
	private void emptyLibrary(){
		for (File f : Persist.FROM_REPOSITORY.listFiles())
			if(f.isFile())
				f.delete();
	}

}
