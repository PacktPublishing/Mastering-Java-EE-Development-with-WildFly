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
public class WriteAccount implements Runnable {

	private static final Logger logger = getLogger(WriteAccount.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	private int accountNumber;
	private double amount;
	private double result;
	private int waitTime;

	@Override
	public void run() {
		try {
			latch = new CountDownLatch(1);
			Account account = entityManager.find(Account.class, accountNumber);
			account.add(amount);
			entityManager.merge(account);
			latch.await(waitTime, MILLISECONDS);
			result = account.getCredit();
		} catch (Exception e) {
			logger.log(SEVERE, "error bank thread", e);
		}
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public double getResult() {
		return result;
	}

	public void setResult(double result) {
		this.result = result;
	}

	public int getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}

}
