package database;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import hibernate_entity.Comment;
import hibernate_entity.Image;
import hibernate_entity.User;
import utils.HibernateUtil;
import utils.Persist;

public class DBComments {
	
	public static int addComment(Comment comment) {
		System.out.println("TEXTE ADD COMMENT : ");
		Session session = HibernateUtil.currentSession();
		Transaction tx = null;
		if(session != null) {
			try {
				tx = session.beginTransaction();
				session.save(comment);
				session.flush();
				tx.commit();
				HibernateUtil.closeSession();
				return Persist.SUCCESS;
			}
			catch(HibernateException e) {
				if(tx != null) tx.rollback();
				e.printStackTrace();
			}
			finally {
				HibernateUtil.closeSession();
			}
		}
		return Persist.ERROR;
	}

	public static List<Comment> getCommentsFromImage(Image img){
		String hql = "from Comment";
		List<Comment> rslt = new ArrayList<Comment>();
		Session session = HibernateUtil.currentSession();
		if(session != null) {
			try {
				List<Comment> comments = session.createQuery(hql)
						.getResultList();
				for(Comment comment : comments) {
					if(comment.getImage().getId() == img.getId())
						rslt.add(comment);
				}
			}
			finally {
				HibernateUtil.closeSession();
			}
		}
		return rslt;
	}
}
