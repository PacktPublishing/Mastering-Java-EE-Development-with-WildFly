package it.vige.businesscomponents.injection.context;

import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;

@ConversationScoped
public class ConversationBean extends CountBean implements Serializable {

	private static final long serialVersionUID = -5575830634866764183L;

	@Inject
	private Conversation conversation;

	public void initConversation() {
		conversation.begin();
	}

	public void endConversation() {
		if (!conversation.isTransient()) {
			conversation.end();
		};
	}

	public Conversation getConversation() {
		return conversation;
	}

}
