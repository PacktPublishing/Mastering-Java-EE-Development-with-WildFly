package it.vige.businesscomponents.transactions.distributed;

import java.util.List;

public interface Magazine<T> {

	void write(T element);

	List<T> read();

}
