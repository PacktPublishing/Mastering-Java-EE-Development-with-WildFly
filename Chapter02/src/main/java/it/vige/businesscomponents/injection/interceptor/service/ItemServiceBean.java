package it.vige.businesscomponents.injection.interceptor.service;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.interceptor.ExcludeClassInterceptors;
import javax.interceptor.Interceptors;

import it.vige.businesscomponents.injection.interceptor.Audit;
import it.vige.businesscomponents.injection.interceptor.ExcludedInterceptor;
import it.vige.businesscomponents.injection.interceptor.ExcludingInterceptor;
import it.vige.businesscomponents.injection.interceptor.Logging;

@Stateless
public class ItemServiceBean implements ItemService {

	private List<Item> items;
	private final CountDownLatch latch = new CountDownLatch(1);
	private static boolean timerServiceCalled = false;
	private String interceptorResults = "";

	@Resource
	private TimerService timerService;

	@ExcludeClassInterceptors
	@Override
	public void createTimer() {
		timerService.createTimer(100, null);
	}

	@Audit
	@Logging
	public ItemServiceBean() {
		items = new ArrayList<Item>();
	}

	@Audit
	@Logging
	@Override
	public void create(Item item) {
		items.add(item);
	}

	@Audit
	@Logging
	@Override
	public List<Item> getList() {
		return items;
	}

	@Interceptors({ ExcludedInterceptor.class, ExcludingInterceptor.class })
	@Override
	public List<Item> getExcludedList() {
		return items;
	}

	@Timeout
	@Interceptors(ExcludingInterceptor.class)
	private void timeout(Timer timer) {
		timerServiceCalled = true;
		interceptorResults += "@Timeout";
		latch.countDown();
	}

	@ExcludeClassInterceptors
	public boolean awaitTimerCall() {
		try {
			latch.await(10, SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return timerServiceCalled;
	}

	public String getInterceptorResults() {
		return interceptorResults;
	}

}
