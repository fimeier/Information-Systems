package eth.infsys.group1.dbspec;

import java.util.HashMap;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import eth.infsys.webserver.ValidInproceedingsId;
import eth.infsys.webserver.ValidInproceedingsProcId;
import eth.infsys.webserver.ValidYear;

@ValidInproceedingsId
@ValidInproceedingsProcId
public class InProceedings_simple_input{
	
	public String mode = "";
	public DBProvider myDB;

	private static String[] noAuthors = new String[0];

	
	public String id;

	
	@NotNull(message="The attribute Publication.title must be of type string and must not be null.")
	public String title;
	
	@ValidYear
	public int year; // => conferenceEdition
	public String electronicEdition; //ee


	@Size(min=1, max=1000, message="There exists at least one author for each publication")
	public String[] authors = noAuthors; //authors

	@NotNull
	@Pattern(regexp = "Draft|Submitted|Accepted|Published")
	public String note = ""; //always empty

	@Pattern(regexp = "\\d+-\\d+|\\d+", message="The attribute InProceedings.page’ must match to one of the following three patterns: &lt;Integer&gt; (e.g.750), &lt;Integer&gt;-&lt;Integer&gt; (e.g. 750-757) or null")
	public String pages; //pages

	public String crossref; //crossref => proceedings

	/**conference
	 * 
	 */
	public String conferenceName; //booktitle
	public int conferenceEdition; //not in use; like year-Tag!!!!!;


	//empty constructor
	public InProceedings_simple_input(){
	}

	//constructor for Webserver
	public InProceedings_simple_input(HashMap<String, String> args, String mode, DBProvider myDB){

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
		conferenceEdition=year; //not in use; like year-Tag!!!!!;

		if (args.containsKey("ee")){
			electronicEdition=args.get("ee");
		} else {
			electronicEdition=null;
		}

		if (args.containsKey("authors")){

			String temp = args.get("authors");
			int length = temp.split(";").length;
			authors = new String[length];
			for (int i = 0; i<length; i++){
				authors[i] = temp.split(";")[i];				
			}
		} else {
			authors=noAuthors;
		}

		if (args.containsKey("note")){
			note=args.get("note");
		} else {
			note=null;
		}

		if (args.containsKey("pages")){
			pages=args.get("pages");
		} else {
			pages=null;
		}

		if (args.containsKey("proc_id")){
			crossref=args.get("proc_id");
		} else {
			crossref=null;
		}

		if (args.containsKey("conference")){
			conferenceName=args.get("conference");
		} else {
			conferenceName=null;
		}


	}

}
