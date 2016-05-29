package eth.infsys.webserver;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eth.infsys.group1.dbspec.Proceedings_simple_input;

public class ValidProceedingsNoteValidator
implements ConstraintValidator<ValidProceedingsNote, Proceedings_simple_input> {

	@Override
	public void initialize(ValidProceedingsNote constraintAnnotation) {
	}

	@Override
	public boolean isValid(Proceedings_simple_input proc, ConstraintValidatorContext context) {
		if ( proc == null ) {
			return false;
		} else if (proc.id == null){
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate( "Wrong DomainObject.id, cannot validate note..." )
					.addPropertyNode( "note" ).addConstraintViolation();
			return false;
		}

		//no constraints for new proceedings
		if (proc.mode.equals("add")){
			return true;
		}
		
		String currentIsbn = proc.myDB.IO_get_isbn_by_proc_id(proc.id);
		
		//isbn hasn't changed
		if (currentIsbn.equals(proc.isbn)){
			return true;
		}
		
		String compareNote = "ISBN updated, old value was "+currentIsbn;				
		
		if (compareNote.equals(proc.note)){
			return true;
		}
		else {	
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate( "expected entry: ISBN updated, old value was &lt;old ISBN&gt;" )
					.addPropertyNode( "note" ).addConstraintViolation();
			return false;
		}
	}
}

