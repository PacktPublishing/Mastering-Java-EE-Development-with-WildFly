package it.vige.realtime.asynchronousejb.timer;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalObject;

public interface OldSpecsLocal extends EJBLocalObject {

	void fireInThirtySeconds() throws EJBException;
	String getWhyWasICalled() throws EJBException;
}
