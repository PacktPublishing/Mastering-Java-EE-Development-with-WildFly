package it.vige.businesscomponents.businesslogic;

import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ScopesTestCase {

	private static final Logger logger = getLogger(ScopesTestCase.class.getName());

	@Inject
	private MyPosts myPosts;

	@Inject
	@Hard
	private MyHardPosts myHardPosts;

	@Inject
	@Harder
	private MyHarderPosts myHarderPosts;

	@Deployment
	public static JavaArchive createEJBDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "scopes-test.jar");
		jar.addPackage(MyPosts.class.getPackage());
		jar.addAsManifestResource(new FileAsset(new File("src/test/resources/META-INF/persistence-test.xml")),
				"persistence.xml");
		return jar;
	}

	@Test
	public void testScopes() {
		logger.info("Start stateful test");
		myPosts.getPostsByDay(0);
		List<Post> posts = myPosts.getLastRequestedPosts();
		assertEquals("two posts", 2, posts.size());
		myHardPosts.getPostsByDay(0);
		List<Post> hardPosts = myHardPosts.getLastRequestedPosts();
		assertEquals("two posts", 2, hardPosts.size());
		myHarderPosts.getPostsByDay(0);
		List<Post> harderPosts = myHarderPosts.getLastRequestedPosts();
		assertEquals("four posts", 2, harderPosts.size());
		assertEquals("through the dependson the singleton will get two new posts", 2, myHarderPosts.getPosts().size());
	}
}
