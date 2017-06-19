package it.vige.businesscomponents.injection.interceptor.service;

import java.util.ArrayList;
import java.util.List;

/**
 * The class, that holds history of {@link ItemServiceBean} methods invocation. {@link AuditInterceptor} is responsible for
 * updating history.
 *
 * @author ievgen.shulga
 */
public class History {
    private static List<String> itemHistory = new ArrayList<>();

    public static List<String> getItemHistory() {
        return itemHistory;
    }

}
