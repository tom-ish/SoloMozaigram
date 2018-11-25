package database;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import hibernate_entity.Comment;
import hibernate_entity.Image;
import hibernate_entity.User;
import hibernate_entity.UserTask;
import utils.HibernateUtil;
import utils.Persist;

public class DBImage {

	public static Image getImageById(int imgId) {
		String hql = "from Image as img where img.id=:id";
		Session session = HibernateUtil.currentSession();
		if(session != null) {
			try {
				List<Image> imgs = session.createQuery(hql)
						.setParameter("id", imgId)
						.getResultList();
				for(Image img : imgs) {
					if(img.getId() == imgId) {
						HibernateUtil.closeSession();
						return img;
					}
				}
			}
			finally {
				HibernateUtil.closeSession();
			}
		}
		return null;
	}

	public static List<Image> getImageFromUserId(int userid){
		String hql = "from Image image where image.id in "
				+ "(select images_id from library_images where library_id = "
				+ "(select id from library where userid ='" + userid +"'))";

		Session session = HibernateUtil.currentSession();
		if(session != null) {
			try {
				List<Image> rslt = session.createQuery(hql).getResultList();
				HibernateUtil.closeSession();
				return rslt;
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

	public static List<Image> getImages (){
		String hql="from Image";
		Session session = HibernateUtil.currentSession();
		if (session != null) {
			try {
				List<Image> imgs = session.createQuery(hql).getResultList();
				HibernateUtil.closeSession();
				return imgs;
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

	public static List<Image> getUserImages(User user) {
		ArrayList<Image> rslt = new ArrayList<Image>();
		String hql = "from Image";
		Session session = HibernateUtil.currentSession();
		if(session != null) {
			try {
				List<Image> imgs = session.createQuery(hql)
						.getResultList();
				for(Image img : imgs) {
					if(img.getUser().getId() == user.getId())
						rslt.add(img);
				}
			}
			catch(HibernateException e) {
				e.printStackTrace();
			}
			finally {
				HibernateUtil.closeSession();
			}
		}
		return rslt;
	}

	public static boolean existeImageId(int imgId) {
		String hql = "from Image as img where img.id=:id";
		Session session = HibernateUtil.currentSession();
		if(session != null) {
			try {
				List<Image> imgs = session.createQuery(hql)
						.setParameter("id", imgId)
						.getResultList();
				for(Image img : imgs) {
					if(img.getId() == imgId) {
						HibernateUtil.closeSession();
						return true;
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
		return false;
	}

	public static int addComment(Image img, String txt) {
		Comment comment = new Comment();

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

	public static Image addImage(String imgPath, String originalFilename, String keyword, User user) {
		Image img = new Image(imgPath, originalFilename, keyword, user);
		Session session = HibernateUtil.currentSession();
		Transaction tx = null;
		if(session != null) {
			try {
				tx = session.beginTransaction();
				session.save(img);
				session.flush();
				tx.commit();
				HibernateUtil.closeSession();
				System.out.println(img.toString() + " stored in DBImages");
			}
			catch(HibernateException e) {
				if(tx != null) tx.rollback();
				e.printStackTrace();
			}
			finally {
				HibernateUtil.closeSession();
			}
		}

		return img;
	}


	public static String getPathFromImgId(int id) {
		String hql = "from Image as img where img.id=:id";

		Session session = HibernateUtil.currentSession();
		if(session != null) {
			try {
				List<Image> images = session.createQuery(hql)
						.setParameter("id", id)
						.getResultList();
				for(Image img : images) {
					if(img.getId() == id) {
						HibernateUtil.closeSession();
						return img.getLink();
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

	public static Image getImgFromUserTask(UserTask userTask) {
		int imgId = userTask.getImgId();
		String hql = "from Image as img where img.id=:id";
		Session session = HibernateUtil.currentSession();
		if(session != null) {
			try {
				List<Image> images = session.createQuery(hql)
						.setParameter("id",  imgId)
						.getResultList();
				for(Image img : images) {
					if(img.getId() == imgId) {
						HibernateUtil.closeSession();
						return img;
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

}
