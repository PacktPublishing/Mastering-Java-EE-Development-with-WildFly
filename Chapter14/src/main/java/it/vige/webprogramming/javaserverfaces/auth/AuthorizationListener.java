package it.vige.webprogramming.javaserverfaces.auth;

import static it.vige.rubia.PortalUtil.getUser;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.ejb.EJB;
import javax.faces.FacesException;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import it.vige.rubia.auth.ActionContext;
import it.vige.rubia.auth.ForumsACLProvider;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.auth.UserModule;

@SecureActionForum
@Interceptor
public class AuthorizationListener implements Serializable {

	private static final long serialVersionUID = 1297507762601849153L;

	@EJB
	private ForumsACLProvider forumsACLProvider;

	@EJB
	private UserModule userModule;

	@AroundInvoke
	public Object accessAllowed(InvocationContext ctx) throws Exception {
		Method businessAction = ctx.getMethod();
		Object managedBean = ctx.getTarget();
		boolean isAccessAllowed = false;

		try {

			ActionContext securityContext = new ActionContext(getUser(userModule));
			securityContext.setBusinessAction(businessAction);
			securityContext.setManagedBean(managedBean);

			isAccessAllowed = forumsACLProvider.hasAccess(securityContext);
			if (!isAccessAllowed)
				return null;
		} catch (NoSuchMethodException nsme) {
			throw new FacesException("Error calling action method of component with id " + nsme, nsme);
		} catch (Exception e) {
			throw new FacesException("Error calling action method of component with id " + e, e);
		}
		return ctx.proceed();
	}
}