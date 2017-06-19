package it.vige.businesscomponents.businesslogic.context.old;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface Ejb21StateRemoteHome extends EJBHome {
	  public Ejb21StateRemote create() throws CreateException, RemoteException;
	  public Ejb21StateRemote create(String message) throws CreateException, RemoteException;
	  public Ejb21StateRemote create(Collection<?> messages) throws CreateException, RemoteException;
}
