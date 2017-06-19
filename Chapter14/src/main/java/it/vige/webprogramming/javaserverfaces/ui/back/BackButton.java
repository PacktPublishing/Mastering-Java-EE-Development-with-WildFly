package it.vige.webprogramming.javaserverfaces.ui.back;

import static javax.faces.event.PhaseId.INVOKE_APPLICATION;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class BackButton implements PhaseListener {

	private static final long serialVersionUID = -4584749689883200929L;

	public static final String BACK_BUTTON = "backButton";

	private PhaseId phaseId = INVOKE_APPLICATION;

	private String oldViewId;

	public String getOldViewId() {
		return oldViewId;
	}

	@Override
	public void afterPhase(PhaseEvent arg0) {
		FacesContext facesContext = arg0.getFacesContext();
		UIViewRoot uiViewRoot = facesContext.getViewRoot();
		if (getCancelButton(uiViewRoot) != null)
			facesContext.getViewRoot().setViewId(oldViewId);
	}

	@Override
	public void beforePhase(PhaseEvent arg0) {
		FacesContext facesContext = arg0.getFacesContext();
		UIViewRoot uiViewRoot = facesContext.getViewRoot();
		if (getCancelButton(uiViewRoot) == null)
			oldViewId = uiViewRoot.getViewId();
	}

	@Override
	public PhaseId getPhaseId() {
		return phaseId;
	}
	
	private UIComponent getCancelButton(UIViewRoot uiViewRoot) {
		List<UIComponent> result = new ArrayList<UIComponent>();
		findComponents("cancel", uiViewRoot.getChildren(), result);
		UIComponent cancelButton = result.isEmpty() ? null : result.get(0);
		return cancelButton;
	}

	private void findComponents(String id, List<UIComponent> children, List<UIComponent> result) {
		for (UIComponent child : children) {
			if (child.getId().equals(id))
				result.add(child);
			else
				findComponents(id, child.getChildren(), result);
		}
	}

}
