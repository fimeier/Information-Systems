package eth.infsys.group1.task1.dbobjs;

import java.util.HashSet;
import java.util.Set;

public class Proceedings_simple_input {
    public String id;
    public String title;
    public Set<String> editors = new HashSet<String>();
    public int year;
    public String electronicEdition;
    public String note;
    public int number;
    public String publisher;
    public String volume;
    public String isbn;
    public String series;
    public int conferenceEdition;
    public String conferenceName;
    
    public Proceedings_simple_input(){
    	
    }
    
    public Proceedings_simple_input(String id, String title, Set<String> editors, int year, String electronicEdition, String note, int number, String publisher, String volume, String isbn, String series, int conferenceEdition, String conferenceName){
    	this.id = id;
    	this.title = title;
    	this.editors = new HashSet<String>(editors);
    	this.year = year;
    	this.electronicEdition = electronicEdition;
    	this. note = note;
    	this.number = number;
    	this.publisher = publisher;
    	this.volume = volume;
    	this.isbn = isbn;
    	this.conferenceEdition = conferenceEdition;
    	this. conferenceName = conferenceName;    	
    }
}
