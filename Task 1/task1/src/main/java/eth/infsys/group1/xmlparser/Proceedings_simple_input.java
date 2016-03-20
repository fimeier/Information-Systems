package eth.infsys.group1.xmlparser;

public class Proceedings_simple_input {

	private static String[] noEditors = new String[0];


	public String id; //key
	public String title; //title
	public String[] editors = noEditors; //editors
	public int year; //publication date proceeding (not conferenceEdition)
	public String electronicEdition; //ee
	public String note;//
	public int number; //number
	public String publisher; //publisher
	public String volume; //volume
	public String isbn; //isbn
	public String series; //series
	public int conferenceEdition; //year from inproceedings
	public String conferenceName; //booktitle from inproceedings

	public Proceedings_simple_input(){
	}

	/*
    public Proceedings_simple_input(String id, String title, Set<String> editors, int year, String electronicEdition, String note, int number, String publisher, String volume, String isbn, String series, int conferenceEdition, String conferenceName){
    	this.id = id;
    	this.title = title;
    	this.editors = editors;
    	this.year = year;
    	this.electronicEdition = electronicEdition;
    	this. note = note;
    	this.number = number;
    	this.publisher = publisher;
    	this.volume = volume;
    	this.isbn = isbn;
    	this.conferenceEdition = conferenceEdition;
    	this. conferenceName = conferenceName;    	
    }*/
}
