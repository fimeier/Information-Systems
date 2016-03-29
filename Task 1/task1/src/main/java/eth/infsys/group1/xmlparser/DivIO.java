package eth.infsys.group1.xmlparser;

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;

public class DivIO {
	//type
	public Boolean is_empty = false;
	public Boolean is_a_conference = false;
	public Boolean is_a_conference_edition = false;
	public Boolean is_a_person = false;
	public Boolean is_a_publisher = false;
	public Boolean is_a_series = false;

	//object key/id
	public String id = "";

	//Conference
	public String Conference_name = "";
	public List<Pair<Integer,String>> Conference_editions_year_id = new ArrayList<>();

	//ConferenceEdition
	public String ConferenceEdition_conference_name = "";
	public String ConferenceEdition_conference_id = "";
	public int ConferenceEdition_year = 0;
	public String ConferenceEditions_proceedings_title = "";
	public String ConferenceEditions_proceedings_id = "";

	//Person
	public String Person_name = "";
	public List<Pair<String,String>> Person_authoredPublications_title_id = new ArrayList<>();
	public List<Pair<String,String>> Person_editedPublications_title_id = new ArrayList<>();

	//Publisher
	public String Publisher_name = "";
	public List<Pair<String,String>> Publisher_publications_title_id = new ArrayList<>();

	//Series
	public String Series_name = "";
	public List<Pair<String,String>> Series_publications_title_id = new ArrayList<>();


	public String get_all(){
		String ret="";

		if (is_empty){
			ret += "<br>DivIO is empty<br>";
			return ret;}
		
		//Conference ok
		else if (is_a_conference){
			ret += "Conference-id: "+id +"<br>";
			ret += "name: <b>"+Conference_name +"</b><br>";


			int show = -1;
			ret += "editions: ";
			for (Pair<Integer,String> confEdYid: Conference_editions_year_id){
				int year = confEdYid.getKey();
				String id = confEdYid.getValue();
				ret += "<a href='/test/?func=confEd_by_id&id=" + id + "'>"+year +"</a> ";
				if (--show == 0)
					break;
			}
			ret += "<br>";
		}
		
		//ConferenceEdition
		else if (is_a_conference_edition){
			ret += "ConferenceEdition-id: "+id +"<br>";
			ret += "year: <b>"+ConferenceEdition_year +"</b><br>";
			
			//ret += "<a href='/test/?func=conf_by_id&id=" +ConferenceEdition_conference_id+ "'>conference name: "+ConferenceEdition_conference_name+"</a><br>";
			ret += "conference: <a href='/test/?func=conf_by_id&id=" +ConferenceEdition_conference_id+ "'>"+ConferenceEdition_conference_name+"</a><br>";

			//ret += "<a href='/test/?func=proceeding_by_id&key=" +ConferenceEditions_proceedings_id+ "'>proceeding title: "+ConferenceEditions_proceedings_title+"</a><br>";
			ret += "Proceeding: "+ ConferenceEditions_proceedings_title + " (<a href='/test/?func=inproceeding_by_id&key=" + ConferenceEditions_proceedings_id + "'>"+ConferenceEditions_proceedings_id+"</a>)<br>";

		}

		//Person ok
		else if (is_a_person){
			ret += "Person-id: "+id +"<br>";
			ret += "name: <b>"+ Person_name +"</b><br>";
			
			int show = -1;

			
			ret += "Proceedings:";
			if (Person_editedPublications_title_id.size()==0)
				ret += " no proceedings";
			ret += "<ol type='1'>";
			for (Pair<String,String> prTid: Person_editedPublications_title_id){
				String title = prTid.getKey().substring(0, Math.min(200,prTid.getKey().length()));
				String id = prTid.getValue();
				//ret += "<a href='/test/?func=proceeding_by_id&key=" + id + "'>proceeding-id: "+id+"</a> title: "+title +"<br>";
				ret += "<li>"+title + " (<a href='/test/?func=proceeding_by_id&key=" + id + "'>"+id+"</a>)</li>";
				if (--show == 0)
					break;
			}
			ret += "</ol>";

			
			ret += "InProceedings:";
			if (Person_authoredPublications_title_id.size()==0)
				ret += " no inproceedings";
			ret += "<ol type='1'>";
			for (Pair<String,String> inprTid: Person_authoredPublications_title_id){
				String title = inprTid.getKey().substring(0, Math.min(200,inprTid.getKey().length()));
				String id = inprTid.getValue();
				//ret += "<a href='/test/?func=inproceeding_by_id&key=" + id + "'>inproceeding-id: "+id+"</a> title: "+title +"<br>";
				ret += "<li>"+title + " (<a href='/test/?func=inproceeding_by_id&key=" + id + "'>"+id+"</a>)</li>";
				if (--show == 0)
					break;
			}
			ret += "</ol>";


		}

		//Publisher ok
		else if (is_a_publisher){
			ret += "Publisher-id: "+id +"<br>";
			ret += "name: <b>"+ Publisher_name +"</b><br>";
			
			
			
			ret += "Proceedings:";
			if (Publisher_publications_title_id.size()==0)
				ret += " no proceedings";
			ret += "<ol type='1'>";
			
			int show = -1;
			for (Pair<String,String> publTid: Publisher_publications_title_id){
				String title = publTid.getKey().substring(0, Math.min(200,publTid.getKey().length()));
				String id = publTid.getValue();
				//ret += "<a href='/test/?func=publication_by_id&key=" + id + "'>(details) </a>"+title +"<br>";
				ret += "<li>"+title + " (<a href='/test/?func=proceeding_by_id&key=" + id + "'>"+id+"</a>)</li>";

				if (--show == 0)
					break;
			}
			ret += "</ol>";


		}

		//Series ok
		else if (is_a_series){
			ret += "Series-id: "+id +"<br>";
			ret += "name: <b>"+ Series_name +"</b><br>";
			
			ret += "Proceedings:";
			if (Series_publications_title_id.size()==0)
				ret += " no proceedings";
			ret += "<ol type='1'>";
			int show = -1;
			for (Pair<String,String> publTid: Series_publications_title_id){
				String title = publTid.getKey().substring(0, Math.min(200,publTid.getKey().length()));
				String id = publTid.getValue();
				//ret += "<a href='/test/?func=publication_by_id&key=" + id + "'>(details) </a>"+title +"<br>";
				ret += "<li>"+title + " (<a href='/test/?func=proceeding_by_id&key=" + id + "'>"+id+"</a>)</li>";
				if (--show == 0)
					break;
			}
			ret += "</ol>";

			

		}


		return ret;		
	}







}
