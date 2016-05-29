package eth.infsys.webserver;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = { ValidInproceedingsIdValidator.class })
@Documented
public @interface ValidInproceedingsId {

	String message() default "The attribute 'DomainObject.id' must be unique.... default message";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
