package database;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import hibernate_entity.Friendship;
import hibernate_entity.User;
import utils.HibernateUtil;
import utils.Persist;
import utils.Tools;

public class DBFriendship {

	public static int addFriend(User user, User friend) {
		if(user.getId() == friend.getId())
			return Persist.ERROR_FRIENDSHIP_SAME_USER_ID;

		if(user != null && friend != null) {
			Friendship friendship = new Friendship();
			friendship.setFriend(friend);
			friendship.setUser(user);
			friendship.setState(Persist.STATUS_FRIENDSHIP_REQUEST_SENT);
			Session session = HibernateUtil.currentSession();
			Transaction tx = null;
			try {
				if(session != null) {
					tx = session.beginTransaction();
					session.save(friendship);
					session.flush();
					tx.commit();
					HibernateUtil.closeSession();
					return Persist.SUCCESS;
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
		return Persist.ERROR;
	}

	public static int getFriendshipRequestStatus(User user, User friend) {
		String hql = "from Friendship as f where f.user=:user and f.friend=:friend";
		Session session = HibernateUtil.currentSession();
		if(session != null) {
			try {
				List<Friendship> friendships = session.createQuery(hql)
						.setParameter("user", user)
						.setParameter("friend", friend)
						.getResultList();
				for(Friendship friendship : friendships) {
					if(friendship.getUser().equals(user) && friendship.getFriend().equals(friend)) {
						HibernateUtil.closeSession();
						return friendship.getState();
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
		return Persist.ERROR_FRIENDSHIP_NOT_FOUND;
	}

	public static boolean isFriend(User user1, User user2) {
		List<User> friendsOfUser1 = getAllFriendsIds(user1);
		for(User friendIdFromUser1 : friendsOfUser1) {
			if(friendIdFromUser1.equals(user2))
				return true;
		}

		List<User> friendsOfUser2 = getAllFriendsIds(user2);
		for(User friendIdFromUser2 : friendsOfUser2) {
			if(friendIdFromUser2.equals(user1))
				return true;
		}	
		return false;
	}

	public static List<User> getAllFriendsIds(User user) {
		String hql = "from Friendship as f where f.user=:user";
		ArrayList<User> rslt = new ArrayList<User>();
		Session session = HibernateUtil.currentSession();
		if(session != null) {
			try {
				List<Friendship> friendships = session.createQuery(hql)
						.setParameter("user", user)
						.getResultList();
				for(Friendship friendship : friendships)
					if(friendship.getUser().equals(user))
						rslt.add(friendship.getFriend());
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

	public static Set<User> getAllFriends(User user) {
		String hql = "from Friendship as f where f.user=:user";
		Set<User> rslt = new HashSet<User>();

		Session session = HibernateUtil.currentSession();
		if(session != null) {
			try {
				List<Friendship> friendships = session.createQuery(hql)
						.setParameter("user", user)
						.getResultList();
				for(Friendship friendship : friendships)
					if(friendship.getUser().equals(user))
						for(Friendship f : friendship.getUser().getAllFriends())
							if(f.getState() == Persist.STATUS_FRIENDSHIP_REQUEST_ACCEPTED)
								rslt.add(f.getFriend());
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

	public static Set<User> getAllFriendRequests(User user) {
		String hql = "from Friendship as f where f.user=:user";
		Set<User> rslt = new HashSet<User>();

		Session session = HibernateUtil.currentSession();
		if(session != null) {
			try {
				List<Friendship> friendships = session.createQuery(hql)
						.setParameter("user", user)
						.getResultList();
				for(Friendship friendship : friendships)
					if(friendship.getUser().equals(user))
						for(Friendship f : friendship.getUser().getAllFriends())
							if(f.getState() == Persist.STATUS_FRIENDSHIP_REQUEST_SENT)
								rslt.add(f.getFriend());
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


}
