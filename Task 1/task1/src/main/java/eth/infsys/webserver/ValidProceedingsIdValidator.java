package eth.infsys.webserver;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eth.infsys.group1.dbspec.InProceedings_simple_input;
import eth.infsys.group1.dbspec.MyCustomConstraints.CaseMode;
import eth.infsys.group1.dbspec.Proceedings_simple_input;

public class ValidProceedingsIdValidator
implements ConstraintValidator<ValidProceedingsId, Proceedings_simple_input> {

	@Override
	public void initialize(ValidProceedingsId constraintAnnotation) {
	}

	@Override
	public boolean isValid(Proceedings_simple_input proc, ConstraintValidatorContext context) {
		if ( proc == null ) {
			return false;
		} else if (proc.id == null){
			return false;
		}

		//id mustn't exist
		if (proc.mode.equals("add")){
			boolean result = proc.myDB.IO_exists_proceedings_id(proc.id);
			
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate( "The attribute Proceedings-ID/Key must be unique." )
					.addPropertyNode( "id" ).addConstraintViolation();
			
			return !result;
		}

		//update: id must exist
		else {
			boolean result = proc.myDB.IO_exists_proceedings_id(proc.id);
			
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate( "The attribute Proceedings-ID/Key must exist." )
					.addPropertyNode( "id" ).addConstraintViolation();
			
			return result;
		}
	}
}

