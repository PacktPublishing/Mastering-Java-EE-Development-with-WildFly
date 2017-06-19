package it.vige.realtime.asynchronousejb.timer;

import static java.util.logging.Logger.getLogger;

import java.rmi.RemoteException;
import java.util.logging.Logger;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.Local;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TimedObject;
import javax.ejb.Timer;
import javax.ejb.TimerService;

@Stateless
@Local(value = OldSpecsLocal.class)
public class OldSpecsBean implements OldSpecsLocal, SessionBean, TimedObject {

	private static final long serialVersionUID = -8749720663891187827L;
	private static final Logger logger = getLogger(OldSpecsBean.class.getName());

	private String whyWasICalled;

	// This method is called by the EJB container upon Timer expiration.
	public void ejbTimeout(Timer theTimer) {

		// Any code typically placed in an EJB method can be placed here.

		String whyWasICalled = (String) theTimer.getInfo();
		this.whyWasICalled = whyWasICalled;
		logger.info("I was called because of" + whyWasICalled);
	}

	// Instance variable to hold the EJB context.
	private SessionContext theEJBContext;

	// This method is called by the EJB container upon bean creation.
	public void setSessionContext(SessionContext theContext) {

		// Save the entity context passed in upon bean creation.
		theEJBContext = theContext;
	}

	// This business method causes the ejbTimeout method to begin in 30 seconds.
	@Override
	public void fireInThirtySeconds() throws EJBException {

		TimerService theTimerService = theEJBContext.getTimerService();
		String aLabel = "30SecondTimeout";
		Timer theTimer = theTimerService.createTimer(300, aLabel);
		logger.info("timer is: " + theTimer);
	} // end of method fireInThirtySeconds

	@Override
	public void ejbActivate() throws EJBException, RemoteException {
		logger.info("old specs entity bean");
	}

	@Override
	public void ejbPassivate() throws EJBException, RemoteException {
		logger.info("old specs entity bean");
	}

	@Override
	public void ejbRemove() throws EJBException, RemoteException {
		logger.info("old specs entity bean");
	}

	@Override
	public EJBLocalHome getEJBLocalHome() throws EJBException {
		return null;
	}

	@Override
	public Object getPrimaryKey() throws EJBException {
		return null;
	}

	@Override
	public void remove() throws RemoveException, EJBException {

	}

	@Override
	public boolean isIdentical(EJBLocalObject obj) throws EJBException {
		return false;
	}

	public String getWhyWasICalled() {
		return whyWasICalled;
	}

}