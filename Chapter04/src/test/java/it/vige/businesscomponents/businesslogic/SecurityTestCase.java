package it.vige.businesscomponents.businesslogic;

import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.EJBAccessException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.businesscomponents.businesslogic.security.Caller;
import it.vige.businesscomponents.businesslogic.security.Movie;
import it.vige.businesscomponents.businesslogic.security.Movies;

@RunWith(Arquillian.class)
public class SecurityTestCase {

	private static final Logger logger = getLogger(SecurityTestCase.class.getName());

	@EJB(mappedName = "java:module/Movies")
	private Movies movies;

	@EJB(mappedName = "java:module/ManagerBean")
	private Caller manager;

	@EJB(mappedName = "java:module/EmployeeBean")
	private Caller employee;

	@Deployment
	public static JavaArchive createEJBDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "security-test.jar");
		jar.addPackage(Movie.class.getPackage());
		return jar;
	}

	@Test
	public void testAsManager() throws Exception {
		manager.call(new Callable<Object>() {
			public Object call() throws Exception {

				Movie movie1 = new Movie("Sabina Guzzanti", "La trattativa", 2014);
				Movie movie2 = new Movie("Roberto Saviano", "Gomorra", 2008);
				Movie movie3 = new Movie("Joel Coen", "The Big Lebowski", 1998);
				movies.addMovie(movie1);
				movies.addMovie(movie2);
				movies.addMovie(movie3);

				List<Movie> list = movies.getMovies();
				assertEquals("List.size()", 3, list.size());

				movies.deleteMovie(movie1);
				movies.deleteMovie(movie2);
				movies.deleteMovie(movie3);

				assertEquals("Movies.getMovies()", 0, movies.getMovies().size());
				return null;
			}
		});
	}

	@Test
	public void testAsEmployee() throws Exception {
		employee.call(new Callable<Object>() {
			public Object call() throws Exception {
				Movie movie1 = new Movie("Sabina Guzzanti", "La trattativa", 2014);
				Movie movie2 = new Movie("Roberto Saviano", "Gomorra", 2008);
				Movie movie3 = new Movie("Joel Coen", "The Big Lebowski", 1998);
				movies.addMovie(movie1);
				movies.addMovie(movie2);
				movies.addMovie(movie3);

				List<Movie> list = movies.getMovies();
				assertEquals("List.size()", 3, list.size());

				try {
					movies.deleteMovie(movie1);
					fail("Employees should not be allowed to delete");
				} catch (EJBAccessException e) {
					logger.info("It go here. Good");
				}

				// The list should still be three movies long
				assertEquals("Movies.getMovies()", 3, movies.getMovies().size());
				return null;
			}
		});
	}

	@Test
	public void testUnauthenticated() throws Exception {
		try {
			movies.addMovie(new Movie("Sabina Guzzanti", "La trattativa", 2014));
			fail("Unauthenticated users should not be able to add movies");
		} catch (EJBAccessException e) {
			logger.info("It go here. Good");
		}

		try {
			movies.deleteMovie(null);
			fail("Unauthenticated users should not be allowed to delete");
		} catch (EJBAccessException e) {
			logger.info("It go here. Very good");
		}

		try {
			// Read access should be allowed

			movies.getMovies();
		} catch (EJBAccessException e) {
			fail("Read access should be allowed");
		}
	}
}
