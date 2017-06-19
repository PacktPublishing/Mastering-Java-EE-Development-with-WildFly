package it.vige.businesscomponents.businesslogic.context.nnn;

import static java.util.logging.Logger.getLogger;

import java.security.Principal;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

@Stateless(name = "engineLocal")
public class EngineLocalBean implements EngineLocal {

	private static final Logger logger = getLogger(EngineLocalBean.class.getName());

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
		EngineLocal engineLocal = context.getBusinessObject(EngineLocal.class);
		boolean isCallerInRole = context.isCallerInRole("admin");
		logger.info("engineLocalBean principal: " + principal);
		logger.info("engineLocalBean contextData:" + contextData);
		logger.info("engineLocalBean invokedBusinessInterface:" + invokedBusinessInterface);
		logger.info("engineLocalBean engineLocal:" + engineLocal);
		logger.info("engineLocalBean isCallerInRole:" + isCallerInRole);
	}

}
