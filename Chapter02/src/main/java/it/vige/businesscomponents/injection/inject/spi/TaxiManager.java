package it.vige.businesscomponents.injection.inject.spi;

import java.util.List;

import javax.inject.Singleton;

@Singleton
public class TaxiManager {

	private List<Taxi> taxies;

	public List<Taxi> getTaxies() {
		return taxies;
	}

	public void setTaxies(List<Taxi> taxies) {
		this.taxies = taxies;
	}
	
}
