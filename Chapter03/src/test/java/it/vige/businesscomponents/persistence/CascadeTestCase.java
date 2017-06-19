package it.vige.businesscomponents.persistence;

import static it.vige.businesscomponents.persistence.Default.createJavaArchive;
import static java.util.logging.Logger.getLogger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.businesscomponents.persistence.cascade.AllAuthor;
import it.vige.businesscomponents.persistence.cascade.AllBook;
import it.vige.businesscomponents.persistence.cascade.Author;
import it.vige.businesscomponents.persistence.cascade.Book;
import it.vige.businesscomponents.persistence.cascade.Comment;
import it.vige.businesscomponents.persistence.cascade.Info;
import it.vige.businesscomponents.persistence.cascade.InfoDetails;
import it.vige.businesscomponents.persistence.cascade.PMAuthor;
import it.vige.businesscomponents.persistence.cascade.PMBook;

@RunWith(Arquillian.class)
public class CascadeTestCase {

	private static final Logger logger = getLogger(CascadeTestCase.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	@Inject
	private UserTransaction userTransaction;

	@Deployment
	public static JavaArchive createJavaDeployment() {
		return createJavaArchive("cascade-test.jar", Info.class.getPackage());
	}

	private void clean() {
		entityManager.createQuery("delete from Author").executeUpdate();
		entityManager.createQuery("delete from Book").executeUpdate();
		entityManager.createQuery("delete from PMAuthor").executeUpdate();
		entityManager.createQuery("delete from PMBook").executeUpdate();
		entityManager.createQuery("delete from AllAuthor").executeUpdate();
		entityManager.createQuery("delete from AllBook").executeUpdate();
		entityManager.createQuery("delete from Comment").executeUpdate();
		entityManager.createQuery("delete from InfoDetails").executeUpdate();
		entityManager.createQuery("delete from Info").executeUpdate();
	}

	@Test
	public void testPersistOneToOne() {
		logger.info("starting persist one to one cascade test");
		Info info = new Info();
		info.setName("Wildfly Book Info");
		InfoDetails details = new InfoDetails();
		info.addDetails(details);
		try {
			userTransaction.begin();
			clean();
			entityManager.persist(info);
			userTransaction.commit();
		} catch (NotSupportedException | SystemException | IllegalStateException | SecurityException
				| HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
			fail();
		}
		@SuppressWarnings("unchecked")
		List<Info> infos = entityManager.createNativeQuery("select * from info").getResultList();
		assertEquals("the info table exists", 1, infos.size());
		@SuppressWarnings("unchecked")
		List<Object[]> infoDetails = entityManager.createNativeQuery("select * from infodetails").getResultList();
		assertEquals("the infoDetails table exists", 1, infoDetails.size());
		assertFalse("the infoDetails is now visible", (boolean) infoDetails.get(0)[2]);
	}

	@Test
	public void testMergeOneToOne() {
		logger.info("starting merge one to one cascade test");
		testPersistOneToOne();
		Info info = (Info) entityManager.createQuery("from Info").getSingleResult();
		info.getDetails().setVisible(true);
		try {
			userTransaction.begin();
			entityManager.merge(info);
			userTransaction.commit();
		} catch (NotSupportedException | SystemException | IllegalStateException | SecurityException
				| HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
			fail();
		}
		@SuppressWarnings("unchecked")
		List<Info> infos = entityManager.createNativeQuery("select * from info").getResultList();
		assertEquals("the info table exists", 1, infos.size());
		@SuppressWarnings("unchecked")
		List<Object[]> infoDetails = entityManager.createNativeQuery("select * from infodetails").getResultList();
		assertEquals("the infoDetails table exists", 1, infoDetails.size());
		assertTrue("the infoDetails is now visible", (boolean) infoDetails.get(0)[2]);
	}

	@Test
	public void testDeleteOneToOne() {
		logger.info("starting merge one to one cascade test");
		testPersistOneToOne();
		try {
			userTransaction.begin();
			Info info = (Info) entityManager.createQuery("from Info").getSingleResult();
			entityManager.remove(info);
			userTransaction.commit();
		} catch (NotSupportedException | SystemException | IllegalStateException | SecurityException
				| HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
			fail();
		}
		@SuppressWarnings("unchecked")
		List<Info> infos = entityManager.createNativeQuery("select * from info").getResultList();
		assertEquals("the info table has not values", 0, infos.size());
		@SuppressWarnings("unchecked")
		List<Object[]> infoDetails = entityManager.createNativeQuery("select * from infodetails").getResultList();
		assertEquals("the infoDetails table has not values", 0, infoDetails.size());
	}

	@Test
	public void testDeleteChildOneToOne() {
		logger.info("starting delete child one to one cascade test");
		testPersistOneToOne();
		try {
			userTransaction.begin();
			Info info = (Info) entityManager.createQuery("from Info").getSingleResult();
			info.removeDetails();
			userTransaction.commit();
		} catch (NotSupportedException | SystemException | IllegalStateException | SecurityException
				| HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
			fail();
		}
		@SuppressWarnings("unchecked")
		List<Info> infos = entityManager.createNativeQuery("select * from info").getResultList();
		assertEquals("the info table has values", 1, infos.size());
		@SuppressWarnings("unchecked")
		List<Object[]> infoDetails = entityManager.createNativeQuery("select * from infodetails").getResultList();
		assertEquals("the infoDetails table has not values", 0, infoDetails.size());
	}

	@Test
	public void testPersistOneToMany() {
		logger.info("starting persist one to many cascade test");
		Info info = new Info();
		info.setName("Wildfly Book Info");
		Comment comment1 = new Comment();
		comment1.setReview("Good book!");
		Comment comment2 = new Comment();
		comment2.setReview("Nice book!");
		info.addComment(comment1);
		info.addComment(comment2);
		try {
			userTransaction.begin();
			clean();
			entityManager.persist(info);
			userTransaction.commit();
		} catch (NotSupportedException | SystemException | IllegalStateException | SecurityException
				| HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
			fail();
		}
		@SuppressWarnings("unchecked")
		List<Info> infos = entityManager.createNativeQuery("select * from info").getResultList();
		assertEquals("the info table exists", 1, infos.size());
		@SuppressWarnings("unchecked")
		List<Object[]> comments = entityManager.createNativeQuery("select * from comment").getResultList();
		assertEquals("the comments table exists", 2, comments.size());
		assertEquals("the comment is done", "Good book!", comments.get(0)[1]);
	}

	@Test
	public void testMergeOneToMany() {
		logger.info("starting merge one to many cascade test");
		testPersistOneToMany();
		Info info = (Info) entityManager.createQuery("from Info").getSingleResult();
		@SuppressWarnings("unchecked")
		List<Comment> comments = entityManager.createQuery("from Comment where info = :info").setParameter("info", info)
				.getResultList();

		comments.stream().filter(comment -> comment.getReview().toLowerCase().contains("nice")).findAny()
				.ifPresent(comment -> comment.setReview("Keep up the good work!"));
		info.setComments(comments);
		try {
			userTransaction.begin();
			entityManager.merge(info);
			userTransaction.commit();
		} catch (NotSupportedException | SystemException | IllegalStateException | SecurityException
				| HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
			fail();
		}
		@SuppressWarnings("unchecked")
		List<Info> infosFromDb = entityManager.createNativeQuery("select * from info").getResultList();
		assertEquals("the info table exists", 1, infosFromDb.size());
		@SuppressWarnings("unchecked")
		List<Object[]> commentsFromDB = entityManager.createNativeQuery("select * from comment").getResultList();
		assertEquals("the comment table exists", 2, commentsFromDB.size());
		assertEquals("the comment is reviewed", "Keep up the good work!", commentsFromDB.get(1)[1]);
	}

	@Test
	public void testDeleteOneToMany() {
		logger.info("starting delete one to many cascade test");
		testPersistOneToMany();
		try {
			userTransaction.begin();
			Info info = (Info) entityManager.createQuery("from Info").getSingleResult();
			entityManager.remove(info);
			userTransaction.commit();
		} catch (NotSupportedException | SystemException | IllegalStateException | SecurityException
				| HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
			fail();
		}
		@SuppressWarnings("unchecked")
		List<Info> infosFromDb = entityManager.createNativeQuery("select * from info").getResultList();
		assertEquals("the info table don't exist", 0, infosFromDb.size());
		@SuppressWarnings("unchecked")
		List<Object[]> commentsFromDB = entityManager.createNativeQuery("select * from comment").getResultList();
		assertEquals("the comment table don't exist", 0, commentsFromDB.size());
	}

	@Test
	public void testDeleteChildOneToMany() {
		logger.info("starting delete child one to many cascade test");
		testPersistOneToMany();
		try {
			userTransaction.begin();
			Info info = (Info) entityManager.createQuery("select i " + "from Info i " + "join fetch i.comments ")
					.getSingleResult();
			info.removeComment(info.getComments().get(0));
			userTransaction.commit();
		} catch (NotSupportedException | SystemException | IllegalStateException | SecurityException
				| HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
			fail();
		}
		@SuppressWarnings("unchecked")
		List<Info> infosFromDb = entityManager.createNativeQuery("select * from info").getResultList();
		assertEquals("the info table exists", 1, infosFromDb.size());
		@SuppressWarnings("unchecked")
		List<Object[]> commentsFromDB = entityManager.createNativeQuery("select * from comment").getResultList();
		assertEquals("the comment table is only one", 1, commentsFromDB.size());
		assertEquals("the comment is reviewed", "Nice book!", commentsFromDB.get(0)[1]);
	}

	@Test
	public void testPersistManyToMany() {
		logger.info("starting persist many to many cascade test");
		Author luca_stancapiano = new Author("Luca Stancapiano");
		Author davide_barillari = new Author("Davide Barillari");
		Author davide_scala = new Author("Davide Scala");
		Book wildfly_book = new Book("Mastering Java EE Development with WildFly 10");
		Book maledetti_grillini = new Book("Maledetti grillini");
		luca_stancapiano.addBook(wildfly_book);
		davide_barillari.addBook(wildfly_book);
		luca_stancapiano.addBook(maledetti_grillini);
		davide_barillari.addBook(maledetti_grillini);
		davide_scala.addBook(maledetti_grillini);

		PMAuthor luca_stancapiano2 = new PMAuthor("Luca Stancapiano");
		PMAuthor davide_barillari2 = new PMAuthor("Davide Barillari");
		PMAuthor davide_scala2 = new PMAuthor("Davide Scala");
		PMBook wildfly_book2 = new PMBook("Mastering Java EE Development with WildFly 10");
		PMBook maledetti_grillini2 = new PMBook("Maledetti grillini");
		luca_stancapiano2.addBook(wildfly_book2);
		davide_barillari2.addBook(wildfly_book2);
		luca_stancapiano2.addBook(maledetti_grillini2);
		davide_barillari2.addBook(maledetti_grillini2);
		davide_scala2.addBook(maledetti_grillini2);

		AllAuthor luca_stancapiano3 = new AllAuthor("Luca Stancapiano");
		AllAuthor davide_barillari3 = new AllAuthor("Davide Barillari");
		AllAuthor davide_scala3 = new AllAuthor("Davide Scala");
		AllBook wildfly_book3 = new AllBook("Mastering Java EE Development with WildFly 10");
		AllBook maledetti_grillini3 = new AllBook("Maledetti grillini");
		luca_stancapiano3.addBook(wildfly_book3);
		davide_barillari3.addBook(wildfly_book3);
		luca_stancapiano3.addBook(maledetti_grillini3);
		davide_barillari3.addBook(maledetti_grillini3);
		davide_scala3.addBook(maledetti_grillini3);

		try {
			userTransaction.begin();
			clean();
			entityManager.persist(luca_stancapiano);
			entityManager.persist(davide_barillari);
			entityManager.persist(davide_scala);

			entityManager.persist(luca_stancapiano2);
			entityManager.persist(davide_barillari2);
			entityManager.persist(davide_scala2);

			entityManager.persist(luca_stancapiano3);
			entityManager.persist(davide_barillari3);
			entityManager.persist(davide_scala3);
			userTransaction.commit();
		} catch (NotSupportedException | SystemException | IllegalStateException | SecurityException
				| HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
			fail();
		}
		@SuppressWarnings("unchecked")
		List<Book> booksFromDb = entityManager.createNativeQuery("select * from Book").getResultList();
		assertEquals("the book table exists", 2, booksFromDb.size());
		@SuppressWarnings("unchecked")
		List<Author> authorsFromDb = entityManager.createNativeQuery("select * from Author").getResultList();
		assertEquals("the author table exists", 3, authorsFromDb.size());
		@SuppressWarnings("unchecked")
		List<Author> bookAuthorsFromDb = entityManager.createNativeQuery("select * from Book_Author").getResultList();
		assertEquals("the author table exists", 5, bookAuthorsFromDb.size());

		@SuppressWarnings("unchecked")
		List<PMBook> booksFromDb2 = entityManager.createNativeQuery("select * from PMBook").getResultList();
		assertEquals("the pm book table exists", 2, booksFromDb2.size());
		@SuppressWarnings("unchecked")
		List<PMAuthor> authorsFromDb2 = entityManager.createNativeQuery("select * from PMAuthor").getResultList();
		assertEquals("the pm author table exists", 3, authorsFromDb2.size());
		@SuppressWarnings("unchecked")
		List<PMAuthor> bookAuthorsFromDb2 = entityManager.createNativeQuery("select * from Book_PM_Author")
				.getResultList();
		assertEquals("the pm author table exists", 5, bookAuthorsFromDb2.size());

		@SuppressWarnings("unchecked")
		List<AllBook> booksFromDb3 = entityManager.createNativeQuery("select * from AllBook").getResultList();
		assertEquals("the all book table exists", 2, booksFromDb3.size());
		@SuppressWarnings("unchecked")
		List<AllAuthor> authorsFromDb3 = entityManager.createNativeQuery("select * from AllAuthor").getResultList();
		assertEquals("the all author table exists", 3, authorsFromDb3.size());
		@SuppressWarnings("unchecked")
		List<AllAuthor> bookAuthorsFromDb3 = entityManager.createNativeQuery("select * from Book_All_Author")
				.getResultList();
		assertEquals("the all author table exists", 5, bookAuthorsFromDb3.size());
	}

	@Test
	public void testDeleteManyToMany() {
		logger.info("starting merge many to many cascade test");
		testPersistManyToMany();
		try {
			userTransaction.begin();
			Author davide_scala = (Author) entityManager.createQuery("from Author where fullName = :fullName")
					.setParameter("fullName", "Davide Scala").getSingleResult();
			davide_scala.remove();
			entityManager.remove(davide_scala);
			userTransaction.commit();
		} catch (NotSupportedException | SystemException | IllegalStateException | SecurityException
				| HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
			fail();
		}
		@SuppressWarnings("unchecked")
		List<Book> booksFromDb = entityManager.createNativeQuery("select * from Book").getResultList();
		assertEquals("the book table exists", 2, booksFromDb.size());
		@SuppressWarnings("unchecked")
		List<Author> authorsFromDb = entityManager.createNativeQuery("select * from Author").getResultList();
		assertEquals("the author table exists", 2, authorsFromDb.size());
		@SuppressWarnings("unchecked")
		List<Author> bookAuthorsFromDb = entityManager.createNativeQuery("select * from Book_Author").getResultList();
		assertEquals("the author table exists", 4, bookAuthorsFromDb.size());
	}

	@Test
	public void testDeleteBooksAllManyToMany() {
		logger.info("starting merge many to many cascade test");
		testPersistManyToMany();
		try {
			userTransaction.begin();
			PMAuthor davide_scala = (PMAuthor) entityManager.createQuery("from PMAuthor where fullName = :fullName")
					.setParameter("fullName", "Davide Scala").getSingleResult();
			entityManager.remove(davide_scala);
			userTransaction.commit();
		} catch (NotSupportedException | SystemException | IllegalStateException | SecurityException
				| HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
			fail();
		}
		@SuppressWarnings("unchecked")
		List<PMBook> booksFromDb = entityManager.createNativeQuery("select * from PMBook").getResultList();
		assertEquals("the pm book table exists", 1, booksFromDb.size());
		@SuppressWarnings("unchecked")
		List<PMAuthor> authorsFromDb = entityManager.createNativeQuery("select * from PMAuthor").getResultList();
		assertEquals("the pm author table exists", 2, authorsFromDb.size());
		@SuppressWarnings("unchecked")
		List<PMAuthor> bookAuthorsFromDb = entityManager.createNativeQuery("select * from Book_PM_Author")
				.getResultList();
		assertEquals("the pm book author table exists", 2, bookAuthorsFromDb.size());
	}

	@Test
	public void testDeleteBooksJoinTableAllManyToMany() {
		logger.info("starting merge many to many cascade test");
		testPersistManyToMany();
		try {
			userTransaction.begin();
			AllAuthor davide_scala = (AllAuthor) entityManager.createQuery("from AllAuthor where fullName = :fullName")
					.setParameter("fullName", "Davide Scala").getSingleResult();
			entityManager.remove(davide_scala);
			userTransaction.commit();
		} catch (NotSupportedException | SystemException | IllegalStateException | SecurityException
				| HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
			fail();
		}
		@SuppressWarnings("unchecked")
		List<AllBook> booksFromDb = entityManager.createNativeQuery("select * from AllBook").getResultList();
		assertEquals("the all book table doesn't exist", 0, booksFromDb.size());
		@SuppressWarnings("unchecked")
		List<AllAuthor> authorsFromDb = entityManager.createNativeQuery("select * from AllAuthor").getResultList();
		assertEquals("the all author table doesn't exist", 0, authorsFromDb.size());
		@SuppressWarnings("unchecked")
		List<AllAuthor> bookAuthorsFromDb = entityManager.createNativeQuery("select * from Book_All_Author")
				.getResultList();
		assertEquals("the all book author table exists", 0, bookAuthorsFromDb.size());
	}
}
