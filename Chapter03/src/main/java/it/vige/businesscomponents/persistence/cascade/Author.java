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
import javax.persistence.ManyToMany;

@Entity
public class Author {

	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;

	@Column(name = "full_name", nullable = false)
	private String fullName;

	@ManyToMany(mappedBy = "authors", cascade = { PERSIST, MERGE })
	private List<Book> books = new ArrayList<>();

	public Author() {
	}

	public Author(String fullName) {
		this.fullName = fullName;
	}

	public Long getId() {
		return id;
	}

	public void addBook(Book book) {
		books.add(book);
		book.getAuthors().add(this);
	}

	public void removeBook(Book book) {
		books.remove(book);
		book.getAuthors().remove(this);
	}

	public void remove() {
		for (Book book : new ArrayList<>(books)) {
			removeBook(book);
		}
	}
}