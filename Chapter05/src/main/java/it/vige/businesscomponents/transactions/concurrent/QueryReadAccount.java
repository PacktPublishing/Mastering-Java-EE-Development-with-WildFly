package it.vige.businesscomponents.transactions.concurrent;

import static it.vige.businesscomponents.transactions.concurrent.ConcurrentStatus.latch;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional(value = REQUIRES_NEW)
public class QueryReadAccount implements Runnable {

	private static final Logger logger = getLogger(QueryReadAccount.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	private int firstResult;
	private int secondResult;
	private int firstWaitTime;
	private int secondWaitTime;

	@Override
	public void run() {
		try {
			latch = new CountDownLatch(1);
			latch.await(firstWaitTime, MILLISECONDS);
			firstResult = entityManager.createNamedQuery("SelectAll").getResultList().size();
			latch.await(secondWaitTime, MILLISECONDS);
			secondResult = entityManager.createNamedQuery("SelectAll").getResultList().size();
		} catch (Exception e) {
			logger.log(SEVERE, "error bank thread", e);
		}
	}

	public int getFirstResult() {
		return firstResult;
	}

	public int getSecondResult() {
		return secondResult;
	}

	public int getFirstWaitTime() {
		return firstWaitTime;
	}

	public void setFirstWaitTime(int firstWaitTime) {
		this.firstWaitTime = firstWaitTime;
	}

	public int getSecondWaitTime() {
		return secondWaitTime;
	}

	public void setSecondWaitTime(int secondWaitTime) {
		this.secondWaitTime = secondWaitTime;
	}

}
