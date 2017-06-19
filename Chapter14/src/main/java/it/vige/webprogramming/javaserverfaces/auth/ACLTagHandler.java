package it.vige.webprogramming.javaserverfaces.auth;

import java.io.IOException;

import javax.ejb.TransactionAttribute;
import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

import it.vige.rubia.auth.ForumsACLProvider;
import it.vige.rubia.auth.UserModule;

public class ACLTagHandler extends TagHandler {

	private TagAttribute fragment;
	private TagAttribute contextData;
	private TagAttribute forumsACLProviderAttr;
	private TagAttribute userModuleAttr;

	public ACLTagHandler(TagConfig config) {
		super(config);

		fragment = getRequiredAttribute("fragment");

		contextData = getAttribute("contextData");

		forumsACLProviderAttr = getAttribute("forumsACLProvider");

		userModuleAttr = getAttribute("userModule");
	}

	@TransactionAttribute
	public void apply(FaceletContext ctx, UIComponent parent) throws IOException, FacesException, ELException {
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
			nextHandler.apply(ctx, parent);
			return;
		}

		String resource = fragment.getValue();
		String contextStr = null;

		if (this.contextData != null) {
			contextStr = contextData.getValue();
		}
		boolean isAccessAllowed = new ACLRenderingController().aclCheck(resource, contextStr, forumsACLProvider,
				userModule, ctx);

		if (isAccessAllowed) {
			nextHandler.apply(ctx, parent);
		}
	}
}