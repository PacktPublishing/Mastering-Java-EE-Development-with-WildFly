package it.vige.businesscomponents.businesslogic.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class SimpleAppException extends Exception {

	private static final long serialVersionUID = -9221489579106787662L;

}
