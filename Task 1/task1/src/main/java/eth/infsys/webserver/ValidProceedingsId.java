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
@Constraint(validatedBy = { ValidProceedingsIdValidator.class })
@Documented
public @interface ValidProceedingsId {

	String message() default "The id must be unique.";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
