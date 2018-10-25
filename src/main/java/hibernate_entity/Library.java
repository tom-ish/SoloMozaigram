package hibernate_entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="library")
public class Library {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="userid", nullable=false)
	private User user;
	
	@Column(name="name")
	private String name;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="imgid")
	private Set<Image> images;

	@Column(name="creation_date")
	@CreationTimestamp
	private Date creationDate;
	
	public Library() {}
	
	public Library(User user, String name) {
		this.user = user;
		this.name = name;
		this.images = new HashSet<Image>();
	}
	
	public int getId() { return this.id; }
	public User getUser() { return this.user; }
	public Set<Image> getImages() { return this.images; }
	public Date getCreationDate() { return this.creationDate; }
	public String getName() { return this.name; }
	
	public void setId(int id) { this.id = id; }
	public void setUser(User user) { this.user = user; }
	public void setImages(Set<Image> images) { this.images = images; }
	public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
	public void setName(String name) { this.name = name; }

}
