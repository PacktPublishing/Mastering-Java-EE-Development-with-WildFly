package it.vige.businesscomponents.businesslogic.context.old;

import static java.util.logging.Logger.getLogger;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.Init;
import javax.ejb.Local;
import javax.ejb.LocalHome;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;

import it.vige.businesscomponents.businesslogic.context.old.Ejb21StateLocal;
import it.vige.businesscomponents.businesslogic.context.old.Ejb21StateLocalHome;

@Stateful(name = "ejb21StateEngineLocal")
@LocalHome(value = Ejb21StateLocalHome.class)
@Local(value = Ejb21StateLocal.class)
public class Ejb21StateEngineLocalBean implements Ejb21StateLocal {

	private static final Logger logger = getLogger(Ejb21StateEngineLocalBean.class.getName());
	private int speed;

	@Resource
	private SessionContext context;

	@Init
	public void init() {
		speed = 1;
	}

	@Init
	public void init(String message) {
		speed = 1;
	}

	@Init
	public void init(Collection<?> messages) {
		speed = 1;
	}

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

	@Override
	public void add(Object data) {
		context.getContextData().put("state_engine_data", data);
	}

	public void log() {
		Principal principal = context.getCallerPrincipal();
		Map<String, Object> contextData = context.getContextData();
		EJBLocalHome ejbLocalHome = context.getEJBLocalHome();
		EJBLocalObject ejbLocalObject = context.getEJBLocalObject();
		Ejb21StateLocal stateLocalEngine = context.getBusinessObject(Ejb21StateLocal.class);
		boolean isCallerInRole = context.isCallerInRole("admin");
		logger.info("stateLocalEngineBean principal: " + principal);
		logger.info("stateLocalEngineBean contextData:" + contextData);
		logger.info("stateLocalEngineBean ejbLocalHome:" + ejbLocalHome);
		logger.info("stateLocalEngineBean ejbLocalObject:" + ejbLocalObject);
		logger.info("stateLocalEngineBean stateLocalEngineBean:" + stateLocalEngine);
		logger.info("stateLocalEngineBean isCallerInRole:" + isCallerInRole);
	}

	@Override
	public EJBLocalHome getEJBLocalHome() throws EJBException {
		logger.info("getEJBLocalHome");
		return null;
	}

	@Override
	public Object getPrimaryKey() throws EJBException {
		logger.info("getPrimaryKey");
		return null;
	}

	@Override
	public void remove() throws RemoveException, EJBException {
		logger.info("remove");
	}

	@Override
	public boolean isIdentical(EJBLocalObject obj) throws EJBException {
		logger.info("isIdentical");
		return false;
	}

}
