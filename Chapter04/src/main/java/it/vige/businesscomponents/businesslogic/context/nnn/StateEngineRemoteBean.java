package it.vige.businesscomponents.businesslogic.context.nnn;

import static java.util.logging.Logger.getLogger;

import java.rmi.RemoteException;
import java.security.Principal;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;

@Stateful(name = "stateEngineRemote")
public class StateEngineRemoteBean implements StateEngineRemote, SessionSynchronization {

	private static final Logger logger = getLogger(StateEngineRemoteBean.class.getName());

	private int speed;

	@Resource
	private SessionContext context;

	@Override
	public int go(int speed) {
		return this.speed += speed;
	}

	@Override
	public int retry(int speed) {
		return this.speed -= speed;
	}

	@Override
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	@Override
	public void add(Object data) {
		context.getContextData().put("engine_data", data);
	}

	@Override
	public void log() {
		Principal principal = context.getCallerPrincipal();
		Map<String, Object> contextData = context.getContextData();
		Class<?> invokedBusinessInterface = context.getInvokedBusinessInterface();
		StateEngineRemote engineRemote = context.getBusinessObject(StateEngineRemote.class);
		boolean isCallerInRole = context.isCallerInRole("admin");
		logger.info("stateEngineRemoteBean principal: " + principal);
		logger.info("stateEngineRemoteBean contextData:" + contextData);
		logger.info("stateEngineRemoteBean invokedBusinessInterface:" + invokedBusinessInterface);
		logger.info("stateEngineRemoteBean engineRemote:" + engineRemote);
		logger.info("stateEngineRemoteBean isCallerInRole:" + isCallerInRole);
	}

	@Override
	public void afterBegin() throws EJBException, RemoteException {
		logger.info("the bean is begun");
	}

	@Override
	public void beforeCompletion() throws EJBException, RemoteException {
		logger.info("the bean is completing");
	}

	@Override
	public void afterCompletion(boolean committed) throws EJBException, RemoteException {
		logger.info("the bean is completed");
	}

}
