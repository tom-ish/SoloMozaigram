package utils;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import hibernate_entity.Comment;
import hibernate_entity.Friendship;
import hibernate_entity.Image;
import hibernate_entity.Library;
import hibernate_entity.User;
import hibernate_entity.UserSession;
import hibernate_entity.UserTask;

public class HibernateUtil {


	private static final SessionFactory sessionFactory;
	static {
		try {
			// Créé la SessionFactory
			sessionFactory = new Configuration()
					.configure("hibernate.cfg.xml")
					.addAnnotatedClass(User.class)
					.addAnnotatedClass(Image.class)
					.addAnnotatedClass(Library.class)
					.addAnnotatedClass(UserSession.class)
					.addAnnotatedClass(Friendship.class)
					.addAnnotatedClass(Comment.class)
					.addAnnotatedClass(UserTask.class)
					.buildSessionFactory();
		} catch (HibernateException e) {
			throw new RuntimeException("Probleme de configuration : " + e.getMessage(), e);
		}
	}

	public static final ThreadLocal<Session> session = new ThreadLocal<Session>();
	public static Session currentSession() throws HibernateException {
		Session s = (Session) session.get();
		// ouvre une nouvelle Session, si ce Thread n'en a aucune
		if(s == null) {
			s = sessionFactory.openSession();
			session.set(s);
		}
		return s;
	}

	public static void closeSession() throws HibernateException {
		Session s = (Session) session.get();
		session.set(null);
		if(s != null) {
			s.close();
		}
	}

}
