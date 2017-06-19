package it.vige.businesscomponents.transactions;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({ @NamedQuery(name = "SelectAll", query = "SELECT e FROM Account e") })
public class Account {

	@Id
	private int number;

	private double credit;

	public Account() {
	}

	public Account(int number, double credit) {
		this.number = number;
		this.credit = credit;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	public void add(double credit) {
		this.credit += credit;
	}

	public void less(double credit) throws Exception {
		if (credit > this.credit)
			throw new Exception();
		this.credit -= credit;
	}
}
