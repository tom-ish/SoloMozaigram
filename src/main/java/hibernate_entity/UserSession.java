package hibernate_entity;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="user_session")
public class UserSession {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="sessionkey")
	private String sessionkey;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="userid")
	private User user;
	
	@Column(name="since")
	@CreationTimestamp
	private Date since;
	
	public UserSession() {}
	
	public UserSession(String sessionkey) {
		this.sessionkey = sessionkey;
	}
	
	public int getId() { return this.id; }
	public String getSessionkey() { return this.sessionkey; }
	public Date getSince() { return this.since; }
	public User getUser() { return this.user; }
	
	public void setId(int id) { this.id = id; }
	public void setSessionkey(String sessionkey) { this.sessionkey = sessionkey; }
	public void setSince(Date created) { this.since = created; }
	public void setUser(User user) { this.user = user; }

	@Override
	public String toString() {
		return "UserSession [sessionkey=" + sessionkey + ", userId=" + this.user.getId() + ", since=" + since + "]";
	}

}
