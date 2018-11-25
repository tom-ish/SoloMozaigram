package database;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import hibernate_entity.UserSession;
import hibernate_entity.UserTask;
import utils.HibernateUtil;
import utils.Persist;

public class DBUserTask {

	public static boolean isUserTaskCompleted(UserSession userSession, int userTaskId) {
		String hql = "from UserTask usertask where usertask.userSession=:userSession and usertask.id=:userTaskId";
		Session session = HibernateUtil.currentSession();
		if(session != null) {
			try {
				List<UserTask> userTasks = session.createQuery(hql)
						.setParameter("userSession", userSession)
						.setParameter("userTaskId", userTaskId)
						.getResultList();
				for(UserTask task : userTasks) {
					if(task.getId() == userTaskId 
							&& task.getUserSession().getSessionkey().equals(userSession.getSessionkey())
							&& task.getId() == userTaskId
							&& task.getStatus() == Persist.PROCESS_COMPLETE) {
						HibernateUtil.closeSession();
						return Persist.OK;
					}
				}
			}
			catch(HibernateException e) {
				e.printStackTrace();
			}
			finally {
				HibernateUtil.closeSession();
			}
		}
		return Persist.KO;
	}

	public static UserTask createUserTask(String sessionkey) {
		UserSession userSession = DBSessionKey.getUserSessionFromSessionKey(sessionkey);
		UserTask userTask = new UserTask(userSession, Persist.PROCESS_NOT_COMPLETED_YET, "");

		System.out.println("NEW USER TASK CREATED WITH sessionkey  " + userTask.getUserSession().getSessionkey());
		Session session = HibernateUtil.currentSession();
		Transaction tx = null;
		if(session != null) {
			try {
				tx = session.beginTransaction();
				session.save(userTask);
				session.flush();
				tx.commit();
				HibernateUtil.closeSession();
				return userTask;
			}
			catch(HibernateException e) {
				e.printStackTrace();
			}
			finally {
				HibernateUtil.closeSession();
			}
		}
		return null;
	}


	public static UserTask notifyUserTaskComplete(UserSession userSession, String path, int imgId) {
		String hql = "from UserTask";

		System.out.println("NOTIFY FUNCTION ENTERED");
		System.out.println(userSession);
		Session session = HibernateUtil.currentSession();
		Transaction tx = null;
		if(session != null) {
			try {
				List<UserTask> userTasks = session.createQuery(hql)
						.getResultList();
				for(UserTask userTask : userTasks) {
					System.out.println(userTask);
					if(userTask.getUserSession().getSessionkey().equals(userSession.getSessionkey())) {
						tx = session.beginTransaction();
						UserTask taskToUpdate = session.load(UserTask.class, userTask.getId());
						taskToUpdate.setStatus(Persist.PROCESS_COMPLETE);
						taskToUpdate.setPath(path);
						taskToUpdate.setImgId(imgId);
						session.flush();
						tx.commit();
						System.out.println("USER TASK COMPLETED");
						HibernateUtil.closeSession();
						return userTask;
					}
				}
			}
			catch(HibernateException e) {
				if(tx != null) tx.rollback();
				e.printStackTrace();
			}
			finally {
				HibernateUtil.closeSession();
			}
		}
		return null;
	}

	public static String getImgPath(UserSession userSession) {
		String hql = "from UserTask as usertask where usertask.userSession=:userSession and usertask.status=:status";
		Session session = HibernateUtil.currentSession();
		if(session != null) {
			try { 
				List<UserTask> userTasks = session.createQuery(hql)
						.setParameter("userSession", userSession)
						.setParameter("status", Persist.PROCESS_COMPLETE)
						.getResultList();

				for(UserTask userTask : userTasks) {
					if(userTask.getUserSession().getSessionkey().equals(userSession)
							&& userTask.getStatus() == Persist.PROCESS_COMPLETE
							&& !userTask.getPath().isEmpty()) {
						HibernateUtil.closeSession();
						return userTask.getPath();
					}
				}
			}
			catch(HibernateException e) {
				e.printStackTrace();
			}
			finally {
				HibernateUtil.closeSession();
			}
		}
		return "";
	}

	
	public static UserTask getUserTask(UserSession userSession, int userTaskId) {
		String hql = "from UserTask as usertask where usertask.userSession=:userSession and usertask.id=:userTaskId";
		Session session = HibernateUtil.currentSession();
		if(session != null) {
			try {
				List<UserTask> userTasks = session.createQuery(hql)
						.setParameter("userSession", userSession)
						.setParameter("userTaskId", userTaskId)
						.getResultList();
				for(UserTask userTask : userTasks) {
					if(userTask.getId() == userTaskId
							&& userTask.getUserSession().getSessionkey().equals(userSession.getSessionkey()) 
							&& userTask.getUserSession().getUser().getId() == userSession.getUser().getId()) {
						HibernateUtil.closeSession();
						return userTask;
					}
				}
			}
			catch(HibernateException e) {
				e.printStackTrace();
			}
			finally {
				HibernateUtil.closeSession();
			}
		}
		return null;
	}
	
	

/*
	public static boolean removeUserTask(UserSession userSession) {
		String hql = "delete UserTask where userSession=:userSession";

		Session session = HibernateUtil.currentSession();
		if(session != null) {
			try {
				if(session.createQuery(hql).setParameter("userSession", userSession).executeUpdate() > 0) {
					session.flush();
					HibernateUtil.closeSession();
					return true;
				}
			}
			catch(HibernateException e) {
				e.printStackTrace();
			}
			finally {
				HibernateUtil.closeSession();
			}
		}
		return false;
	}
	*/
	
	public static boolean removeUserTask(UserSession userSession, int userTaskId) {
		System.out.println("REMOVE_USER_TASK");
		String hql = "from UserTask";
		Session session = HibernateUtil.currentSession();
		Transaction tx = null;
		if(session != null) {
			try {
				List<UserTask> userTasks = session.createQuery(hql)
						.getResultList();
				for(UserTask userTask : userTasks) {
					System.out.println(userTask);
					if(userTask.getUserSession().getSessionkey().equals(userSession.getSessionkey()) && userTask.getId() == userTaskId) {
						tx = session.beginTransaction();
						session.delete(userTask);
						session.flush();
						tx.commit();
						System.out.println("USER TASK SUCCESSFULLY REMOVED");
						HibernateUtil.closeSession();
						return true;
					}
				}
			}
			catch(HibernateException e) {
				if(tx != null) tx.rollback();
				e.printStackTrace();
			}
			finally {
				HibernateUtil.closeSession();
			}
		}
		return false;
	}


}
