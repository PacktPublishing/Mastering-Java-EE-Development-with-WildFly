package it.vige.businesscomponents.businesslogic.context.old;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface Ejb21Remote extends EJBObject {

	int go(int speed) throws RemoteException;

	int retry(int speed) throws RemoteException;

	int getSpeed() throws RemoteException;

	void log() throws RemoteException;

	void add(Object data) throws RemoteException;
}
