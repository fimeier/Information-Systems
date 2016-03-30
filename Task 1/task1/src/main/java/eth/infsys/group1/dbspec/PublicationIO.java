package eth.infsys.group1.dbspec;

import java.util.ArrayList;
import java.util.List;

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
		} else if (is_a_proceeding){
			ret += "Proceeding-";
		} else {
			ret += "InProceeding-";
		}


		ret += "id: "+id +"<br>";
		ret += "title: <b>"+title+"</b><br>";
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
			ret += "</ol>";

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
			ret += "Proceeding: "+ proceeding_title + " (<a href='/test/?func=inproceeding_by_id&key=" + proceeding_id + "'>"+proceeding_id+"</a>)<br>";




		}
		
		return ret;
	}		
	
}
