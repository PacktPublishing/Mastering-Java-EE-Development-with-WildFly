package it.vige.businesscomponents.transactions.concurrent;

import java.io.Serializable;

import javax.transaction.TransactionScoped;

@TransactionScoped
public class MyTransactionScopedBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public boolean isInTx() {
        return true;
    }
}
