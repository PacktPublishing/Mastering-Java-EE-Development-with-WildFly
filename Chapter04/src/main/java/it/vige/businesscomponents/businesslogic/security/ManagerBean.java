package it.vige.businesscomponents.businesslogic.security;

import java.util.concurrent.Callable;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RunAs;
import javax.ejb.Stateless;

@Stateless
@RunAs("Manager")
public class ManagerBean implements Caller {

	@PermitAll
	public <V> V call(Callable<V> callable) throws Exception {
		return callable.call();
	}
}
