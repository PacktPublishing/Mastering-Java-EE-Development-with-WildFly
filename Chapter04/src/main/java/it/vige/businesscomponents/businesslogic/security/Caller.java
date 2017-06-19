package it.vige.businesscomponents.businesslogic.security;

import java.util.concurrent.Callable;

public interface Caller {
	<V> V call(Callable<V> callable) throws Exception;
}
