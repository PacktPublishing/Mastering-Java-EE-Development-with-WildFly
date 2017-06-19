package it.vige.businesscomponents.injection.interceptor.service;

import javax.ejb.EJBLocalObject;

public interface SimpleInterface extends EJBLocalObject {

	String getText();

	void setText(String text);
}
