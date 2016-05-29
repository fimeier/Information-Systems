package eth.infsys.group1.task5;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

import eth.infsys.group1.dbspec.InProceedings_simple_input;
import eth.infsys.group1.dbspec.Proceedings_simple_input;

public class PublicationIOTest {

	private static Validator validator;
	
	/**
	 * 
	 * @return a proceedings with no errors; !!!You have to set the fields mode and myDB!!!
	 */
	private static Proceedings_simple_input create_proc(){
		Proceedings_simple_input proc_static = new Proceedings_simple_input();

		proc_static = new Proceedings_simple_input();
		
		proc_static.id="conf/gbrpr/2005";
		proc_static.title="Graph-Based Representations in Pattern Recognition, 5th IAPR InternationalWorkshop, GbRPR 2005, Poitiers, France, April 11-13, 2005, Proceedings"; 
		proc_static.year=2005; 
		proc_static.electronicEdition="";

		String temp[] = new String[2];
		temp[0]="Mario Vento"; temp[1]="Luc Brun";
		proc_static.editors =  temp;
				
		proc_static.note="ISBN updated, old value was 666-540-25270-3";
		proc_static.number=0;
		proc_static.publisher="Springer";
		proc_static.volume="";
		proc_static.isbn="3-540-25270-3";
		proc_static.series="Lecture Notes in Computer Science";

		proc_static.conferenceEdition=2005; //year from inproceedings
		proc_static.conferenceName="GbRPR"; //booktitle from inproceedings
		
		return proc_static;
	}
	
	/**
	 * 
	 * @return an inproceedings with no errors; !!!You have to set the fields mode and myDB!!!
	 * 
	 */
	private static InProceedings_simple_input create_inproc(){
		InProceedings_simple_input inproc_static = new InProceedings_simple_input();
		
		inproc_static.id="conf/gbrpr/CuissartH05";
		inproc_static.title="A Direct Algorithm to Find a Largest Common Connected Induced Subgraph of Two Graphs."; 
		inproc_static.year=2005; 
		inproc_static.electronicEdition="http://dx.doi.org/10.1007/978-3-540-31988-7_15";

		String temp[] = new String[2];
		temp[0]="Bertrand Cuissart"; temp[1]="Jean-Jacques Hébrard"; 
		inproc_static.authors =  temp;
		
		inproc_static.note = "Draft"; //always empty
		inproc_static.pages="162-171"; //pages
		inproc_static.crossref="conf/gbrpr/2005"; //crossref => proceedings

		inproc_static.conferenceName="GbRPR"; //booktitle
		inproc_static.conferenceEdition=inproc_static.year; //not in use; like year-Tag!!!!!;
		
		return inproc_static;
	}
	
	

	@BeforeClass
	public static void setUpValidator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		
		
		
	}

	/**
	 * Check constraints for proceedings
	 * 
	 * 
	 */
	@Test
	public void procEverythingOk() {
		Proceedings_simple_input proc = create_proc();
		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( proc);
		assertEquals( 0, constraintViolations.size() );
	}


	@Test
	public void procTitleIsNotNullAndOfTypeString() {
		Proceedings_simple_input proc = create_proc();
		
		proc.title = null;
		
		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( proc);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "darf nicht null sein", constraintViolations.iterator().next().getMessage() );
	}
	
	@Test
	public void procYearIsInRange() {
		Proceedings_simple_input proc = create_proc();
		
		proc.year = 1492;
		
		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( proc);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "muss zwischen 1901 und 2017 liegen", constraintViolations.iterator().next().getMessage() );
	}
	
	@Test
	public void procIsbnUpdate() {
		Proceedings_simple_input proc = create_proc();
		
		proc.year = 1492;
		
		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( proc);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "muss zwischen 1901 und 2017 liegen", constraintViolations.iterator().next().getMessage() );
	}
	
	
	/**
	 * Check constraints for inproceedings
	 * 
	 * 
	 */
	@Test
	public void inprocEverythingOk() {
		InProceedings_simple_input inproc = create_inproc();
		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( inproc );
		assertEquals( 0, constraintViolations.size() );
	}

	
	@Test
	public void inprocAtLeastOneAuthor() {
		InProceedings_simple_input inproc = create_inproc();
		
		inproc.authors = new String[0];
		
		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( inproc);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "muss zwischen 1 und 1000 liegen", constraintViolations.iterator().next().getMessage() );
	}
	
	@Test
	public void inprocPages() {
		InProceedings_simple_input inproc = create_inproc();
		
		//should be OK
		//inproc.pages = "123";
		//inproc.pages = "1-1";
		//inproc.pages = null;

		//shouldn't be OK
		inproc.pages = "123-";
		inproc.pages = "-123";

		
		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( inproc);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "muss auf Ausdruck \"\\d+-\\d+|\\d+\" passen", constraintViolations.iterator().next().getMessage() );
	}
	
	@Test
	public void inprocNote() {
		InProceedings_simple_input inproc = create_inproc();
	
		inproc.note = "bla";
		inproc.note = "";
		inproc.note = null;
		inproc.note = "blub";

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( inproc);
		assertEquals( 1, constraintViolations.size() );
		//constraintViolations.
		assertEquals( "muss auf Ausdruck \"Draft|Submitted|Accepted|Published\" passen", constraintViolations.iterator().next().getMessage() );
	}
	
	
	


}
