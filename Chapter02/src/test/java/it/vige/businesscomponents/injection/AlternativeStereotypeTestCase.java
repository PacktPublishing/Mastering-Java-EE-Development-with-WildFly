package it.vige.businesscomponents.injection;

import static it.vige.businesscomponents.injection.interceptor.service.History.getItemHistory;
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

import it.vige.businesscomponents.injection.alternative.stereotype.ServiceStereotype;
import it.vige.businesscomponents.injection.alternative.stereotype.service.DuplicatedItemServiceBean;
import it.vige.businesscomponents.injection.interceptor.Audit;
import it.vige.businesscomponents.injection.interceptor.service.Item;
import it.vige.businesscomponents.injection.interceptor.service.ItemService;

@RunWith(Arquillian.class)
public class AlternativeStereotypeTestCase {

	private static final Logger logger = getLogger(AlternativeStereotypeTestCase.class.getName());

	@Deployment
	public static JavaArchive createJavaDeployment() {
		final JavaArchive jar = create(JavaArchive.class, "interceptors-alternative-stereotype-test.jar");
		jar.addPackage(Audit.class.getPackage());
		jar.addPackage(Item.class.getPackage());
		jar.addPackage(ServiceStereotype.class.getPackage());
		jar.addPackage(DuplicatedItemServiceBean.class.getPackage());
		jar.addAsManifestResource(
				new FileAsset(new File("src/test/resources/META-INF/beans-alternative-stereotype.xml")), "beans.xml");
		return jar;
	}

	@Inject
	private ItemService itemService;

	@Test
	public void testAuditInterceptor() {
		logger.info("Start interecptor test");
		Item item = new Item();
		item.setName("testItem");
		itemService.create(item);
		List<Item> items = itemService.getList();
		assertEquals(2, items.size());
		assertEquals(2, getItemHistory().size());
	}
}
