package eth.infsys.group1.xmlparser;

import java.util.ArrayList;
import java.util.List;
import eth.infsys.group1.dbspec.*;

import javafx.util.Pair;

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
	public List<String> editors = new ArrayList<>();
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
	public String conferenceName = "";
	public int conferenceEdition = 0;

	//inproceedings
	//public List<String> authors = new ArrayList<>();
	public List<Pair<String,String>> authors_name_id = new ArrayList<>();

	//already defined String note; Interface is wrong...
	public String pages = "";
	public String proceeding_title = "";
	public String proceeding_id = "";

	/*
	public void print_all(){
		if (is_empty){
			System.out.println("Publication is empty");
			return;
		} else if (is_a_proceeding){
			System.out.println("<Proceeding>");
		} else {
			System.out.println("<InProceeding>");
		}


		System.out.println("id: "+id);
		System.out.println("title: "+title);
		System.out.println("year: "+year);
		System.out.println("ee: "+electronicEdition);
		System.out.println("conferenceName: "+conferenceName);
		System.out.println("conferenceEdition: "+conferenceEdition);
		
		
		if ( is_a_proceeding ){
			String edi = "";
			for (String editor: editors){
				edi = edi + editor + ", ";
			}
			System.out.println("editors: " + edi);
			
			System.out.println("note: " + note);
			System.out.println("number: " + number);
			System.out.println("publisher-name: " + publisher_name);
			System.out.println("publisher-id: " + publisher_id);
			System.out.println("volume: " + volume);
			System.out.println("series-name: " + series_name);
			System.out.println("series-id: " + series_id);
			System.out.println("inproceedings: ");
			int show = 5;
			for (Pair<String,String> inprTid: inproceedings_title_id){
				String title = inprTid.getKey().substring(0, Math.min(50,inprTid.getKey().length()));
				String id = inprTid.getValue();
				System.out.println("(id="+id+") : "+title);
				if (--show == 0)
					break;
			}		

		}
		else
		{
			String aut = "";
			for (String author: authors){
				aut = aut + author + ", ";
			}
			System.out.println("authors: " + aut);
			System.out.println("note: " + note);
			System.out.println("pages: " + pages);
			System.out.println("proceeding-title: " + proceeding_title);
			System.out.println("proceeding-id: " + proceeding_id);


		}


	}*/
	
	public String get_all(){
		String ret="";
		
		if (is_empty){
			ret += "Publication is empty<br>";
			return ret;
		} else if (is_a_proceeding){
			ret += "<Proceeding><br>";
		} else {
			ret += "<InProceeding><br>";
		}


		ret += "id: "+id +"<br>";
		ret += "title: "+title+"<br>";
		ret += "year: "+year+"<br>";
		ret += "ee: "+electronicEdition+"<br>";
		ret += "conferenceName: "+conferenceName+"<br>";
		ret += "conferenceEdition: "+conferenceEdition+"<br>";
		
		
		if ( is_a_proceeding ){
			String edi = "";
			for (String editor: editors){
				edi = edi + editor + ", ";
			}
			ret += "editors: " + edi+"<br>";
			
			ret += "note: " + note+"<br>";
			ret += "number: " + number+"<br>";
			ret += "publisher-name: " + publisher_name+"<br>";
			ret += "publisher-id: " + publisher_id+"<br>";
			ret += "volume: " + volume+"<br>";
			ret += "series-name: " + series_name+"<br>";
			ret += "series-id: " + series_id+"<br>";
			ret += "inproceedings:<br>";
			int show = -1;
			for (Pair<String,String> inprTid: inproceedings_title_id){
				String title = inprTid.getKey().substring(0, Math.min(200,inprTid.getKey().length()));
				String id = inprTid.getValue();
				//ret += "(id="+id+") : "+title +"<br>";
				ret += "<a href='/test/?func=inproceeding_by_id&key=" + id + "'>inproceeding-id: "+id+"</a> title: "+title +"<br>";
				if (--show == 0)
					break;
			}		

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

			ret += "note: " + note +"<br>";
			ret += "pages: " + pages +"<br>";
			ret += "proceeding-title: " + proceeding_title +"<br>";
			//ret += "proceeding-id: " + proceeding_id +"<br>";
			ret += "<a href=/test/?func=proceeding_by_id&key=" + proceeding_id +">proceeding-id: " + proceeding_id + "</a><br>";



		}
		
		return ret;
	}		
	
}
