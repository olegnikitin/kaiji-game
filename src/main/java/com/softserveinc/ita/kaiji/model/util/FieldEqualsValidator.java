package com.softserveinc.ita.kaiji.model.util;

import java.lang.reflect.Method;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/** * 
 * @author Oleg Nikitin
 * @version 0.1
 * @since 04.08.14
 */
public class FieldEqualsValidator implements ConstraintValidator<FieldEquals, Object> {
	private String field;
	private String equalsTo;
	private String message = FieldEquals.MESSAGE;

	public void initialize(FieldEquals constraintAnnotation) {
		this.message = constraintAnnotation.message();
		this.field = constraintAnnotation.field();
		this.equalsTo = constraintAnnotation.equalsTo();
	}

	public boolean isValid(Object value, ConstraintValidatorContext context) {
		try {
			final Object fieldObject = getProperty(value, field, null);
			final Object equalsToObject = getProperty(value, equalsTo, null);

			if (fieldObject == null && equalsToObject == null) {
				return true;
			}

			boolean matches = (fieldObject != null)
					&& fieldObject.equals(equalsToObject);

			if (!matches) {
				String msg = this.message;
				if( this.message == null  
						|| "".equals( this.message ) 
						|| FieldEquals.MESSAGE.equals( this.message ) ){
					msg = field + " is not equal to " + equalsTo;
				}
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate( msg )
						.addNode(equalsTo).addConstraintViolation();
			}

			return matches;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private Object getProperty(Object value, String fieldName,
			Object defaultValue) {
		Class<?> clazz = value.getClass();
		String methodName = "get" + Character.toUpperCase(fieldName.charAt(0))
				+ fieldName.substring(1);
		try {
			Method method = clazz.getDeclaredMethod(methodName, new Class[0]);
			return method.invoke(value);
		} catch (Exception e) {
		}
		return defaultValue;
	}
}
