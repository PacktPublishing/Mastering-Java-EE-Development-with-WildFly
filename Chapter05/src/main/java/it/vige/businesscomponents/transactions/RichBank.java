package it.vige.businesscomponents.transactions;

import javax.transaction.Transactional;

@Transactional(rollbackOn = Exception.class)
public class RichBank extends Bank {
}
