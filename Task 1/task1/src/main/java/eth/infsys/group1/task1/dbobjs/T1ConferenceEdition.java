package eth.infsys.group1.task1.dbobjs;

import ch.ethz.globis.isk.domain.*;

/**
 * Implementation of the {@link ch.ethz.globis.isk.domain.ConferenceEdition} interface for ZooDB.
 */
public class T1ConferenceEdition extends T1DomainObject implements ConferenceEdition {

    private Conference conference;
	private int year;
	private Proceedings proceedings;

	public T1ConferenceEdition() { }

	public Conference getConference() {
		zooActivateRead();
		return this.conference;
	}

    public void setConference(Conference conference) {
    	zooActivateWrite();
    	this.conference = conference;
	}

    public int getYear() {
		zooActivateRead();
		return this.year;
	}

    public void setYear(int year) {
    	zooActivateWrite();
    	this.year = year;
	}

    public Proceedings getProceedings() {
		zooActivateRead();
		return this.proceedings;
	}

    public void setProceedings(Proceedings proceedings) {
    	zooActivateWrite();
    	this.proceedings = proceedings;
	}
    
}