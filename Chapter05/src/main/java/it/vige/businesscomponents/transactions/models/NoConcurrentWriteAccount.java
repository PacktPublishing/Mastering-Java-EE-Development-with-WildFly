package it.vige.businesscomponents.transactions.models;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import it.vige.businesscomponents.transactions.Account;

@Transactional(value = REQUIRES_NEW)
public class NoConcurrentWriteAccount {

	private static final Logger logger = getLogger(NoConcurrentWriteAccount.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	private int accountNumber;
	private double amount;

	public void run() {
		try {
			Account account = new Account(accountNumber, amount);
			entityManager.persist(account);
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
}
