package it.vige.businesscomponents.businesslogic.context.old;

import javax.ejb.EJBLocalObject;

public interface Ejb21StateLocal extends EJBLocalObject {

	int go(int speed);

	int retry(int speed);

	int getSpeed();

	void log();

	void add(Object data);
}
