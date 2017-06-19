package it.vige.businesscomponents.persistence;

import static it.vige.businesscomponents.persistence.Default.createJavaArchive;
import static java.util.Calendar.DATE;
import static java.util.Calendar.getInstance;
import static java.util.logging.Logger.getLogger;
import static org.apache.lucene.search.BooleanClause.Occur.MUST;
import static org.apache.lucene.search.NumericRangeQuery.newLongRange;
import static org.apache.lucene.search.SortField.Type.LONG;
import static org.hibernate.search.Search.getFullTextSession;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SearchTestCase {

	private static final Logger logger = getLogger(SearchTestCase.class.getName());

	private final static String TOPIC_TEXT = "Can I play with madness?";
	private final static String POST_TEXT = "Iron Maiden";

	@PersistenceContext
	private EntityManager entityManager;

	@Inject
	private UserTransaction userTransaction;

	@Deployment
	public static JavaArchive createJavaDeployment() {
		JavaArchive jar = createJavaArchive("search-test.jar", Forum.class.getPackage());
		jar.addAsResource(new FileAsset(new File("src/main/resources/forums.import.sql")), "forums.import.sql");
		return jar;
	}

	private List<Post> findPosts() {
		try {
			FullTextSession fullTextSession = getFullTextSession((Session) entityManager.getDelegate());
			Builder builder = new Builder();
			String[] fields = new String[] { "message.text", "topic.subject" };
			MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
			builder.add(parser.parse(POST_TEXT), MUST);
			builder.add(new TermQuery(new Term("topic.forum.id", "0")), MUST);
			builder.add(new TermQuery(new Term("topic.forum.category.id", "0")), MUST);
			builder.add(new WildcardQuery(new Term("poster.userId", "root")), MUST);
			addPostTimeQuery(builder);
			FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(builder.build(), Post.class);
			fullTextQuery.setSort(getSort());
			fullTextQuery.setFirstResult(0);
			fullTextQuery.setMaxResults(15);
			@SuppressWarnings("unchecked")
			List<Post> posts = fullTextQuery.list();
			return posts;
		} catch (ParseException e) {
			logger.severe("error");
			return null;
		}
	}

	private List<Topic> findTopics() {
		try {
			FullTextSession fullTextSession = getFullTextSession((Session) entityManager.getDelegate());
			Builder builder = new Builder();
			String[] fields = new String[] { "message.text", "topic.subject" };
			MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
			builder.add(parser.parse(TOPIC_TEXT), MUST);
			builder.add(new TermQuery(new Term("topic.forum.id", "0")), MUST);
			builder.add(new TermQuery(new Term("topic.forum.category.id", "0")), MUST);
			builder.add(new WildcardQuery(new Term("poster.userId", "root")), MUST);
			FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(builder.build(), Post.class);
			fullTextQuery.setSort(getSort());
			fullTextQuery.setProjection("topic.id");
			LinkedHashSet<Integer> topicIds = new LinkedHashSet<Integer>();
			LinkedHashSet<Integer> topicToDispIds = new LinkedHashSet<Integer>();
			int index = 0;
			for (Object o : fullTextQuery.list()) {
				Integer id = (Integer) ((Object[]) o)[0];

				if (topicIds.add(id)) {

					if (index >= 0 && index < 15) {
						topicToDispIds.add(id);
					}

					index++;
				}
			}

			List<Topic> topics = null;
			if (topicToDispIds.size() > 0) {
				TypedQuery<Topic> q = entityManager
						.createQuery("from Topic as t join fetch t.poster where t.id IN ( :topicIds )", Topic.class);
				q.setParameter("topicIds", topicToDispIds);

				List<Topic> results = q.getResultList();

				topics = new LinkedList<Topic>();
				for (Integer id : topicToDispIds) {
					for (Topic topic : results) {
						if (id.equals(topic.getId())) {
							topics.add(topic);
							break;
						}
					}
				}
			}

			return topics;
		} catch (ParseException e) {
			logger.severe("error");
			return null;
		}
	}

	private Sort getSort() {
		return new Sort(new SortField("createDate", LONG, true));
	}

	private void addPostTimeQuery(Builder query) {
		Calendar calendar = getInstance();
		Date endDate = calendar.getTime();
		calendar.add(DATE, -1);
		Date startDate = calendar.getTime();
		query.add(newLongRange("createDate", startDate.getTime(), endDate.getTime(), true, true), MUST);
	}

	@Test
	public void testSearch() {
		Forum forum = entityManager.find(Forum.class, 0);
		Poster poster = new Poster("root");
		Topic topic = new Topic(forum, TOPIC_TEXT);
		topic.setPoster(poster);
		Post post = new Post(topic, POST_TEXT);
		post.setCreateDate(new Date());
		post.setPoster(poster);
		try {
			userTransaction.begin();
			entityManager.persist(poster);
			entityManager.persist(topic);
			entityManager.persist(post);
			userTransaction.commit();
		} catch (NotSupportedException | SystemException | IllegalStateException | SecurityException
				| HeuristicMixedException | HeuristicRollbackException | RollbackException e) {
			e.printStackTrace();
		}
		List<Topic> topics = findTopics();
		List<Post> posts = findPosts();
		assertEquals("find topics", 1, topics.size());
		assertEquals("find posts", 1, posts.size());
	}
}
