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
public class AllAuthor {

	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;

	@Column(name = "full_name", nullable = false)
	private String fullName;

	@ManyToMany(mappedBy = "allAuthors", cascade = { ALL })
	private List<AllBook> allBooks = new ArrayList<>();

	public AllAuthor() {
	}

	public AllAuthor(String fullName) {
		this.fullName = fullName;
	}

	public Long getId() {
		return id;
	}

	public void addBook(AllBook book) {
		allBooks.add(book);
		book.getAllAuthors().add(this);
	}

	public void removeBook(AllBook book) {
		allBooks.remove(book);
		book.getAllAuthors().remove(this);
	}

	public void remove() {
		for (AllBook book : new ArrayList<>(allBooks)) {
			removeBook(book);
		}
	}
}