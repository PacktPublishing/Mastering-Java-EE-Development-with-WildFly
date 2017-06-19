package it.vige.clustering.sharedata.singleton;

import static org.jboss.msc.service.ServiceName.JBOSS;
import static org.jboss.msc.service.ServiceName.parse;
import static org.wildfly.clustering.singleton.SingletonDefaultRequirement.SINGLETON_POLICY;

import org.jboss.as.server.ServerEnvironment;
import org.jboss.as.server.ServerEnvironmentService;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistryException;
import org.jboss.msc.value.InjectedValue;
import org.wildfly.clustering.singleton.SingletonPolicy;

public class HAServicePolicyActivator implements ServiceActivator {

	public static final ServiceName SERVICE_NAME = JBOSS.append("test", "haservice", "default-policy");

	@Override
	public void activate(ServiceActivatorContext context) throws ServiceRegistryException {
		try {
			SingletonPolicy policy = (SingletonPolicy) context.getServiceRegistry()
					.getRequiredService(parse(SINGLETON_POLICY.getName())).awaitValue();
			InjectedValue<ServerEnvironment> env = new InjectedValue<>();
			HAService service = new HAService(env);
			policy.createSingletonServiceBuilder(SERVICE_NAME, service).build(context.getServiceTarget())
					.addDependency(ServerEnvironmentService.SERVICE_NAME, ServerEnvironment.class, env).install();
		} catch (InterruptedException e) {
			throw new ServiceRegistryException(e);
		}
	}
}
