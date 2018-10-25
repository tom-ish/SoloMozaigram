package services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;

import database.DBAuthentification;
import database.DBFriendship;
import database.DBSessionKey;
import hibernate_entity.User;
import utils.Persist;
import utils.Tools;

public class SearchService {
	
		
	public static int verifyParameters(String sessionkey, String searchword) {
		if(Tools.isNullParameter(sessionkey) || Tools.isNullParameter(searchword))
			return Persist.ERROR_NULL_PARAMETER;

		//		if(DBSessionKey.isSessionKeyExpired(userId))		
		return Persist.SUCCESS;
	}
	
	
	public static ArrayList<String> searchResults(String searchword) {
		ArrayList<String> users=DBAuthentification.getUserNames();
		ArrayList<String> results=new ArrayList<String>();
		
		for (String user:users) {
			if (user.contains(searchword)) {
				results.add(user);
			}
		}
		return results;
		
	}
	
	public static int searchResults(String sessionkey, String searchWord, JSONObject json) {
		if(Tools.isNullParameter(searchWord))
			return Persist.ERROR_NULL_PARAMETER;
		else if(DBSessionKey.isSessionKeyExpired(sessionkey) == Persist.ERROR_SESSION_KEY_EXPIRED)
			return Persist.ERROR_SESSION_KEY_EXPIRED;
		else if(DBSessionKey.isSessionKeyExpired(sessionkey) == Persist.ERROR_SESSION_KEY_NOT_FOUND)
			return Persist.ERROR_SESSION_KEY_NOT_FOUND;
		else {
			User u = DBSessionKey.getUserByKey(sessionkey);
			Set<User> rslt = new HashSet<User>();
			Set<User> users = DBAuthentification.getAllUsers();
			Set<Boolean> isFriend = new HashSet<Boolean>();
			for(User user : users) {
				if(u.getUsername().contains(searchWord)) {
					rslt.add(u);
					if(DBFriendship.isFriend(u, user))
						isFriend.add(true);
					else 
						isFriend.add(false);
				}
			}
			
			return Persist.SUCCESS;
		}

	}
	

	
}
