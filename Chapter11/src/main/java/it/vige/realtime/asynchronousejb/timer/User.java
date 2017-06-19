package it.vige.realtime.asynchronousejb.timer;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;

@Stateful
@StatefulTimeout(value = 15, unit = MILLISECONDS)
public class User {

	private boolean registered = false;

	public void register() {
		registered = true;
	}

	public boolean isRegistered() {
		return registered;
	}
	
	@Remove
	public void remove() {
		registered = false;
	}
}
