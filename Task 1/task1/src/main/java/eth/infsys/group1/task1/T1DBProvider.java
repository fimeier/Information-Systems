package eth.infsys.group1.task1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.zoodb.jdo.ZooJdoHelper;
import org.zoodb.tools.ZooHelper;

import eth.infsys.group1.dbspec.DBProvider;
import eth.infsys.group1.dbspec.DivIO;
import eth.infsys.group1.dbspec.InProceedings_simple_input;
import eth.infsys.group1.dbspec.Proceedings_simple_input;
import eth.infsys.group1.dbspec.PublicationIO;
import eth.infsys.group1.task1.dbobjs.Conference;
import eth.infsys.group1.task1.dbobjs.ConferenceEdition;
import eth.infsys.group1.task1.dbobjs.DomainObject;
import eth.infsys.group1.task1.dbobjs.InProceedings;
import eth.infsys.group1.task1.dbobjs.Person;
import eth.infsys.group1.task1.dbobjs.Proceedings;
import eth.infsys.group1.task1.dbobjs.Publication;
import eth.infsys.group1.task1.dbobjs.Publisher;
import eth.infsys.group1.task1.dbobjs.Series;
import javafx.util.Pair;

@SuppressWarnings("restriction")
public class T1DBProvider extends DBProvider {
//DBProvider<Conference, ConferenceEdition, InProceedings, Person, Proceedings, Publication, Publisher, Series> {

	
	/**
	 * Class and DB-Stuff
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	private PersistenceManager pm;


	/**
	 * Create DBProvider
	 * 
	 * @param dbName the name of the database to be created
	 * @param mode OPEN_DB_APPEND or OPEN_DB_OVERRIDE
	 */
	public T1DBProvider(String dbName, int mode) {
		createDB(dbName, mode);		
	}

	protected void createDB(String dbName, int mode) {
		// create database
		// By default, all database files will be created in %USER_HOME%/zoodb

		switch (mode) {
		case OPEN_DB_APPEND:
			/**
			 * create DB and open database connection
			 * do not delete if already exist
			 */
			this.pm = ZooJdoHelper.openOrCreateDB(dbName);

			pm.currentTransaction().setRetainValues(true);

			break;

			//OPEN_DB_OVERRIDE
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

	protected void schemaManager() {

		pm.currentTransaction().begin();
		ZooJdoHelper.createIndex(pm, DomainObject.class, "id", true);
		pm.currentTransaction().commit();

		pm.currentTransaction().begin();
		ZooJdoHelper.createIndex(pm, Person.class, "name", false);
		pm.currentTransaction().commit();

		pm.currentTransaction().begin();
		ZooJdoHelper.createIndex(pm, Publication.class, "year", false);
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
		@SuppressWarnings("unchecked")
		Collection<Publication> publs = (Collection<Publication>)q.execute();

		int collection_size = publs.size();
		//		int skip = boff-eoff;
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

		@SuppressWarnings("unchecked")
		Collection<Person> persons = (Collection<Person>) q.execute();

		int collection_size = persons.size();
		//		int skip = boff-eoff;
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

		@SuppressWarnings("unchecked")
		Collection<Conference> confs = (Collection<Conference>) q.execute();

		int collection_size = confs.size();
		//		int skip = boff-eoff;
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

		@SuppressWarnings("unchecked")
		Collection<Publisher> publishers = (Collection<Publisher>) q.execute();

		int collection_size = publishers.size();
		//		int skip = boff-eoff;
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

		@SuppressWarnings("unchecked")
		Collection<Series> series = (Collection<Series>) q.execute();

		int collection_size = series.size();
		//		int skip = boff-eoff;
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


	@SuppressWarnings("unused")
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
		String series_id = Series.calculate_series_id(args.series);
		Series serie = get_series_by_id(series_id);
		if ( serie == null ){
			serie = create_serie(args.series);
			System.out.println("Series (" + args.series + ") created...");
		}
		else {
			System.out.println("Series (" + args.series + ") already exists...");
		}
		pm.currentTransaction().commit();

		/*
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
		 */

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
				System.out.println("Author(" + author_name + ") already exists...");
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


	private Comparator<Person> compare_Person_name = new Comparator<Person>() {
		@Override
		public int compare(Person o1, Person o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};

	private Comparator<InProceedings> compare_InProceedings_title = new Comparator<InProceedings>() {
		@Override
		public int compare(InProceedings o1, InProceedings o2) {
			return o1.getTitle().compareTo(o2.getTitle());
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
					publication.Conference_name_id = new Pair<String, String>(name,confid);

					//ConferenceEdition	
					int year = proc.getConferenceEdition().getYear();
					String confEdid = proc.getConferenceEdition().getId();
					publication.ConferenceEdition_year_id = new Pair<Integer, String>(year,confEdid);
				}
			}

			/*for (Person editor: proc.getEditors()){
				publication.editors.add(editor.getName());
			}*/
			for (Person editor: proc.getEditors()){
				String name = editor.getName();
				String id = editor.getId();
				publication.editors_name_id.add(new Pair<String, String>(name, id));
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
				publication.inproceedings_title_id.add(new Pair<String, String>(title, id));
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
						publication.Conference_name_id = new Pair<String, String>(name,confid);

						//ConferenceEdition	
						int year = proc.getConferenceEdition().getYear();
						String confEdid = proc.getConferenceEdition().getId();
						publication.ConferenceEdition_year_id = new Pair<Integer, String>(year,confEdid);
					}
				}
			}

			for (Person author: inproc.getAuthors()){
				String name = author.getName();
				String id = author.getId();
				publication.authors_name_id.add(new Pair<String, String>(name, id));
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
				divobj.Conference_editions_year_id.add(new Pair<Integer, String>(year, id));
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
				divobj.Person_authoredPublications_title_id.add(new Pair<String, String>(title, id));
			}
			divobj.Person_authoredPublications_title_id.sort(comparePairTitleId);

			for (Publication publ: pers.getEditedPublications()){
				String title = publ.getTitle();
				String id = publ.getId();
				divobj.Person_editedPublications_title_id.add(new Pair<String, String>(title, id));
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
				divobj.Publisher_publications_title_id.add(new Pair<String, String>(title, id));
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
				divobj.Series_publications_title_id.add(new Pair<String, String>(title, id));
			}
			divobj.Series_publications_title_id.sort(comparePairTitleId);
		}

		return divobj;

	}


	/**
	 * IO-methods
	 * @param sort_by 
	 */
	
	public PublicationIO IO_get_publication_by_id(String publ_id){
		pm.currentTransaction().setNontransactionalRead(true);

		return fill_PublicationIO(get_publication_by_id(publ_id));

	}

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

	public List<PublicationIO> IO_publ_by_person_name_or_id(HashMap<String, String> args) {
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
		if ( publ_mode.equals("all") || publ_mode.equals("editored")){
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

		all_authors.remove(pers);

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

	public String IO_delete_get_person_by_id(String pers_id) {
		pm.currentTransaction().begin();
		String Output = "<br>";
		Person pers = get_person_by_id(pers_id);
		if (pers==null){
			Output += "person with id="+pers_id+" doesn't exists..";
			return Output;
		}
		//remove InProceedings
		for( Publication authored: pers.getAuthoredPublications()){
			InProceedings inproc = (InProceedings) authored;
			if ( inproc.removeAuthor(pers) ){
				Output += "removed from inproceeding (id="+inproc.getId()+")<br>";
			}
			else {
				Output += "ERROR: couldn't remove from inproceeding (id="+inproc.getId()+")<br>";
			}
		}
		//remove Proceedings
		for( Publication editored: pers.getEditedPublications()){
			Proceedings proc = (Proceedings) editored;
			if ( proc.removeEditor(pers) ){
				Output += "removed from proceeding (id="+proc.getId()+")<br>";
			}
			else {
				Output += "ERROR: couldn't remove from Inproceeding (id="+proc.getId()+")<br>";
			}
		}

		pers.jdoZooMarkDeleted();

		//pm.currentTransaction().rollback();
		pm.currentTransaction().commit();;

		return Output;
	}


	public String IO_find_author_distance_path(String name1, String name2) {
		pm.currentTransaction().setNontransactionalRead(true);

		String Output = "";
		HashMap<Person,Pair<Person,Pair<InProceedings,Integer>>> old_authors = new HashMap<>();
		//HashMap<Person,Pair<Person,InProceedings>> working_authors = new HashMap<>();
		List<Person> working_authors = new ArrayList<>();
		HashSet<Person> next_authors = new HashSet<>();



		Person pers1 = get_person_by_name(name1);
		Person pers2 = get_person_by_name(name2);


		if ( pers1 == null || pers2 == null ){
			Output = "<br>name1 (= "+ name1 + ") or name2(= "+ name2 + ") is not a person<br>";
			return Output;
		}

		if ( pers1.getAuthoredPublications()==null || pers2.getAuthoredPublications()==null){
			Output = "<br>name1 (= "+ name1 + ") or name2(= "+ name2 + ") hasn't authored anything<br>";
			return Output;
		}

		int distance = 1;
		next_authors.add(pers1);

		int k = 0;
		for (;distance <=15; distance++){
			working_authors.clear();
			working_authors.addAll(next_authors);
			next_authors.clear();
			for (Person person: working_authors){
				//get inproceedings for person
				for (Publication publ: person.getAuthoredPublications()){
					InProceedings inproc = (InProceedings) publ;
					//for each inproceeding get the list of authors
					for (Person p: inproc.getAuthors()){
						//check if the author was already "searched"
						if ( old_authors.containsKey(p) ){
							continue;
						}
						//creates Pair with (father-node and (common edge,distance))
						k++;
						Pair<InProceedings,Integer> inproc_dist = new Pair<InProceedings, Integer>(inproc,distance);
						Pair<Person,Pair<InProceedings,Integer>> node_edge_dist = new Pair<Person, Pair<InProceedings, Integer>>(person,inproc_dist);
						old_authors.put(p, node_edge_dist);
						next_authors.add(p);

						if ( old_authors.containsKey(pers2) ){
							Output += "<br><b>The distance between "+pers2.getName()+ " and "+pers1.getName()+ " is "+distance+"</b><br><br>one possible path:<br>";
							Person persA = pers2;
							for (int i = distance; i>0; i--){
								Pair<Person,Pair<InProceedings,Integer>> node_edge_dist2 = old_authors.get(persA);

								Person persB = node_edge_dist2.getKey();
								InProceedings inproc1 = node_edge_dist2.getValue().getKey();
								int dist = node_edge_dist2.getValue().getValue();
								Output += "<a href='/test/?func=person_by_id&id="+persA.getId()+"'>"+persA.getName()+"</a> <--"+inproc1.getTitle()+"--> " + "<a href='/test/?func=person_by_id&id="+persB.getId()+"'>"+persB.getName()+"</a> ("+dist+")<br>";
								persA = persB;
							}
							Output += "<br>looked at "+k+" entries<br>";
							return Output;
						}
					}
				}

			}
		}

		Output += "<br>nothing found with distance max"+(distance-1)+" <br>";
		return Output;
	}

	public String IO_avg_authors_per_inproceedings() {
		pm.currentTransaction().setNontransactionalRead(true);

		int count_inproc = 0;
		double avg = 0;
		Extent<InProceedings> ext = pm.getExtent(InProceedings.class);
		for (InProceedings inproc: ext) {
			count_inproc++;
			avg += inproc.getAuthors().size();
		}
		ext.closeAll();

		double temp = avg / (double) count_inproc;
		String Output = "<br>there are <b>"+count_inproc+" inproceedings</b> in total with an average of <b>"+String.valueOf(temp)+" authors per inproceedings</b><br>";

		return Output;
	}

	public String IO_count_publications_per_interval(int y1, int y2) {
		pm.currentTransaction().setNontransactionalRead(true);

		String Output = "";

		/**
		 * performance??
		 */
		long start, stop;

		/*

		System.out.println("implementation with Extent start...");
		start = System.nanoTime();
		int count_publ = 0;
        Extent<Publication> ext = pm.getExtent(Publication.class);
        for (Publication publ: ext) {
        	int year = publ.getYear();
        	if( y1 <= year && year <= y2){
        		count_publ++;
        	}
        }
        ext.closeAll();
		stop = System.nanoTime();
		System.out.println("implementation with Extent took="+(stop-start)/1.e9);

		String temp = String.valueOf(count_publ);
		Output += "<br>the number of publications in the interval ["+y1+","+y2+"] is: "+temp+"<br>";
		 */

		/**
		 * is not faster without index*/
		start = System.nanoTime();
		Query q = pm.newQuery (Publication.class, ":y1<= this.year && this.year <= :y2");
		//String filter = "'y1'<= year && year <= 'y2'";
		//q.setFilter(filter);
		@SuppressWarnings("unchecked")
		Collection<Publication> ret = (Collection<Publication>) q.execute(y1,y2);
		int tempsize = ret.size();
		stop = System.nanoTime();
		Output += "<br>the number of publications in the interval ["+y1+","+y2+"] is: "+tempsize+"<br>";
		System.out.println("implementation with Extent took="+(stop-start)/1.e9);


		return Output;
	}

	public String IO_inproceedings_for_a_conference(String conf_id, String mode) {
		pm.currentTransaction().setNontransactionalRead(true);
		String Output ="";
		int count = 0;
		Conference conf = get_conference_by_id(conf_id);
		if (conf==null){
			Output += "<br>not a conference<br>";
			return Output;
		}

		if (mode.equals("count")){
			for (ConferenceEdition confEd: conf.getEditions()){
				try {
					count += confEd.getProceedings().getPublications().size();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Output += "<br>there are <b>"+count+" inproceedings</b> for the conference <a href='/test/?func=conf_by_id&id="+conf.getId()+"'>"+ conf.getName()+"</a><br>";
			//Output += "<br>there are "+count+" inproceedings for the conference "+conf.getName() +"<br>";
			return Output;
		}
		else {
			List<InProceedings> list_inproc = new ArrayList<>();
			for (ConferenceEdition confEd: conf.getEditions()){
				try {
					list_inproc.addAll(confEd.getProceedings().getPublications());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			list_inproc.sort(compare_InProceedings_title);
			Output += "<br>the inproceedings for the conference <a href='/test/?func=conf_by_id&id="+conf.getId()+"'>"+ conf.getName()+"</a> are:<br>";
			for (InProceedings inproc: list_inproc){
				Output += "<a href='/test/?func=inproceeding_by_id&key="+inproc.getId()+"'>"+inproc.getTitle()+"</a><br>";
			}

			return Output;
		}
	}

	public String IO_authors_editors_for_a_conference(String conf_id, String mode) {
		pm.currentTransaction().setNontransactionalRead(true);
		String Output ="";

		Conference conf = get_conference_by_id(conf_id);
		if (conf==null){
			Output += "<br>not a conference<br>";
			return Output;
		}
		HashSet<Person> authors = new HashSet<>();
		HashSet<Person> editors = new HashSet<>();

		for (ConferenceEdition confEd: conf.getEditions()){
			try {
				editors.addAll(confEd.getProceedings().getEditors());				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (confEd.getProceedings().getPublications() != null){
				for (InProceedings inproc: confEd.getProceedings().getPublications()){
					try {
						authors.addAll(inproc.getAuthors());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		if (mode.equals("count")){
			int count = 0;
			count += authors.size();
			count += editors.size();

			Output += "<br>there are <b>"+authors.size()+" authors</b> and <b>"+editors.size()+" editors</b> (total="+count+") for the conference <a href='/test/?func=conf_by_id&id="+conf_id+"'>"+conf.getName()+"</a><br>";

			return Output;
		}
		else {
			Output+= "<br><b>all authors:</b><br>";

			List<Person> list_authors= new ArrayList<>(authors);
			list_authors.sort(compare_Person_name);
			for (Person pers: list_authors){
				Output+= "<a href='/test?func=person_by_id&id="+pers.getId()+"'>"+pers.getName()+"</a> ";

			}

			Output +="<br><br><b>all editors:</b><br>";
			List<Person> list_editors= new ArrayList<>(editors);
			list_editors.sort(compare_Person_name);
			for (Person pers: list_editors){
				Output+= "<a href='/test?func=person_by_id&id="+pers.getId()+"'>"+pers.getName()+"</a> ";

			}
			return Output;
		}
	}

	public String IO_person_is_author_and_editor() {
		pm.currentTransaction().setNontransactionalRead(true);

		String Output = "";

		HashMap<Person,List<Pair<Proceedings,InProceedings>>> authoreditor = new HashMap<>();

		Extent<Proceedings> ext = pm.getExtent(Proceedings.class);
		for (Proceedings proc: ext) {
			Set<Person> editors = proc.getEditors();
			for (InProceedings inproc: proc.getPublications()){
				for (Person p: inproc.getAuthors()){
					if ( editors.contains(p)){
						Pair<Proceedings,InProceedings> authedit = new Pair<Proceedings, InProceedings>(proc,inproc);
						if (authoreditor.containsKey(p)){
							List<Pair<Proceedings,InProceedings>> old_entry = authoreditor.get(p);
							old_entry.add(authedit);
							authoreditor.put(p, old_entry);
						}
						else {
							List<Pair<Proceedings,InProceedings>> new_entry = new ArrayList<Pair<Proceedings,InProceedings>>();
							new_entry.add(authedit);
							authoreditor.put(p, new_entry);
						}
					}
				}
			}

		}
		ext.closeAll();

		final String[] temp = new String[1];
		temp[0] = "";
		BiConsumer<Person, List<Pair<Proceedings, InProceedings>>> return_stuff = new BiConsumer<Person, List<Pair<Proceedings, InProceedings>>>(){

			@Override
			public void accept(Person p, List<Pair<Proceedings, InProceedings>> proc_inproc) {
				temp[0] += p.getName()+" is author and editor in the following proceedings/inproceedings <br>";
				for (Pair<Proceedings, InProceedings> entry: proc_inproc){
					//System.out.println(p.getName()+" is author and editor in the proc ("+ entry.getKey().getId()+") and the inproc ("+ entry.getValue().getId()+")");
					temp[0] += "(<a href='/test/?func=proceeding_by_id&key=" + entry.getKey().getId()+"'>"+entry.getKey().getId()+ "</a> / <a href='/test/?func=inproceeding_by_id&key=" + entry.getValue().getId()+"'>"+entry.getValue().getId()+ "</a>), ";
				}
				temp[0] += "<br><br>";
			}
		};
		authoreditor.forEach(return_stuff);

		Output += "<br><br>" + temp[0];
		return Output;
	}

	public List<PublicationIO> IO_person_is_last_author(String pers_id) {
		pm.currentTransaction().setNontransactionalRead(true);

		List<PublicationIO> return_list = new ArrayList<PublicationIO>();	

		Person pers = get_person_by_id(pers_id);
		if (pers==null){
			//Output += "<br>inexisting person-id<br>";
			return return_list;
		}

		//		List<InProceedings> last_author = new ArrayList<>();

		Set<Publication> authoredPublications = pers.getAuthoredPublications();

		for (Publication publ: authoredPublications){
			InProceedings inproc = (InProceedings) publ;
			try {
				int last = inproc.getAuthors().size() -1;
				if ( inproc.getAuthors().get(last).getId().equals(pers_id)){
					return_list.add(fill_PublicationIO(publ));				
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace(); 
			}

		}


		return return_list;
	}

	public String IO_get_statistics() {
		pm.currentTransaction().setNontransactionalRead(true);

		int num_DomainObject = 0;
		int num_Person =0;
		int num_Conference =0;
		int num_ConferenceEdition = 0;
		int num_Publications = 0;
		int num_InProceedings = 0;
		int num_Proceedings = 0;
		int num_Publisher = 0;
		int num_Series = 0;

		long start, stop;
		start = System.nanoTime();

		Extent<DomainObject> ext = pm.getExtent(DomainObject.class);
		for (DomainObject domainobj: ext) {
			num_DomainObject++;
			if ( domainobj instanceof Person ){
				num_Person++;
			}
			else if ( domainobj instanceof Conference ){
				num_Conference++;
			}
			else if ( domainobj instanceof ConferenceEdition ){
				num_ConferenceEdition++;
			}
			else if ( domainobj instanceof  Publication){
				num_Publications++;
				Publication publ = (Publication) domainobj;
				if ( publ instanceof InProceedings){
					num_InProceedings++;
				}
				else if ( publ instanceof Proceedings){
					num_Proceedings++;
				}
			}

			else if ( domainobj instanceof Publisher ){
				num_Publisher++;
			}
			else if ( domainobj instanceof  Series){
				num_Series++;
			}

		}
		ext.closeAll();
		stop = System.nanoTime();
		System.out.println("implementation with Extent took="+(stop-start)/1.e9);



		String Output ="<br>";
		Output += "Number of DomainObject: " +num_DomainObject+"<br>";
		Output += "Number of Person: " +num_Person+"<br>";
		Output += "Number of Conference: " +num_Conference+"<br>";
		Output += "Number of ConferenceEdition: " +num_ConferenceEdition+"<br>";
		Output += "Number of Publications: " +num_Publications+"<br>";
		Output += "Number of InProceedings: " +num_InProceedings+"<br>";
		Output += "Number of Proceedings: " +num_Proceedings+"<br>";
		Output += "Number of Publisher: " +num_Publisher+"<br>";
		Output += "Number of Series: " +num_Series+"<br>";



		return Output;
	}

	public List<DivIO> IO_publishers_whose_authors_in_interval(int y1, int y2) {
		pm.currentTransaction().setNontransactionalRead(true);

		Set<Publisher> publisher = new HashSet<Publisher>();
		List<DivIO> return_list = new ArrayList<DivIO>();

		/**
		 * is not faster
		 * Query is not faster to get all inproceedings in the interval...
		 */


		/*

		long start,stop;
		//start = System.nanoTime();
		Query q = pm.newQuery (InProceedings.class, ":y1<= this.year && this.year <= :y2");
		//String filter = "id.contains('"+args.get("name_contains").toLowerCase()+"')";
		//q.setFilter(filter);
		Collection<InProceedings> ret = (Collection<InProceedings>) q.execute(y1,y2);
		List<InProceedings> ret2 = new ArrayList<>();
		for (Publication publ: ret){
			if(!(publ instanceof Proceedings)){
				ret2.add((InProceedings) publ);
			}
		}
		for (InProceedings inproc: ret2) {
			try {
				//add publisher to the Set
				Publisher temp = inproc.getProceedings().getPublisher();
				if (temp == null){
					System.out.println("publisher is null....");
				}
				publisher.add(inproc.getProceedings().getPublisher());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 */

		/*
		Extent<InProceedings> ext = pm.getExtent(InProceedings.class);
		for (InProceedings inproc: ext) {
			int year = inproc.getYear();
			if( y1 <= year && year <= y2){
				try {
					//add publisher to the Set
					Publisher temp = inproc.getProceedings().getPublisher();
					if (temp == null){
						System.out.println("publisher is null....");
					}
					publisher.add(inproc.getProceedings().getPublisher());

					//if(publisher.add(inproc.getProceedings().getPublisher())){
						//System.out.println("das fehlte");
				//	}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		ext.closeAll();*/


		//alternative
		Extent<Proceedings> ext = pm.getExtent(Proceedings.class);
		for (Proceedings proc: ext) {
			try {
				int year  = proc.getPublications().iterator().next().getYear();
				if( y1 <= year && year <= y2){
					publisher.add(proc.getPublisher());
				} 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		ext.closeAll();


		for (Publisher publi: publisher){

			return_list.add(fill_DivIO(publi));
		}
		return_list.sort(compareDivIO_Publisher_name);


		return return_list;
	}

	@Override
	public String IO_find_co_authors_returns_String(String pers_name) {
		// TODO Auto-generated method stub
		return null;
	}




}



