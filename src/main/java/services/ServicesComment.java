package services;

import org.json.JSONObject;

import database.DBComments;
import database.DBImage;
import database.DBSessionKey;
import hibernate_entity.User;
import utils.Persist;
import utils.Tools;

public class ServicesComment {
	
	public static int addComment(String sessionkey, String comment, int imgid, JSONObject json) {
		if(Tools.isNullParameter(sessionkey) || Tools.isNullParameter(comment))
			System.err.println("Sessionkey NULL :" +Persist.ERROR_NULL_PARAMETER);
		else if(DBSessionKey.isSessionKeyExpired(sessionkey) == Persist.ERROR_SESSION_KEY_EXPIRED)
			System.err.println("Sessionkey Expired : "+ Persist.ERROR_SESSION_KEY_EXPIRED);
		else if(DBSessionKey.isSessionKeyExpired(sessionkey) == Persist.ERROR_SESSION_KEY_NOT_FOUND)
			System.err.println("Sessionkey NOT FOUND : "+ Persist.ERROR_SESSION_KEY_NOT_FOUND);
		else if(!DBImage.existeImageId(imgid))
			System.err.println("Image ID NOT FOUND : "+ Persist.ERROR_FRIEND_NOT_FOUND);
		else {
			User user = DBSessionKey.getUserByKey(sessionkey);
			hibernate_entity.Image img = DBImage.getImageById(imgid);
			int rslt = DBComments.addComment(img, comment, user);
			System.out.println("ADDCOMMENTS return " + rslt);
			if(rslt==Persist.SUCCESS) {
				json.put("authorId", user.getUsername());
				json.put("text", comment);
				json.put("imgId", imgid);
				return Persist.SUCCESS;
			} 
		}
		return Persist.ERROR;
	}
}
