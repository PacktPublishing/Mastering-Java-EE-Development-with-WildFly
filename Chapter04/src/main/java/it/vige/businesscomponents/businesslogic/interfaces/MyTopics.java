package it.vige.businesscomponents.businesslogic.interfaces;

import javax.ejb.Stateless;

@Stateless
public class MyTopics implements Topics {

	@Override
	public String getCurrentTopicName() {
		return "current_topic_name";
	}
}
