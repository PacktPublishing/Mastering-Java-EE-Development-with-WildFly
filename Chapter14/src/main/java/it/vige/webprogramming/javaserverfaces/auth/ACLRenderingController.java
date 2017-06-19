package it.vige.webprogramming.javaserverfaces.auth;

import static it.vige.rubia.PortalUtil.getUser;

import java.util.StringTokenizer;

import javax.ejb.EJB;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.inject.Named;

import it.vige.rubia.auth.ForumsACLProvider;
import it.vige.rubia.auth.UIContext;
import it.vige.rubia.auth.UserModule;

@Named("aclRenderingController")
@RequestScoped
public class ACLRenderingController {

	@EJB
	private ForumsACLProvider forumsACLProvider;

	@EJB
	private UserModule userModule;

	public boolean aclCheck(String fragment, Object contextData) {
		boolean isAccessAllowed;
		try {
			Object[] runtime = new Object[1];
			if (contextData != null) {
				runtime[0] = contextData;
			}

			UIContext securityContext = new UIContext(getUser(userModule));
			securityContext.setFragment(fragment);
			securityContext.setContextData(runtime);
			isAccessAllowed = forumsACLProvider.hasAccess(securityContext);
		} catch (NoSuchMethodException nsme) {
			throw new FacesException(nsme);
		} catch (Exception e) {
			throw new FacesException(e);
		}

		return isAccessAllowed;
	}

	public boolean aclCheck(String fragment, String contextData, ForumsACLProvider forumsACLProvider,
			UserModule userModule, FaceletContext ctx) {
		boolean isAccessAllowed;
		try {
			Object[] runtime = null;
			FacesContext facesContext = ctx.getFacesContext();
			if (contextData != null && contextData.trim().length() > 0) {
				StringTokenizer st = new StringTokenizer(contextData, ",");
				runtime = new Object[st.countTokens()];
				int i = 0;
				while (st.hasMoreTokens()) {
					String parameter = st.nextToken();
					Object parameterValue = null;

					ExpressionFactory f = ctx.getExpressionFactory();
					ValueExpression expr = f.createValueExpression(ctx, parameter, Object.class);
					parameterValue = expr.getValue(facesContext.getELContext());

					runtime[i++] = parameterValue;
				}
			}

			UIContext securityContext = new UIContext(getUser(userModule));
			securityContext.setFragment(fragment);
			securityContext.setContextData(runtime);
			isAccessAllowed = forumsACLProvider.hasAccess(securityContext);
		} catch (NoSuchMethodException nsme) {
			throw new FacesException(nsme);
		} catch (Exception e) {
			throw new FacesException(e);
		}

		return isAccessAllowed;
	}
}
