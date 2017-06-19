package it.vige.businesscomponents.injection;

import static java.util.logging.Logger.getLogger;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.AlterableContext;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.vige.businesscomponents.injection.context.ApplicationBean;
import it.vige.businesscomponents.injection.context.ConversationBean;
import it.vige.businesscomponents.injection.context.DependentBean;
import it.vige.businesscomponents.injection.context.FieldScopedBean;
import it.vige.businesscomponents.injection.context.MethodScopedBean;
import it.vige.businesscomponents.injection.context.RequestBean;
import it.vige.businesscomponents.injection.context.SessionBean;

@RunWith(Arquillian.class)
public class ContextSPITestCase {

	private static final Logger logger = getLogger(ContextSPITestCase.class.getName());

	@Inject
	private ConversationBean conversationBean;

	@Inject
	private SessionBean sessionBean;

	@Inject
	private ApplicationBean applicationBean;

	@Inject
	private RequestBean requestBean1;

	@Inject
	private RequestBean requestBean2;

	@Inject
	private DependentBean dependentBean1;

	@Inject
	private DependentBean dependentBean2;

	@Inject
	private FieldScopedBean fieldScopedBean;

	@Inject
	private MethodScopedBean methodScopedBean;

	@Deployment
	public static JavaArchive createJavaDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "context-spi-test.jar");
		jar.addPackage(ConversationBean.class.getPackage());
		jar.addAsManifestResource(INSTANCE, "beans.xml");
		return jar;
	}

	/**
	 * Tests simple conversation in a jar archive
	 */
	@Test
	public void testConversation() {
		logger.info("start conversation test");
		Conversation conversation = conversationBean.getConversation();
		assertEquals("deafult timeout is 600000 millis", 600000, conversation.getTimeout());
		assertEquals("when the conversation is not started it is transient", true, conversation.isTransient());
		assertNull("I have not started the conversation so the id will be null", conversation.getId());
		conversationBean.initConversation();
		assertEquals("verifiing the openeded conversation", "1", conversation.getId());
		assertEquals("when the conversation is started it is not transient", false, conversation.isTransient());
		conversationBean.endConversation();
		assertNull("when I close the conversation the id will be null", conversation.getId());
		assertEquals("when the conversation is ended it is transient again", true, conversation.isTransient());
		logger.info("end conversation test");
	}

	/**
	 * Tests if the injection of the different scopes is done
	 */
	@Test
	public void testInjectionScope() {
		assertNotNull("The session bean must be injected", sessionBean);
		assertNotNull("The application bean must be injected", applicationBean);
		assertEquals("The counter of the application bean should be incremneted by the InitializeApplicationObserver",
				1, applicationBean.getCounter());
	}

	/**
	 * Tests if the injection of the different scopes inside fields and methods
	 * is done
	 */
	@Test
	public void testInjectionFieldsAndMethodsScope() {
		assertNotNull("The field bean must be injected", fieldScopedBean);
		assertNotNull("The method bean must be injected", methodScopedBean);
	}

	@Test
	public void testDependent() {
		assertNotNull("The dependent bean must be injected", dependentBean1);
		assertNotNull("The dependent bean must be injected", dependentBean2);
		assertNotEquals("The dependent beans will be different instances", dependentBean1, dependentBean2);
		assertEquals(
				"The counter of the dependent bean not is incremented by the InitializeDependenteObserver because is ever a new instance",
				0, dependentBean1.getCounter());
	}

	@Test
	public void testRequest() {
		assertNotNull("The request bean must be injected", requestBean1);
		assertNotNull("The request bean must be injected", requestBean2);
		assertEquals("The request beans will be equal instances", requestBean1, requestBean2);
	}

	@Test
	public void callContexts() {
		AlterableContext dependentContext = (AlterableContext) methodScopedBean.getBeanManager()
				.getContext(Dependent.class);
		assertEquals("All scoped annotations have own context", true, dependentContext.isActive());
		Contextual<DependentBean> contextualDependentBean = new Contextual<DependentBean>() {

			private DependentBean dependentBean;

			@Override
			public DependentBean create(CreationalContext<DependentBean> creationalContext) {
				dependentBean = new DependentBean();
				creationalContext.push(dependentBean);
				return dependentBean;
			}

			@Override
			public void destroy(DependentBean instance, CreationalContext<DependentBean> creationalContext) {
				creationalContext.release();
				dependentBean = null;
			}

		};
		DependentBean myCustomDependentBean = dependentContext.get(contextualDependentBean, getCreationalContext());
		myCustomDependentBean.incrementCounter();
		assertEquals("The Context can created a new bean and it can be used can be used ", 1,
				myCustomDependentBean.getCounter());
		try {
			dependentContext.destroy(contextualDependentBean);
			fail("the destry is not supported for the dependent context");
		} catch (Exception ex) {
			logger.info("the destry is not supported for the dependent context");
		}
		Context sessionContext = methodScopedBean.getBeanManager().getContext(SessionScoped.class);
		assertEquals("All scoped annotations have own context", true, sessionContext.isActive());
		AlterableContext applicationContext = (AlterableContext) methodScopedBean.getBeanManager()
				.getContext(ApplicationScoped.class);
		assertEquals("All scoped annotations have own context", true, applicationContext.isActive());
		Contextual<ApplicationBean> contextualApplicationBean = new Contextual<ApplicationBean>() {

			private ApplicationBean applicationBean;
			private boolean removed;

			@Override
			public ApplicationBean create(CreationalContext<ApplicationBean> creationalContext) {
				// for test, I let to create a bean only if the destry method is
				// not executed
				if (!removed) {
					applicationBean = new ApplicationBean();
					creationalContext.push(applicationBean);
					return applicationBean;
				} else
					return null;
			}

			@Override
			public void destroy(ApplicationBean instance, CreationalContext<ApplicationBean> creationalContext) {
				creationalContext.release();
				applicationBean = null;
				removed = true;
			}

		};
		ApplicationBean myCustomApplicationBean = applicationContext.get(contextualApplicationBean,
				getCreationalContext());
		myCustomApplicationBean.incrementCounter();
		myCustomApplicationBean.getCounter();
		assertEquals("The Context can created a new bean and it can be used can be used ", 1,
				myCustomApplicationBean.getCounter());
		applicationContext.destroy(contextualApplicationBean);
		assertNull("the destroy is available to the application context",
				applicationContext.get(contextualApplicationBean, getCreationalContext()));
		Context requestContext = methodScopedBean.getBeanManager().getContext(RequestScoped.class);
		assertEquals("All scoped annotations have own context", true, requestContext.isActive());
	}

	private <T> CreationalContext<T> getCreationalContext() {

		CreationalContext<T> creationalContext = new CreationalContext<T>() {

			private List<T> applicationBeans = new ArrayList<T>();
			private T currentInstance;

			@Override
			public void push(T incompleteInstance) {
				applicationBeans.add(incompleteInstance);
				currentInstance = incompleteInstance;

			}

			@Override
			public void release() {
				applicationBeans.remove(currentInstance);

			}

		};
		return creationalContext;
	}
}
