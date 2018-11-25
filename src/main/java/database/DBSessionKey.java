
package database;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import hibernate_entity.User;
import hibernate_entity.UserSession;
import hibernate_entity.UserTask;
import utils.HibernateUtil;
import utils.Persist;

public class DBSessionKey {

	public static String generateKey() {
		return DBSessionKey.generateKey(32);
	}

	public static String generateKey(int length) {
		String rslt = "";
		String dictionnary = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

		for(int i = 0; i < length; i++)
			rslt += dictionnary.charAt((int) (Math.random()*dictionnary.length()));

		if(getUserByKey(rslt) != null)
			return generateKey();
		else
			return rslt;
	}

	public static User getUserByKey(String key) {
		String hql = "from UserSession as usersession where usersession.sessionkey=:sessionkey";
		Session session = HibernateUtil.currentSession();
		if(session != null) {
			try {
				List<UserSession> userSessions = session.createQuery(hql)
						.setParameter("sessionkey", key)
						.getResultList();

				for(UserSession userSession : userSessions) {
					if(userSession.getSessionkey().equals(key)) {
						HibernateUtil.closeSession();
						return userSession.getUser();
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

	public static String getSessionkeyByUsername(String username) {
		User user = DBAuthentification.getUserByUsername(username);
		String hql = "from UserSession as usersession where usersession.user=:user";
		Session session = HibernateUtil.currentSession();
		if(session != null) {
			try {
				List<UserSession> userSessions = session.createQuery(hql)
						.setParameter("user", user)
						.getResultList();
				for(UserSession userSession : userSessions) {
					if(userSession.getUser().getId() == user.getId()) {
						HibernateUtil.closeSession();
						return userSession.getSessionkey();		
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

	public static String addSessionKey(User user) {
		return addSessionKey(user, generateKey());
	}

	public static String addSessionKey(User user, String sessionkey) {
		if(user == null) return null;

		UserSession newSession = new UserSession();
		newSession.setSessionkey(sessionkey);
		newSession.setUser(user);
		Session session = HibernateUtil.currentSession();
		Transaction tx = null;
		if(session != null) {
			try {
				tx = session.beginTransaction();
				session.save(newSession);
				session.flush();
				tx.commit();
				HibernateUtil.closeSession();
				return sessionkey;
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

	public static int isSessionKeyExpired(String sessionkey) {
		String hql = "from UserSession as usersession where usersession.sessionkey=:sessionkey";

		Session session = HibernateUtil.currentSession();
		if(session != null) {
			try {
				List<UserSession> userSessions = session.createQuery(hql)
						.setParameter("sessionkey", sessionkey)
						.getResultList();
				for(UserSession userSession : userSessions) {
					System.out.println(userSession);
					if(userSession.getSessionkey().equals(sessionkey)) {
						Instant now = Instant.now();
						Calendar cal = Calendar.getInstance();
						cal.setTime(userSession.getSince());
						cal.add(Calendar.HOUR_OF_DAY, Persist.TIMEOUT_HOUR);
						Date timeout_date = cal.getTime();
						HibernateUtil.closeSession();
						if(timeout_date.toInstant().isBefore(now))
							return Persist.ERROR_SESSION_KEY_EXPIRED;
						else
							return Persist.SESSION_KEY_STILL_VALID;
					}
				}
			}
			finally {
				HibernateUtil.closeSession();
			}
		}
		System.out.println("key not found : " + sessionkey);
		return Persist.ERROR_SESSION_KEY_NOT_FOUND;
	}

	public static UserSession getUserSessionFromSessionKey(String sessionkey) {
		String hql = "from UserSession as usersession where usersession.sessionkey=:sessionkey";

		Session session = HibernateUtil.currentSession();
		if(session != null) {
			try {
				List<UserSession> userSessions = session.createQuery(hql)
						.setParameter("sessionkey", sessionkey)
						.getResultList();
				for(UserSession userSession : userSessions) {
					if(userSession.getSessionkey().equals(sessionkey)) {
						HibernateUtil.closeSession();
						return userSession;
					}
				}
			}
			finally {
				HibernateUtil.closeSession();
			}
		}
		return null;
	}

	public static int resetSessionKey(UserSession sessionToReset) {
		String hql = "from UserSession";
		Session session = HibernateUtil.currentSession();
		Transaction tx = null;
		if(session != null) {
			try {
				tx = session.beginTransaction();
				UserSession sessionToUpdate = session.load(UserSession.class, sessionToReset.getId());
				sessionToUpdate.setSince(new Date(System.currentTimeMillis()));
				session.flush();
				tx.commit();						
				HibernateUtil.closeSession();
				return Persist.RESET_SESSION_KEY_OK;
			}
			catch(HibernateException e) {
				if(tx != null) tx.rollback();
				e.printStackTrace();
			}
			finally {
				HibernateUtil.closeSession();
			}		
		}
		return Persist.RESET_SESSION_KEY_KO;
	}

}
