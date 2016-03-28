package eth.infsys.group1.task1.dbobjs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import eth.infsys.group1.xmlparser.InProceedings_simple_input;

public class InProceedings extends Publication {
	
	private List<Person> authors = new ArrayList<>();
	
	private String note;
	private String pages;
	private Proceedings proceedings;

	/**
	 * Should only be used by the database
	 */
	public InProceedings() { }

	public InProceedings(InProceedings_simple_input args, List<Person> authors, Proceedings proc) {
    	super(args.title, args.year, args.electronicEdition);

    	this.authors = new ArrayList<>(authors);
    	for (Person p: this.authors){
    		p.addAuthoredPublications(this);
    	}
    	
    	if (note != null){
			this.note = args.note;}
		else {
			this.note = "";}
    	
    	if (pages != null){
    		this.pages = args.pages;}
		else {
			this.pages = "";}
    	
    	
		this.proceedings = proc;
		proc.addInProceedings(this);
		
    	this.setId(args.id);
	}
	
	//dummy.. always us key from xml
	//example "conf/uist/Binding88"
	static public String calculate_Inproceedings_id(String key){
		return key;
	}
	
	public Boolean removeAuthor(Person per){
    	zooActivateWrite();
    	return authors.remove(per);
	}
	
	public List<Person> getAuthors() {
		zooActivateRead();
		return Collections.unmodifiableList(this.authors);
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