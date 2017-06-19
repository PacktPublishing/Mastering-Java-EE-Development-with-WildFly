package it.vige.businesscomponents.injection.common;

import javax.inject.Named;

@Named("my_named_test")
public class NamedBean {

	public double giveMeThePrize() {
		return 5.6;
	}
}
