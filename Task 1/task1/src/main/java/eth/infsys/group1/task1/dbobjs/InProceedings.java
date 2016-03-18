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
	
	public InProceedings(String title, String electronicEdition, List<Person> authors, String note, String pages, Proceedings proc) {
    	super(title, proc.getConferenceEdition().getYear(), electronicEdition);

    	this.authors = new ArrayList<>(authors);
    	for (Person p: this.authors){
    		p.addAuthoredPublications(this);
    	}
    	
		this.note = note;
		this.pages = pages;
    	
		this.proceedings = proc;
		proc.addInProceedings(this);
		
		String id = (proc.getId() + "/" + authors.get(0).getName()).toUpperCase();
        System.out.println("calculated id in inproceedings constructor=" + this.getId());

    	this.setId(id);
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