/**Problems
 * Interface to Gui... OO to String...
 * !!!! getid()... etc. compare toUpperCase()
 */


package eth.infsys.group1.task1;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

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
import eth.infsys.group1.xmlparser.DivIO;
import eth.infsys.group1.xmlparser.InProceedings_simple_input;
import eth.infsys.group1.xmlparser.Proceedings_simple_input;
import eth.infsys.group1.xmlparser.PublicationIO;
import javafx.util.Pair;

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


			//pm.currentTransaction().setRetainValues(true);

			/*
			pm.currentTransaction().begin();
			ZooJdoHelper.schema(pm).addClass(DomainObject.class);
			pm.currentTransaction().commit();

			 */
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

		pm.currentTransaction().begin();
		ZooJdoHelper.createIndex(pm, Person.class, "name", true);
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

	public void tr_begin() {
		this.pm.currentTransaction().begin();
	}
	public void tr_commit() {
		this.pm.currentTransaction().commit();
	}





	private Series create_serie(String series_name) {
		Series serie = new Series(series_name);
		pm.makePersistent(serie);
		return serie;
	}

	private Series get_series_by_id(String id) {
		Query q = pm.newQuery ("select unique from eth.infsys.group1.task1.dbobjs.DomainObject where id == :compare_id");
		return (Series) q.execute(id);
	}

	private Person create_person(String person_name) {
		Person pers = new Person(person_name);
		pm.makePersistent(pers);
		return pers;
	}

	private Person get_person_by_id(String id) {
		Query q = pm.newQuery ("select unique from eth.infsys.group1.task1.dbobjs.DomainObject where id == :compare_id");
		return (Person) q.execute(id);
	}
	private Person get_person_by_name(String name) {
		Query q = pm.newQuery ("select unique from eth.infsys.group1.task1.dbobjs.Person where name == :compare_name");
		return (Person) q.execute(name);
	}

	private Publisher create_publisher(String publisher_name) {
		Publisher publ = new Publisher(publisher_name);
		pm.makePersistent(publ);
		return publ;
	}

	private Publisher get_publisher_by_id(String id) {
		Query q = pm.newQuery ("select unique from eth.infsys.group1.task1.dbobjs.DomainObject where id == :compare_id");
		return (Publisher) q.execute(id);
	}

	private ConferenceEdition create_conferenceEdition(Conference conf, int conferenceEdition) {
		ConferenceEdition confEd = new ConferenceEdition(conf, conferenceEdition);
		pm.makePersistent(confEd);
		return confEd;
	}

	private ConferenceEdition get_conferenceEdition_by_id(String id) {
		Query q = pm.newQuery ("select unique from eth.infsys.group1.task1.dbobjs.DomainObject where id == :compare_id");
		return (ConferenceEdition) q.execute(id);
	}

	private Conference create_conference(String conferenceName) {
		Conference conf = new Conference(conferenceName);
		pm.makePersistent(conf);
		return conf;
	}

	private Conference get_conference_by_id(String id) {
		Query q = pm.newQuery ("select unique from eth.infsys.group1.task1.dbobjs.DomainObject where id == :compare_id");
		return (Conference) q.execute(id);
	}

	/**
	 * returns proceeding object or null
	 * 
	 * @param id DomainObject id
	 * @return Proceedings proceeding
	 */
	private Proceedings get_proceeding_by_id(String id) {
		Query q = pm.newQuery ("select unique from eth.infsys.group1.task1.dbobjs.DomainObject where id == :compare_id");
		return (Proceedings) q.execute(id);
	}


	private InProceedings get_inproceeding_by_id(String inproc_id) {
		Query q = pm.newQuery ("select unique from eth.infsys.group1.task1.dbobjs.DomainObject where id == :inproc_id");
		return (InProceedings) q.execute(inproc_id);
	}

	/**
	 * returns Publication object or null
	 * 
	 * @param publ_id <=> DomainObject id
	 * @param sort_by 
	 * @return Publication object (possibly null)
	 */
	private Publication get_publication_by_id(String publ_id) {
		Query q = pm.newQuery ("select unique from eth.infsys.group1.task1.dbobjs.DomainObject where id == :id");
		return (Publication) q.execute(publ_id);
	}


	private Collection<Publication> get_publ_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		//String order_by = "title ascending";
		Query q = pm.newQuery (Publication.class);
		//q.setFilter("title.contains('"+title+"')");
		//q.setFilter("title.toLowerCase().contains('"+title.toLowerCase()+"')");
		q.setFilter(filter);
		q.setOrdering(order_by);

		//unsupportedOperation
		//q.setRange(boff +","+eoff);

		//Query q = pm.newQuery ("select from eth.infsys.group1.task1.dbobjs.Publication GROUP BY title RANGE :boff, :eoff");
		Collection<Publication> publs = (Collection<Publication>)q.execute();

		int collection_size = publs.size();
		int skip = boff-eoff;
		if (collection_size < boff){
			return null;
		}
		if (boff==0 && eoff==0){
			System.out.println("Return all....");
			return publs;
		}

		Collection<Publication> publs_range = new ArrayList<>();

		/**
		 * iterator points "to nothing"
		 * first iter.next() gives the first element in the list
		 */
		Iterator<Publication> iter = publs.iterator();


		int i;
		for(i=1; i<boff;i++){
			iter.next();
		}
		for(;i<=eoff && iter.hasNext();i++){
			publs_range.add(iter.next());
		}

		return publs_range;
	}

	private Collection<Person> get_person_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		Query q = pm.newQuery (Person.class);
		q.setFilter(filter);
		q.setOrdering(order_by);

		Collection<Person> persons = (Collection<Person>) q.execute();

		int collection_size = persons.size();
		int skip = boff-eoff;
		if (collection_size < boff){
			return null;
		}
		if (boff==0 && eoff==0){
			System.out.println("Return all....");
			return persons;
		}

		Collection<Person> persons_range = new ArrayList<>();

		Iterator<Person> iter = persons.iterator();


		int i;
		for(i=1; i<boff;i++){
			iter.next();
		}
		for(;i<=eoff && iter.hasNext();i++){
			persons_range.add(iter.next());
		}

		return persons_range;
	}

	private Collection<Conference> get_conference_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		Query q = pm.newQuery (Conference.class);
		q.setFilter(filter);
		q.setOrdering(order_by);

		Collection<Conference> confs = (Collection<Conference>) q.execute();

		int collection_size = confs.size();
		int skip = boff-eoff;
		if (collection_size < boff){
			return null;
		}
		if (boff==0 && eoff==0){
			System.out.println("Return all....");
			return confs;
		}

		Collection<Conference> confs_range = new ArrayList<>();

		Iterator<Conference> iter = confs.iterator();

		int i;
		for(i=1; i<boff;i++){
			iter.next();
		}
		for(;i<=eoff && iter.hasNext();i++){
			confs_range.add(iter.next());
		}

		return confs_range;
	}

	private Collection<Publisher> get_publisher_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		Query q = pm.newQuery (Publisher.class);
		q.setFilter(filter);
		q.setOrdering(order_by);

		Collection<Publisher> publishers = (Collection<Publisher>) q.execute();

		int collection_size = publishers.size();
		int skip = boff-eoff;
		if (collection_size < boff){
			return null;
		}
		if (boff==0 && eoff==0){
			System.out.println("Return all....");
			return publishers;
		}

		Collection<Publisher> publishers_range = new ArrayList<>();

		Iterator<Publisher> iter = publishers.iterator();


		int i;
		for(i=1; i<boff;i++){
			iter.next();
		}
		for(;i<=eoff && iter.hasNext();i++){
			publishers_range.add(iter.next());
		}

		return publishers_range;
	}

	private Collection<Series> get_series_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		Query q = pm.newQuery (Series.class);
		q.setFilter(filter);
		q.setOrdering(order_by);

		Collection<Series> series = (Collection<Series>) q.execute();

		int collection_size = series.size();
		int skip = boff-eoff;
		if (collection_size < boff){
			return null;
		}
		if (boff==0 && eoff==0){
			System.out.println("Return all....");
			return series;
		}

		Collection<Series> series_range = new ArrayList<>();

		Iterator<Series> iter = series.iterator();


		int i;
		for(i=1; i<boff;i++){
			iter.next();
		}
		for(;i<=eoff && iter.hasNext();i++){
			series_range.add(iter.next());
		}

		return series_range;
	}


	private DomainObject get_domainobject_by_id(String object_id) {
		Query q = pm.newQuery ("select unique from eth.infsys.group1.task1.dbobjs.DomainObject where id == :compare_id");
		return (DomainObject) q.execute(object_id);
	}

	private ConferenceEdition get_conferenceedition_by_id(String object_id) {
		Query q = pm.newQuery ("select unique from eth.infsys.group1.task1.dbobjs.ConferenceEdition where id == :compare_id");
		return (ConferenceEdition) q.execute(object_id);
	}




	/**
	 * Batch-Loader-methods
	 */

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
			serie = get_series_by_id(series_id);
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


	/**
	 * Creates and returns a new (or existing) inproceeding
	 * 
	 * @param args
	 * @return String inproc_id which is always equal to args.id
	 */
	public String batch_createInProceedings(InProceedings_simple_input args) {

		pm.currentTransaction().begin();
		pm.currentTransaction().setRetainValues(true);
		InProceedings inproc = get_inproceeding_by_id(args.id);
		pm.currentTransaction().commit();

		//return existing InProceeding
		if ( ! (inproc == null) ){
			pm.currentTransaction().begin();
			//System.out.println("!!!!!!!! InProceeding already exists... id=" + inproc.getId() + " and title=" + inproc.getTitle());
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



	/**
	 * IO-methods: helper
	 */

	private Comparator<Pair<String, String>> comparePairTitleId = new Comparator<Pair<String,String>>() {
		@Override
		public int compare(Pair<String, String> o1, Pair<String, String> o2) {
			return o1.getKey().compareTo(o2.getKey());
			//					int cmp = o1.getKey().compareTo(o2.getKey());
			//					if (cmp==0) {
			//						return o1.getValue().compareTo(o2.getValue());
			//					}
			//					return 0;
		}
	};

	private Comparator<Pair<Integer, String>> comparePairYearId = new Comparator<Pair<Integer,String>>() {
		@Override
		public int compare(Pair<Integer, String> o1, Pair<Integer, String> o2) {
			return o1.getKey().compareTo(o2.getKey());
		}
	};

	private Comparator<DivIO> compareDivIO_Person_name = new Comparator<DivIO>() {
		@Override
		public int compare(DivIO o1, DivIO o2) {
			return o1.Person_name.compareTo(o2.Person_name);
		}
	};



	private PublicationIO fill_PublicationIO(Publication publ){
		PublicationIO publication = new PublicationIO();
		if ( publ == null ){
			publication.is_empty = true;
			return publication;
		}
		else if ( publ instanceof Proceedings ){
			Proceedings proc = (Proceedings) publ;

			publication.is_a_proceeding = true;
			publication.id = proc.getId();
			publication.title = proc.getTitle();
			publication.year = proc.getYear();
			publication.electronicEdition = proc.getElectronicEdition();
			
			if (proc.getConferenceEdition() != null){
				if (proc.getConferenceEdition().getConference() != null){
					
					//Conference
					String name = proc.getConferenceEdition().getConference().getName();
					String confid = proc.getConferenceEdition().getConference().getId();
					publication.Conference_name_id = new Pair(name,confid);
					
					//ConferenceEdition	
					int year = proc.getConferenceEdition().getYear();
					String confEdid = proc.getConferenceEdition().getId();
					publication.ConferenceEdition_year_id = new Pair(year,confid);
				}
			}
			
			/*for (Person editor: proc.getEditors()){
				publication.editors.add(editor.getName());
			}*/
			for (Person editor: proc.getEditors()){
				String name = editor.getName();
				String id = editor.getId();
				publication.editors_name_id.add(new Pair(name, id));
			}
			
			publication.note = proc.getNote();
			publication.number = proc.getNumber();

			Publisher publisher = proc.getPublisher();
			if ( publisher != null){
				publication.publisher_name = publisher.getName();
				publication.publisher_id = publisher.getId();
			}

			publication.volume = proc.getVolume();
			publication.isbn = proc.getIsbn();

			Series serie = proc.getSeries();
			if ( serie != null){
				publication.series_name = serie.getName();
				publication.series_id = serie.getId();
			}

			for (InProceedings inprocs: proc.getPublications()){
				String title = inprocs.getTitle();
				String id = inprocs.getId();
				publication.inproceedings_title_id.add(new Pair(title, id));
			}
			publication.inproceedings_title_id.sort(comparePairTitleId);


		}
		else
		{
			InProceedings inproc = (InProceedings) publ;

			publication.is_an_inproceeding = true;
			publication.id = inproc.getId();
			publication.title = inproc.getTitle();
			publication.year = inproc.getYear();
			publication.electronicEdition = inproc.getElectronicEdition();
			//check einbauen			
			//publication.conferenceName = inproc.getProceedings().getConferenceEdition().getConference().getName();
			//publication.conferenceEdition = inproc.getProceedings().getConferenceEdition().getYear(); //should be the same as year
			Proceedings proc = inproc.getProceedings();
			if (proc != null){
				if (proc.getConferenceEdition() != null){
					if (proc.getConferenceEdition().getConference() != null){

						//Conference
						String name = proc.getConferenceEdition().getConference().getName();
						String confid = proc.getConferenceEdition().getConference().getId();
						publication.Conference_name_id = new Pair(name,confid);

						//ConferenceEdition	
						int year = proc.getConferenceEdition().getYear();
						String confEdid = proc.getConferenceEdition().getId();
						publication.ConferenceEdition_year_id = new Pair(year,confid);
					}
				}
			}

			for (Person author: inproc.getAuthors()){
				String name = author.getName();
				String id = author.getId();
				publication.authors_name_id.add(new Pair(name, id));
			}
			publication.note = inproc.getNote();
			publication.pages = inproc.getPages();
			publication.proceeding_title = inproc.getProceedings().getTitle();
			publication.proceeding_id = inproc.getProceedings().getId();

		}
		return publication;
	}

	private DivIO fill_DivIO(DomainObject dblp) {
		DivIO divobj = new DivIO();
		if ( dblp == null ){
			divobj.is_empty = true;
			return divobj;
		}

		//Conference ok
		else if ( dblp instanceof Conference ){
			divobj.is_a_conference = true;
			Conference conf = (Conference) dblp;
			divobj.id = conf.getId();
			divobj.Conference_name = conf.getName();

			for (ConferenceEdition confed: conf.getEditions()){
				int year = confed.getYear();
				String id = confed.getId();
				divobj.Conference_editions_year_id.add(new Pair(year, id));
			}
			divobj.Conference_editions_year_id.sort(comparePairYearId);

			return divobj;
		}

		//ConferenceEdition
		else if ( dblp instanceof ConferenceEdition ){
			divobj.is_a_conference_edition = true;
			ConferenceEdition confed = (ConferenceEdition) dblp;
			divobj.id = confed.getId();
			divobj.ConferenceEdition_year = confed.getYear();
			divobj.ConferenceEdition_conference_name = confed.getConference().getName();
			divobj.ConferenceEdition_conference_id = confed.getConference().getId();
			divobj.ConferenceEditions_proceedings_title = confed.getProceedings().getTitle();
			divobj.ConferenceEditions_proceedings_id = confed.getProceedings().getId();
			return divobj;
		}

		//Person ok
		else if ( dblp instanceof Person ){
			divobj.is_a_person = true;
			Person pers = (Person) dblp;
			divobj.id = pers.getId();
			divobj.Person_name = pers.getName();

			for (Publication publ: pers.getAuthoredPublications()){
				String title = publ.getTitle();
				String id = publ.getId();
				divobj.Person_authoredPublications_title_id.add(new Pair(title, id));
			}
			divobj.Person_authoredPublications_title_id.sort(comparePairTitleId);

			for (Publication publ: pers.getEditedPublications()){
				String title = publ.getTitle();
				String id = publ.getId();
				divobj.Person_editedPublications_title_id.add(new Pair(title, id));
			}
			divobj.Person_editedPublications_title_id.sort(comparePairTitleId);

			return divobj;
		}

		//Publisher ok
		else if ( dblp instanceof Publisher ){
			divobj.is_a_publisher = true;
			Publisher publisher = (Publisher) dblp;
			divobj.id = publisher.getId();
			divobj.Publisher_name = publisher.getName();

			for (Publication publi: publisher.getPublications()){
				String title = publi.getTitle();
				String id = publi.getId();
				divobj.Publisher_publications_title_id.add(new Pair(title, id));
			}
			divobj.Publisher_publications_title_id.sort(comparePairTitleId);

			return divobj;
		}

		//Series ok
		else if ( dblp instanceof Series ){
			divobj.is_a_series = true;
			Series series = (Series) dblp;
			divobj.id = series.getId();

			divobj.Series_name = series.getName();

			for (Publication publi: series.getPublications()){
				String title = publi.getTitle();
				String id = publi.getId();
				divobj.Series_publications_title_id.add(new Pair(title, id));
			}
			divobj.Series_publications_title_id.sort(comparePairTitleId);
		}

		return divobj;

	}


	/**
	 * IO-methods
	 * @param sort_by 
	 */
	//Query 1
	public PublicationIO IO_get_publication_by_id(String publ_id){
		pm.currentTransaction().setNontransactionalRead(true);

		return fill_PublicationIO(get_publication_by_id(publ_id));

	}

	//Query 2, 3
	public List<PublicationIO> IO_get_publ_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		pm.currentTransaction().setNontransactionalRead(true);

		Collection<Publication> publs = get_publ_by_filter_offset(filter, boff, eoff, order_by);

		List<PublicationIO> return_list = new ArrayList<PublicationIO>();	

		if (publs == null){
			return return_list;
		}
		/*
		if (publs.isEmpty()){
			PublicationIO publ = fill_PublicationIO(null);
			return_list.add(publ);
			return return_list;
		}*/


		for (Publication publ: publs){
			return_list.add(fill_PublicationIO(publ));
		}
		return return_list;
	}

	public List<PublicationIO> IO_get_publ_by_person(HashMap<String, String> args) {
		pm.currentTransaction().setNontransactionalRead(true);

		List<PublicationIO> return_list = new ArrayList<PublicationIO>();	
		Person pers;

		if( args.containsKey("id") ){
			//get person by id
			pers = get_person_by_id(args.get("id"));
		}
		else {
			//get person by name
			pers = get_person_by_name(args.get("name"));
		}

		if ( pers == null ){
			//return empty list
			return return_list;
		}

		String publ_mode = args.get("publ");
		if ( publ_mode.equals("all") || publ_mode.equals("edited")){
			//get proceedings
			Set<Publication> editedPublications = pers.getEditedPublications();
			for (Publication publ: editedPublications){
				return_list.add(fill_PublicationIO(publ));
			}
		}
		if ( publ_mode.equals("all") || publ_mode.equals("authored")){
			//get inproceedings
			Set<Publication> authoredPublications = pers.getAuthoredPublications();
			for (Publication publ: authoredPublications){
				return_list.add(fill_PublicationIO(publ));
			}
		}

		return return_list;
	}

	public List<DivIO> IO_get_person_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		pm.currentTransaction().setNontransactionalRead(true);

		Collection<Person> persons = get_person_by_filter_offset(filter, boff, eoff, order_by);

		List<DivIO> return_list = new ArrayList<DivIO>();

		if (persons == null){
			return return_list;
		}

		for (Person person: persons){
			return_list.add(fill_DivIO(person));
		}
		return return_list;
	}

	public List<DivIO> IO_get_conference_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		pm.currentTransaction().setNontransactionalRead(true);

		Collection<Conference> confs = get_conference_by_filter_offset(filter, boff, eoff, order_by);

		List<DivIO> return_list = new ArrayList<DivIO>();	

		if (confs == null){
			return return_list;
		}
		for (Conference conf: confs){
			return_list.add(fill_DivIO(conf));
		}
		return return_list;
	}

	public DivIO IO_get_confEd_by_id(String confEd_id) {
		pm.currentTransaction().setNontransactionalRead(true);

		return fill_DivIO((get_conferenceedition_by_id(confEd_id)));
	}

	public DivIO IO_get_conf_by_id(String conf_id) {
		pm.currentTransaction().setNontransactionalRead(true);

		return fill_DivIO((get_conference_by_id(conf_id)));
	}

	public DivIO IO_get_person_by_id(String pers_id) {
		pm.currentTransaction().setNontransactionalRead(true);

		return fill_DivIO((get_person_by_id(pers_id)));
	}

	public List<DivIO> IO_find_co_authors(String pers_name) {
		pm.currentTransaction().setNontransactionalRead(true);

		List<DivIO> return_list = new ArrayList<DivIO>();

		HashSet<Person> all_authors = new HashSet<Person>();	

		Person pers = get_person_by_name(pers_name);

		if ( pers == null ){
			//return empty list
			return return_list;
		}

		//get inproceedings
		Set<Publication> authoredPublications = pers.getAuthoredPublications();
		for (Publication publ: authoredPublications){
			InProceedings inproc = (InProceedings) publ;
			all_authors.addAll(inproc.getAuthors());
		}

		for (Person pers1: all_authors){
			System.out.println(pers1.getName());
			return_list.add(fill_DivIO(pers1));
		}

		return_list.sort(compareDivIO_Person_name);

		return return_list;

	}

	public DivIO IO_get_publisher_by_id(String publ_id) {
		pm.currentTransaction().setNontransactionalRead(true);

		return fill_DivIO((get_publisher_by_id(publ_id)));
	}

	public List<DivIO> IO_get_publisher_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		pm.currentTransaction().setNontransactionalRead(true);

		Collection<Publisher> publishers = get_publisher_by_filter_offset(filter, boff, eoff, order_by);

		List<DivIO> return_list = new ArrayList<DivIO>();

		if (publishers == null){
			return return_list;
		}

		for (Publisher publisher: publishers){
			return_list.add(fill_DivIO(publisher));
		}
		return return_list;
	}

	public DivIO IO_get_series_by_id(String series_id) {
		pm.currentTransaction().setNontransactionalRead(true);

		return fill_DivIO((get_series_by_id(series_id)));
	}

	public List<DivIO> IO_get_series_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		pm.currentTransaction().setNontransactionalRead(true);

		Collection<Series> series = get_series_by_filter_offset(filter, boff, eoff, order_by);

		List<DivIO> return_list = new ArrayList<DivIO>();

		if (series == null){
			return return_list;
		}

		for (Series ser: series){
			return_list.add(fill_DivIO(ser));
		}
		return return_list;
	}












}



