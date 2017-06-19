package it.vige.businesscomponents.services.components;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class MyComponent implements Feature {

	@Override
	public boolean configure(FeatureContext context) {
		context.property("configured_myComponent", true);
		return true;
	}

}
