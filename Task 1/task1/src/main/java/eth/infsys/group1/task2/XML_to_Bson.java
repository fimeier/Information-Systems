package eth.infsys.group1.task2;


import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import eth.infsys.group1.dbspec.InProceedings_simple_input;
import eth.infsys.group1.dbspec.Proceedings_simple_input;

public class XML_to_Bson {


	static Document Proceedings_to_Bson_Document (Proceedings_simple_input proc){

		Document document = new Document();
		
		document.append("_id", (String) proc.id);
		
		
		document.append("title", (String) proc.title);
		document.append("year", (int) proc.year);
		document.append("electronicEdition", (String) proc.electronicEdition );
		
		//Editors
		List<Document> editors = new ArrayList<Document>();
		for (String edit: proc.editors){
			editors.add(new Document("editor_id", T2DBProvider.calculate_person_id(edit))
					.append("name", edit));
		}
		if (editors.size()!=0)
			document.append("editors", editors);
		
		if (proc.note!=null && !proc.note.isEmpty() )
			document.append("note", (String) proc.note );
		
		if (proc.number!=0)
			document.append("number", (int) proc.number );
		
		//Publisher
		if (proc.publisher!=null && !proc.publisher.isEmpty() )
			document.append("publisher_id", T2DBProvider.calculate_publisher_id(proc.publisher) );
			document.append("publisher_name", (String) proc.publisher );
		
		if (proc.volume!=null && !proc.volume.isEmpty() )
			document.append("volume", (String) proc.volume );
		
		if (proc.isbn!=null && !proc.isbn.isEmpty() )
			document.append("isbn", (String) proc.isbn );
		
		//Series
		if (proc.series!=null && !proc.series.isEmpty() ){
			document.append("series_id",(String) T2DBProvider.calculate_series_id(proc.series));
			document.append("series_name",(String) proc.series);
		}
		
		//Conference
		document.append("conference_id", T2DBProvider.calculate_conference_id(proc.conferenceName));
		document.append("conference_name", proc.conferenceName);
		
		//ConferenceEdition
		//document.append("conferenceEdition_id", T2DBProvider.calculate_conferenceEdition_id(proc.conferenceName, proc.conferenceEdition));
		document.append("conferenceEdition", (int) proc.conferenceEdition);

		return document;
	}
	
	
	static Document InProceedings_to_Bson_Document (InProceedings_simple_input inproc){

		Document document = new Document();
		
		document.append("inproceedings_id", (String) inproc.id);
		
		
		document.append("inproceedings_title", (String) inproc.title);
		document.append("year", (int) inproc.year);
		document.append("electronicEdition", (String) inproc.electronicEdition );
		
		//Authors
		List<Document> authors = new ArrayList<Document>();
		for (String auth: inproc.authors){
			authors.add(new Document("author_id", T2DBProvider.calculate_person_id(auth))
					.append("name", auth));
		}
		if (authors.size()!=0)
			document.append("authors", authors);
		
		if (inproc.note!=null && !inproc.note.isEmpty() )
			document.append("note", (String) inproc.note );
		
		if (inproc.pages!=null && !inproc.pages.isEmpty() )
			document.append("pages", (String) inproc.pages);

		return document;
	}
	
	
	/*
	@SuppressWarnings("restriction")
	static Document Person_to_Bson_Document (DivIO pers){
		
		if (pers.is_a_person != true)
			System.err.println("pers.is_a_person != true");
						

		Document document = new Document();
		
		document.append("_id", (String) pers.id);
		
		document.append("name", (String) pers.Person_name);
		
		//Authors
		List<Document> authoredPublications = new ArrayList<Document>();
		for (javafx.util.Pair<String, String> authored: pers.Person_authoredPublications_title_id){
			String title = authored.getKey();
			String id = authored.getValue();
			authoredPublications.add(
					new Document("inproceeding_id", (String) id)
					.append("title", (String) title));
		}
		if (authoredPublications.size()!=0)
			document.append("authoredPublications", authoredPublications);

		//Editors
		List<Document> editoredPublications = new ArrayList<Document>();
		for (javafx.util.Pair<String, String> editored: pers.Person_editedPublications_title_id){
			String title = editored.getKey();
			String id = editored.getValue();
			authoredPublications.add(
					new Document("proceeding_id", (String) id)
					.append("title", (String) title));
		}
		if (editoredPublications.size()!=0)
			document.append("editoredPublications", editoredPublications);


		return document;
	}*/
	

}
