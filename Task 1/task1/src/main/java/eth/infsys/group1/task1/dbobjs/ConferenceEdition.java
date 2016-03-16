package eth.infsys.group1.task1.dbobjs;

public class ConferenceEdition extends DomainObject {

    private Conference conference;
	private int year;
	private Proceedings proceedings;
	
	/**
	 * Should only be used by the database
	 */
	protected ConferenceEdition() { }
	
	public ConferenceEdition(Conference conference, int year) {
		this.conference = conference;
		this.year = year;
	}

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