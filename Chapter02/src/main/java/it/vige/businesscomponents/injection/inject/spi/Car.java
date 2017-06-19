package it.vige.businesscomponents.injection.inject.spi;

import static java.util.logging.Logger.getLogger;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Provider;

public class Car {

	private static final Logger logger = getLogger(Car.class.getName());

	@Inject
	public Car(Provider<Seat> seatProvider) {
		Seat driver = seatProvider.get();
		logger.info(driver + "");
		Seat passenger = seatProvider.get();
		logger.info(passenger + "");
	}
}
