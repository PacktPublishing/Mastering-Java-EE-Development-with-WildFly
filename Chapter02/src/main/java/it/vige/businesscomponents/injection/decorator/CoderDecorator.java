package it.vige.businesscomponents.injection.decorator;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Decorated;
import javax.enterprise.inject.spi.Bean;
import javax.inject.Inject;

@Decorator
public abstract class CoderDecorator implements Coder {

	@Inject
	@Delegate
	@Any
	private Coder coder;

	@Inject
	@Decorated
	private Bean<Coder> bean;

	@Override
	public String codeString(String s, int tval) {
		int len = s.length();

		return "\"" + s + "\" becomes " + "\"" + coder.codeString(s, tval) + "\", " + len + " characters in length";
	}

	@Override
	public Bean<Coder> getBean() {
		return bean;
	}
}
