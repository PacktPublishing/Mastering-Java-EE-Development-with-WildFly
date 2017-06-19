package it.vige.webprogramming.javaserverfaces.auth;

import java.io.IOException;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

public class ACLOtherwiseTagHandler extends TagHandler {

	public ACLOtherwiseTagHandler(TagConfig config) {
		super(config);
	}

	public void apply(FaceletContext ctx, UIComponent parent) throws IOException, FacesException, ELException {
		nextHandler.apply(ctx, parent);
	}
}
