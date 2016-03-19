package eth.infsys.group1.task1.dbobjs;

import java.util.ArrayList;
import java.util.List;

public class InProceedings_simple_input {

	public String id;
	public String title;
	public int year;
	public String electronicEdition;
	public List<String> authors = new ArrayList<String>();
	public String note;
	public String pages;
	public String proceedings;
	public int conferenceEdition;
	public String conferenceName;


	public InProceedings_simple_input(){
		
	}
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
	}

}
