package database;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import hibernate_entity.Image;
import hibernate_entity.Library;
import hibernate_entity.User;
import utils.HibernateUtil;
import utils.Persist;

public class DBLibrary {

	public static int addImageToLibrary(User user, Image img, Library library) {
		System.out.println("addImageToLibrary() called");
		System.out.println(user);
		System.out.println(img);
		System.out.println(library);
		if(user != null && img != null && library != null) {
			Session session = HibernateUtil.currentSession();
			Transaction tx = null;
			library.getImages().add(img);
			if(session != null) {
				try {
					tx = session.beginTransaction();
					session.save(library);
					session.flush();
					tx.commit();
				}
				catch(HibernateException e) {
					if(tx != null) tx.rollback();
					e.printStackTrace();
				}
				finally {
					HibernateUtil.closeSession();
				}
			}
			return Persist.SUCCESS;
		}
		return Persist.ERROR;
	}

	public static Library getUserLibraryFromName(String name) {
		String hql = "from Library as l where l.name=:name";
		Session session = HibernateUtil.currentSession();
		if(session != null) {
			try {  
				List<Library> libraries = session.createQuery(hql)
						.setParameter("name", name)
						.getResultList();
				for(Library library : libraries)
					if(library.getName().equals(name)) {
						HibernateUtil.closeSession();
						return library;
					}
			}
			catch(HibernateException e) {
				e.printStackTrace();
			}
			finally {
				HibernateUtil.closeSession();
			}		
		}
		System.out.println("NOLIBRARY FOUND");
		return null;
	}

	public static Library getUserDefaultLibrary(User user) {
		return getUserLibraryFromName(user.getUsername()+Persist.DEFAULT_LIBRARY);
	}

	public static Library createDefaultLibrary(User user) {
		Library defaultLibrary = new Library(user, user.getUsername()+Persist.DEFAULT_LIBRARY); 
		Session session = HibernateUtil.currentSession();
		Transaction tx = null;
		if(session != null) {
			try {
				tx = session.beginTransaction();
				session.save(defaultLibrary);
				session.flush();
				tx.commit();
				System.out.println("NEW DEFAULT_LIBRARY CREATED");
			}
			catch(HibernateException e) {
				if(tx != null) tx.rollback();
				e.printStackTrace();
			}
			finally {
				HibernateUtil.closeSession();
			}
		}
		return defaultLibrary;
	}

}
