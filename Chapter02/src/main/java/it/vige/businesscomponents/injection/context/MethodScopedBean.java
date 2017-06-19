package it.vige.businesscomponents.injection.context;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.spi.BeanManager;

public class MethodScopedBean {

	private String textSession;

	private String textApplication;

	private String textRequest;

	private String textConversation;

	private String textDependent;

	@Resource(lookup = "java:comp/BeanManager")
	private BeanManager beanManager;
	
	@SessionScoped
	public String getTextSession() {
		return textSession;
	}

	public void setTextSession(String textSession) {
		this.textSession = textSession;
	}

	@ApplicationScoped
	public String getTextApplication() {
		return textApplication;
	}

	public void setTextApplication(String textApplication) {
		this.textApplication = textApplication;
	}

	public String getTextRequest() {
		return textRequest;
	}

	@RequestScoped
	public void setTextRequest(String textRequest) {
		this.textRequest = textRequest;
	}

	public String getTextConversation() {
		return textConversation;
	}

	@ConversationScoped
	public void setTextConversation(String textConversation) {
		this.textConversation = textConversation;
	}

	@Dependent
	public String getTextDependent() {
		return textDependent;
	}

	@Dependent
	public void setTextDependent(String textDependent) {
		this.textDependent = textDependent;
	}

	public BeanManager getBeanManager() {
		return beanManager;
	}
}
