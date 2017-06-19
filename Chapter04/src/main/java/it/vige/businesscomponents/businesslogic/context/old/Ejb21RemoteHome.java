package it.vige.businesscomponents.businesslogic.context.old;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface Ejb21RemoteHome extends EJBHome {
	public Ejb21Remote create() throws CreateException, RemoteException;
}
