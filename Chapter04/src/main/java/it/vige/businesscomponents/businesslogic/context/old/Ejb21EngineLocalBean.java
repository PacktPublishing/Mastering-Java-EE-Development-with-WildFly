package it.vige.businesscomponents.businesslogic.context.old;

import static java.util.logging.Logger.getLogger;

import java.rmi.RemoteException;
import java.security.Principal;
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
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

@Stateless(name = "ejb21EngineLocal")
@LocalHome(value = Ejb21LocalHome.class)
@Local(value = Ejb21Local.class)
public class Ejb21EngineLocalBean implements Ejb21Local, SessionBean {

	private static final long serialVersionUID = -1785386056310291324L;
	private static final Logger logger = getLogger(Ejb21EngineLocalBean.class.getName());
	private int speed;

	@Resource
	private SessionContext context;

	@Init
	public void init() {
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
		context.getContextData().put("_engine_data", data);
	}

	public void log() {
		Principal principal = context.getCallerPrincipal();
		Map<String, Object> contextData = context.getContextData();
		EJBLocalHome ejbLocalHome = context.getEJBLocalHome();
		EJBLocalObject ejbLocalObject = context.getEJBLocalObject();
		Ejb21Local LocalEngine = context.getBusinessObject(Ejb21Local.class);
		boolean isCallerInRole = context.isCallerInRole("admin");
		logger.info("ejb21LocalEngineBean principal: " + principal);
		logger.info("ejb21LocalEngineBean contextData:" + contextData);
		logger.info("ejb21LocalEngineBean ejbLocalHome:" + ejbLocalHome);
		logger.info("ejb21LocalEngineBean ejbLocalObject:" + ejbLocalObject);
		logger.info("ejb21LocalEngineBean LocalEngineBean:" + LocalEngine);
		logger.info("ejb21LocalEngineBean isCallerInRole:" + isCallerInRole);
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

	@Override
	public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
		logger.info("setSessionContext");
	}

	@Override
	public void ejbRemove() throws EJBException, RemoteException {
		logger.info("ejbRemove");
	}

	@Override
	public void ejbActivate() throws EJBException, RemoteException {
		logger.info("ejbActivate");
	}

	@Override
	public void ejbPassivate() throws EJBException, RemoteException {
		logger.info("ejbPassivate");
	}

}
