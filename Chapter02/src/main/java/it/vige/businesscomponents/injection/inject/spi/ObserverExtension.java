package it.vige.businesscomponents.injection.inject.spi;

import static java.util.logging.Logger.getLogger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.AfterTypeDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.inject.spi.ProcessBeanAttributes;
import javax.enterprise.inject.spi.ProcessInjectionPoint;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.enterprise.inject.spi.ProcessManagedBean;
import javax.enterprise.inject.spi.ProcessObserverMethod;
import javax.enterprise.inject.spi.ProcessProducer;
import javax.enterprise.inject.spi.ProcessProducerField;
import javax.enterprise.inject.spi.ProcessProducerMethod;
import javax.enterprise.inject.spi.ProcessSessionBean;
import javax.enterprise.inject.spi.ProcessSyntheticAnnotatedType;
import javax.enterprise.inject.spi.ProducerFactory;
import javax.enterprise.inject.spi.WithAnnotations;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Named;

public class ObserverExtension implements Extension {

	private static final Logger logger = getLogger(ObserverExtension.class.getName());

	private int beforeBeanDiscovery;
	private int processAnnotatedType;
	private int processAnnotatedTypeWithAnnotations;
	private int afterBeanDiscovery;
	private int processBean;
	private int processBeanAttributes;
	private int processInjectionPoint;
	private int processInjectionTarget;
	private int processManagedBean;
	private int processObserverMethod;
	private int processProducer;
	private int processProducerField;
	private int processProducerMethod;
	private int processSessionBean;
	private int processSyntheticAnnotatedType;
	private int producerFactory;
	private int afterDeploymentValidation;
	private int afterTypeDiscovery;

	void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd) {
		logger.info("beginning the scanning process");
		beforeBeanDiscovery++;
	}

	<T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat) {
		logger.info("scanning type: " + pat.getAnnotatedType().getJavaClass().getName());
		processAnnotatedType++;
	}

	<T> void processAnnotatedTypeWithAnnotations(
			@Observes @WithAnnotations({ Named.class }) ProcessAnnotatedType<T> pat) {
		logger.info("scanning type: " + pat.getAnnotatedType().getJavaClass().getName());
		processAnnotatedTypeWithAnnotations++;
	}

	void afterBeanDiscovery(@Observes AfterBeanDiscovery abd) {
		logger.info("finished the scanning process");
		afterBeanDiscovery++;
	}

	<T> void processBean(@Observes ProcessBean<T> pb) {
		logger.info("finished the scanning process");
		processBean++;
	}

	<T> void processBeanAttributes(@Observes ProcessBeanAttributes<T> pb) {
		logger.info("finished the scanning process");
		processBeanAttributes++;
	}

	<K, V> void processInjectionPoint(@Observes ProcessInjectionPoint<K, V> pip) {
		logger.info("finished the scanning process");
		processInjectionPoint++;
	}

	<T> void processInjectionTarget(@Observes ProcessInjectionTarget<T> pit) {
		logger.info("finished the scanning process");
		processInjectionTarget++;
	}

	<T> void processManagedBean(@Observes ProcessManagedBean<T> pmb) {
		logger.info("finished the scanning process");
		processManagedBean++;
	}

	<K, V> void processObserverMethod(@Observes ProcessObserverMethod<K, V> pom) {
		logger.info("finished the scanning process");
		processObserverMethod++;
	}

	<K, V> void processProducer(@Observes ProcessProducer<K, V> pp) {
		logger.info("finished the scanning process");
		processProducer++;
	}

	<K, V> void processProducerField(@Observes ProcessProducerField<K, V> ppf) {
		logger.info("finished the scanning process");
		processProducerField++;
	}

	<K, V> void processProducerMethod(@Observes ProcessProducerMethod<K, V> ppm) {
		logger.info("finished the scanning process");
		processProducerMethod++;
	}

	<T> void processSessionBean(@Observes ProcessSessionBean<T> psb) {
		logger.info("finished the scanning process");
		processSessionBean++;
	}

	<T> void processSyntheticAnnotatedType(@Observes ProcessSyntheticAnnotatedType<T> psa) {
		logger.info("finished the scanning process");
		processSyntheticAnnotatedType++;
	}

	<T> void producerFactory(@Observes ProducerFactory<T> pf) {
		logger.info("finished the scanning process");
		producerFactory++;
	}

	void afterDeploymentValidation(@Observes AfterDeploymentValidation adv) {
		logger.info("finished the scanning process");
		afterDeploymentValidation++;
	}

	void afterTypeDiscovery(@Observes AfterTypeDiscovery atd) {
		logger.info("finished the scanning process");
		afterTypeDiscovery++;
	}

	void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {

		// use this to read annotations of the class
		AnnotatedType<SecurityManager> at = bm.createAnnotatedType(SecurityManager.class);

		// use this to instantiate the class and inject dependencies
		final InjectionTarget<SecurityManager> it = bm.createInjectionTarget(at);
		abd.addBean(new Bean<SecurityManager>() {
			@Override
			public Class<?> getBeanClass() {
				return SecurityManager.class;
			}

			@Override
			public Set<InjectionPoint> getInjectionPoints() {
				return it.getInjectionPoints();
			}

			@Override
			public String getName() {
				return "securityManager";
			}

			@Override
			public Set<Annotation> getQualifiers() {
				Set<Annotation> qualifiers = new HashSet<Annotation>();
				qualifiers.add(new AnnotationLiteral<Default>() {

					private static final long serialVersionUID = -7995171126928455799L;
				});
				qualifiers.add(new AnnotationLiteral<Any>() {

					private static final long serialVersionUID = 3307114201849744582L;
				});
				return qualifiers;
			}

			@Override
			public Class<? extends Annotation> getScope() {
				return ApplicationScoped.class;
			}

			@Override
			public Set<Class<? extends Annotation>> getStereotypes() {
				return Collections.emptySet();
			}

			@Override
			public Set<Type> getTypes() {
				Set<Type> types = new HashSet<Type>();
				types.add(SecurityManager.class);
				types.add(Object.class);
				return types;
			}

			@Override
			public boolean isAlternative() {
				return false;
			}

			@Override
			public boolean isNullable() {
				return false;
			}

			@Override
			public SecurityManager create(CreationalContext<SecurityManager> ctx) {
				SecurityManager instance = it.produce(ctx);
				it.inject(instance, ctx);
				it.postConstruct(instance);
				return instance;
			}

			@Override
			public void destroy(SecurityManager instance, CreationalContext<SecurityManager> ctx) {
				it.preDestroy(instance);
				it.dispose(instance);
				ctx.release();
			}
		});
	}

	public int getBeforeBeanDiscovery() {
		return beforeBeanDiscovery;
	}

	public int getProcessAnnotatedType() {
		return processAnnotatedType;
	}

	public int getAfterBeanDiscovery() {
		return afterBeanDiscovery;
	}

	public int getProcessAnnotatedTypeWithAnnotations() {
		return processAnnotatedTypeWithAnnotations;
	}

	public int getProcessBean() {
		return processBean;
	}

	public int getProcessBeanAttributes() {
		return processBeanAttributes;
	}

	public int getProcessInjectionPoint() {
		return processInjectionPoint;
	}

	public int getProcessInjectionTarget() {
		return processInjectionTarget;
	}

	public int getProcessManagedBean() {
		return processManagedBean;
	}

	public int getProcessObserverMethod() {
		return processObserverMethod;
	}

	public int getProcessProducer() {
		return processProducer;
	}

	public int getProcessProducerField() {
		return processProducerField;
	}

	public int getProcessProducerMethod() {
		return processProducerMethod;
	}

	public int getProcessSessionBean() {
		return processSessionBean;
	}

	public int getProcessSyntheticAnnotatedType() {
		return processSyntheticAnnotatedType;
	}

	public int getProducerFactory() {
		return producerFactory;
	}

	public int getAfterDeploymentValidation() {
		return afterDeploymentValidation;
	}

	public int getAfterTypeDiscovery() {
		return afterTypeDiscovery;
	}
}