package it.vige.businesscomponents.businesslogic;

import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

import java.util.logging.Logger;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.businesscomponents.businesslogic.interfaces.Topics;

@RunWith(Arquillian.class)
public class InterfacesTestCase {

	private static final Logger logger = getLogger(InterfacesTestCase.class.getName());

	@EJB(mappedName = "java:module/AllTopics")
	private Topics allTopics;

	@EJB(mappedName = "java:module/MyTopics")
	private Topics myTopics;

	@Deployment
	public static JavaArchive createEJBDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "interfaces-test.jar");
		jar.addPackage(Topics.class.getPackage());
		return jar;
	}

	@Test
	public void testStateful() {
		logger.info("Start interfaces test");
		String allTopicsName = allTopics.getCurrentTopicName();
		assertEquals("all_topic_name", allTopicsName);
		String myTopicsName = myTopics.getCurrentTopicName();
		assertEquals("current_topic_name", myTopicsName);
	}
}
