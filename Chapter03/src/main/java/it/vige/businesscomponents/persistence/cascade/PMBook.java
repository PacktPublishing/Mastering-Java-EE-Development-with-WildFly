package it.vige.businesscomponents.persistence.cascade;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.GenerationType.AUTO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class PMBook {

	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;

	@Column(name = "title", nullable = false)
	private String title;

	@ManyToMany(cascade = { PERSIST, MERGE })
	@JoinTable(name = "Book_PM_Author", joinColumns = {
			@JoinColumn(name = "book_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "author_id", referencedColumnName = "id") })
	private List<PMAuthor> pmAuthors = new ArrayList<>();

	public PMBook() {
	}

	public PMBook(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<PMAuthor> getPmAuthors() {
		return pmAuthors;
	}

	public void setPmAuthors(List<PMAuthor> pmAuthors) {
		this.pmAuthors = pmAuthors;
	}
}
