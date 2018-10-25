package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import database.DBAuthentification;
import database.DBComments;
import database.DBImage;
import database.DBSessionKey;
import hibernate_entity.Comment;
import hibernate_entity.Image;
import hibernate_entity.User;
import utils.Persist;
import utils.Tools;

public class ServicesMyMozaik {
	
	public static JSONArray buildMozaikThumnails(String sessionkey) {
		if(Tools.isNullParameter(sessionkey))
			return null;
		User user = DBSessionKey.getUserByKey(sessionkey);
		JSONArray res = new JSONArray();
		
		//get all images of user with sessionkey in parameters
//		List<Image> images = DBImage.getImageFromUserId(user.getId());		
		List<Image> images = DBImage.getUserImages(user);
		
		//get all comments from each image
		Map<Image, List<Comment>> mapComments= new HashMap<>();
		for(Image img : images) {
			mapComments.put(img, DBComments.getCommentsFromImage(img));
		}
		
		//build json structure 
		for(Entry<Image, List<Comment>> mapc : mapComments.entrySet()) {
			JSONArray commentsArray = new JSONArray();
			JSONObject imgjson = new JSONObject();
			imgjson.put("id", mapc.getKey().getId());
			imgjson.put("link", mapc.getKey().getLink());
			imgjson.put("username",user.getUsername());
			
			for(Comment c : mapc.getValue()) {
				JSONObject commentObject = new JSONObject();
				commentObject.put("author", c.getAuteur().getUsername());
				commentObject.put("comment", c.getText());
				commentObject.put("date", c.getDate());
				commentsArray.put(commentObject);
			}
			
			imgjson.put("comments", commentsArray);
			
			//add all in the final JSONArray
			res.put(imgjson);
		}
		return res;
	}
	
	public static JSONArray buildMozaikThumnails(String sessionkey, String searchRsltUsername) {
		if(Tools.isNullParameter(sessionkey) || Tools.isNullParameter(searchRsltUsername))
			return null;
		User user = DBAuthentification.getUserByUsername(searchRsltUsername);
		JSONArray res = new JSONArray();
		
		//get all images of user with sessionkey in parameters
//		List<Image> images = DBImage.getImageFromUserId(user.getId());		
		List<Image> images = DBImage.getUserImages(user);
		
		//get all comments from each image
		Map<Image, List<Comment>> mapComments= new HashMap<>();
		for(Image img : images) {
			mapComments.put(img, DBComments.getCommentsFromImage(img));
		}
		
		//build json structure 
		for(Entry<Image, List<Comment>> mapc : mapComments.entrySet()) {
			JSONArray commentsArray = new JSONArray();
			JSONObject imgjson = new JSONObject();
			imgjson.put("id", mapc.getKey().getId());
			imgjson.put("link", mapc.getKey().getLink());
			imgjson.put("username",user.getUsername());
			
			for(Comment c : mapc.getValue()) {
				JSONObject commentObject = new JSONObject();
				commentObject.put("author", c.getAuteur().getUsername());
				commentObject.put("comment", c.getText());
				commentObject.put("date", c.getDate());
				commentsArray.put(commentObject);
			}
			
			imgjson.put("comments", commentsArray);
			
			//add all in the final JSONArray
			res.put(imgjson);
		}
		return res;
	}
}
