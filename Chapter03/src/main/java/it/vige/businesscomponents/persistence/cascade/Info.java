package it.vige.businesscomponents.persistence.cascade;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.AUTO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Info {

	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;

	private String name;

	@OneToOne(mappedBy = "info", cascade = ALL, orphanRemoval = true)
	private InfoDetails details;

	@OneToMany(cascade = ALL, mappedBy = "info", orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public InfoDetails getDetails() {
		return details;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addDetails(InfoDetails details) {
		this.details = details;
		details.setInfo(this);
	}

	public void removeDetails() {
		if (details != null) {
			details.setInfo(null);
		}
		this.details = null;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public void addComment(Comment comment) {
		comments.add(comment);
		comment.setInfo(this);
	}

	public void removeComment(Comment comment) {
		comment.setInfo(null);
		this.comments.remove(comment);
	}
}
