package it.vige.realtime.messaging.remote;

import java.io.Serializable;

public class Questionary implements Serializable {

	private static final long serialVersionUID = 7097408487111792846L;
	private String question;
	private String response;
	private boolean approved;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}
}
