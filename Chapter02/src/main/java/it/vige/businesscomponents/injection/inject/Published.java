package it.vige.businesscomponents.injection.inject;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

@Retention(RUNTIME)
@Qualifier
public @interface Published {

}
