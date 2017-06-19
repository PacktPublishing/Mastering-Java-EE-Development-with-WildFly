package it.vige.businesscomponents.injection.inject.model;

public class Book {

	private String title;
	private String author;
	private StateBook state;

	public Book(String title, String author, StateBook state) {
		super();
		this.title = title;
		this.author = author;
		this.state = state;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public StateBook getState() {
		return state;
	}

	public void setState(StateBook state) {
		this.state = state;
	}

}
