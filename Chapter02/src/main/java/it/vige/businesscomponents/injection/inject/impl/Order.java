package it.vige.businesscomponents.injection.inject.impl;

import javax.enterprise.context.Dependent;

@Dependent
public class Order {

	private int ticket;

	public int getTicket() {
		return ticket;
	}

	public void setTicket(int ticket) {
		this.ticket = ticket;
	}

}
