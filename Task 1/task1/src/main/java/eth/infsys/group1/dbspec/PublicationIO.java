package eth.infsys.group1.dbspec;


import javax.validation.constraints.*;


import java.util.ArrayList;
import java.util.List;

import eth.infsys.group1.task1.dbobjs.Conference;
import eth.infsys.group1.task1.dbobjs.ConferenceEdition;
import eth.infsys.group1.task1.dbobjs.Person;
import eth.infsys.group1.task1.dbobjs.Publisher;
import eth.infsys.group1.task1.dbobjs.Series;
import javafx.util.Pair;

@SuppressWarnings("restriction")
public class PublicationIO {

	//type
	public Boolean is_empty = false;
	public Boolean is_a_proceeding = false;
	public Boolean is_an_inproceeding = false;
	
	//object key/id
	public String id = "";
	
	//publication
	public String title= "";
	public int year = 0;
	public String electronicEdition= "";
	
	//proceedings
	public List<Pair<String,String>> editors_name_id = new ArrayList<>();

	public String note = "";
	public int number = 0;
	public String publisher_name = "";
	public String publisher_id = "";
	public String volume = "";
	public String isbn = "";
	public String series_name = "";
	public String series_id = "";

	
	public List<Pair<String,String>> inproceedings_title_id = new ArrayList<>();
	
	//conference
	public Pair<String,String> Conference_name_id = new Pair<String, String>("","");
	public Pair<Integer,String> ConferenceEdition_year_id = new Pair<Integer, String>(0,"");
	
	//conference new - Batchloader
	public String Conference_name = "";
	public String Conference_id = "";
	public int ConferenceEdition_year = 0;
	public String ConferenceEdition_id = "";

	

	//inproceedings
	public List<Pair<String,String>> authors_name_id = new ArrayList<>();

	//already defined String note; Interface is wrong...
	public String pages = "";
	public String proceeding_title = "";
	public String proceeding_id = "";

	
	
	public String get_all(){
		String ret="";
		
		if (is_empty){
			ret += "Publication is empty<br>";
			return ret;
		} /*else if (is_a_proceeding){
			ret += "Proceeding-";
		} else {
			ret += "InProceeding-";
		}*/

		ret += "<h3>"+title+"</h3>";
		ret += "id: "+id +"<br>";
		ret += "year: "+year+"<br>";
	
		ret += "ee: <a href='"+electronicEdition+"'>"+electronicEdition+"</a><br>";
		
		ret += "conference: <a href='/test/?func=conf_by_id&id=" +Conference_name_id.getValue()+ "'>"+Conference_name_id.getKey()+"</a><br>";
		
		ret += "conference-edition: <a href='/test/?func=confEd_by_id&id=" +ConferenceEdition_year_id.getValue()+ "'>"+ConferenceEdition_year_id.getKey()+"</a><br>";


		
		
		if ( is_a_proceeding ){
			
			String edi = "";
			for (Pair<String,String> editor: editors_name_id){
				String name = editor.getKey();
				String id = editor.getValue();
				edi += "<a href='/test/?func=person_by_id&id=" +id+ "'>"+name+"</a>, ";
			}
			ret += "editors: " + edi +"<br>";
			
			if (note==null)
				note = "-";
			ret += "note: " + note+"<br>";
			ret += "number: " + number+"<br>";
			
			ret += "publisher: <a href='/test/?func=publisher_by_id&id=" +publisher_id+ "'>"+publisher_name+"</a><br>";
			ret += "isbn: "+isbn+"<br>";

			ret += "volume: " + volume+"<br>";
			
			//ret += "series-name: " + series_name+"<br>";
			//ret += "series-id: " + series_id+"<br>";
			
			ret += "series: <a href='/test/?func=series_by_id&id=" +series_id+ "'>"+series_name+"</a><br>";

			ret += "InProceedings:<br>";
			ret += "<ol type='1'>";
			int show = -1;
			for (Pair<String,String> inprTid: inproceedings_title_id){
				String title = inprTid.getKey().substring(0, Math.min(300,inprTid.getKey().length()));
				String id = inprTid.getValue();
				//ret += "(id="+id+") : "+title +"<br>";
				ret += "<li>"+title + " (<a href='/test/?func=inproceeding_by_id&key=" + id + "'>"+id+"</a>)</li>";
				if (--show == 0)
					break;
			}
			ret += "</ol><hr>";

		}
		else
		{
			String aut = "";
			for (Pair<String,String> author: authors_name_id){
				String name = author.getKey();
				String id = author.getValue();
				aut += "<a href='/test/?func=person_by_id&id=" +id+ "'>"+name+"</a>, ";
			}
			ret += "authors: " + aut +"<br>";
			
			if (note==null)
				note = "-";
			ret += "note: " + note +"<br>";
			
			ret += "pages: " + pages +"<br>";
			//ret += "proceeding-title: " + proceeding_title +"<br>";			
			//ret += "proceeding-id: <a href=/test/?func=proceeding_by_id&key=" + proceeding_id +">" + proceeding_id + "</a><br>";
			ret += "Proceeding: "+ proceeding_title + " (<a href='/test/?func=proceeding_by_id&key=" + proceeding_id + "'>"+proceeding_id+"</a>)<br>";
			ret += "<hr>";
		}
		
		return ret;
	}
	public PublicationIO(){};
	
	public PublicationIO(Proceedings_simple_input input){
		//type
		this.is_a_proceeding = true;
		
		//object key/id
		this.id = input.id;
		
		//publication
		this.title= input.title;
		this.year = input.year;
		
		this.electronicEdition= input.electronicEdition;
		if (input.electronicEdition != null){
			this.electronicEdition= input.electronicEdition;}
		else {
			this.electronicEdition = "";}
		
		//Editors
		for (String editor: input.editors){
			String name = editor;
			String id = Person.calculate_person_id(editor);
			this.editors_name_id.add(new Pair<String, String>(name, id));
		}

		if (input.note != null){
			this.note = input.note;}
		else {
			this.note = "";}
		
		this.number = input.number;

		if (input.publisher != null){
			this.publisher_name = input.publisher;
			this.publisher_id = Publisher.calculate_publisher_id(this.publisher_name);
		}
		else {
			this.publisher_name = "null";
			this.publisher_id = Publisher.calculate_publisher_id("null");
		}
		
		if (input.volume != null){
			this.volume = input.volume;}
		else {
			this.volume = "";}
		
		if (input.isbn != null){
			this.isbn = input.isbn;}
		else {
			this.isbn = "";}

		if (input.series!=null){
			this.series_name = input.series;
			this.series_id = Series.calculate_series_id(this.series_name);
		}
		else {
			this.series_name = "null";
			this.series_id = Series.calculate_series_id("null");
		}

		
		//inproceedings_title_id = new ArrayList<>();
		
		//conference
		this.Conference_name = input.conferenceName;
		this.Conference_id = Conference.calculate_conference_id(input.conferenceName);
		this.ConferenceEdition_year = input.conferenceEdition;
		this.ConferenceEdition_id = ConferenceEdition.calculate_conferenceEdition_id(input.conferenceName, input.conferenceEdition);

		Conference_name_id = new Pair<String, String>(this.Conference_name,this.Conference_id);
		ConferenceEdition_year_id = new Pair<Integer, String>(this.ConferenceEdition_year,this.ConferenceEdition_id);
		
		
	}
	public PublicationIO(InProceedings_simple_input input){
		//type
		this.is_an_inproceeding = true;

		//object key/id
		this.id = input.id;

		//publication
		this.title= input.title;
		this.year = input.year;

		this.electronicEdition= input.electronicEdition;
		if (input.electronicEdition != null){
			this.electronicEdition= input.electronicEdition;}
		else {
			this.electronicEdition = "";}

		//Authors
		for (String author: input.authors){
			String name = author;
			String id = Person.calculate_person_id(author);
			this.authors_name_id.add(new Pair<String, String>(name, id));
		}

		if (input.note != null){
			this.note = input.note;}
		else {
			this.note = "";}
		

		if (input.pages != null){
			this.pages = input.pages;}
		else {
			this.pages = "";}
		
		this.proceeding_id = input.crossref;
	}
	
}
