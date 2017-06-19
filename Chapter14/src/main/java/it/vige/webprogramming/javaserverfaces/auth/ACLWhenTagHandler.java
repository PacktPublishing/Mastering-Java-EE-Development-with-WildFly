package it.vige.webprogramming.javaserverfaces.auth;

import static it.vige.rubia.PortalUtil.getUser;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

import it.vige.rubia.auth.ForumsACLProvider;
import it.vige.rubia.auth.UIContext;
import it.vige.rubia.auth.UserModule;

public class ACLWhenTagHandler extends TagHandler {

	private TagAttribute fragment;
	private TagAttribute contextData;
	private TagAttribute forumsACLProviderAttr;
	private TagAttribute userModuleAttr;

	public ACLWhenTagHandler(TagConfig config) {
		super(config);

		fragment = getRequiredAttribute("fragment");

		contextData = getAttribute("contextData");

		forumsACLProviderAttr = getAttribute("forumsACLProvider");

		userModuleAttr = getAttribute("userModule");
	}

	public void apply(FaceletContext ctx, UIComponent parent) throws IOException, FacesException, ELException {
		nextHandler.apply(ctx, parent);
	}

	public boolean isAllowed(FaceletContext ctx) {
		boolean isAllowed = false;

		FacesContext facesContext = ctx.getFacesContext();
		ForumsACLProvider forumsACLProvider = (ForumsACLProvider) forumsACLProviderAttr.getObject(ctx);
		UserModule userModule = (UserModule) userModuleAttr.getObject(ctx);

		boolean skipAuth = false;
		try {
			if (forumsACLProvider == null) {
				skipAuth = true;
			}
		} catch (Exception e) {
			skipAuth = true;
		}

		if (skipAuth) {
			isAllowed = true;
			return isAllowed;
		}

		try {
			String resource = fragment.getValue();
			String contextStr = null;

			if (contextData != null) {
				contextStr = contextData.getValue();
			}

			Object[] runtime = null;
			if (contextStr != null && contextStr.trim().length() > 0) {
				StringTokenizer st = new StringTokenizer(contextStr, ",");
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
			securityContext.setFragment(resource);
			securityContext.setContextData(runtime);

			isAllowed = forumsACLProvider.hasAccess(securityContext);
		} catch (NoSuchMethodException nsme) {
			throw new FacesException(nsme);
		} catch (Exception e) {
			throw new FacesException(e);
		}

		return isAllowed;
	}
}
