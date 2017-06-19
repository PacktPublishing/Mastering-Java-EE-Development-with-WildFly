package it.vige.businesscomponents.transactions.concurrent;

import static it.vige.businesscomponents.transactions.concurrent.ConcurrentStatus.foundTransactionScopedBean;
import static it.vige.businesscomponents.transactions.concurrent.ConcurrentStatus.latch;

import javax.inject.Inject;
import javax.transaction.Transactional;

public class MyTaskWithTransaction implements Runnable {

	private int id;

	@Inject
	private MyTransactionScopedBean bean;

	public MyTaskWithTransaction() {
	}

	public MyTaskWithTransaction(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	@Transactional
	public void run() {
		try {
			foundTransactionScopedBean = bean.isInTx();
		} catch (Exception e) {
			e.printStackTrace();
		}
		latch.countDown();
	}

}
