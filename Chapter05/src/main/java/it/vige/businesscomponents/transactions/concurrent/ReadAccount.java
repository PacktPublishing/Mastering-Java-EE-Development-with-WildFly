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

import it.vige.businesscomponents.transactions.Account;

@Transactional(value = REQUIRES_NEW)
public class ReadAccount implements Runnable {

	private static final Logger logger = getLogger(ReadAccount.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	private int accountNumber;
	private double firstResult;
	private double secondResult;
	private int firstWaitTime;
	private int secondWaitTime;

	@Override
	public void run() {
		try {
			latch = new CountDownLatch(1);
			latch.await(firstWaitTime, MILLISECONDS);
			Account account = entityManager.find(Account.class, accountNumber);
			firstResult = account.getCredit();
			latch.await(secondWaitTime, MILLISECONDS);
			account = entityManager.find(Account.class, accountNumber);
			secondResult = account.getCredit();
		} catch (Exception e) {
			logger.log(SEVERE, "error bank thread", e);
		}
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public double getFirstResult() {
		return firstResult;
	}

	public double getSecondResult() {
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
