package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.cloudinary.Cloudinary;

import hibernate_entity.Comment;
import hibernate_entity.Friendship;
import hibernate_entity.Image;
import hibernate_entity.Library;
import hibernate_entity.User;
import hibernate_entity.UserSession;
import hibernate_entity.UserTask;
import utils.Persist;

public class DBStatic {
	
	//private static String mysql_host = "132.227.201.129:3306";	// from home: 132.227.201.129
	private static String mysql_host="localhost:3307";
	private static String mysql_db= "mozaik_generator";
	private static String mysql_username = "stldar";
	private static String mysql_password = "stldar$";
	private static boolean mysql_pooling = false;
	private static Database database = null;
	
	
	public static Connection getMySQLConnection() throws SQLException {
		try {
			System.out.println("1");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			System.out.println("2");
			if(DBStatic.mysql_pooling == false)
				return (DriverManager.getConnection("jdbc:mysql://" + DBStatic.mysql_host + "/" + DBStatic.mysql_db,DBStatic.mysql_username,DBStatic.mysql_password));
			else{
				if(database == null)
					database = new Database("jdbc/db");
				return (database.getConnection());
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (DriverManager.getConnection("jdbc:mysql://" + DBStatic.mysql_host + "/" + DBStatic.mysql_db,DBStatic.mysql_username,DBStatic.mysql_password));
	}

	private static boolean postgresql_pooling = false;
	private static String postgresql_username = System.getenv(Persist.PSQL_USERNAME);
	private static String postgresql_password = System.getenv(Persist.PSQL_PASSWORD);
	
	public static Connection getPostgreSQLConnection() throws SQLException {
		String url = System.getenv(Persist.AWS_URL);
		try {
			Class.forName("org.postgresql.Driver").newInstance();
			if(DBStatic.postgresql_pooling == false)
				return (DriverManager.getConnection(url, DBStatic.postgresql_username, DBStatic.postgresql_password));
			else {
				if(database == null)
					database =  new Database("jdbc/db");
				return (database.getConnection());
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (DriverManager.getConnection(url, DBStatic.postgresql_username, DBStatic.postgresql_password));
	}
	
	public static Session getHibernateSession() {
		SessionFactory factory = new Configuration()
				.configure("hibernate.cfg.xml")
				.addAnnotatedClass(User.class)
				.addAnnotatedClass(Image.class)
				.addAnnotatedClass(Library.class)
				.addAnnotatedClass(UserSession.class)
				.addAnnotatedClass(Friendship.class)
				.addAnnotatedClass(Comment.class)
				.addAnnotatedClass(UserTask.class)
				.buildSessionFactory();
		return factory.openSession();
	}
	
	private static String CLOUDINARY_CLOUD_NAME = System.getenv("CLOUDINARY_CLOUD_NAME");
	private static String CLOUDINARY_API_KEY = System.getenv("CLOUDINARY_API_KEY");
	private static String CLOUDINARY_API_SECRET = System.getenv("CLOUDINARY_API_SECRET");
	public static Cloudinary getCloudinaryInstance() {
		Map<String, String> config = new HashMap<String, String>();
		config.put("cloud_name", CLOUDINARY_CLOUD_NAME);
		config.put("api_key", CLOUDINARY_API_KEY);
		config.put("api_secret", CLOUDINARY_API_SECRET);
		Cloudinary cloudinary = new Cloudinary(config);
		return cloudinary;
	}
	
	/*
	public static Connection getConnection() {
		String dburl = System.getenv("DATABASE_URL");
		if(dburl == null || dburl.isEmpty()) {
			dburl = url;
 
		}
		return (DriverManager.getConnection(dburl, DBStatic.postgresql_username, DBStatic.postgresql_password));
	}
	*/
}
