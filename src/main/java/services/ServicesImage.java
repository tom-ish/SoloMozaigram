package services;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import database.DBAuthentification;
import database.DBImage;
import database.DBLibrary;
import database.DBSessionKey;
import database.DBUserTask;
import hibernate_entity.Library;
import hibernate_entity.User;
import hibernate_entity.UserSession;
import hibernate_entity.UserTask;
import utils.Persist;
import utils.Tools;

public class ServicesImage {
	
	public static Integer addImage(String sessionkey, String imgPath, String originalFilename, String keyword) {
		if(Tools.isNullParameter(sessionkey) || Tools.isNullParameter(imgPath))
			return Persist.ERROR_NULL_PARAMETER;
		else if(DBSessionKey.isSessionKeyExpired(sessionkey) == Persist.ERROR_SESSION_KEY_EXPIRED)
			return Persist.ERROR_SESSION_KEY_EXPIRED;
		else if(DBSessionKey.isSessionKeyExpired(sessionkey) == Persist.ERROR_SESSION_KEY_NOT_FOUND)
			return Persist.ERROR_SESSION_KEY_NOT_FOUND;
		else {
			User user = DBSessionKey.getUserByKey(sessionkey);
			// On stocke l'adresse de l'image dans la DB Images
			String imgURL = Persist.AMAZON_S3_SERVER_URL+imgPath;
			hibernate_entity.Image img = DBImage.addImage(imgURL, originalFilename, keyword, user);

			// si l'id de l'image != 0, c'est que l'ajout s'est bien deroule
			if(img != null) {
				//  => on l'ajoute au default_library de l'user
				Library defaultLibrary = DBLibrary.getUserDefaultLibrary(user);		
				if(defaultLibrary == null)
					System.out.println("No default library found for " + user.getId() + " - " + user.getUsername());
				else {
					System.out.println("Default library found for " + user.getId() + " - " + user.getUsername() + ", Library : " + defaultLibrary);
				}
				if(DBLibrary.addImageToLibrary(user, img, defaultLibrary) == Persist.SUCCESS) {
					System.out.println("BEFORE NOTIFYING...");
					// on notifie dans la table UserTask la completion de la generation de mosaique
					UserSession userSession = DBSessionKey.getUserSessionFromSessionKey(sessionkey);
					UserTask userTask = DBUserTask.notifyUserTaskComplete(userSession, img.getLink(), img.getId());
					
					if(userTask != null)
						return Persist.SUCCESS;
					else
						return Persist.ERROR_DB_USER_TASK_NOT_FOUND;
				}
				else {
					System.out.println("ERROR_DB_LIBRARY_CANNOT_ADD_NEW_INSTANCE");
					return Persist.ERROR_DB_LIBRARY_CANNOT_ADD_NEW_INSTANCE;
				}
			}
		}
		return Persist.ERROR_DB_IMAGE_CANNOT_ADD_NEW_INSTANCE;
	}
	
	public static String getPathFromImgId(int imgId) {
		if(imgId <= 0) {
			return null;
		}
		return DBImage.getPathFromImgId(imgId);
	}
	
	public static List<String> getPathsFromUser(User user) {
		List<hibernate_entity.Image> images = DBImage.getUserImages(user);
		if(images == null)
			return null;
		List<String> rslt = new ArrayList<String>();
		for(hibernate_entity.Image img : images) {
			rslt.add(img.getLink());
		}
		return rslt;
	}
	
	public static List<hibernate_entity.Image> getImagesFromUserAsHibernateEntity(User user) {
		return DBImage.getUserImages(user);
	}
	
	public static ArrayList<String> getPathsfromUser (String username){
		List<hibernate_entity.Image> images = DBImage.getImages();
		ArrayList<String> results= new ArrayList<String>();
		for (hibernate_entity.Image i:images){
			if (i.getUser().getUsername()==username) {
				results.add(i.getLink());
			}
		}
		return results;
	}
	
	public static Image getImageFromPath(String path) {
		try {
			Image image = ImageIO.read(new File(path));
			return image;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException in getBufferedImageFromPath() - cannot read File : " + path);
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<String> getImgfromSearch(ArrayList<String> results) {
		ArrayList<String> images=new ArrayList<String>();
		for (String u:results) {
			List<String> img=getPathsFromUser(DBAuthentification.getUserByUsername(u));
			if (img.size()==1) {
				images.add(img.get(0));
			}
			else if (img.size()>=2) {
				images.add(img.get(0));
				images.add(img.get(1));
			}
		}
		return images;
	}

}
