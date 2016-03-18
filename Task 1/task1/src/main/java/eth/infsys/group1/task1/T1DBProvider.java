/**Problems
 * Interface to Gui... OO to String...
 * !!!! getid()... etc. compare toUpperCase()
 */


package eth.infsys.group1.task1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.zoodb.jdo.ZooJdoHelper;
import org.zoodb.tools.ZooHelper;

import eth.infsys.group1.task1.dbobjs.*;
import eth.infsys.group1.ui.DBProvider;

public class T1DBProvider extends
		DBProvider<Conference, ConferenceEdition, InProceedings, Person, Proceedings, Publication, Publisher, Series> {

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
			break;

		default:
			/**
			 * create database and/or remove first if it exists
			 */
			if (ZooHelper.dbExists(dbName)) {
				ZooHelper.removeDb(dbName);
			}
			this.pm = ZooJdoHelper.openOrCreateDB(dbName);
		}
	}

	/**
     * Close the database connection.
     * 
     * @param pm The current PersistenceManager.
     */
    protected void closeDB() {
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
     * Create and return new (or existing) proceeding
     * 
     * @param id DomainObject id... ignore it
     * @return Proceedings new_or_existing_proceeding
     */
    public Proceedings createProceeding(String id, String title, Set<String> editors, int year, String electronicEdition, String note, int number, String publisher, String volume, String isbn, String series, int conferenceEdition, String conferenceName) {
    	id = "conf/" + conferenceName + "/" + conferenceEdition;
    	id = id.toUpperCase();

    	pm.currentTransaction().begin();
    	Proceedings proc = get_proceeding_by_id(id);
    	pm.currentTransaction().commit();
    	//return existing proceeding
    	if ( ! (proc == null) ){
        	pm.currentTransaction().begin();
            System.out.println("proceeding already exists... id=" + proc.getId() + " and title=" + proc.getTitle());
        	pm.currentTransaction().commit();
            return proc;
    	}
    	//create new proceeding (one big transaction)
        System.out.println("create new proceeding");
    	pm.currentTransaction().begin();
    	
    	//Conference
    	Conference conf = get_conference_by_id(conferenceName);
    	if ( conf == null ){
    		conf = create_conference(conferenceName);
    	}
    	
    	//ConferenceEdition
    	ConferenceEdition confEd = create_conferenceEdition(conf, conferenceEdition);
    	
    	//Editors
    	Set<Person> edit = new HashSet<>();
    	for (String editor_id: editors){
    		Person editor = get_person_by_id(editor_id);
    		if ( editor == null ){
        		editor = create_person(editor_id);
        	}
    		edit.add(editor);
    	}
    	
    	//Publisher
    	Publisher publ = get_publisher_by_id(publisher);
    	if ( publ == null ){
    		publ = create_publisher(publisher);
    	}
    	
    	//Series
    	Series serie = get_serie_by_id(series);
    	if ( serie == null ){
    		serie = create_serie(series);
    	}
    	
    	//!!!!!!!!!Proceeding!!!!!!!!!!!!
    	//no inproceedings <=> no publications
    	proc = new Proceedings(title, electronicEdition, edit, note, number, publ, volume,isbn,serie,confEd);
    
    	pm.currentTransaction().commit();
    	return proc;
    }


	private Series create_serie(String series) {
		Series serie = new Series(series);
		pm.makePersistent(serie);
		return serie;
	}

	private Series get_serie_by_id(String series) {
		// TODO Auto-generated method stub
		return null;
	}

	private Person create_person(String person_name) {
		Person pers = new Person(person_name);
		pm.makePersistent(pers);
		return pers;
	}

	private Person get_person_by_id(String person_id) {
		// TODO Auto-generated method stub
		return null;
	}

	private Publisher create_publisher(String publisher) {
		Publisher publ = new Publisher(publisher);
		pm.makePersistent(publ);
		return publ;
	}

	private Publisher get_publisher_by_id(String publisher) {
		// TODO Auto-generated method stub
		return null;
	}

	private ConferenceEdition create_conferenceEdition(Conference conf, int conferenceEdition) {
		ConferenceEdition confEd = new ConferenceEdition(conf, conferenceEdition);
		pm.makePersistent(confEd);
		return confEd;
	}

	private Conference create_conference(String conferenceName) {
		Conference conf = new Conference(conferenceName);
		pm.makePersistent(conf);
		return conf;
	}

	private Conference get_conference_by_id(String conferenceName) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
     * returns proceeding object or null
     * 
     * @param id DomainObject id
     * @return Proceedings proceeding
     */
	private Proceedings get_proceeding_by_id(String id) {
		//Query q = pm.newQuery(Proceedings.class, "id == '" + id + "'");

		// Proceedings proc = (Proceedings) pm.newQuery("select unique from eth.infsys.group1.task1.dbobjs.Proceedings where id == '" + id + "'").execute();

		/* if (proc == null){
        	return null;
        }
        return proc;
		 
		return null;*/
		Extent<Proceedings> ext = pm.getExtent(Proceedings.class);
        for (Proceedings p: ext) {
        	if (p.getId().equals(id)){
        		//System.out.println("already exists: " + p.getId());
                ext.closeAll();
                return p;
        	}
        }
        ext.closeAll();
        return null;
	}

	/**
     * Create and return new (or existing) inproceeding
     * 
     * @param id DomainObject id... ignore it
     * @param proceedings Proceeding id... ignore it
     * @return Inproceedings new_or_existing_inproceeding
     */
	@Override
	public InProceedings createInProceedings(String id, String title, int year, String electronicEdition, List<String> authors, String note, String pages, List<Proceedings> proceedings, int conferenceEdition, String conferenceName) {
		//calculate Proceeding id
		String proc_id = ("conf/" + conferenceName + "/" + conferenceEdition).toUpperCase();
        System.out.println("Searching proceeding by id=" + proc_id);
    	pm.currentTransaction().begin();
    	Proceedings proc = get_proceeding_by_id(proc_id);
    	pm.currentTransaction().commit();
    	//Abort if Proceeding is not existent (Error???)
    	if ( proc == null ){
            System.out.println("Proceeding with id=" + proc_id + " is not existing. Error Inproceeding creation...");
    		return null;
    	}
    	
    	//calculate inproceeding id
    	id = (proc_id + "/" + authors.get(0)).toUpperCase();
        System.out.println("Searching inproceeding by id=" + id);
        
    	pm.currentTransaction().begin();
        InProceedings inproc = get_inproceeding_by_id(id);
    	pm.currentTransaction().commit();
    	//return existing InProceeding
    	if ( !(inproc == null) ){
        	pm.currentTransaction().begin();
    		System.out.println("inproceeding already exists... id=" + inproc.getId() + " and title=" + inproc.getTitle());
        	pm.currentTransaction().commit();
            return inproc;
    	}


    	//create new inproceeding (one big transaction)
        System.out.println("create new inproceeding for proceeding with id=" + proc_id);
    	pm.currentTransaction().begin();
    	
    	//Authors
    	List<Person> auth = new ArrayList<>();
    	for (String author_id: authors){
    		Person author = get_person_by_id(author_id);
    		if ( author == null ){
        		author = create_person(author_id);
        	}
    		auth.add(author);
    	}
    	
    	//!!!!!!!!!Inroceeding!!!!!!!!!!!!
    	inproc = new InProceedings(title, electronicEdition, auth, note, pages, proc);

    	pm.currentTransaction().commit();
    	
    	return inproc;
	}

	private InProceedings get_inproceeding_by_id(String id) {
		Extent<InProceedings> ext = pm.getExtent(InProceedings.class);
        for (InProceedings p: ext) {
        	if (p.getId().equals(id)){
                ext.closeAll();
                return p;
        	}
        }
        ext.closeAll();
        return null;
	}

	@Override
	public String getTitle(InProceedings inProceedings) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Conference> getConferences(int startIndex, int endIndex, int sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConferenceEdition> getConferenceEditions(int startIndex, int endIndex, int sort,
			Conference conference) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InProceedings> getInProceedings(int startIndex, int endIndex, int sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InProceedings> getInProceedingsOfAuthor(int startIndex, int endIndex, int sort, Person author) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InProceedings> getInProceedingsOfProceedings(int startIndex, int endIndex, int sort,
			Proceedings proceedings) {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
     * dummy_implementation
     */
	@Override
	public List<Proceedings> getProceedings(int startIndex, int endIndex, int sort) {
		pm.currentTransaction().begin();
		
		List<Proceedings> all_proc = new ArrayList<Proceedings>();
		Extent<Proceedings> ext = pm.getExtent(Proceedings.class);
        for (Proceedings p: ext) {
        	all_proc.add(p);
        }
        ext.closeAll();
        
    	pm.currentTransaction().commit();

		
		return all_proc;
	}

	@Override
	public List<Proceedings> getProceedingsOfAuthor(int startIndex, int endIndex, int sort, Person author) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Publisher> getPublishers(int startIndex, int endIndex, int sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Person> getPersons(int startIndex, int endIndex, int sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Person> getPersons(int startIndex, int endIndex, int sort, String nameFilter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Series> getSeries(int startIndex, int endIndex, int sort) {
		// TODO Auto-generated method stub
		return null;
	}

	//HACK!!!
	public PersistenceManager getpm() {
		return pm;
	}

	

	

}
