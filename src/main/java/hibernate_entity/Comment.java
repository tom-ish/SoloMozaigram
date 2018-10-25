package hibernate_entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="comments")
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="imgid", nullable=false)
	private Image img;
	
	@ManyToOne
	@JoinColumn(name="authorid", nullable=false)
	private User auteur;
	
	@Column(name="text")
	private String text;
	
	@Column(name="date")
	@CreationTimestamp
	private Date date;
	
	public Comment() {}
	
	public Comment(String text, User auteur, Image img) {
		this.text = text;
		this.auteur = auteur;
		this.img = img;
	}
	
	public int getId() { return this.id; }
	public Image getImage() { return this.img; }
	public User getAuteur() { return this.auteur; }
	public String getText() { return this.text; }
	public Date getDate() { return this.date; }
	
	public void setId(int id) { this.id = id; }
	public void setImage(Image img) { this.img = img; }
	public void setAuteur(User auteur) { this.auteur = auteur; }
	public void setDate(Date date) { this.date = date; }

	@Override
	public String toString() {
		return "Comment [id=" + id + ", img=" + img + ", auteur=" + auteur + ", text=" + text + ", date=" + date + "]";
	}

}