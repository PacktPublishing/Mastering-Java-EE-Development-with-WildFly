package it.vige.businesscomponents.injection.inject.any;

public class BankOfAmerica implements Bank {

	@Override
	public String withdrawal() {
		return "Withdrawal from Bank of America";
	}

	@Override
	public String deposit() {
		return "Deposit to Bank of America";
	}
}
