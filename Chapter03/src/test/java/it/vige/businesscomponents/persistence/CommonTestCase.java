package it.vige.businesscomponents.persistence;

import static it.vige.businesscomponents.persistence.Default.createJavaArchive;
import static java.util.logging.Logger.getLogger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.businesscomponents.persistence.resultset.ResultDetails;
import it.vige.businesscomponents.persistence.resultset.ResultEntity;

@RunWith(Arquillian.class)
public class CommonTestCase {

	private static final Logger logger = getLogger(CommonTestCase.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	@Deployment
	public static JavaArchive createJavaDeployment() {
		JavaArchive jar = createJavaArchive("common-test.jar", Forum.class.getPackage(),
				ResultEntity.class.getPackage());
		jar.addAsResource(new FileAsset(new File("src/main/resources/forums.import.sql")), "forums.import.sql");
		return jar;
	}

	/**
	 * Tests annotation literals in a jar archive
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testSqlScript() {
		logger.info("starting common persistence test");
		assertNotNull("The injected entity manager", entityManager);
		List<Category> categories = entityManager.createNamedQuery("findCategories").getResultList();
		assertEquals("one category created by the sql script", 1, categories.size());
		List<Forum> forums = entityManager.createNamedQuery("findForumsByCategoryId")
				.setParameter("categoryId", categories.get(0)).getResultList();
		assertEquals("two forums created by the sql script", 2, forums.size());
	}

	@Test
	public void testQuery() {
		TypedQuery<Forum> query = entityManager
				.createQuery("select f from Forum f left outer join fetch f.topics where f.id = :forumId", Forum.class);
		query.setParameter("forumId", 1);
		List<Forum> forumList = query.getResultList();
		assertEquals("simple query ok", 1, forumList.size());
		query = entityManager.createNamedQuery("findForumByIdFetchTopics", Forum.class);
		query.setParameter("forumId", 1);
		forumList = query.getResultList();
		assertEquals("named query ok", 1, forumList.size());
		String namedQuery = "my_runtime_named_query";
		entityManager.getEntityManagerFactory().addNamedQuery(namedQuery, query);
		query = entityManager.createNamedQuery(namedQuery, Forum.class);
		query.setParameter("forumId", 1);
		forumList = query.getResultList();
		assertEquals("named query ok", 1, forumList.size());
	}

	@Test
	public void testResultSet() {
		Query query = entityManager
				.createNativeQuery(
						"SELECT c.JBP_ID as id, c.JBP_TITLE as title, f.JBP_POST_COUNT as orderCount, f.JBP_POST_COUNT AS postCount "
								+ "FROM JBP_FORUMS_CATEGORIES c, JBP_FORUMS_FORUMS f "
								+ "WHERE f.JBP_CATEGORY_ID = c.JBP_ID " + "GROUP BY c.JBP_ID, c.JBP_TITLE",
						"CustomerDetailsResult");
		@SuppressWarnings("unchecked")
		List<ResultDetails> resultDetails = query.getResultList();
		assertEquals("created through the constructor", 1, resultDetails.size());

	}
}
