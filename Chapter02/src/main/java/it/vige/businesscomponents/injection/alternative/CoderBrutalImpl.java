package it.vige.businesscomponents.injection.alternative;

import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Specializes;
import javax.enterprise.inject.spi.Bean;

import it.vige.businesscomponents.injection.decorator.Coder;
import it.vige.businesscomponents.injection.decorator.CoderImpl;

@Alternative
@Specializes
public class CoderBrutalImpl extends CoderImpl {

	@Override
	public String codeString(String s, int tval) {
		return "hiiiiiiiii";
	}

	@Override
	public Bean<Coder> getBean() {
		return null;
	}

}
