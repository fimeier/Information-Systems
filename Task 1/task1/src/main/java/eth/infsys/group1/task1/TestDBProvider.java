package eth.infsys.group1.task1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;

import eth.infsys.group1.task1.dbobjs.InProceedings;
import eth.infsys.group1.task1.dbobjs.Person;
import eth.infsys.group1.task1.dbobjs.Proceedings;
import eth.infsys.group1.ui.DBProvider;
import eth.infsys.group1.ui.fxobjs.FxInProceedings;
import eth.infsys.group1.ui.fxobjs.FxProceedings;

public class TestDBProvider {

	/** arguments for a new proceeding
	 * 
	 */
	//DomainObject-Fields
	static String id; //<proceedings... key>
	//Publication-Fields
	static String title; //<title>
	static Set<String> editors; //multiple <editor>
	static String editorString; //<editor1>, <editor2>, ...
	static int year; // <year>
	static String electronicEdition; //<ee>
	//Proceedings-Fields
	static String note;
	static int number;
	static String publisher; //<publisher>
	static String volume;
	static String isbn; //<isbn>
	static String series; //<series>
	static int conferenceEdition; //<year>
	//never add inproceeding for a new proceeding
	//static String publications = "";
	//other fields
	static String conferenceName; //<booktitle>
	static FxProceedings<Proceedings> fxProceedings;
	
	
	/** arguments for a new inproceeding
	 * !!!! id!!! 90 => 1990
	 */
	//DomainObject-Fields:
	//static String id; //<(in)proceedings... key>
	//Publication-Fields:
	//static String title; //<title>
	//static int year; // <year>
	//static String electronicEdition; //<ee>
	//Inproceedings-Fields:
	static List<String> authors; //multiple <author>
	//static String note;
	static String pages;
	static String proceedings;
	static FxInProceedings<InProceedings> fxInProceedings;
	
	
	public static void main(String[] args) {
		String dbName = "Project1_ZooDB_new.zdb";

		T1DBProvider myDB = new T1DBProvider(dbName, T1DBProvider.OPEN_DB_OVERRIDE);

		//HACK
		PersistenceManager pm = myDB.getpm();


		//set_arg_proceeding1();


		set_arg_proceeding2();
		System.out.println("id=" + id);
		myDB.createProceeding(id, title, editors, year, electronicEdition, note, number, publisher, volume,isbn,series,conferenceEdition,conferenceName);

		//getProceedings
		List<Proceedings> proceedings = myDB.getProceedings(0, 0, DBProvider.SORT_BY_NAME);
		pm.currentTransaction().begin();
		for (Proceedings p: proceedings){
			System.out.println("id=" + p.getId());

			System.out.println("title=" + p.getTitle());
			for (Person editor: p.getEditors()){
				System.out.println("editor=" + editor.getName());
			}
			System.out.println("year=" + p.getYear());
			System.out.println("year=" + p.getElectronicEdition());
			System.out.println("note=" + p.getNote());
			System.out.println("number=" + p.getNumber());
			System.out.println("publisher=" + p.getPublisher().getName());
			System.out.println("volume=" + p.getVolume());
			System.out.println("isbn=" + p.getIsbn());
			System.out.println("series=" + p.getSeries().getName());
			System.out.println("conferenceEdition=" + p.getConferenceEdition().getYear());
			System.out.println("conferenceName=" + p.getConferenceEdition().getConference().getName());
		}
		pm.currentTransaction().commit();
		
		
		//create INPROCEEDINGS
		set_arg_inproceeding2A();
		myDB.createInProceedings(id, title, year, electronicEdition, authors, note, pages, proceedings, conferenceEdition, conferenceName);
		
		myDB.createInProceedings(id, title, year, electronicEdition, authors, note, pages, proceedings, conferenceEdition, conferenceName);
	}

	public static void set_arg_proceeding1() {
		id = "conf/hmi/1987";
		title = "Proceedings of the ACM Conference on History of Medical Informatics, Bethesda, Maryland, USA, November 5-6, 1987";
		editors = new HashSet<String>(); editors.add("Bruce I. Blum");
		year = 1987; 
		electronicEdition = ""; //url or ee or both?
		note = "";
		number = 0;
		publisher = "ACM";
		volume = "";
		isbn = "0-89791-248-9";
		series = "";
		conferenceEdition = year;
		conferenceName = "dummy-Name";
		fxProceedings = new FxProceedings<Proceedings>(null, id, title, year, electronicEdition, editors, note, number, publisher, volume, isbn, series, conferenceName, year, 0);
	}

	public static void set_arg_proceeding2() {
		id = "conf/echt/90";
		title = "Hypertext: Concepts, Systems and Applications, Proceedings of the European Conference on Hypertext, INRIA, France, November 1990.";
		editors = new HashSet<String>(); editors.add("Antoine Rizk"); editors.add("Norbert A. Streitz"); editors.add("J. André");
		year = 1990; 
		electronicEdition = ""; //url or ee or both?
		note = "";
		number = 0;
		publisher = "Cambridge University Press";
		volume = "";
		isbn = "0-521-40517-3";
		series = "The Cambridge Series on Electronic Publishing";
		conferenceEdition = year;
		conferenceName = "ECHT";
		fxProceedings = new FxProceedings<Proceedings>(null, id, title, year, electronicEdition, editors, note, number, publisher, volume, isbn, series, conferenceName, year, 0);
	}
	
	public static void set_arg_inproceeding2A() {
		id = "conf/echt/HofmannSL90";
		title = "An Integrated Approach of Knowledge Acquisition by the Hypertext System CONCORDE.";
		year = 1990; 
		electronicEdition = ""; //url or ee or both?
		authors = new ArrayList<String>(); authors.add("Martin Hofmann 0002"); authors.add("Uwe Schreiweis"); authors.add("Horst Langendörfer");
		note = "";
		pages = "166-179";
		
		proceedings = "conf/echt/1990";
		
		conferenceEdition = year;
		conferenceName = "ECHT";
		fxInProceedings = new FxInProceedings<InProceedings>(null, id, title, year, electronicEdition, authors, proceedings, pages, note);
	}
}





