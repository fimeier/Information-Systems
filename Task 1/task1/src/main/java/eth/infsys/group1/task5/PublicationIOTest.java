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

import eth.infsys.group1.dbspec.DBProvider;
import eth.infsys.group1.dbspec.InProceedings_simple_input;
import eth.infsys.group1.dbspec.Proceedings_simple_input;
import eth.infsys.group1.task1.T1DBProvider;
import eth.infsys.group1.task2.T2DBProvider;
import eth.infsys.group1.task3.T3DBProvider;

public class PublicationIOTest {

	private static Validator validator;

	private static int nextYear = 0;


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


	private static DBProvider myZooDB;
	private static String dbNameZooDB = "ZooDB_big.zdb";

	private static DBProvider myMongoDB;
	private static String dbNamemyMongoDB = "myDBLPv2";

	private static DBProvider myBaseX;
	private static String dbNamemyBaseX = "big_xml";




	@BeforeClass
	public static void setUpValidator() {

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

		nextYear = Calendar.getInstance().get(Calendar.YEAR)+1;



		/**initialize dbProviders
		 * 
		 */
		myZooDB = new T1DBProvider(dbNameZooDB, DBProvider.OPEN_DB_APPEND);
		myMongoDB = (T2DBProvider) new T2DBProvider(dbNamemyMongoDB, DBProvider.OPEN_DB_APPEND);
		myBaseX = (T3DBProvider) new T3DBProvider(dbNamemyBaseX, "admin", "admin");

	}



	/**
	 * ZooDB
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	/**
	 * Check constraints for proceedings
	 * 
	 * 
	 */

	/**
	 * T Start
	 */
	@Test
	public void zooDBUpdateProcEverythingOk() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "update";
		publ.myDB = myZooDB;


		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 0, constraintViolations.size() );
	}

	@Test
	public void zooDBAddProcEverythingOk() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "add";
		publ.myDB = myZooDB;

		publ.id = "something/new";


		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 0, constraintViolations.size() );
	}
	/**
	 * T End
	 */

	/**
	 * T1 Start
	 */
	@Test
	public void zooDBAddProcT1IdUnique() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "add";
		publ.myDB = myZooDB;

		//id already exists
		//publ.id = "something/new";

		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute Proceedings-ID/Key must be unique.", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T1 End
	 */

	/**
	 * T2 Start
	 */
	@Test
	public void zooDBUpdateProcT2Title() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "update";
		publ.myDB = myZooDB;

		publ.title = null;


		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute Publication.title must be of type string and must not be null.", constraintViolations.iterator().next().getMessage() );

	}

	@Test
	public void zooDBAddProcT2Title() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "add";
		publ.myDB = myZooDB;

		publ.id = "something/new";

		publ.title = null;

		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute Publication.title must be of type string and must not be null.", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T2 End
	 */

	/**
	 * T3 Start
	 */
	@Test
	public void zooDBUpdateProcT3Year() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "update";
		publ.myDB = myZooDB;

		publ.year = 123;


		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute year must be of type integer and between the values 1901 and "+nextYear, constraintViolations.iterator().next().getMessage() );

	}

	@Test
	public void zooDBAddProcT3Year() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "add";
		publ.myDB = myZooDB;

		publ.id = "something/new";

		publ.year = 3000;

		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute year must be of type integer and between the values 1901 and "+nextYear, constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T3 End
	 */

	/**
	 * T4 Start
	 *
	 * no tests for proceedings
	 *
	 * T4 End
	 */

	/**
	 * T5 Start
	 */
	@Test
	public void zooDBUpdateProcT5IsbnNote() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "update";
		publ.myDB = myZooDB;

		String oldIsbn = publ.isbn;

		publ.isbn = "666-66-6-6666";

		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "ISBN updated, old value was "+oldIsbn, constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T5 End
	 */

	/**
	 * T6 Start
	 *
	 * no tests for proceedings
	 *
	 * T6 End
	 */

	/**
	 * T7 Start
	 *
	 * no tests for proceedings
	 *
	 * T7 End
	 */

	/**
	 * T8 Start
	 *
	 * not implemented
	 *
	 * T8 End
	 */

	/**
	 * T9 Start
	 *
	 * no tests for proceedings
	 *
	 * T9 End
	 */


	/**
	 * Check constraints for inproceedings
	 * 
	 *
	 */

	/**
	 * T Start
	 */
	@Test
	public void zooDBUpdateInprocEverythingOk() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "update";
		publ.myDB = myZooDB;


		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 0, constraintViolations.size() );
	}

	@Test
	public void zooDBAddInprocEverythingOk() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myZooDB;
		publ.id = "something/new";


		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 0, constraintViolations.size() );
	}
	/**
	 * T End
	 */

	/**
	 * T1 Start
	 */
	@Test
	public void zooDBAddInprocT1IdUnique() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myZooDB;


		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute DomainObject.id must be unique.", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T1 End
	 */

	/**
	 * T2 Start
	 */
	@Test
	public void zooDBUpdateInprocT2Title() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "update";
		publ.myDB = myZooDB;

		publ.title = null;

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute Publication.title must be of type string and must not be null.", constraintViolations.iterator().next().getMessage() );
	}

	@Test
	public void zooDBAddInprocT2Title() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myZooDB;
		publ.id = "something/new";

		publ.title = null;

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute Publication.title must be of type string and must not be null.", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T2 End
	 */

	/**
	 * T3 Start
	 */
	@Test
	public void zooDBUpdateInprocT3Year() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "update";
		publ.myDB = myZooDB;

		publ.year = nextYear+1;

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute year must be of type integer and between the values 1901 and "+nextYear, constraintViolations.iterator().next().getMessage() );
	}

	@Test
	public void zooDBAddInprocT3Year() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myZooDB;
		publ.id = "something/new";

		publ.year = 1900;

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute year must be of type integer and between the values 1901 and "+nextYear, constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T3 End
	 */

	/**
	 * T4 Start
	 */
	@Test
	public void zooDBUpdateInprocT4Authors() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "update";
		publ.myDB = myZooDB;

		publ.authors = null;

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "cannot be null", constraintViolations.iterator().next().getMessage() );
	}

	@Test
	public void zooDBAddInprocT4Authors() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myZooDB;
		publ.id = "something/new";

		publ.authors = new String[0];

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "There exists at least one author for each publication", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T4 End
	 */

	/**
	 * T6 Start
	 */
	@Test
	public void zooDBUpdateInprocT6Pages() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "update";
		publ.myDB = myZooDB;

		publ.pages = "123-";

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute InProceedings.page must match to one of the following three patterns: &lt;Integer&gt; (e.g.750), &lt;Integer&gt;-&lt;Integer&gt; (e.g. 750-757) or null", constraintViolations.iterator().next().getMessage() );
	}

	@Test
	public void zooDBAddInprocT6Pages() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myZooDB;
		publ.id = "something/new";

		publ.pages = "asd";

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute InProceedings.page must match to one of the following three patterns: &lt;Integer&gt; (e.g.750), &lt;Integer&gt;-&lt;Integer&gt; (e.g. 750-757) or null", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T6 End
	 */

	/**
	 * T7 Start
	 */
	@Test
	public void zooDBUpdateInprocT7Crossref() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "update";
		publ.myDB = myZooDB;

		publ.crossref = "wrong/proc/id";

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute InProceedings.proceedings must not be null and must reference an existing Proceedings", constraintViolations.iterator().next().getMessage() );
	}

	@Test
	public void zooDBAddInprocT7Crossref() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myZooDB;
		publ.id = "something/new";

		publ.crossref = "wrong/proc/id";

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute InProceedings.proceedings must not be null and must reference an existing Proceedings", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T7 End
	 */

	/**
	 * T8 Start
	 *
	 * not implemented
	 *
	 * T8 End
	 */

	/**
	 * T9 Start
	 */
	@Test
	public void zooDBUpdateInprocT9Note() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "update";
		publ.myDB = myZooDB;

		publ.note = null;

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "cannot be null", constraintViolations.iterator().next().getMessage() );
	}

	@Test
	public void zooDBAddInprocT9Note() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myZooDB;
		publ.id = "something/new";

		publ.note = "bla";

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "must be one of the following: Draft|Submitted|Accepted|Published", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T9 End
	 */





	/**
	 * MongoDB
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	/**
	 * Check constraints for proceedings
	 * 
	 * 
	 */

	/**
	 * T Start
	 */
	@Test
	public void mongoDBUpdateProcEverythingOk() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "update";
		publ.myDB = myMongoDB;


		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 0, constraintViolations.size() );
	}

	@Test
	public void mongoDBAddProcEverythingOk() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "add";
		publ.myDB = myMongoDB;

		publ.id = "something/new";


		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 0, constraintViolations.size() );
	}
	/**
	 * T End
	 */

	/**
	 * T1 Start
	 */
	@Test
	public void mongoDBAddProcT1IdUnique() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "add";
		publ.myDB = myMongoDB;

		//id already exists
		//publ.id = "something/new";

		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute Proceedings-ID/Key must be unique.", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T1 End
	 */

	/**
	 * T2 Start
	 */
	@Test
	public void mongoDBUpdateProcT2Title() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "update";
		publ.myDB = myMongoDB;

		publ.title = null;


		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute Publication.title must be of type string and must not be null.", constraintViolations.iterator().next().getMessage() );

	}

	@Test
	public void mongoDBAddProcT2Title() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "add";
		publ.myDB = myMongoDB;

		publ.id = "something/new";

		publ.title = null;

		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute Publication.title must be of type string and must not be null.", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T2 End
	 */

	/**
	 * T3 Start
	 */
	@Test
	public void mongoDBUpdateProcT3Year() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "update";
		publ.myDB = myMongoDB;

		publ.year = 123;


		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute year must be of type integer and between the values 1901 and "+nextYear, constraintViolations.iterator().next().getMessage() );

	}

	@Test
	public void mongoDBAddProcT3Year() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "add";
		publ.myDB = myMongoDB;

		publ.id = "something/new";

		publ.year = 3000;

		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute year must be of type integer and between the values 1901 and "+nextYear, constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T3 End
	 */

	/**
	 * T4 Start
	 *
	 * no tests for proceedings
	 *
	 * T4 End
	 */

	/**
	 * T5 Start
	 */
	@Test
	public void mongoDBUpdateProcT5IsbnNote() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "update";
		publ.myDB = myMongoDB;

		String oldIsbn = publ.isbn;

		publ.isbn = "666-66-6-6666";

		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "ISBN updated, old value was "+oldIsbn, constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T5 End
	 */

	/**
	 * T6 Start
	 *
	 * no tests for proceedings
	 *
	 * T6 End
	 */

	/**
	 * T7 Start
	 *
	 * no tests for proceedings
	 *
	 * T7 End
	 */

	/**
	 * T8 Start
	 *
	 * not implemented
	 *
	 * T8 End
	 */

	/**
	 * T9 Start
	 *
	 * no tests for proceedings
	 *
	 * T9 End
	 */


	/**
	 * Check constraints for inproceedings
	 * 
	 *
	 */

	/**
	 * T Start
	 */
	@Test
	public void mongoDBUpdateInprocEverythingOk() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "update";
		publ.myDB = myMongoDB;


		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 0, constraintViolations.size() );
	}

	@Test
	public void mongoDBAddInprocEverythingOk() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myMongoDB;
		publ.id = "something/new";


		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 0, constraintViolations.size() );
	}
	/**
	 * T End
	 */

	/**
	 * T1 Start
	 */
	@Test
	public void mongoDBAddInprocT1IdUnique() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myMongoDB;


		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute DomainObject.id must be unique.", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T1 End
	 */

	/**
	 * T2 Start
	 */
	@Test
	public void mongoDBUpdateInprocT2Title() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "update";
		publ.myDB = myMongoDB;

		publ.title = null;

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute Publication.title must be of type string and must not be null.", constraintViolations.iterator().next().getMessage() );
	}

	@Test
	public void mongoDBAddInprocT2Title() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myMongoDB;
		publ.id = "something/new";

		publ.title = null;

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute Publication.title must be of type string and must not be null.", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T2 End
	 */

	/**
	 * T3 Start
	 */
	@Test
	public void mongoDBUpdateInprocT3Year() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "update";
		publ.myDB = myMongoDB;

		publ.year = nextYear+1;

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute year must be of type integer and between the values 1901 and "+nextYear, constraintViolations.iterator().next().getMessage() );
	}

	@Test
	public void mongoDBAddInprocT3Year() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myMongoDB;
		publ.id = "something/new";

		publ.year = 1900;

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute year must be of type integer and between the values 1901 and "+nextYear, constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T3 End
	 */

	/**
	 * T4 Start
	 */
	@Test
	public void mongoDBUpdateInprocT4Authors() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "update";
		publ.myDB = myMongoDB;

		publ.authors = null;

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "cannot be null", constraintViolations.iterator().next().getMessage() );
	}

	@Test
	public void mongoDBAddInprocT4Authors() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myMongoDB;
		publ.id = "something/new";

		publ.authors = new String[0];

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "There exists at least one author for each publication", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T4 End
	 */

	/**
	 * T6 Start
	 */
	@Test
	public void mongoDBUpdateInprocT6Pages() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "update";
		publ.myDB = myMongoDB;

		publ.pages = "123-";

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute InProceedings.page must match to one of the following three patterns: &lt;Integer&gt; (e.g.750), &lt;Integer&gt;-&lt;Integer&gt; (e.g. 750-757) or null", constraintViolations.iterator().next().getMessage() );
	}

	@Test
	public void mongoDBAddInprocT6Pages() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myMongoDB;
		publ.id = "something/new";

		publ.pages = "asd";

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute InProceedings.page must match to one of the following three patterns: &lt;Integer&gt; (e.g.750), &lt;Integer&gt;-&lt;Integer&gt; (e.g. 750-757) or null", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T6 End
	 */

	/**
	 * T7 Start
	 */
	@Test
	public void mongoDBUpdateInprocT7Crossref() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "update";
		publ.myDB = myMongoDB;

		publ.crossref = "wrong/proc/id";

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute InProceedings.proceedings must not be null and must reference an existing Proceedings", constraintViolations.iterator().next().getMessage() );
	}

	@Test
	public void mongoDBAddInprocT7Crossref() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myMongoDB;
		publ.id = "something/new";

		publ.crossref = "wrong/proc/id";

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute InProceedings.proceedings must not be null and must reference an existing Proceedings", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T7 End
	 */

	/**
	 * T8 Start
	 *
	 * not implemented
	 *
	 * T8 End
	 */

	/**
	 * T9 Start
	 */
	@Test
	public void mongoDBUpdateInprocT9Note() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "update";
		publ.myDB = myMongoDB;

		publ.note = null;

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "cannot be null", constraintViolations.iterator().next().getMessage() );
	}

	@Test
	public void mongoDBAddInprocT9Note() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myMongoDB;
		publ.id = "something/new";

		publ.note = "bla";

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "must be one of the following: Draft|Submitted|Accepted|Published", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T9 End
	 */









	/**
	 * BaseX
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	/**
	 * Check constraints for proceedings
	 * 
	 * 
	 */

	/**
	 * T Start
	 */
	@Test
	public void baseXUpdateProcEverythingOk() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "update";
		publ.myDB = myBaseX;


		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 0, constraintViolations.size() );
	}

	@Test
	public void baseXAddProcEverythingOk() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "add";
		publ.myDB = myBaseX;

		publ.id = "something/new";


		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 0, constraintViolations.size() );
	}
	/**
	 * T End
	 */

	/**
	 * T1 Start
	 */
	@Test
	public void baseXAddProcT1IdUnique() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "add";
		publ.myDB = myBaseX;

		//id already exists
		//publ.id = "something/new";

		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute Proceedings-ID/Key must be unique.", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T1 End
	 */

	/**
	 * T2 Start
	 */
	@Test
	public void baseXUpdateProcT2Title() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "update";
		publ.myDB = myBaseX;

		publ.title = null;


		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute Publication.title must be of type string and must not be null.", constraintViolations.iterator().next().getMessage() );

	}

	@Test
	public void baseXAddProcT2Title() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "add";
		publ.myDB = myBaseX;

		publ.id = "something/new";

		publ.title = null;

		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute Publication.title must be of type string and must not be null.", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T2 End
	 */

	/**
	 * T3 Start
	 */
	@Test
	public void baseXUpdateProcT3Year() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "update";
		publ.myDB = myBaseX;

		publ.year = 123;


		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute year must be of type integer and between the values 1901 and "+nextYear, constraintViolations.iterator().next().getMessage() );

	}

	@Test
	public void baseXAddProcT3Year() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "add";
		publ.myDB = myBaseX;

		publ.id = "something/new";

		publ.year = 3000;

		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute year must be of type integer and between the values 1901 and "+nextYear, constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T3 End
	 */

	/**
	 * T4 Start
	 *
	 * no tests for proceedings
	 *
	 * T4 End
	 */

	/**
	 * T5 Start
	 */
	@Test
	public void baseXUpdateProcT5IsbnNote() {
		Proceedings_simple_input publ = create_proc();

		publ.mode = "update";
		publ.myDB = myBaseX;

		String oldIsbn = publ.isbn;

		publ.isbn = "666-66-6-6666";

		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( publ);
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "ISBN updated, old value was "+oldIsbn, constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T5 End
	 */

	/**
	 * T6 Start
	 *
	 * no tests for proceedings
	 *
	 * T6 End
	 */

	/**
	 * T7 Start
	 *
	 * no tests for proceedings
	 *
	 * T7 End
	 */

	/**
	 * T8 Start
	 *
	 * not implemented
	 *
	 * T8 End
	 */

	/**
	 * T9 Start
	 *
	 * no tests for proceedings
	 *
	 * T9 End
	 */


	/**
	 * Check constraints for inproceedings
	 * 
	 *
	 */

	/**
	 * T Start
	 */
	@Test
	public void baseXUpdateInprocEverythingOk() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "update";
		publ.myDB = myBaseX;


		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 0, constraintViolations.size() );
	}

	@Test
	public void baseXAddInprocEverythingOk() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myBaseX;
		publ.id = "something/new";


		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 0, constraintViolations.size() );
	}
	/**
	 * T End
	 */

	/**
	 * T1 Start
	 */
	@Test
	public void baseXAddInprocT1IdUnique() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myBaseX;


		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute DomainObject.id must be unique.", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T1 End
	 */

	/**
	 * T2 Start
	 */
	@Test
	public void baseXUpdateInprocT2Title() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "update";
		publ.myDB = myBaseX;

		publ.title = null;

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute Publication.title must be of type string and must not be null.", constraintViolations.iterator().next().getMessage() );
	}

	@Test
	public void baseXAddInprocT2Title() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myBaseX;
		publ.id = "something/new";

		publ.title = null;

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute Publication.title must be of type string and must not be null.", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T2 End
	 */

	/**
	 * T3 Start
	 */
	@Test
	public void baseXUpdateInprocT3Year() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "update";
		publ.myDB = myBaseX;

		publ.year = nextYear+1;

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute year must be of type integer and between the values 1901 and "+nextYear, constraintViolations.iterator().next().getMessage() );
	}

	@Test
	public void baseXAddInprocT3Year() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myBaseX;
		publ.id = "something/new";

		publ.year = 1900;

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute year must be of type integer and between the values 1901 and "+nextYear, constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T3 End
	 */

	/**
	 * T4 Start
	 */
	@Test
	public void baseXUpdateInprocT4Authors() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "update";
		publ.myDB = myBaseX;

		publ.authors = null;

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "cannot be null", constraintViolations.iterator().next().getMessage() );
	}

	@Test
	public void baseXAddInprocT4Authors() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myBaseX;
		publ.id = "something/new";

		publ.authors = new String[0];

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "There exists at least one author for each publication", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T4 End
	 */

	/**
	 * T6 Start
	 */
	@Test
	public void baseXUpdateInprocT6Pages() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "update";
		publ.myDB = myBaseX;

		publ.pages = "123-";

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute InProceedings.page must match to one of the following three patterns: &lt;Integer&gt; (e.g.750), &lt;Integer&gt;-&lt;Integer&gt; (e.g. 750-757) or null", constraintViolations.iterator().next().getMessage() );
	}

	@Test
	public void baseXAddInprocT6Pages() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myBaseX;
		publ.id = "something/new";

		publ.pages = "asd";

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute InProceedings.page must match to one of the following three patterns: &lt;Integer&gt; (e.g.750), &lt;Integer&gt;-&lt;Integer&gt; (e.g. 750-757) or null", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T6 End
	 */

	/**
	 * T7 Start
	 */
	@Test
	public void baseXUpdateInprocT7Crossref() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "update";
		publ.myDB = myBaseX;

		publ.crossref = "wrong/proc/id";

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute InProceedings.proceedings must not be null and must reference an existing Proceedings", constraintViolations.iterator().next().getMessage() );
	}

	@Test
	public void baseXAddInprocT7Crossref() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myBaseX;
		publ.id = "something/new";

		publ.crossref = "wrong/proc/id";

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "The attribute InProceedings.proceedings must not be null and must reference an existing Proceedings", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T7 End
	 */

	/**
	 * T8 Start
	 *
	 * not implemented
	 *
	 * T8 End
	 */

	/**
	 * T9 Start
	 */
	@Test
	public void baseXUpdateInprocT9Note() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "update";
		publ.myDB = myBaseX;

		publ.note = null;

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "cannot be null", constraintViolations.iterator().next().getMessage() );
	}

	@Test
	public void baseXAddInprocT9Note() {
		InProceedings_simple_input publ = create_inproc();

		publ.mode = "add";
		publ.myDB = myBaseX;
		publ.id = "something/new";

		publ.note = "bla";

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( publ );
		assertEquals( 1, constraintViolations.size() );
		assertEquals( "must be one of the following: Draft|Submitted|Accepted|Published", constraintViolations.iterator().next().getMessage() );
	}
	/**
	 * T9 End
	 */





}
