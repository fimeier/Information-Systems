package eth.infsys.group1.dbspec;

import java.beans.Beans;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eth.infsys.group1.dbspec.MyCustomConstraints.CaseMode;
import eth.infsys.group1.dbspec.MyCustomConstraints.CheckCase;
import eth.infsys.webserver.Webserver;

public class CheckCaseValidator implements ConstraintValidator<CheckCase, String> {

	private CaseMode caseMode;

	@Override
	public void initialize(CheckCase constraintAnnotation) {
		this.caseMode = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
		if ( object == null ) {
			return true;
		}

		//id mustn't exist
		if ( caseMode == CaseMode.add ) {
			//DBProvider myDB = Webserver.myDB;
			//boolean result = myDB.IO_exists_inproceedings_id(object);
			boolean result = true;
			return !result;
		}
		//update: id must exist
		else {
			//DBProvider myDB = Webserver.myDB;
			//boolean result = myDB.IO_exists_inproceedings_id(object);
			boolean result = true;
			return result;
		}
		
	}
}