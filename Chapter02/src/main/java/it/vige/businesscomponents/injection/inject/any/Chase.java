package it.vige.businesscomponents.injection.inject.any;

public class Chase implements Bank {

	@Override
	public String withdrawal() {
		return "Withdrawal from Chase";
	}

	@Override
	public String deposit() {
		return "Deposit to Chase";
	}
}
