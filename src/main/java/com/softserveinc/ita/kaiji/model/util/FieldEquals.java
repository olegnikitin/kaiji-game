package com.softserveinc.ita.kaiji.model.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = FieldEqualsValidator.class)
@Documented
/** * 
 * @author Oleg Nikitin
 * @version 0.1
 * @since 04.08.14
 */
public @interface FieldEquals {
	public static final String MESSAGE = "fields.notMatches";

	String message() default MESSAGE;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Target(TYPE)
	@Retention(RUNTIME)
	@Documented
	@interface List {
		FieldEquals[] value();
	}

	String field();

	String equalsTo();
}
