package it.vige.businesscomponents.businesslogic.security;

import java.util.concurrent.Callable;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RunAs;
import javax.ejb.Stateless;

@Stateless
@RunAs("Employee")
public class EmployeeBean implements Caller {

	@PermitAll
	public <V> V call(Callable<V> callable) throws Exception {
		return callable.call();
	}
}
