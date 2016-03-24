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
			ret += "DivIO is empty<br>";
			return ret;}
		
		//Conference ok
		else if (is_a_conference){
			ret += "<Conference><br>";
			ret += "id: "+id +"<br>";
			ret += "name: "+Conference_name +"<br>";


			int show = -1;
			for (Pair<Integer,String> confEdYid: Conference_editions_year_id){
				int year = confEdYid.getKey();
				String id = confEdYid.getValue();
				ret += "<a href='/test/?func=confEd_by_id&id=" + id + "'>conference edition-id: "+id+"</a> year: "+year +"<br>";
				if (--show == 0)
					break;
			}
		}
		
		//ConferenceEdition
		else if (is_a_conference_edition){
			ret += "<ConferenceEdition><br>";
			ret += "id: "+id +"<br>";
			ret += "year: "+ConferenceEdition_year +"<br>";
			ret += "<a href='/test/?func=conf_by_id&id=" +ConferenceEdition_conference_id+ "'>conference name: "+ConferenceEdition_conference_name+"</a><br>";
			ret += "<a href='/test/?func=proceeding_by_id&key=" +ConferenceEditions_proceedings_id+ "'>proceeding title: "+ConferenceEditions_proceedings_title+"</a><br>";

		}

		//Person ok
		else if (is_a_person){
			ret += "<Person><br>";
			ret += "id: "+id +"<br>";
			ret += "name: "+ Person_name +"<br>";
			
			int show = -1;
			for (Pair<String,String> inprTid: Person_authoredPublications_title_id){
				String title = inprTid.getKey().substring(0, Math.min(200,inprTid.getKey().length()));
				String id = inprTid.getValue();
				ret += "<a href='/test/?func=inproceeding_by_id&key=" + id + "'>inproceeding-id: "+id+"</a> title: "+title +"<br>";
				if (--show == 0)
					break;
			}
			
			for (Pair<String,String> prTid: Person_editedPublications_title_id){
				String title = prTid.getKey().substring(0, Math.min(200,prTid.getKey().length()));
				String id = prTid.getValue();
				ret += "<a href='/test/?func=proceeding_by_id&key=" + id + "'>proceeding-id: "+id+"</a> title: "+title +"<br>";
				if (--show == 0)
					break;
			}

		}

		else if (is_a_publisher){
			ret += "<Publisher><br>";
			ret += "id: "+id +"<br>";

		}


		else if (is_a_series){
			ret += "<Series><br>";
			ret += "id: "+id +"<br>";

		}




		return ret;		
	}







}
