package it.vige.businesscomponents.injection.interceptor.service;

import java.util.List;

public interface ItemService {
	void create(Item item);
	List<Item> getList();
	List<Item> getExcludedList();
	void createTimer();
	boolean awaitTimerCall();
    String getInterceptorResults();
}
