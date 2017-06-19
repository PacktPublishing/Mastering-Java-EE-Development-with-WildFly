package it.vige.businesscomponents.businesslogic.context.old;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface Ejb21StateLocalHome extends EJBLocalHome {
	public Ejb21StateLocal create() throws CreateException;
	public Ejb21StateLocal create(String message) throws CreateException;
	public Ejb21StateLocal create(Collection<?> messages) throws CreateException;
}
