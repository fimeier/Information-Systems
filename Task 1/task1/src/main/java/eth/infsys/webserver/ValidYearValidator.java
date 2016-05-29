package eth.infsys.webserver;

import java.util.Calendar;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class ValidYearValidator implements ConstraintValidator<ValidYear, Integer> {
	
	public int nextYear = 0;
	public void initialize(ValidYear constraintAnnotation) {
		
		this.nextYear = Calendar.getInstance().get(Calendar.YEAR)+1;
		
		
	}
	

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(
				"The attribute year must be of type integer and between the values 1901 and "+nextYear
		)
		.addConstraintViolation();
		
		return (1901 <= value && value <= nextYear);
	}
	

}
