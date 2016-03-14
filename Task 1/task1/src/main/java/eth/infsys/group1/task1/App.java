package eth.infsys.group1.task1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.print.attribute.standard.PrinterMessageFromOperator;

import org.zoodb.jdo.ZooJdoHelper;
import org.zoodb.tools.ZooHelper;

import ch.ethz.globis.isk.domain.DomainObject;
import ch.ethz.globis.isk.domain.InProceedings;
import ch.ethz.globis.isk.domain.Person;
import ch.ethz.globis.isk.domain.Proceedings;
import ch.ethz.globis.isk.domain.Publication;
import ch.ethz.globis.isk.domain.Publisher;
import eth.infsys.group1.task1.dbobjs.T1DomainObject;
import eth.infsys.group1.task1.dbobjs.T1InProceedings;
import eth.infsys.group1.task1.dbobjs.T1Person;
import eth.infsys.group1.task1.dbobjs.T1Proceedings;
import eth.infsys.group1.task1.dbobjs.T1Publisher;

/**
 * Hello world!
 *
 */



public class App 
{
	private static void createDB(String dbName) {
		/**
		 * create DB and open database connection
		 * do not delete if already exist
		 */
		PersistenceManager pm = ZooJdoHelper.openOrCreateDB(dbName);
		
		/*
		// remove database if it exists
        if (ZooHelper.dbExists(dbName)) {
            ZooHelper.removeDb(dbName);
        }

        // create database
        // By default, all database files will be created in %USER_HOME%/zoodb
        ZooHelper.createDb(dbName);
        */
    }

	public static void create_some_stuff(String dbName){
        PersistenceManager pm = ZooJdoHelper.openDB(dbName);
   

        
        
/** create:
 *Task1 <proceedings mdate="2004-10-08" key="conf/hmi/1987">
 *Task2 <inproceedings mdate="2006-02-15" key="conf/hmi/Estrin87">
 *Task3 <inproceedings mdate="2006-02-15" key="conf/hmi/Hammond87">
 */
 
/**
 * Task1
 */
        //check if proceedings exist
        String id = "conf/hmi/1987";
        String title = "Proceedings of the ACM Conference on History of Medical Informatics, Bethesda, Maryland, USA, November 5-6, 1987";
        List<String> authors = new ArrayList<String>(); //authors.add("");
        Set<String> editors = new HashSet<String>(); editors.add("Bruce I. Blum");

        int year = 1987; //??? compare conferenceEdition
        String electronicEdition = "db/conf/hmi/hmi1987.html"; //url or ee or both?
        String note = "";
        int number = 0;
        String publisher = "ACM";
        String volume = "";
        String isbn = "0-89791-248-9";

        //!!!!!!! for Task1
    	pm.currentTransaction().begin();

        if (!proceedings_exist(pm, id)){
           
            System.out.println("create Id=" + id );
            T1Proceedings newp = new T1Proceedings();
            pm.makePersistent(newp);
            /**
             * DomainObject
             */
            newp.setId(id);
            /**
             * Publication
             */
            newp.setTitle(title);
            //newp.setAuthors(authors to empty list???);
            
            newp.setEditors(create_and_return_editors(pm, editors));

        
            newp.setYear(year);
            newp.setElectronicEdition(electronicEdition);
            /**
             * Proceedings
             */
            newp.setNote(note);
            newp.setNumber(number);
            
            //create Publisher
            newp.setPublisher(create_and_return_publisher(pm, publisher));
            
            newp.setVolume(volume);
            newp.setIsbn(isbn);
            //newp.setSeries(series);
            //newp.setConferenceEdition(conferenceEdition);
            
            //Set<Publication>
            //newp.setPublications(publications);
            
            //empty Set... constructor???
            Set<InProceedings> publications = new HashSet<InProceedings>();
            newp.setPublications(publications);
            
            //!!!!!!! for Task1
    		pm.currentTransaction().commit();

 
            
/**
* Task2
*/
            /*
             * create and add Inproceedings
             * how to pass arguments?
             */
           
            pm.currentTransaction().begin();
            //add check: if (!inproceedings_exist(pm, id))....
            T1InProceedings inp1 = new T1InProceedings();
            pm.makePersistent(inp1); //????
            //DomainObject
            id = "conf/hmi/Estrin87";
            inp1.setId(id);
            //Publication
            title = "The UCLA Brain Research Institute data processing laboratory.";
            inp1.setTitle(title);
            
            authors = new ArrayList<String>(); authors.add("Thelma Estrin");
            inp1.setAuthors(create_and_return_authors(pm, authors));

            editors = new HashSet<String>(); editors.add("");
            //inp1.setEditors(editors to obje);

            year = 1987;
            inp1.setYear(year);
            
            electronicEdition = "http://doi.acm.org/10.1145/41526.41533"; //url or ee or both?
            inp1.setElectronicEdition(electronicEdition);
            
            //InProceedings
            note = "";
            inp1.setNote(note);
            
            String pages = "75-83";
            inp1.setPages(pages);
            
            inp1.setProceedings(newp);
            add_to_proceedings(pm, inp1, newp);

    		pm.currentTransaction().commit();

            
/**
* Task3
*/    
            pm.currentTransaction().begin();

            //add check: if (!inproceedings_exist(pm, id))....
            T1InProceedings inp2 = new T1InProceedings();
            pm.makePersistent(inp2); //????
            //DomainObject
            id = "conf/hmi/Hammond87";
            inp2.setId(id);
            //Publication
            title = "Patient management systems: the early years.";
            inp2.setTitle(title);
            
            authors = new ArrayList<String>(); authors.add("W. E. Hammond");
            inp2.setAuthors(create_and_return_authors(pm, authors));

            editors = new HashSet<String>(); editors.add("");
            //inp1.setEditors(editors to obje);

            year = 1987;
            inp2.setYear(year);
            
            electronicEdition = "http://doi.acm.org/10.1145/41526.41541"; //url or ee or both?
            inp2.setElectronicEdition(electronicEdition);
            
            //InProceedings
            note = "";
            inp2.setNote(note);
            
            pages = "153-164";
            inp2.setPages(pages);
            
            inp2.setProceedings(newp);
            add_to_proceedings(pm, inp2, newp);

    		pm.currentTransaction().commit();


        }
        else {
            System.out.println("Id=" + id + " already exists!");
        }
   

		closeDB(pm);
		
	}
	

	private static void add_to_proceedings(PersistenceManager pm, T1InProceedings inp1, T1Proceedings newp) {
		
		Set<InProceedings> old_set = newp.getPublications();
		//not working...
		//Set<InProceedings> new_set = old_set;
		//pm.makePersistent(new_set);
		//newp.setPublications(new_set);
        Set<InProceedings> publications = new HashSet<InProceedings>();
        //copy existing InProceedings
        for (InProceedings inp: old_set){
    		System.out.println("existing InProceedings: " + inp.getTitle());
    		publications.add(inp);
        }
        //add new Inproceeding (if already exists: not a problem, is a Set
        publications.add(inp1);
        newp.setPublications(publications);
	}

	/*private static void create_and_add_inproceedings(PersistenceManager pm, T1Proceedings newp, List<String> arg) {
		// TODO Auto-generated method stub
		
	}*/

	private static boolean proceedings_exist(PersistenceManager pm, String id) {

		//using reference in query {
		//Query q = pm.newQuery(T1Proceedings.class, getId() == id );
		//Collection<T1Proceedings> asdf = (Collection<T1Proceedings>)q.execute();
        //Query q = pm.newQuery("select unique from eth.infsys.group1.task1.dbobjs.T1Proceedings where id == '" + id + "'");
		
        //T1Proceedings asdf = (T1Proceedings) q.execute();
        
        Extent<T1Proceedings> ext = pm.getExtent(T1Proceedings.class);
        for (T1Proceedings p: ext) {
        	if (p.getId().equals(id)){
        		//System.out.println("already exists: " + p.getId());
                ext.closeAll();
                return true;
        	}
        }
        ext.closeAll();

		return false;
	}
	
	private static List<Person> create_and_return_authors(PersistenceManager pm, List<String> authors){
		List<Person> set_authors = new ArrayList<Person>();
		Extent<T1Person> ext = pm.getExtent(T1Person.class);
        for (T1Person p: ext) {
        	if(authors.remove(p.getName())){
        		//Author already exists as a Person; add object to Set
        		System.out.println("Author (" + p.getName() + ") already exists as a Person ");
        		set_authors.add(p);
        	}
        }
        ext.closeAll();
        //create not existing Authors; add objects to Set
        for (String author: authors) {
        	System.out.println("create Author (" + author + ") as a Person ");
        	set_authors.add( create_and_return_person(pm, author) );
        }
		return set_authors;
	};
	
	private static Set<Person> create_and_return_editors(PersistenceManager pm, Set<String> editors){
		Set<Person> set_editors = new HashSet<Person>();
		Extent<T1Person> ext = pm.getExtent(T1Person.class);
        for (T1Person p: ext) {
        	if(editors.remove(p.getName())){
        		//Editor already exists as a Person; add object to Set
        		System.out.println("Editor (" + p.getName() + ") already exists as a Person ");
        		set_editors.add(p);
        	}
        	
        }
        ext.closeAll();
        //create not existing Editors; add objects to Set
        for (String editor: editors) {
        	System.out.println("create Editor (" + editor + ") as a Person ");
        	set_editors.add( create_and_return_person(pm, editor) );
        }
        
        
		return set_editors;
	};

	private static Publisher create_and_return_publisher(PersistenceManager pm, String publisher) {
		Extent<T1Publisher> ext = pm.getExtent(T1Publisher.class);
        for (T1Publisher p: ext) {
        	if( publisher.equals(p.getName()) ){
        		//Publisher already exists; return Publisher as object
        		System.out.println("Publisher (" + p.getName() + ") already exists");
        		return p;
        	}
        }
        
        //create new Publisher
        String id = "";
		String name = publisher;
		Set<Publication> publications = new HashSet<Publication>();
		
		System.out.println("create publisher = " + name );
        T1Publisher newp = new T1Publisher();
        pm.makePersistent(newp);
		/**
         * DomainObject
         */
        newp.setId(id);
        /**
         * Publisher
         */
        newp.setName(name);
        //newp.setPublications(publications);
		
        return newp;
	}


	private static Person create_and_return_person(PersistenceManager pm, String pname) {
		String id = "";
		String name = pname;
		Set<Publication> authoredPublications = new HashSet<Publication>();
		Set<Publication> editedPublications = new HashSet<Publication>();
		
		System.out.println("create person = " + name );
        T1Person newp = new T1Person();
        pm.makePersistent(newp);
		/**
         * DomainObject
         */
        newp.setId(id);
        /**
         * Person
         */
        newp.setName(name);
        //newp.setAuthoredPublications(authoredPublications);
        //newp.setEditedPublications(editedPublications);
		
        return newp;
		
	}

	
	
	private static void closeDB(PersistenceManager pm) {
        if (pm.currentTransaction().isActive()) {
            pm.currentTransaction().rollback();
        }
        pm.close();
        pm.getPersistenceManagerFactory().close();
    }
	
    public static void main( String[] args )
    {
    	String dbName = "Project1_ZooDB.zdb";
        createDB(dbName);
        create_some_stuff(dbName);
        
        /*
        //set test
        Set<String> myset = new HashSet<String>();
        myset.add("shit");
        myset.add("shit");
        for (Object o : myset){
            System.out.println(o);
        }
        
        //List test
        List<String> mylist = new ArrayList<String>();
        mylist.add("Hans1");
        mylist.add("Urs");
        mylist.add("Hans");
        for (Object o : mylist){
            System.out.println(o);
        }
        System.out.println(mylist.get(1));

*/

    	        
        
        
    }
}


