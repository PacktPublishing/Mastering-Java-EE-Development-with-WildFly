package it.vige.businesscomponents.injection.inject.impl;

import static it.vige.businesscomponents.injection.inject.model.StateBook.DRAFT;
import static java.util.Arrays.asList;

import java.util.List;

import javax.enterprise.inject.Produces;

import it.vige.businesscomponents.injection.inject.Draft;
import it.vige.businesscomponents.injection.inject.Service;
import it.vige.businesscomponents.injection.inject.model.Book;

public class BookService implements Service {

	@Draft
	@Produces
	public List<Book> getDraftBooks() {
		Book[] books = new Book[] { new Book("Glassfish", "Luca Stancapiano", DRAFT),
				new Book("Maven working", "Luca Stancapiano", DRAFT) };
		return asList(books);
	}
}
