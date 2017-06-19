package it.vige.businesscomponents.businesslogic.context.old;

import javax.ejb.EJB;
import javax.ejb.EJBs;
import javax.ejb.Stateless;
import javax.naming.InitialContext;

@Stateless
@EJBs({ @EJB(name = "injected1", beanInterface = Ejb21Local.class),
		@EJB(name = "injected2", beanInterface = Ejb21StateLocal.class) })
public class BeanCallingOtherBeans {

	public Ejb21Local getEjb21Local() {
		try {
			InitialContext jndiContext = new InitialContext();
			Ejb21LocalHome sessionHome = (Ejb21LocalHome) jndiContext.lookup("java:comp/env/injected1");
			return sessionHome.create();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Ejb21StateLocal getEjb21StateLocal() {
		try {
			InitialContext jndiContext = new InitialContext();
			Ejb21StateLocalHome sessionHome = (Ejb21StateLocalHome) jndiContext.lookup("java:comp/env/injected2");
			return sessionHome.create();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
