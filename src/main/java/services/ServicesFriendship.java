package services;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import database.DBAuthentification;
import database.DBFriendship;
import database.DBSessionKey;
import hibernate_entity.Comment;
import hibernate_entity.User;
import utils.Persist;
import utils.Tools;

public class ServicesFriendship {
	
	public static int addFriend(String sessionkey, String friendname, JSONObject json) {
		if(Tools.isNullParameter(sessionkey))
			return Persist.ERROR_NULL_PARAMETER;
		else if(DBSessionKey.isSessionKeyExpired(sessionkey) == Persist.ERROR_SESSION_KEY_EXPIRED)
			return Persist.ERROR_SESSION_KEY_EXPIRED;
		else if(DBSessionKey.isSessionKeyExpired(sessionkey) == Persist.ERROR_SESSION_KEY_NOT_FOUND)
			return Persist.ERROR_SESSION_KEY_NOT_FOUND;
		else if(!DBAuthentification.existeLogin(friendname))
			return Persist.ERROR_FRIEND_NOT_FOUND;
		else {
			User user = DBSessionKey.getUserByKey(sessionkey);
			User friend = DBAuthentification.getUserByUsername(friendname);
			int status = DBFriendship.getFriendshipRequestStatus(user, friend);
			switch(status) {
			case Persist.STATUS_FRIENDSHIP_REQUEST_SENT :
			case Persist.STATUS_FRIENDSHIP_REQUEST_ACCEPTED :
				return status;
			// on ajoute un friend uniquement si on n'a pas envoyé de demande, donc seulement quand
			// il n'y a pas de relation friendship
			case Persist.ERROR_FRIENDSHIP_NOT_FOUND :
				int rslt = DBFriendship.addFriend(user, friend);
				if(rslt == Persist.SUCCESS) {
					json.put("userId", ""+user.getId());
					return Persist.SUCCESS;
				}
				else
					return rslt;
			default:
				return Persist.ERROR;
			}
		}
	}
	
	
	public static int getAllFriends(String sessionkey, JSONObject json) {
		if(Tools.isNullParameter(sessionkey))
			return Persist.ERROR_NULL_PARAMETER;
		else if(DBSessionKey.isSessionKeyExpired(sessionkey) == Persist.ERROR_SESSION_KEY_EXPIRED)
			return Persist.ERROR_SESSION_KEY_EXPIRED;
		else if(DBSessionKey.isSessionKeyExpired(sessionkey) == Persist.ERROR_SESSION_KEY_NOT_FOUND)
			return Persist.ERROR_SESSION_KEY_NOT_FOUND;
		else {
			User user = DBSessionKey.getUserByKey(sessionkey);
			JSONArray friendsArray = getJSONArrayFromUserEntry(Tools.getSecuredUserSet(DBFriendship.getAllFriends(user)));
			json.put("friends", friendsArray);
			return Persist.SUCCESS;
		}
	}
	
	public static int getAllFriendRequest(String sessionkey, JSONObject json) {
		if(Tools.isNullParameter(sessionkey))
			return Persist.ERROR_NULL_PARAMETER;
		else if(DBSessionKey.isSessionKeyExpired(sessionkey) == Persist.ERROR_SESSION_KEY_EXPIRED)
			return Persist.ERROR_SESSION_KEY_EXPIRED;
		else if(DBSessionKey.isSessionKeyExpired(sessionkey) == Persist.ERROR_SESSION_KEY_NOT_FOUND)
			return Persist.ERROR_SESSION_KEY_NOT_FOUND;
		else {
			User user = DBSessionKey.getUserByKey(sessionkey);
			JSONArray friendRequests = getJSONArrayFromUserEntry(Tools.getSecuredUserSet(DBFriendship.getAllFriendRequests(user)));
			json.put("friendRequests", friendRequests);
			return Persist.SUCCESS;
		}
	}
	
	
	public static JSONArray getJSONArrayFromUserEntry(Set<Entry<Integer, String>> entries) {
		JSONArray jsonArray = new JSONArray();
		for(Entry<Integer, String> pair : entries) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("userid", pair.getKey());
			jsonObject.put("username", pair.getValue());
			jsonArray.put(jsonObject);
		}
		return jsonArray;
	}

}
