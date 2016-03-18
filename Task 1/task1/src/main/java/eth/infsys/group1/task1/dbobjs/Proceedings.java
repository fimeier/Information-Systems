package eth.infsys.group1.task1.dbobjs;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Proceedings extends Publication {
	
	private Set<Person> editors = new HashSet<>();

	private String note;
	private int number;
	private Publisher publisher;
	private String volume;
	private String isbn;
	private Series series;
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
	
    public Proceedings(String title, String electronicEdition, Set<Person> edit, String note, int number,
			Publisher publ, String volume, String isbn, Series serie, ConferenceEdition confEd) {
    	super(title, confEd.getYear(), electronicEdition);
    	
    	this.editors = new HashSet<>(edit);
    	for (Person p: this.editors){
    		p.addEditedPublications(this);
    	}
    	
    	this.note = note;
    	this.number = number;
    	
    	this.publisher = publ;
    	publ.addPublication(this);
    	
    	this.volume = volume;
    	this.isbn = isbn;
    	
    	this.series = serie;
    	serie.addPublication(this);
    	
    	this.conferenceEdition = confEd;
    	confEd.setProceedings(this);
    	
    	String id = "conf/" + confEd.getConference().getName() + "/" + confEd.getYear();
    	this.setId(id);
        System.out.println("calculated id=" + this.getId());
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