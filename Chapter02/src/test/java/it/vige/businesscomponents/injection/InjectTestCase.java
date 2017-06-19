package it.vige.businesscomponents.injection;

import static it.vige.businesscomponents.injection.inject.any.BankName.BankOfAmerica;
import static it.vige.businesscomponents.injection.inject.any.BankName.Chase;
import static it.vige.businesscomponents.injection.inject.any.BankName.HSBC;
import static it.vige.businesscomponents.injection.inject.model.StateBook.DRAFT;
import static it.vige.businesscomponents.injection.inject.model.StateBook.PUBLISHED;
import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.businesscomponents.injection.inject.Draft;
import it.vige.businesscomponents.injection.inject.Published;
import it.vige.businesscomponents.injection.inject.Service;
import it.vige.businesscomponents.injection.inject.Writer;
import it.vige.businesscomponents.injection.inject.any.Bank;
import it.vige.businesscomponents.injection.inject.any.BankProducer;
import it.vige.businesscomponents.injection.inject.any.BankType;
import it.vige.businesscomponents.injection.inject.impl.BookService;
import it.vige.businesscomponents.injection.inject.impl.Comment;
import it.vige.businesscomponents.injection.inject.impl.CommentService;
import it.vige.businesscomponents.injection.inject.impl.CommentWriter;
import it.vige.businesscomponents.injection.inject.impl.Revision;
import it.vige.businesscomponents.injection.inject.impl.RevisionService;
import it.vige.businesscomponents.injection.inject.model.Book;
import it.vige.businesscomponents.injection.inject.produces.UserNumberBean;
import it.vige.businesscomponents.injection.inject.veto.MockBean;
import it.vige.businesscomponents.injection.inject.veto.TestBean;

@RunWith(Arquillian.class)
public class InjectTestCase {

	private static final Logger logger = getLogger(InjectTestCase.class.getName());

	@Inject
	private Writer writer;

	@Inject
	private Service service;

	@Inject
	@Revision
	private Service revisionService;

	@Inject
	private UserNumberBean userNumberBean;

	@Inject
	@Published
	private List<Book> publishedBooks;

	@Inject
	@Draft
	private List<Book> draftBooks;

	@Inject
	@BankType(BankOfAmerica)
	@BankProducer
	private Bank bankOfAmerica;

	@Inject
	@BankType(Chase)
	@BankProducer
	private Bank chase;

	@Inject
	@BankType(HSBC)
	@BankProducer
	private Bank hsbc;

	@Inject
	private BeanManager beanManager;

	@Inject
	private Comment<String> orderManager;

	@Deployment
	public static JavaArchive createJavaDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "inject-test.jar");
		jar.addPackage(Service.class.getPackage());
		jar.addPackage(CommentService.class.getPackage());
		jar.addPackage(Book.class.getPackage());
		jar.addPackage(UserNumberBean.class.getPackage());
		jar.addPackage(Bank.class.getPackage());
		jar.addPackage(MockBean.class.getPackage());
		jar.addAsManifestResource(INSTANCE, "beans.xml");
		return jar;
	}

	/**
	 * Tests default annotation in a jar archive
	 */
	@Test
	public void testInjectWriter() {
		logger.info("starting inject test");
		assertTrue("it takes the default annotated writer", writer instanceof CommentWriter);
		assertTrue("it takes the default annotated service", service instanceof BookService);
		assertEquals("the published is called", PUBLISHED, publishedBooks.get(0).getState());
		assertEquals("I injected the draft books, so the method with any annotation is called", DRAFT,
				draftBooks.get(0).getState());
		assertEquals("the BankFactory is not used", "Deposit to Bank of America", bankOfAmerica.deposit());
		assertEquals("the BankFactory is used", "Deposit to Chase", chase.deposit());
		assertEquals("the BankFactory is used", "Deposit to HSBC", hsbc.deposit());
	}

	/**
	 * Tests the produces a jar archive
	 */
	@Test
	public void testProduces() {
		logger.info("starting produces test");
		int myChosenNumber = 20;
		userNumberBean.validateNumberRange(myChosenNumber);
		userNumberBean.setUserNumber(myChosenNumber);
		userNumberBean.check();
		userNumberBean.reset();
	}

	/**
	 * Tests the specializes a jar archive
	 */
	@Test
	public void testSpecializes() {
		logger.info("starting specializes test");
		assertTrue("the revisionService with Specializes", revisionService instanceof RevisionService);
		Bean<?> bean = beanManager.getBeans(Service.class, new AnnotationLiteral<Revision>() {

			private static final long serialVersionUID = -4491584612013368113L;
		}).iterator().next();
		assertEquals("RevisionService should inherit the SessionScope without the Specializes annotation",
				SessionScoped.class, bean.getScope());
	}

	/**
	 * Tests the specializes a jar archive
	 */
	@Test
	public void testTransient() {
		String message = "the order is marked as @TransientReference so, after it is created, it will be removed. A session scoped can inject a dependent bean only if there is this annotation. I we remove the annotation, we get an error because the order is not serializable";
		assertNotNull(message, orderManager);
		assertNotNull(message, orderManager.getOrder());
	}

	/**
	 * Tests the veto packages
	 */
	@Test
	public void testVeto() {
		String message = "the bean cannot be injected because its package is marked as vetoed";
		try {
			beanManager.getBeans(UserNumberBean.class).iterator().next();
		} catch (NoSuchElementException ex) {
			fail("the bean never fails because the vetoed is not declared");
		}
		try {
			beanManager.getBeans(MockBean.class).iterator().next();
			fail(message);
		} catch (NoSuchElementException ex) {
			logger.info(message);
		}
		try {
			beanManager.getBeans(TestBean.class).iterator().next();
			fail(message);
		} catch (NoSuchElementException ex) {
			logger.info(message);
		}
	}
}
