package hibernate_entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="user_task")
public class UserTask {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="since")
	@CreationTimestamp
	private Date since;
	
	@OneToOne
	@JoinColumn(name="session")
	private UserSession userSession;
	
	@Column(name="status")
	private int status;
	
	@Column(name="path")
	private String path;
	
	@Column(name="img_id")
	private int imgId;
		
	public UserTask() {}
	
	public UserTask(UserSession userSession, int status, String path) {
		this.userSession = userSession;
		this.status = status; 
		this.path = path;
	}

	public int getId() { return this.id; }
	public Date getCreationDate() { return this.since; }
	public UserSession getUserSession() { return this.userSession; }
	public int getStatus() { return this.status; }
	public String getPath() { return this.path; }
	public int getImgId() { return this.imgId; }
	
	public void setId(int id) { this.id = id; }
	public void setCreationDate(Date since) { this.since = since; }
	public void setUserSession(UserSession userSession) { this.userSession = userSession; }
	public void setStatus(int status) { this.status = status; }
	public void setPath(String path) { this.path = path; }
	public void setImgId(int imgId) { this.imgId = imgId; }

	@Override
	public String toString() {
		return "UserTask [id=" + id + ", since=" + since + ", userSession=" + userSession + ", status=" + status
				+ ", path=" + path + ", imgId=" + imgId + "]";
	}
	
}
