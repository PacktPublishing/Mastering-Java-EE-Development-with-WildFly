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
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "book", indexes = { @Index(name = "index_title", columnList = "title", unique = true),
		@Index(name = "index_publisher", columnList = "publisher", unique = false) })
public class Book {

	@Id
	@GeneratedValue(strategy = AUTO)
	private Long id;

	@Column(name = "title", nullable = false)
	private String title;

	private String publisher;

	@ManyToMany(cascade = { PERSIST, MERGE })
	@JoinTable(name = "Book_Author", joinColumns = {
			@JoinColumn(name = "book_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "author_id", referencedColumnName = "id") })
	private List<Author> authors = new ArrayList<>();

	public Book() {
	}

	public Book(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
}
