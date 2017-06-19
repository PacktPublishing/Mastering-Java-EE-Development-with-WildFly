package it.vige.businesscomponents.injection.inject.any;

public class HSBC implements Bank {

	@Override
	public String withdrawal() {
		return "Withdrawal from HSBC";
	}

	@Override
	public String deposit() {
		return "Deposit to HSBC";
	}
}
