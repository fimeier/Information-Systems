package eth.infsys.group1.task1.dbobjs;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Proceedings extends Publication {

	private String note;
	private int number;
	private Publisher publisher;
	private String volume;
	private String isbn;
	private Series series;
	private ConferenceEdition conferenceEdtion;
	private Set<InProceedings> publications = new HashSet<>();
	
	/**
	 * Should only be used by the database
	 */
	protected Proceedings() { }
	
	public Proceedings(String title, ConferenceEdition confEdition) {
		super(title, confEdition.getYear());
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
    	return this.conferenceEdtion;
    }

    public void setConferenceEdition(ConferenceEdition conferenceEdition) {
    	zooActivateWrite();
    	this.conferenceEdtion = conferenceEdition;
    }

    public Set<InProceedings> getPublications() {
    	zooActivateRead();
    	return Collections.unmodifiableSet(this.publications);
    }

    public void setPublications(Set<InProceedings> publications) {
    	zooActivateWrite();
    	this.publications = new HashSet<>(publications);
    }

}