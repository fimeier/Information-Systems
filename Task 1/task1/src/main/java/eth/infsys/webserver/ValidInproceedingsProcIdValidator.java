package eth.infsys.webserver;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eth.infsys.group1.dbspec.InProceedings_simple_input;
import eth.infsys.group1.dbspec.Proceedings_simple_input;

public class ValidInproceedingsProcIdValidator
implements ConstraintValidator<ValidInproceedingsProcId, InProceedings_simple_input> {

	@Override
	public void initialize(ValidInproceedingsProcId constraintAnnotation) {
	}

	@Override
	public boolean isValid(InProceedings_simple_input inproc, ConstraintValidatorContext context) {
		if ( inproc == null ) {
			return false;
		}
		if (inproc.crossref == null){
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate( "The attribute InProceedings.proceedings must not be null and must reference an existing Proceedings" )
			.addPropertyNode( "crossref" ).addConstraintViolation();
			return false;
		}

		if (inproc.myDB.IO_exists_proceedings_id(inproc.crossref)){
			return true;
		}
		else {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate( "The attribute InProceedings.proceedings must not be null and must reference an existing Proceedings" )
			.addPropertyNode( "crossref" ).addConstraintViolation();
			return false;
		}

	}
}

