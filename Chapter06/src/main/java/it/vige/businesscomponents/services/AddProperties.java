package it.vige.businesscomponents.services;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class AddProperties implements Feature {

	@Override
	public boolean configure(FeatureContext context) {
		context.property("configured_add_property", true);
		return true;
	}

}
