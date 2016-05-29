package eth.infsys.group1.dbspec;

import java.util.HashMap;

import javax.validation.constraints.NotNull;

import eth.infsys.webserver.ValidProceedingsId;
import eth.infsys.webserver.ValidProceedingsNote;
import eth.infsys.webserver.ValidYear;


@ValidProceedingsId
@ValidProceedingsNote
public class Proceedings_simple_input {
	
	public String mode = "";
	public DBProvider myDB;


	private static String[] noEditors = new String[0];

	
	public String id;

	
	@NotNull(message="The attribute Publication.title must be of type string and must not be null.")
	public String title; //title
	
	@ValidYear
	public int year; //publication date proceedings (not conferenceEdition)
	public String electronicEdition; //ee


	public String[] editors = noEditors; //editors_name
	public String note;
	public int number; //number
	public String publisher; //publisher
	public String volume; //volume
	public String isbn; //isbn
	public String series; //series_name

	
	public int conferenceEdition; //year from inproceedings
	public String conferenceName; //booktitle from inproceedings

	//no inproceedings here....


	//empty constructor
	public Proceedings_simple_input(){
	}
	
	//constructor for Webserver
		public Proceedings_simple_input(HashMap<String, String> args, String mode, DBProvider myDB){

			this.mode = mode;
			this.myDB = myDB;

			
			if (args.containsKey("id")){
				id=args.get("id");
			} else {
				id=null;
			}

			if (args.containsKey("title")){
				title=args.get("title");
			} else {
				title=null;
			}

			if (args.containsKey("year")){
				try {
					year=Integer.parseInt(args.get("year"));					
				}
				catch (NumberFormatException e) {
					year = 0;
				}
			} else {
				year=0;
			}

			if (args.containsKey("ee")){
				electronicEdition=args.get("ee");
			} else {
				electronicEdition=null;
			}
			
			if (args.containsKey("conference")){
				conferenceName=args.get("conference");
			} else {
				conferenceName=null;
			}

			if (args.containsKey("conference-edition")){

				try {
					conferenceEdition=Integer.parseInt(args.get("conference-edition"));				
				}
				catch (NumberFormatException e) {
					conferenceEdition = 0;
				}
			} 
			else {
				conferenceEdition=0;
			}

			if (args.containsKey("editors")){

				String temp = args.get("editors");
				int length = temp.split(";").length;
				editors = new String[length];
				for (int i = 0; i<length; i++){
					editors[i] = temp.split(";")[i];				
				}
			} else {
				editors=noEditors;
			}

			if (args.containsKey("note")){
				note=args.get("note");
			} else {
				note=null;
			}

			if (args.containsKey("number")){
				try {
					number=Integer.parseInt(args.get("number"));
				}
				catch (NumberFormatException e) {
					number = 0;
				}
			} else {
				number=0;
			}

			if (args.containsKey("publisher")){
				publisher=args.get("publisher");
			} else {
				publisher=null;
			}
			
			if (args.containsKey("isbn")){
				isbn=args.get("isbn");
			} else {
				isbn=null;
			}
			
			/**update-mode: get current isbn and create note
			 * 
			 */
			if ( mode.equals("update")){
				String currentIsbn = myDB.IO_get_isbn_by_proc_id(id);
				if (!currentIsbn.equals(isbn)){
					this.note = "ISBN updated, old value was "+currentIsbn;	
				}
			}
			
			if (args.containsKey("volume")){
				volume=args.get("volume");
			} else {
				volume=null;
			}
			
			if (args.containsKey("series")){
				series=args.get("series");
			} else {
				series=null;
			}
			
			//add no inproceedings

		}

}
