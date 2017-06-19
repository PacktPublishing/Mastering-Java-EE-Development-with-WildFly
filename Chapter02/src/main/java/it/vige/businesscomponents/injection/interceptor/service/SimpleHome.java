package it.vige.businesscomponents.injection.interceptor.service;

import javax.ejb.EJBLocalHome;

public interface SimpleHome extends EJBLocalHome {

	SimpleInterface createSimple();

}
