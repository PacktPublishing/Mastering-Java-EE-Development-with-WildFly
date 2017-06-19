package it.vige.businesscomponents.injection.inject.spi;

import javax.enterprise.inject.spi.BeanManager;

public class AnotherFactory {

	final Toy jessie = new Toy("Jessie");

	public static Toy getRex(BeanManager manager, SpaceSuit<Toy> suit) {
		return new Toy("Rex");
	}
}
