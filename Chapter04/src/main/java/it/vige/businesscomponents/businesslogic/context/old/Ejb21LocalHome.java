package it.vige.businesscomponents.businesslogic.context.old;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface Ejb21LocalHome extends EJBLocalHome {
	public Ejb21Local create() throws CreateException;
}
