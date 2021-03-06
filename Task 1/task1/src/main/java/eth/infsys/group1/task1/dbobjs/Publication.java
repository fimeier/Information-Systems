package eth.infsys.group1.task1.dbobjs;

public abstract class Publication extends DomainObject {

	private String title;
	//private List<Person> authors = new ArrayList<>(); // can be authors (InProceedings) or editors (Proceedings)
	private int year;
	private String electronicEdition;

	/**
	 * Should only be used by the database
	 */
	protected Publication() { }

	public Publication(String title, int year, String electronicEdition) {
		//title is always present
		if (title != null){
			this.title = title;}
		else {
			this.title = "";}

		this.year = year;

		if (electronicEdition != null){
			this.electronicEdition = electronicEdition;}
		else {
			this.electronicEdition = "";}
	}
	
    public String getTitle() {
    	zooActivateRead();
		return this.title;
	}

    public void setTitle(String title) {
    	zooActivateWrite();
    	this.title = title;
    }

    /*
    public List<Person> getAuthors() {
    	zooActivateRead();
		return Collections.unmodifiableList(this.authors);
	}

    public void setAuthors(List<Person> authors) {
    	zooActivateWrite();
    	this.authors = new ArrayList<>(authors);
	}*/

    public int getYear() {
    	zooActivateRead();
		return this.year;
	}

    public void setYear(int year) {
    	zooActivateWrite();
    	this.year = year;
	}

    public String getElectronicEdition() {
    	zooActivateRead();
		return this.electronicEdition;
	}

    public void setElectronicEdition(String electronicEdition) {
    	zooActivateWrite();
    	this.electronicEdition = electronicEdition;
	}
    
}