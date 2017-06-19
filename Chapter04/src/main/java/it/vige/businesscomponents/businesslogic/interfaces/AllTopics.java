package it.vige.businesscomponents.businesslogic.interfaces;

import javax.ejb.Stateless;

@Stateless
public class AllTopics implements Topics {

	@Override
	public String getCurrentTopicName() {
		return "all_topic_name";
	}
}
