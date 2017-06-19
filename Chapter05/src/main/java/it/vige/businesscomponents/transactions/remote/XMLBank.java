package it.vige.businesscomponents.transactions.remote;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

@Stateless(name = "bank")
public class XMLBank implements XMLRemote {

    @Resource(lookup="java:jboss/TransactionManager")
    private TransactionManager transactionManager;

    @Override
    public int transactionStatus() {
        try {
            return transactionManager.getStatus();
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
    }
}
