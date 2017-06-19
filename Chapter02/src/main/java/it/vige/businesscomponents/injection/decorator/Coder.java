package it.vige.businesscomponents.injection.decorator;

import javax.enterprise.inject.spi.Bean;

public interface Coder {
	String codeString(String s, int tval);
	Bean<Coder> getBean();
}
