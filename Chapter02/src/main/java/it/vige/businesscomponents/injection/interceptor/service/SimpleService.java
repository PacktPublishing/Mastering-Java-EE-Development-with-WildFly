package it.vige.businesscomponents.injection.interceptor.service;

import javax.interceptor.ExcludeClassInterceptors;
import javax.interceptor.Interceptors;

import it.vige.businesscomponents.injection.interceptor.ExcludedInterceptor;
import it.vige.businesscomponents.injection.interceptor.IncludedInterceptor;

@Interceptors({ ExcludedInterceptor.class, IncludedInterceptor.class })
public class SimpleService {

	private Item item;

	@ExcludeClassInterceptors
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
}
