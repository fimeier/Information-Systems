package eth.infsys.group1.dbspec;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import javax.validation.Constraint;
import javax.validation.Payload;

import eth.infsys.webserver.Webserver;



public class MyCustomConstraints{
	
	public enum CaseMode {
		add,
		update;
	}
	
	@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
	@Retention(RUNTIME)
	@Constraint(validatedBy = CheckCaseValidator.class)
	@Documented
	public @interface CheckCase {

		String message() default "The attribute ’DomainObject.id’must be unique.";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };

		CaseMode value();

		@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
		@Retention(RUNTIME)
		@Documented
		@interface List {
			CheckCase[] value();
		}
	}
	
	

}
