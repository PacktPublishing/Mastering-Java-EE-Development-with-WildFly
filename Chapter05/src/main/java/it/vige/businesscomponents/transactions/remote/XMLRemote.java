package it.vige.businesscomponents.transactions.remote;

import javax.ejb.Remote;

@Remote
public interface XMLRemote {

    int transactionStatus();

}
