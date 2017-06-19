package it.vige.businesscomponents.transactions.concurrent;

import static it.vige.businesscomponents.transactions.concurrent.ConcurrentStatus.latch;

import java.util.concurrent.Callable;

public class MyCallableTask implements Callable<Product> {

	private int id;

	public MyCallableTask(int id) {
		this.id = id;
	}

	@Override
	public Product call() {
		latch.countDown();
		return new Product(id);
	}
}
