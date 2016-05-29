package eth.infsys.webserver;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eth.infsys.group1.dbspec.InProceedings_simple_input;

public class ValidInproceedingsIdValidator
implements ConstraintValidator<ValidInproceedingsId, InProceedings_simple_input> {

	@Override
	public void initialize(ValidInproceedingsId constraintAnnotation) {
	}

	@Override
	public boolean isValid(InProceedings_simple_input inproc, ConstraintValidatorContext context) {
		if ( inproc == null ) {
			return false;
		} else if (inproc.id == null){
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate( "The attribute DomainObject.id must not be null" )
					.addPropertyNode( "id" ).addConstraintViolation();
			return false;
		}

		//id mustn't exist
		if (inproc.mode.equals("add")){
			boolean result = inproc.myDB.IO_exists_inproceedings_id(inproc.id);
			
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate( "The attribute DomainObject.id must be unique." )
					.addPropertyNode( "id" ).addConstraintViolation();
			
			return !result;
		}

		//update: id must exist
		else {
			boolean result = inproc.myDB.IO_exists_inproceedings_id(inproc.id);
			
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate( "The attribute DomainObject.id must exist." )
					.addPropertyNode( "id" ).addConstraintViolation();
			
			return result;
		}
	}
}

