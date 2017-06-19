package it.vige.businesscomponents.persistence.cascade;

import static javax.persistence.GenerationType.AUTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Comment {

	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;

	@ManyToOne
	private Info info;

	private String review;

	public void setInfo(Info info) {
		this.info = info;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}
}
