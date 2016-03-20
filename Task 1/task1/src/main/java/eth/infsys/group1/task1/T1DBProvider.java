/**Problems
 * Interface to Gui... OO to String...
 * !!!! getid()... etc. compare toUpperCase()
 */


package eth.infsys.group1.task1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;

import org.zoodb.jdo.ZooJdoHelper;
import org.zoodb.tools.ZooHelper;

import eth.infsys.group1.dbspec.DBProvider;
import eth.infsys.group1.task1.dbobjs.Conference;
import eth.infsys.group1.task1.dbobjs.ConferenceEdition;
import eth.infsys.group1.task1.dbobjs.DomainObject;
import eth.infsys.group1.task1.dbobjs.InProceedings;
import eth.infsys.group1.task1.dbobjs.Person;
import eth.infsys.group1.task1.dbobjs.Proceedings;
import eth.infsys.group1.task1.dbobjs.Publication;
import eth.infsys.group1.task1.dbobjs.Publisher;
import eth.infsys.group1.task1.dbobjs.Series;
import eth.infsys.group1.ui.fxobjs.FxConference;
import eth.infsys.group1.ui.fxobjs.FxConference.SortOption;
import eth.infsys.group1.ui.fxobjs.FxConferenceEdition;
import eth.infsys.group1.ui.fxobjs.FxInProceedings;
import eth.infsys.group1.ui.fxobjs.FxPerson;
import eth.infsys.group1.ui.fxobjs.FxProceedings;
import eth.infsys.group1.ui.fxobjs.FxPublication;
import eth.infsys.group1.ui.fxobjs.FxPublisher;
import eth.infsys.group1.ui.fxobjs.FxSeries;
import eth.infsys.group1.xml.XMLInProceedings;
import eth.infsys.group1.xml.XMLProceedings;
import eth.infsys.group1.xmlparser.InProceedings_simple_input;
import eth.infsys.group1.xmlparser.Proceedings_simple_input;

public class T1DBProvider extends
DBProvider<Conference, ConferenceEdition, InProceedings, Person, Proceedings, Publication, Publisher, Series> {

	public static final int OPEN_DB_APPEND = 20;
	public static final int OPEN_DB_OVERRIDE = 21;
	public static final int SORT_BY_NAME = 1;
	public static final int SORT_BY_YEAR = 2;
	public static final int SORT_BY_TITLE = 3;


/*
	//HACK!!!
	public PersistenceManager getpm() {
		return pm;
	}
*/

	private PersistenceManager pm;

	/**
	 * Create DBProvider
	 * 
	 * @param dbName The name of the DB
	 * @param mode APPEND or OVERRIDE
	 */
	public T1DBProvider(String dbName, int mode) {
		createDB(dbName, mode);		
	}

	/**
	 * Create DB
	 * 
	 * @param dbName The name of the DB
	 * @param mode APPEND or OVERRIDE
	 */
	private void createDB(String dbName, int mode) {
		// create database
		// By default, all database files will be created in %USER_HOME%/zoodb

		switch (mode) {
		case OPEN_DB_APPEND:
			/**
			 * create DB and open database connection
			 * do not delete if already exist
			 */
			this.pm = ZooJdoHelper.openOrCreateDB(dbName);

			//schemaManager();

			break;

		default:
			/**
			 * create database and/or remove first if it exists
			 */
			if (ZooHelper.dbExists(dbName)) {
				ZooHelper.removeDb(dbName);
			}
			this.pm = ZooJdoHelper.openOrCreateDB(dbName);

			schemaManager();
		}
	}

	//simple implementation
	protected void schemaManager() {
		//unique-Index for DomainObject.id
		pm.currentTransaction().begin();
		ZooJdoHelper.createIndex(pm, DomainObject.class, "id", true);
		pm.currentTransaction().commit();
	}

	/**
	 * Close the database connection.
	 * 
	 * @param pm The current PersistenceManager.
	 */
	public void closeDB() {
		if (pm.currentTransaction().isActive()) {
			pm.currentTransaction().rollback();
		}
		pm.close();
		pm.getPersistenceManagerFactory().close();
	}

	/**
	 * Open the database connection.
	 * 
	 * @param pm The current PersistenceManager.
	 */
	protected void openDB(String dbName) {
		pm = ZooJdoHelper.openDB(dbName);

	}


	/**
	 * Creates and returns a new (or existing) proceeding
	 * 
	 * @param args  args.conferenceEdition / args.conferenceName are defined through booktitle/year from an inproceeding
	 * @return String proc_id which is always equal to args.id
	 */
	public String batch_createProceedings(Proceedings_simple_input args) {

		pm.currentTransaction().begin();
		Proceedings proc = get_proceeding_by_id(args.id);
		pm.currentTransaction().commit();


		//return existing proceeding
		if ( ! (proc == null) ){
			pm.currentTransaction().begin();
			System.out.println("proceeding already exists... id=" + proc.getId() + " and title=" + proc.getTitle());
			String ret_val = proc.getId();
			pm.currentTransaction().commit();
			return ret_val;
		}
		//create new proceeding
		System.out.println("create new proceeding...");


		//Conference
		pm.currentTransaction().begin();
		String conf_id = Conference.calculate_conference_id(args.conferenceName);
		Conference conf = get_conference_by_id(conf_id);
		if ( conf == null ){
			conf = create_conference(args.conferenceName);
			System.out.println("Conference(" + conf_id + ") created...");
		}
		else {
			System.out.println("Conference(" + conf_id + ") already exists...");
		}
		pm.currentTransaction().commit();

		//ConferenceEdition
		pm.currentTransaction().begin();
		String confEd_id = ConferenceEdition.calculate_conferenceEdition_id(args.conferenceName,args.conferenceEdition);
		ConferenceEdition confEd = get_conferenceEdition_by_id(confEd_id);
		if (confEd == null){
			confEd = create_conferenceEdition(conf, args.conferenceEdition);
			System.out.println("ConferenceEdition(" + confEd_id + ") created...");
		}
		else {
			System.out.println("ConferenceEdition(" + conf_id + ") already exists...");
		}
		pm.currentTransaction().commit();


		//Editors
		Set<Person> edit = new HashSet<>();
		for (String editor_name: args.editors){
			pm.currentTransaction().begin();
			String person_id = Person.calculate_person_id(editor_name);
			Person editor = get_person_by_id(person_id);
			if ( editor == null ){
				editor = create_person(editor_name);
				System.out.println("Editor (" + editor_name + ") created...");
			}
			else {
				System.out.println("Editor(" + editor_name + ") already exists...");
			}
			edit.add(editor);
			pm.currentTransaction().commit();
		}


		//Publisher
		pm.currentTransaction().begin();
		String publ_id = Publisher.calculate_publisher_id(args.publisher);
		Publisher publ = get_publisher_by_id(publ_id);
		if ( publ == null ){
			publ = create_publisher(args.publisher);
			System.out.println("Publisher (" + args.publisher + ") created...");
		}
		else {
			System.out.println("Publisher(" + args.publisher + ") already exists...");
		}
		pm.currentTransaction().commit();

		//Series
		pm.currentTransaction().begin();
		Series serie = null;
		if (args.series != null){
			String series_id = Series.calculate_series_id(args.series);
			serie = get_serie_by_id(series_id);
			if ( serie == null ){
				serie = create_serie(args.series);
				System.out.println("Series (" + args.series + ") created...");
			}
			else {
				System.out.println("Series (" + args.series + ") already exists...");
			}
		}
		else {
			System.out.println("No series available for proceeding("+args.id +")...");
		}
		pm.currentTransaction().commit();

		//Proceedings
		pm.currentTransaction().begin();
		proc = new Proceedings(args, edit, publ, serie,confEd);
		//??
		String ret_val = proc.getId();
		pm.currentTransaction().commit();

		return ret_val;

	}




	private Series create_serie(String series_name) {
		Series serie = new Series(series_name);
		pm.makePersistent(serie);
		return serie;
	}

	private Series get_serie_by_id(String series_id) {
		Extent<Series> ext = pm.getExtent(Series.class);
		for (Series p: ext) {
			if (p.getId().equals(series_id)){
				ext.closeAll();
				return p;
			}
		}
		ext.closeAll();
		return null;
	}

	private Person create_person(String person_name) {
		Person pers = new Person(person_name);
		pm.makePersistent(pers);
		return pers;
	}

	private Person get_person_by_id(String person_id) {
		Extent<Person> ext = pm.getExtent(Person.class);
		for (Person p: ext) {
			if (p.getId().equals(person_id)){
				ext.closeAll();
				return p;
			}
		}
		ext.closeAll();
		return null;
	}

	private Publisher create_publisher(String publisher_name) {
		Publisher publ = new Publisher(publisher_name);
		pm.makePersistent(publ);
		return publ;
	}

	private Publisher get_publisher_by_id(String publisher_id) {
		Extent<Publisher> ext = pm.getExtent(Publisher.class);
		for (Publisher p: ext) {
			if (p.getId().equals(publisher_id)){
				ext.closeAll();
				return p;
			}
		}
		ext.closeAll();
		return null;
	}

	private ConferenceEdition create_conferenceEdition(Conference conf, int conferenceEdition) {
		ConferenceEdition confEd = new ConferenceEdition(conf, conferenceEdition);
		pm.makePersistent(confEd);
		return confEd;
	}

	private ConferenceEdition get_conferenceEdition_by_id(String confEd_id) {
		Extent<ConferenceEdition> ext = pm.getExtent(ConferenceEdition.class);
		for (ConferenceEdition p: ext) {
			if (p.getId().equals(confEd_id)){
				ext.closeAll();
				return p;
			}
		}
		ext.closeAll();
		return null;
	}

	private Conference create_conference(String conferenceName) {
		Conference conf = new Conference(conferenceName);
		pm.makePersistent(conf);
		return conf;
	}

	private Conference get_conference_by_id(String conf_id) {
		Extent<Conference> ext = pm.getExtent(Conference.class);
		for (Conference p: ext) {
			if (p.getId().equals(conf_id)){
				ext.closeAll();
				return p;
			}
		}
		ext.closeAll();
		return null;
	}

	/**
	 * returns proceeding object or null
	 * 
	 * @param id DomainObject id
	 * @return Proceedings proceeding
	 */
	private Proceedings get_proceeding_by_id(String proc_id) {
		//Query q = pm.newQuery(Proceedings.class, "id == '" + id + "'");

		// Proceedings proc = (Proceedings) pm.newQuery("select unique from eth.infsys.group1.task1.dbobjs.Proceedings where id == '" + id + "'").execute();

		/* if (proc == null){
        	return null;
        }
        return proc;

		return null;*/
		Extent<Proceedings> ext = pm.getExtent(Proceedings.class);
		for (Proceedings p: ext) {
			if (p.getId().equals(proc_id)){
				//System.out.println("already exists: " + p.getId());
				ext.closeAll();
				return p;
			}
		}
		ext.closeAll();
		return null;
	}

	/**
	 * Creates and returns a new (or existing) inproceeding
	 * 
	 * @param args
	 * @return String inproc_id which is always equal to args.id
	 */
	public String batch_createInProceedings(InProceedings_simple_input args) {

		pm.currentTransaction().begin();
		InProceedings inproc = get_inproceeding_by_id(args.id);
		pm.currentTransaction().commit();

		//return existing InProceeding
		if ( ! (inproc == null) ){
			pm.currentTransaction().begin();
			System.out.println("!!!!!!!! InProceeding already exists... id=" + inproc.getId() + " and title=" + inproc.getTitle());
			String ret_val = inproc.getId();
			pm.currentTransaction().commit();
			return ret_val;
		}

		//Proceeding
		String proc_id = args.crossref; //always the crossref
		pm.currentTransaction().begin();
		Proceedings proc = get_proceeding_by_id(proc_id);
		pm.currentTransaction().commit();
		if ( proc == null ){
			System.out.println("Proceeding with id=" + proc_id + " is not existing. Error Inproceeding creation... 66666666666");
			return null;
		}

		//create new inproceeding (one big transaction)
		System.out.println("create new inproceeding for proceeding with id=" + proc_id);

		//Authors
		List<Person> auth = new ArrayList<>();
		for (String author_name: args.authors){
			pm.currentTransaction().begin();
			String person_id = Person.calculate_person_id(author_name);
			Person author = get_person_by_id(person_id);
			if ( author == null ){
				author = create_person(author_name);
				//System.out.println("Author (" + author_name + ") created...");
			}
			else {
				//System.out.println("Author(" + author_name + ") already exists...");
			}
			auth.add(author);
			pm.currentTransaction().commit();
		}


		//Inroceeding
		pm.currentTransaction().begin();
		inproc = new InProceedings(args, auth, proc);
		String ret_val = inproc.getId();
		pm.currentTransaction().commit();
		return ret_val;
	}

	private InProceedings get_inproceeding_by_id(String inproc_id) {
		Extent<InProceedings> ext = pm.getExtent(InProceedings.class);
		for (InProceedings p: ext) {
			if (p.getId().equals(inproc_id)){
				ext.closeAll();
				return p;
			}
		}
		ext.closeAll();
		return null;
	}


	@Override
	public void createConference(FxConference<Conference> fxObj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createConferenceEdition(FxConferenceEdition<ConferenceEdition> fxObj, Conference conference)
			throws eth.infsys.group1.dbspec.DBProvider.InvalidDBRepException {
		// TODO Auto-generated method stub

	}

	@Override
	public void createInProceedings(FxInProceedings<InProceedings> fxObj, Proceedings proceedings)
			throws eth.infsys.group1.dbspec.DBProvider.InvalidDBRepException {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPerson(FxPerson<Person> fxObj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createProceedings(FxPerson<Person> fxObj, ConferenceEdition confEdition)
			throws eth.infsys.group1.dbspec.DBProvider.InvalidDBRepException {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPublisher(FxPublisher<Publisher> fxObj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createSeries(FxSeries<Series> fxObj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteConference(Conference conference) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteConferenceEdition(ConferenceEdition conferenceEdition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteInProceedings(InProceedings inProceedings) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deletePerson(Person person) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteProceedings(Proceedings proceedings) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deletePublisher(Publisher publisher) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSeries(Series series) {
		// TODO Auto-generated method stub

	}

	@Override
	public Conference getConferenceByID(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConferenceEdition getConferenceEditionByID(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InProceedings getInProceedingsByID(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Person getPersonByID(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Proceedings getProceedingsByID(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Publication getPublicationByID(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Publisher getPublisherByID(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Series getSeriesByID(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getConferences(int startIndex, int endIndex, SortOption sort, String searchTerm,
			List<FxConference<Conference>> out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getConferenceEditions(int startIndex, int endIndex,
			eth.infsys.group1.ui.fxobjs.FxConferenceEdition.SortOption sort, String searchTerm,
			List<FxConferenceEdition<ConferenceEdition>> out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getConferenceEditions(int startIndex, int endIndex,
			eth.infsys.group1.ui.fxobjs.FxConferenceEdition.SortOption sort, Conference conference, String searchTerm,
			List<FxConferenceEdition<ConferenceEdition>> out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getInProceedings(int startIndex, int endIndex,
			eth.infsys.group1.ui.fxobjs.FxInProceedings.SortOption sort, String searchTerm,
			List<FxInProceedings<InProceedings>> out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getInProceedingsOfAuthor(int startIndex, int endIndex,
			eth.infsys.group1.ui.fxobjs.FxInProceedings.SortOption sort, Person author, String searchTerm,
			List<FxInProceedings<InProceedings>> out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getInProceedingsOfProceedings(int startIndex, int endIndex,
			eth.infsys.group1.ui.fxobjs.FxInProceedings.SortOption sort, Proceedings proceedings, String searchTerm,
			List<FxInProceedings<InProceedings>> out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getProceedings(int startIndex, int endIndex, eth.infsys.group1.ui.fxobjs.FxProceedings.SortOption sort,
			String searchTerm, List<FxProceedings<Proceedings>> out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getProceedingsOfEditor(int startIndex, int endIndex,
			eth.infsys.group1.ui.fxobjs.FxProceedings.SortOption sort, Person author, String searchTerm,
			List<FxProceedings<Proceedings>> out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getPublishers(int startIndex, int endIndex, eth.infsys.group1.ui.fxobjs.FxPublisher.SortOption sort,
			String searchTerm, List<FxInProceedings<Publisher>> out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getPersons(int startIndex, int endIndex, eth.infsys.group1.ui.fxobjs.FxPerson.SortOption sort,
			String searchTerm, List<FxPerson<Person>> out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getSeries(int startIndex, int endIndex, eth.infsys.group1.ui.fxobjs.FxSeries.SortOption sort,
			String searchTerm, List<FxSeries<Series>> out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setConferenceTitle(Conference conference, String title) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setConferenceEditionYear(ConferenceEdition edition, int year) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPersonName(Person person, String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateConferenceData(List<FxConference<Conference>> data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateConferenceEditionData(List<FxConferenceEdition<ConferenceEdition>> data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateInProceedingsData(List<FxInProceedings<InProceedings>> data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updatePersonData(List<FxPerson<Person>> data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateProceedingsData(List<FxProceedings<Proceedings>> data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updatePublicationData(List<FxPublication<Publication>> data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updatePublisherData(List<FxPublisher<Publisher>> data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSeriesData(List<FxSeries<Series>> data) {
		// TODO Auto-generated method stub

	}

	@Override
	public InProceedings createInProceedings(XMLInProceedings xmlData, Proceedings proceedings) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Proceedings createProceedings(XMLProceedings xmlData) {
		// TODO Auto-generated method stub
		return null;
	}






}
