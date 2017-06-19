package it.vige.clustering.sharedata.singleton;

import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.as.server.ServerEnvironment;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.Value;

public class HAService implements Service<Environment> {

	private final Value<ServerEnvironment> env;
	private final AtomicBoolean started = new AtomicBoolean(false);

	public HAService(Value<ServerEnvironment> env) {
		this.env = env;
	}

	@Override
	public Environment getValue() {
		if (!started.get()) {
			throw new IllegalStateException();
		}
		return new Environment(env.getValue().getNodeName());
	}

	@Override
	public void start(StartContext context) {
		this.started.set(true);
	}

	@Override
	public void stop(StopContext context) {
		this.started.set(false);
	}
}
