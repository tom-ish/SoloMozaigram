package mozaik_process;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import utils.Persist;

public class MozaikGenerator {
	
	
	public static Entry<Integer, String> generate(List<BufferedImage> savedImages, Image image, String originalFileName) {
		if(image == null)
			return new AbstractMap.SimpleEntry<Integer, String>(Persist.ERROR_INPUT_STREAM_NULL, "");
		
		
		// The actual MozaikGenerator Algorithm is right here
		ImageFrame frame = new ImageFrame(savedImages, image, originalFileName);
		return frame.generateMozaik(Persist.GRAIN);
//		return frame.compute(GRAIN);
	}


}
