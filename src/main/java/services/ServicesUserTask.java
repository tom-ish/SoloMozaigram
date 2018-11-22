package services;

import org.json.JSONObject;

import database.DBSessionKey;
import database.DBUserTask;
import hibernate_entity.UserSession;
import utils.Persist;
import utils.Tools;

public class ServicesUserTask {
	
	public static Integer getImgPath(String sessionkey, JSONObject json) {
		if(Tools.isNullParameter(sessionkey))
			return Persist.ERROR_NULL_PARAMETER;
		else if(DBSessionKey.isSessionKeyExpired(sessionkey) == Persist.ERROR_SESSION_KEY_EXPIRED)
			return Persist.ERROR_SESSION_KEY_EXPIRED;
		else if(DBSessionKey.isSessionKeyExpired(sessionkey) == Persist.ERROR_SESSION_KEY_NOT_FOUND)
			return Persist.ERROR_SESSION_KEY_NOT_FOUND;
		else {
			UserSession userSession = DBSessionKey.getUserSessionFromSessionKey(sessionkey);
			if(DBUserTask.isUserTaskCompleted(userSession) == Persist.OK) {
				System.out.println("TASK COMPLETED");
				// Remove UserTask
				if(!DBUserTask.removeUserTask(userSession)) {
					json.put("userTaskRemoved", ""+Persist.KO);
					return Persist.ERROR_DB_USER_TASK_CANNOT_BE_DELETED;
				}
				else {
					json.put("userTaskRemoved", ""+Persist.OK);
					json.put("status", ""+Persist.PROCESS_COMPLETE);
					json.put("imgPath", ""+DBUserTask.getImgPath(userSession));					
				}
			}
			else {
				json.put("status", ""+Persist.PROCESS_NOT_COMPLETED_YET);
			}
			return Persist.SUCCESS; 
		}
	}

}
