package it.vige.businesscomponents.injection.inject.impl;

import static it.vige.businesscomponents.injection.inject.model.StateBook.PUBLISHED;
import static java.util.Arrays.asList;

import java.util.List;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

import it.vige.businesscomponents.injection.inject.Published;
import it.vige.businesscomponents.injection.inject.Service;
import it.vige.businesscomponents.injection.inject.model.Book;

@Any
public class PublishService implements Service {

	@Published
	@Produces
	public List<Book> getPublishedBooks() {
		Book[] books = new Book[] {
				new Book("Mastering Java EE Development with WildFly 10", "Luca Stancapiano", PUBLISHED),
				new Book("Gatein Cookbook", "Luca Stancapiano", PUBLISHED) };
		return asList(books);
	}

	public void close(@Disposes @Published List<Book> books) {
		books.clear();
	}
}
