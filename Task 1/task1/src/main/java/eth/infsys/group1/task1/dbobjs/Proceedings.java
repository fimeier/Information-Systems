package eth.infsys.group1.task1.dbobjs;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import eth.infsys.group1.dbspec.Proceedings_simple_input;

public class Proceedings extends Publication {

	private Set<Person> editors = new HashSet<>();

	private String note;
	private int number;
	private Publisher publisher;
	private String volume;
	private String isbn;
	private Series series; //can be null
	private ConferenceEdition conferenceEdition;
	private Set<InProceedings> publications = new HashSet<>();

	/**
	 * Should only be used by the database
	 */
	public Proceedings() { }

	/*
	public Proceedings(String title, ConferenceEdition confEdition) {
		super(title, confEdition.getYear());
	}*/

	public Proceedings(Proceedings_simple_input args, Set<Person> edit, Publisher publ, Series serie, ConferenceEdition confEd) {
		super(args.title, args.year, args.electronicEdition);

		this.editors = new HashSet<>(edit);
		for (Person p: this.editors){
			p.addEditedPublications(this);
		}

		if (args.note != null){
			this.note = args.note;}
		else {
			this.note = "";}
		
		this.number = args.number;
				
		this.publisher = publ;
		publ.addPublication(this);

		if (args.volume != null){
			this.volume = args.volume;}
		else {
			this.volume = "";}
		
		if (args.isbn != null){
			this.isbn = args.isbn;}
		else {
			this.isbn = "";}

		this.series = serie;
		//if (this.series != null){
		serie.addPublication(this);
		//}

		//Condition: never null
		this.conferenceEdition = confEd;
		confEd.setProceedings(this);

		this.setId(args.id);
	}

	//dummy.. always us key from xml
	//example "conf/uist/1988"
	static public String calculate_proceedings_id(String key){
		return key;
	}
	
	public Boolean removeEditor(Person per){
    	zooActivateWrite();
    	return editors.remove(per);
	}


	public Set<Person> getEditors() {
		zooActivateRead();
		return Collections.unmodifiableSet(this.editors);
	}

	public String getNote() {
		zooActivateRead();
		return this.note;
	}

	public void setNote(String note) {
		zooActivateWrite();
		this.note = note;
	}

	public int getNumber() {
		zooActivateRead();
		return this.number;
	}

	public void setNumber(int number) {
		zooActivateWrite();
		this.number = number;
	}

	public Publisher getPublisher() {
		zooActivateRead();
		return this.publisher;
	}

	public void setPublisher(Publisher publisher) {
		zooActivateWrite();
		this.publisher = publisher;
	}

	public String getVolume() {
		zooActivateRead();
		return this.volume;
	}

	public void setVolume(String volume) {
		zooActivateWrite();
		this.volume = volume;
	}

	public String getIsbn() {
		zooActivateRead();
		return this.isbn;
	}

	public void setIsbn(String isbn) {
		zooActivateWrite();
		this.isbn = isbn;
	}

	public Series getSeries() {
		zooActivateRead();
		return this.series;
	}

	public void setSeries(Series series) {
		zooActivateWrite();
		this.series = series;
	}

	public ConferenceEdition getConferenceEdition() {
		zooActivateRead();
		return this.conferenceEdition;
	}

	public void setConferenceEdition(ConferenceEdition conferenceEdition) {
		zooActivateWrite();
		this.conferenceEdition = conferenceEdition;
	}

	public Set<InProceedings> getPublications() {
		zooActivateRead();
		return Collections.unmodifiableSet(this.publications);
	}

	public boolean addInProceedings(InProceedings inproc) {
		zooActivateWrite();
		return this.publications.add(inproc);
	}

	public void setPublications(Set<InProceedings> publications) {
		zooActivateWrite();
		this.publications = new HashSet<>(publications);
	}

}