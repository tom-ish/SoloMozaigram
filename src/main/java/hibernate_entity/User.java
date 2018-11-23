package hibernate_entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;
	
	@Column(name="email")
	private String email;
	
	@Column(name="creation_date")
	@CreationTimestamp
	private Date creationDate;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="user")
	private Set<Friendship> friends;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="user")
	private Set<Library> libraries;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="auteur")
	private Set<Comment> comments;
	
	private User user;
		
	public User() {}
	
	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.friends = new HashSet<Friendship>();
		this.libraries = new HashSet<Library>();
		this.comments = new HashSet<Comment>();
		this.user = new User();
	}
	
	public int getId() { return this.id; }
	public String getUsername() { return this.username; }
	public String getPassword() { return this.password; } 
	public String getEmail() { return this.email; }
	public Date getCreationDate() { return this.creationDate; }
	public Set<Friendship> getAllFriends() { return this.friends; }
	public Set<Library> getAllLibraries() { return this.libraries; }
	public Set<Comment> getAllComments() { return this.comments; }

	public void setId(int id) { this.id =  id; }
	public void setUsername(String username) { this.username = username; }
	public void setPassword(String password) { this.password = password; }
	public void setEmail(String email) { this.email = email; }
	public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
	public void setFriends(Set<Friendship> friends) { this.friends = friends; }
	public void setLibraries(Set<Library> libraries) { this.libraries = libraries; }
	public void setComments(Set<Comment> comments) { this.comments = comments; }
	
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", email=" + email + "]";
	}

	
}
