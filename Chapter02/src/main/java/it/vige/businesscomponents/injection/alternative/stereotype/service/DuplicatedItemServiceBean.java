package it.vige.businesscomponents.injection.alternative.stereotype.service;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import it.vige.businesscomponents.injection.alternative.stereotype.ServiceStereotype;
import it.vige.businesscomponents.injection.interceptor.service.Item;
import it.vige.businesscomponents.injection.interceptor.service.ItemService;

@ServiceStereotype
public class DuplicatedItemServiceBean implements ItemService {

	private List<Item> items;

	@PostConstruct
	public void init() {
		items = new LinkedList<>();
	}

	@PreDestroy
	public void destroy() {
		items = null;
	}

	@Override
	public void create(Item item) {
		items.add(item);
		Item duplicatedItem = new Item();
		duplicatedItem.setName(item.getName());
		items.add(duplicatedItem);
	}

	@Override
	public List<Item> getList() {
		return items;
	}

	@Override
	public List<Item> getExcludedList() {
		return items;
	}

	@Override
	public void createTimer() {
		
	}

	@Override
	public boolean awaitTimerCall() {
		return false;
	}

	@Override
	public String getInterceptorResults() {
		return null;
	}

}
