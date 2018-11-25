package services;

import java.util.List;

import org.json.JSONObject;

import database.DBImage;
import database.DBSessionKey;
import database.DBUserTask;
import hibernate_entity.Image;
import hibernate_entity.UserSession;
import hibernate_entity.UserTask;
import utils.Persist;
import utils.Tools;

public class ServicesUserTask {
	
	public static Integer getImage(String sessionkey, String userTaskId, JSONObject json) {
		if(Tools.isNullParameter(sessionkey))
			return Persist.ERROR_NULL_PARAMETER;
		else if(DBSessionKey.isSessionKeyExpired(sessionkey) == Persist.ERROR_SESSION_KEY_EXPIRED)
			return Persist.ERROR_SESSION_KEY_EXPIRED;
		else if(DBSessionKey.isSessionKeyExpired(sessionkey) == Persist.ERROR_SESSION_KEY_NOT_FOUND)
			return Persist.ERROR_SESSION_KEY_NOT_FOUND;
		else {
			UserSession userSession = DBSessionKey.getUserSessionFromSessionKey(sessionkey);
			UserTask userTask = DBUserTask.getUserTask(userSession, Integer.valueOf(userTaskId));
			if(userTask != null) {
				if(DBUserTask.isUserTaskCompleted(userSession, userTask.getId()) == Persist.OK) {
					System.out.println("TASK COMPLETED");
					Image img = DBImage.getImgFromUserTask(userTask);
					if(img != null) {
						// Remove UserTask
						if(!DBUserTask.removeUserTask(userSession, userTask.getId())) {
							json.put("userTaskRemoved", ""+Persist.KO);
							return Persist.ERROR_DB_USER_TASK_CANNOT_BE_DELETED;
						}
						else {
							json.put("userTaskRemoved", ""+Persist.OK);
							json.put("status", ""+Persist.PROCESS_COMPLETE);
							json.put("imgPath", ""+userTask.getPath());
							json.put("imgOriginalFilename", img.getOriginalFilename());
							json.put("imgCreationDate", img.getCreationDate());
						}
						
					}
				}
				else {
					json.put("status", ""+Persist.PROCESS_NOT_COMPLETED_YET);
				}
			}
			else {
				json.put("userTaskError", Persist.ERROR_DB_USER_TASK_NOT_FOUND);
			}
			return Persist.SUCCESS; 
		}
	}

}
