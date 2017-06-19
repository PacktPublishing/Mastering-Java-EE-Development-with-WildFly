package it.vige.businesscomponents.injection.inject.any;

public enum BankName {

    HSBC (HSBC.class),
    Chase (Chase.class),
    BankOfAmerica (BankOfAmerica.class);

    private Class<? extends Bank> bankType;

    private BankName(Class<? extends Bank> bankType) {
        this.bankType = bankType;
    }

    public Class<? extends Bank> getBankType() {
        return bankType;
    }
}
