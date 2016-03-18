package eth.infsys.group1.task1.dbobjs;

import java.util.ArrayList;
import java.util.List;

public class InProceedings extends Publication {
	
	private List<Person> authors = new ArrayList<>();
	
	private String note;
	private String pages;
	private Proceedings proceedings;

	/**
	 * Should only be used by the database
	 */
	public InProceedings() { }
	
	public InProceedings(Proceedings proceedings, String pages, String note) {
		this.proceedings = proceedings;
		this.pages = pages;
		this.note = note;
	}

    public String getNote() {
    	zooActivateRead();
		return this.note;
	}

    public void setNote(String note) {
    	zooActivateWrite();
    	this.note = note;
	}

    public String getPages() {
		zooActivateRead();
		return this.pages;
	}

    public void setPages(String pages) {
    	zooActivateWrite();
    	this.pages = pages;
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