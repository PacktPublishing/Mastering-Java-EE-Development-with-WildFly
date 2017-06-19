package it.vige.clustering.sharedata.singleton;

import static it.vige.clustering.sharedata.singleton.ClusteringConstants.NODE_2;
import static org.jboss.as.server.ServerEnvironmentService.SERVICE_NAME;
import static org.jboss.msc.service.ServiceName.JBOSS;
import static org.wildfly.clustering.singleton.SingletonServiceName.BUILDER;

import org.jboss.as.server.ServerEnvironment;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistryException;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.value.InjectedValue;
import org.wildfly.clustering.singleton.SingletonServiceBuilderFactory;
import org.wildfly.clustering.singleton.election.NamePreference;
import org.wildfly.clustering.singleton.election.PreferredSingletonElectionPolicy;
import org.wildfly.clustering.singleton.election.SimpleSingletonElectionPolicy;

public class HAServiceActivator implements ServiceActivator {

	public static final ServiceName DEFAULT_SERVICE_NAME = JBOSS.append("test", "haservice", "default");
	public static final ServiceName QUORUM_SERVICE_NAME = JBOSS.append("test", "haservice", "quorum");
	private static final String CONTAINER_NAME = "server";
	public static final String PREFERRED_NODE = NODE_2;

	@Override
	public void activate(ServiceActivatorContext context) {
		ServiceTarget target = context.getServiceTarget();
		try {
			SingletonServiceBuilderFactory factory = (SingletonServiceBuilderFactory) context.getServiceRegistry()
					.getRequiredService(BUILDER.getServiceName(CONTAINER_NAME)).awaitValue();
			install(target, factory, DEFAULT_SERVICE_NAME, 1);
			install(target, factory, QUORUM_SERVICE_NAME, 2);
		} catch (InterruptedException e) {
			throw new ServiceRegistryException(e);
		}
	}

	private static void install(ServiceTarget target, SingletonServiceBuilderFactory factory, ServiceName name,
			int quorum) {
		InjectedValue<ServerEnvironment> env = new InjectedValue<>();
		HAService service = new HAService(env);
		factory.createSingletonServiceBuilder(name, service)
				.electionPolicy(new PreferredSingletonElectionPolicy(new SimpleSingletonElectionPolicy(),
						new NamePreference(PREFERRED_NODE)))
				.requireQuorum(quorum).build(target).addDependency(SERVICE_NAME, ServerEnvironment.class, env)
				.install();
	}
}
