package it.vige.businesscomponents.businesslogic.context.old;

import static java.util.logging.Logger.getLogger;

import java.rmi.RemoteException;
import java.security.Principal;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.Init;
import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

@Stateless(name = "ejb21EngineRemote")
@RemoteHome(value = Ejb21RemoteHome.class)
@Remote(value = Ejb21Remote.class)
public class Ejb21EngineRemoteBean implements Ejb21Remote {

	private static final Logger logger = getLogger(Ejb21EngineRemoteBean.class.getName());
	private int speed;

	@Resource
	private SessionContext context;

	@Init
	public void init() {
		speed = 1;
	}

	@Override
	public int go(int speed) throws RemoteException {
		return this.speed += speed;
	}

	@Override
	public int retry(int speed) throws RemoteException {
		return this.speed -= speed;
	}

	@Override
	public int getSpeed() throws RemoteException {
		return speed;
	}

	@Override
	public void add(Object data) throws RemoteException {
		context.getContextData().put("_engine_data", data);
	}

	@Override
	public void log() throws RemoteException {
		Principal principal = context.getCallerPrincipal();
		Map<String, Object> contextData = context.getContextData();
		EJBHome ejbHome = context.getEJBHome();
		EJBObject ejbObject = context.getEJBObject();
		Ejb21Remote RemoteEngine = context.getBusinessObject(Ejb21Remote.class);
		boolean isCallerInRole = context.isCallerInRole("admin");
		logger.info("ejb21RemoteEngineBean principal: " + principal);
		logger.info("ejb21RemoteEngineBean contextData:" + contextData);
		logger.info("ejb21RemoteEngineBean ejbHome:" + ejbHome);
		logger.info("ejb21RemoteEngineBean ejbObject:" + ejbObject);
		logger.info("ejb21RemoteEngineBean RemoteEngineBean:" + RemoteEngine);
		logger.info("ejb21RemoteEngineBean isCallerInRole:" + isCallerInRole);
	}

	@Override
	public EJBHome getEJBHome() throws RemoteException {
		logger.info("getEJBHome");
		return null;
	}

	@Override
	public Object getPrimaryKey() throws RemoteException {
		logger.info("getPrimaryKey");
		return null;
	}

	@Override
	public void remove() throws RemoteException, RemoveException {
		logger.info("remove");
	}

	@Override
	public Handle getHandle() throws RemoteException {
		logger.info("getHandle");
		return null;
	}

	@Override
	public boolean isIdentical(EJBObject ejbo) throws RemoteException {
		logger.info("isIdentical");
		return false;
	}

}
