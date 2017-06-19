package it.vige.businesscomponents.transactions.concurrent;

import java.util.concurrent.CountDownLatch;

public class ConcurrentStatus {
    public static CountDownLatch latch;
    public static boolean foundTransactionScopedBean;
}
