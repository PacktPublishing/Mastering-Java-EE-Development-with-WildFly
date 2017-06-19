package it.vige.webprogramming.javaserverfaces.auth;

import java.io.IOException;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.CompositeFaceletHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletHandler;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandler;

public class ACLChooseTagHandler extends TagHandler {

	private ACLWhenTagHandler when;
	private ACLOtherwiseTagHandler otherwise;

	public ACLChooseTagHandler(TagConfig config) {
		super(config);

		FaceletHandler itr = nextHandler;
		if (itr instanceof CompositeFaceletHandler) {
			FaceletHandler[] handlers = ((CompositeFaceletHandler) itr).getHandlers();
			if (handlers != null && handlers.length > 0 && handlers[0] instanceof ACLWhenTagHandler) {
				when = (ACLWhenTagHandler) handlers[0];
			} else {
				throw new TagException(tag, "isAllowedChoose Tag must have a isAllowedWhen Tag");
			}

			if (handlers.length > 1) {
				FaceletHandler itr2 = handlers[1];
				if (itr2 instanceof ACLOtherwiseTagHandler) {
					otherwise = (ACLOtherwiseTagHandler) itr2;
				}
			}
		} else {
			throw new TagException(tag, "isAllowedChoose Tag must have a CompositeFaceletHandler Tag");
		}
	}

	public void apply(FaceletContext ctx, UIComponent parent) throws IOException, FacesException, ELException {
		if (when.isAllowed(ctx)) {
			when.apply(ctx, parent);
			return;
		} else {
			if (otherwise != null) {
				otherwise.apply(ctx, parent);
			}
		}
	}
}
