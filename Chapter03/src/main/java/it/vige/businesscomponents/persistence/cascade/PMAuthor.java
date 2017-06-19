package it.vige.businesscomponents.persistence.cascade;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.AUTO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class PMAuthor {

	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;

	@Column(name = "full_name", nullable = false)
	private String fullName;

	@ManyToMany(mappedBy = "pmAuthors", cascade = { ALL })
	private List<PMBook> pmBooks = new ArrayList<>();

	public PMAuthor() {
	}

	public PMAuthor(String fullName) {
		this.fullName = fullName;
	}

	public Long getId() {
		return id;
	}

	public void addBook(PMBook book) {
		pmBooks.add(book);
		book.getPmAuthors().add(this);
	}

	public void removeBook(PMBook book) {
		pmBooks.remove(book);
		book.getPmAuthors().remove(this);
	}

	public void remove() {
		for (PMBook book : new ArrayList<>(pmBooks)) {
			removeBook(book);
		}
	}
}