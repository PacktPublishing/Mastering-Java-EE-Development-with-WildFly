package it.vige.businesscomponents.injection.context;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;

public class FieldScopedBean {

	@SessionScoped
	private String textSession;

	@ApplicationScoped
	private String textApplication;

	@RequestScoped
	private String textRequest;

	@ConversationScoped
	private String textConversation;

	@Dependent
	private String textDependent;
	

	public String getTextSession() {
		return textSession;
	}

	public void setTextSession(String textSession) {
		this.textSession = textSession;
	}

	public String getTextApplication() {
		return textApplication;
	}

	public void setTextApplication(String textApplication) {
		this.textApplication = textApplication;
	}

	public String getTextRequest() {
		return textRequest;
	}

	public void setTextRequest(String textRequest) {
		this.textRequest = textRequest;
	}

	public String getTextConversation() {
		return textConversation;
	}

	public void setTextConversation(String textConversation) {
		this.textConversation = textConversation;
	}
}
