package eth.infsys.group1.dbspec;

public class InProceedings_simple_input {
	
	private static String[] noAuthors = new String[0];

	public String id; //key
	public String title; //title
	public int year; //conferenceEdition
	public String electronicEdition; //ee
	public String[] authors = noAuthors; //authors
	public String note = ""; //always empty
	public String pages; //pages
	public String crossref; //crossref
	public int conferenceEdition; //year-Tag!!!!!;
	public String conferenceName; //booktitle


	public InProceedings_simple_input(){
	}
	
	/*
	public InProceedings_simple_input(String id, String title, int year, String electronicEdition, List<String> authors, String note, String pages, String proceedings, int conferenceEdition, String conferenceName){
		this.id = id;
		this.title = title;
		this.year = year;
		this.electronicEdition = electronicEdition;
		this.authors = new ArrayList<String>(authors);
		this.note = note;
		this.pages = pages;
		this.proceedings = proceedings;
		this.conferenceEdition = conferenceEdition;
		this.conferenceName = conferenceName;
	}*/

}
