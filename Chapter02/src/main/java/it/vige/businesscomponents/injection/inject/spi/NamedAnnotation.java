package it.vige.businesscomponents.injection.inject.spi;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Named;

public class NamedAnnotation extends AnnotationLiteral<Named> implements Named {

	private final static long serialVersionUID = 767867576L;
	private final String value;

	public NamedAnnotation(final String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}
}
