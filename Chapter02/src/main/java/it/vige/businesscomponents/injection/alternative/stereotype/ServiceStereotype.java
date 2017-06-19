package it.vige.businesscomponents.injection.alternative.stereotype;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Stereotype;

import it.vige.businesscomponents.injection.interceptor.Audit;
import it.vige.businesscomponents.injection.interceptor.Logging;

/**
 * A stereotype is an annotation, annotated @Stereotype, that packages several
 * other annotations. It specify 2 interceptors bindings (@Logging and @Audit)
 * to be inherited by all beans with that stereotype. This stereotype also
 * indicate that all beans to which it is applied are @Alternatives. An
 * alternative stereotype lets us classify beans by deployment scenario.
 *
 * @author Luca Stancapiano
 */
@Stereotype
@Alternative
@Audit
@Logging
@Retention(RUNTIME)
@Target(TYPE)
public @interface ServiceStereotype {

}
