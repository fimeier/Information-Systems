package eth.infsys.group1.task3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.stream.XMLStreamReader;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQItemType;
import javax.xml.xquery.XQResultSequence;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.Document;
import org.bson.json.JsonReader;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import eth.infsys.group1.dbspec.DBProvider;
import eth.infsys.group1.dbspec.DivIO;
import eth.infsys.group1.dbspec.InProceedings_simple_input;
import eth.infsys.group1.dbspec.Proceedings_simple_input;
import eth.infsys.group1.dbspec.PublicationIO;
import eth.infsys.group1.dbspec.WebFunc;
import javafx.util.Pair;
import net.xqj.basex.BaseXXQDataSource;

public class T3DBProvider extends DBProvider {



	/**
	 * Class and DB-Stuff
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	private XQDataSource xqs;
	private XQConnection conn;
	private String dbName;


	/**
	 * Create DBProvider
	 * 
	 * @param dbName the name of the database to be created
	 * @param mode OPEN_DB_APPEND or OPEN_DB_OVERRIDE
	 * @throws XQException 
	 */
	public T3DBProvider(String dbName, String User, String PW) throws XQException {
		this.dbName = dbName;
		this.xqs = new BaseXXQDataSource();
		xqs.setProperty("serverName", "localhost");
		xqs.setProperty("port", "1984");
		this.conn = xqs.getConnection(User, PW);		
	}

	public void testx() throws XQException{
		// Database XQueries and Updates performed here (covered later!)
		String xqueryString = "";
		xqueryString = "for $x in doc('"+dbName+"')//inproceedings return $x/name";
		xqueryString = "for $inproc in doc('"+dbName+"')//inproceedings[@key = 'conf/iscas/YanCLH06' ]" +
				"return $inproc/author";


		XQExpression xqe = conn.createExpression();
		XQResultSequence rs = xqe.executeQuery(xqueryString);
		int max = 10;
		while(rs.next() && max >0){
			System.out.println(rs.getItemAsString(null));
			max--;
		}

	}


	@Override
	protected void createDB(String dbName, int mode) {
		// TODO Auto-generated method stub

	}

	/**
	 * ID-generator-Methods
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	public static String calculate_person_id(String name){
		return "person/" + name;
	}

	public static String calculate_conference_id(String conferenceName){
		return "conference/" + conferenceName;
	}

	public static String calculate_conferenceEdition_id(String conferenceName, int conferenceEdition){
		return "conferenceedition/" + conferenceName + "/" + conferenceEdition;
	}

	public static String calculate_publisher_id(String name){
		return "publisher/" + name;
	}

	protected static String calculate_series_id(String name){
		return "series/" + name;
	}



	@Override
	public String batch_createProceedings(Proceedings_simple_input data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String batch_createInProceedings(InProceedings_simple_input data) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * Helper-Methods
	 * @throws XQException 
	 * 
	 * 
	 * 
	 * 
	 */



	private XQResultSequence get_proceedings_by_filter_offset(String filter, int boff, int eoff, String order_by) throws XQException {
		String xqueryString = "";
		switch(order_by){
		case "title ascending":
			//xqueryString = "for $proc in doc('"+dbName+"')//proceedings where contains($proc/title,'"+filter+"') order by $proc/title ascending let $proc_id := $proc/@key let $inproc := doc('"+dbName+"')//inproceedings[crossref =  $proc_id] return (<proceedings confEd='{$inproc[1]/year}'> {$proc/(@*, *)} {for $x in $inproc order by $x/title return <inproc_key_title key='{$x/@key}' title='{$x/title}'></inproc_key_title>} </proceedings>)";
			xqueryString = "let $all_procs := for $proc in doc('"+dbName+"')//proceedings where contains(upper-case($proc/title),upper-case('"+filter+"')) order by $proc/title ascending return $proc for $i in ("+boff+" to "+eoff+") let $proc := $all_procs[$i] where $proc != '' let $proc_id := $proc/@key let $inproc := doc('"+dbName+"')//inproceedings[crossref =  $proc_id] return (<proceedings confEd='{$inproc[1]/year}'> {$proc/(@*, *)} {for $x in $inproc order by $x/title return <inproc_key_title key='{$x/@key}' title='{$x/title}'></inproc_key_title>} </proceedings>)";
			break;
		case "title descending":
			xqueryString = "let $all_procs := for $proc in doc('"+dbName+"')//proceedings where contains(upper-case($proc/title),upper-case('"+filter+"')) order by $proc/title descending return $proc for $i in ("+boff+" to "+eoff+") let $proc := $all_procs[$i] where $proc != '' let $proc_id := $proc/@key let $inproc := doc('"+dbName+"')//inproceedings[crossref =  $proc_id] return (<proceedings confEd='{$inproc[1]/year}'> {$proc/(@*, *)} {for $x in $inproc order by $x/title return <inproc_key_title key='{$x/@key}' title='{$x/title}'></inproc_key_title>} </proceedings>)";
			break;
		case "year ascending":
			//xqueryString = "for $proc in doc('"+dbName+"')//proceedings where contains($proc/title,'"+filter+"') order by $proc/title ascending let $proc_id := $proc/@key let $inproc := doc('"+dbName+"')//inproceedings[crossref =  $proc_id] return (<proceedings confEd='{$inproc[1]/year}'> {$proc/(@*, *)} {for $x in $inproc order by $x/title return <inproc_key_title key='{$x/@key}' title='{$x/title}'></inproc_key_title>} </proceedings>)";
			xqueryString = "let $all_procs := for $proc in doc('"+dbName+"')//proceedings where contains(upper-case($proc/title),upper-case('"+filter+"')) order by $proc/year ascending return $proc for $i in ("+boff+" to "+eoff+") let $proc := $all_procs[$i] where $proc != '' let $proc_id := $proc/@key let $inproc := doc('"+dbName+"')//inproceedings[crossref =  $proc_id] return (<proceedings confEd='{$inproc[1]/year}'> {$proc/(@*, *)} {for $x in $inproc order by $x/title return <inproc_key_title key='{$x/@key}' title='{$x/title}'></inproc_key_title>} </proceedings>)";
			break;
		case "year descending":
			xqueryString = "let $all_procs := for $proc in doc('"+dbName+"')//proceedings where contains(upper-case($proc/title),upper-case('"+filter+"')) order by $proc/year descending return $proc for $i in ("+boff+" to "+eoff+") let $proc := $all_procs[$i] where $proc != '' let $proc_id := $proc/@key let $inproc := doc('"+dbName+"')//inproceedings[crossref =  $proc_id] return (<proceedings confEd='{$inproc[1]/year}'> {$proc/(@*, *)} {for $x in $inproc order by $x/title return <inproc_key_title key='{$x/@key}' title='{$x/title}'></inproc_key_title>} </proceedings>)";
			break;
		default:
			//like "title ascending":
			xqueryString = "let $all_procs := for $proc in doc('"+dbName+"')//proceedings where contains(upper-case($proc/title),upper-case('"+filter+"')) order by $proc/title ascending return $proc for $i in ("+boff+" to "+eoff+") let $proc := $all_procs[$i] where $proc != '' let $proc_id := $proc/@key let $inproc := doc('"+dbName+"')//inproceedings[crossref =  $proc_id] return (<proceedings confEd='{$inproc[1]/year}'> {$proc/(@*, *)} {for $x in $inproc order by $x/title return <inproc_key_title key='{$x/@key}' title='{$x/title}'></inproc_key_title>} </proceedings>)";
			break;
		}

		XQExpression xqe = conn.createExpression();
		return xqe.executeQuery(xqueryString);
	}


	private XQResultSequence get_proceedings_by_Editor_name(String pers_name) throws XQException {
		//String xqueryString = "let $proc := doc('"+dbName+"')//proceedings[@key = 'conf/gbrpr/2005' ] let $proc_id := $proc/@key let $inproc := doc('"+dbName+"')//inproceedings[crossref =  $proc_id] return (<proceedings confEd='{$inproc[1]/year}'> {$proc/(@*, *)}</proceedings>,for $x in $inproc order by $x/title return <inproc_key_title key='{$x/@key}' title='{$x/title}'></inproc_key_title>)";
		String xqueryString = "for $proc in doc('"+dbName+"')//proceedings where $proc/editor = '"+pers_name+"' order by $proc/title let $proc_id := $proc/@key let $inproc := doc('"+dbName+"')//inproceedings[crossref =  $proc_id] return (<proceedings confEd='{$inproc[1]/year}'> {$proc/(@*, *)} {for $x in $inproc order by $x/title return <inproc_key_title key='{$x/@key}' title='{$x/title}'></inproc_key_title>} </proceedings>)";

		XQExpression xqe = conn.createExpression();
		return xqe.executeQuery(xqueryString);
	}

	private XQResultSequence get_proceedings_by_id(String id) throws XQException {
		String xqueryString = "let $proc := doc('"+dbName+"')//proceedings[@key = '"+id+"' ] let $proc_id := $proc/@key let $inproc := doc('"+dbName+"')//inproceedings[crossref =  $proc_id] return (<proceedings confEd='{$inproc[1]/year}'> {$proc/(@*, *)} {for $x in $inproc order by $x/title return <inproc_key_title key='{$x/@key}' title='{$x/title}'></inproc_key_title>} </proceedings>)";

		XQExpression xqe = conn.createExpression();
		return xqe.executeQuery(xqueryString);
	}
	
	private XQResultSequence get_inproceedings_by_filter_offset(String filter, int boff, int eoff, String order_by) throws XQException {
		String xqueryString = "";
		switch(order_by){
		case "title ascending":
			xqueryString = "let $all_inprocs := for $inproc in doc('"+dbName+"')//inproceedings where contains(upper-case($inproc/title),upper-case('"+filter+"')) order by $inproc/title ascending return $inproc  for $i in ("+boff+" to "+eoff+") let $inproc := $all_inprocs[$i] where $inproc != '' let $proc_id := $inproc/crossref let $proc_name := doc('"+dbName+"')//proceedings[@key = $proc_id]/title return<inproceedings proc_name='{ $proc_name}'> {$inproc/(@*, *)} </inproceedings>";
			break;
		case "title descending":
			xqueryString = "let $all_inprocs := for $inproc in doc('"+dbName+"')//inproceedings where contains(upper-case($inproc/title),upper-case('"+filter+"')) order by $inproc/title descending return $inproc  for $i in ("+boff+" to "+eoff+") let $inproc := $all_inprocs[$i] where $inproc != '' let $proc_id := $inproc/crossref let $proc_name := doc('"+dbName+"')//proceedings[@key = $proc_id]/title return<inproceedings proc_name='{ $proc_name}'> {$inproc/(@*, *)} </inproceedings>";
			break;
		case "year ascending":
			//xqueryString = "for $proc in doc('"+dbName+"')//proceedings where contains($proc/title,'"+filter+"') order by $proc/title ascending let $proc_id := $proc/@key let $inproc := doc('"+dbName+"')//inproceedings[crossref =  $proc_id] return (<proceedings confEd='{$inproc[1]/year}'> {$proc/(@*, *)} {for $x in $inproc order by $x/title return <inproc_key_title key='{$x/@key}' title='{$x/title}'></inproc_key_title>} </proceedings>)";
			xqueryString = "let $all_inprocs := for $inproc in doc('"+dbName+"')//inproceedings where contains(upper-case($inproc/title),upper-case('"+filter+"')) order by $inproc/year ascending return $inproc  for $i in ("+boff+" to "+eoff+") let $inproc := $all_inprocs[$i] where $inproc != '' let $proc_id := $inproc/crossref let $proc_name := doc('"+dbName+"')//proceedings[@key = $proc_id]/title return<inproceedings proc_name='{ $proc_name}'> {$inproc/(@*, *)} </inproceedings>";
			break;
		case "year descending":
			xqueryString = "let $all_inprocs := for $inproc in doc('"+dbName+"')//inproceedings where contains(upper-case($inproc/title),upper-case('"+filter+"')) order by $inproc/year descending return $inproc  for $i in ("+boff+" to "+eoff+") let $inproc := $all_inprocs[$i] where $inproc != '' let $proc_id := $inproc/crossref let $proc_name := doc('"+dbName+"')//proceedings[@key = $proc_id]/title return<inproceedings proc_name='{ $proc_name}'> {$inproc/(@*, *)} </inproceedings>";
			break;
		default:
			//like "title ascending":
			xqueryString = "let $all_inprocs := for $inproc in doc('"+dbName+"')//inproceedings where contains(upper-case($inproc/title),upper-case('"+filter+"')) order by $inproc/title ascending return $inproc  for $i in ("+boff+" to "+eoff+") let $inproc := $all_inprocs[$i] where $inproc != '' let $proc_id := $inproc/crossref let $proc_name := doc('"+dbName+"')//proceedings[@key = $proc_id]/title return<inproceedings proc_name='{ $proc_name}'> {$inproc/(@*, *)} </inproceedings>";
			break;
		}
		XQExpression xqe = conn.createExpression();
		return xqe.executeQuery(xqueryString);
	}
	
	private XQResultSequence get_inproceedings_by_Author_name(String pers_name) throws XQException {
		String xqueryString = "for $inproc in doc('"+dbName+"')//inproceedings where $inproc/author = '"+pers_name+"' order by $inproc/title let $proc_id := $inproc/crossref let $proc_name := doc('"+dbName+"')//proceedings[@key = $proc_id]/title return<inproceedings proc_name='{ $proc_name}'> {$inproc/(@*, *)} </inproceedings>";
		XQExpression xqe = conn.createExpression();
		return xqe.executeQuery(xqueryString);
	}

	private XQResultSequence get_inproceedings_by_id(String id) throws XQException {
		String xqueryString = "for $inproc in doc('"+dbName+"')//inproceedings[@key = '"+id+"' ] let $proc_id := $inproc/crossref let $proc_name := doc('"+dbName+"')//proceedings[@key = $proc_id]/title  return <inproceedings proc_name='{ $proc_name}'> {$inproc/(@*, *)} </inproceedings>";

		XQExpression xqe = conn.createExpression();
		return xqe.executeQuery(xqueryString);
	}
	
	private XQResultSequence get_person_by_name(String pers_name) throws XQException {
		String xqueryString = "let $procs := doc('"+dbName+"')//proceedings[editor = '"+pers_name+"'] let $inprocs := doc('"+dbName+"')//inproceedings[author = '"+pers_name+"']let $name := '"+pers_name+"' return (<person name= '{$name}'> {for $x in $procs order by $x/title return <proc_key_title key='{$x/@key}' title='{$x/title}'></proc_key_title>} {for $x in $inprocs order by $x/title return <inproc_key_title key='{$x/@key}' title='{$x/title}'></inproc_key_title>} </person>)";

		XQExpression xqe = conn.createExpression();
		return xqe.executeQuery(xqueryString);
	}
	

	private XQResultSequence get_persons_by_filter_offset(String filter, int boff, int eoff, String obd) throws XQException {
		//String xqueryString = "let $procs := doc('"+dbName+"')//proceedings[editor = '"+filter+"'] let $inprocs := doc('"+dbName+"')//inproceedings[author = '"+filter+"']let $name := '"+filter+"' return (<person name= '{$name}'> {for $x in $procs order by $x/title return <proc_key_title key='{$x/@key}' title='{$x/title}'></proc_key_title>} {for $x in $inprocs order by $x/title return <inproc_key_title key='{$x/@key}' title='{$x/title}'></inproc_key_title>} </person>)";
		//String xqueryString = "for $person in distinct-values(doc('"+dbName+"')//inproceedings/author | doc('"+dbName+"')//proceedings/editor ) where contains($person,'"+filter+"') order by $person let $procs := doc('"+dbName+"')//proceedings[editor = $person] let $inprocs := doc('"+dbName+"')//inproceedings[author = $person] let $name := $person return (<person name= '{$name}'> {for $x in $procs order by $x/title return <proc_key_title key='{$x/@key}' title='{$x/title}'></proc_key_title>} {for $x in $inprocs order by $x/title return <inproc_key_title key='{$x/@key}' title='{$x/title}'></inproc_key_title>} </person>)";
		String xqueryString =" let $all_person := for $person in distinct-values(doc('"+dbName+"')//inproceedings/author | doc('"+dbName+"')//proceedings/editor ) where contains(upper-case($person),upper-case('"+filter+"')) order by $person "+obd+" return $person for $i in ("+boff+" to "+eoff+") let $person := $all_person[$i] where $person != '' let $procs := doc('"+dbName+"')//proceedings[editor = $person] let $inprocs := doc('"+dbName+"')//inproceedings[author = $person] let $name := $person return (<person name= '{$name}'> {for $x in $procs order by $x/title return <proc_key_title key='{$x/@key}' title='{$x/title}'></proc_key_title>} {for $x in $inprocs order by $x/title return <inproc_key_title key='{$x/@key}' title='{$x/title}'></inproc_key_title>} </person>)";
		XQExpression xqe = conn.createExpression();
		return xqe.executeQuery(xqueryString);
	}



	/**
	 * IO-Methods: helper
	 * @throws XQException 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	private DivIO fill_DivIO(XQResultSequence doc, String is_a) {
		DivIO divobj = new DivIO();
		if ( doc == null ){
			divobj.is_empty = true;
			return divobj;
		}

		//Person
		else if ( "is_a_person".equals(is_a) ){
			divobj.is_a_person = true;
			try {
				Element pers = (Element) doc.getObject();
				divobj.Person_name = pers.getAttribute("name");
				divobj.id = calculate_person_id(divobj.Person_name);
				
				NodeList childs = pers.getChildNodes();

				for (int i=0; i<= childs.getLength()-1; i++){
					String node_name = childs.item(i).getNodeName();

					switch (node_name) {
					case "title":
						//childs.title = node_text;
						break;
					case "proc_key_title":					
						//editedPublications		
						Element proc = (Element) childs.item(i);
						String proceedings_id = proc.getAttribute("key");
						String proceedings_title = proc.getAttribute("title");
						divobj.Person_editedPublications_title_id.add(new Pair<String, String>(proceedings_title, proceedings_id));
						//not needed
						//divobj.Person_editedPublications_title_id.sort(comparePairTitleId);
						break;	
					case "inproc_key_title":					
						//authoredPublications
						Element inproc = (Element) childs.item(i);
						String Inproceedings_id = inproc.getAttribute("key");
						String Inproceedings_title = inproc.getAttribute("title");
						divobj.Person_authoredPublications_title_id.add(new Pair<String, String>(Inproceedings_title, Inproceedings_id));
						//not needed
						//divobj.Person_authoredPublications_title_id.sort(comparePairTitleId);
						break;	

					default:
						continue;
					}
				
				}

				
			} catch (XQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			

			

			return divobj;
		}
		return divobj;
	}

	
	
	private PublicationIO fill_PublicationIO(XQResultSequence doc, String is_a){
		PublicationIO publication = new PublicationIO();
		if ( doc == null ){
			publication.is_empty = true;
			return publication;
		}

		//Proceedings
		else if ("is_a_proceeding".equals(is_a) ){
			publication.is_a_proceeding = true;
			try {
				Element proc = (Element) doc.getObject();
				publication.id = proc.getAttribute("key");
				NodeList proc_childs = proc.getChildNodes();

				for (int i=0; i<= proc_childs.getLength()-1; i++){
					String node_name = proc_childs.item(i).getNodeName();
					String node_text = proc_childs.item(i).getTextContent();

					switch (node_name) {
					case "title":
						publication.title = node_text;
						break;
					case "year":
						try {
							publication.year = Integer.parseInt(node_text);
						}
						catch (NumberFormatException e) {
							publication.year = 0;
						}
						break;
					case "ee":
						publication.electronicEdition = node_text;
						break;
					case "booktitle":
						publication.Conference_name_id = new Pair(node_text,calculate_conference_id(node_text));
						int temp = 0;
						try {
							temp = Integer.parseInt(proc.getAttribute("confEd"));
						}
						catch (NumberFormatException e) {
							//e.printStackTrace();
							System.out.println("Error: NumberFormatException...");
						}
						publication.ConferenceEdition_year_id = new Pair(temp,calculate_conferenceEdition_id(publication.Conference_name_id.getKey(), temp));
						break;
					case "editor":
						publication.editors_name_id.add(new Pair(node_text,calculate_person_id(node_text)));
						break;
					case "note":
						publication.note = node_text;
						break;
					case "number":
						publication.number = Integer.parseInt(node_text);
						break;
					case "publisher":
						publication.publisher_name = node_text;
						publication.publisher_id = calculate_publisher_id(publication.publisher_name);
						break;
					case "volume":
						publication.volume = node_text;
						break;
					case "series":
						publication.series_name = node_text;
						publication.series_id = calculate_series_id(publication.series_name);
						break;
					case "inproc_key_title":
						Element inproc = (Element) proc_childs.item(i);
						publication.inproceedings_title_id.add(new Pair(inproc.getAttribute("title"),inproc.getAttribute("key")));
						break;	
					
					default:
						continue;
					}
				}
				/*while ( doc.next() ){
					int a = 55;
					Element inproc = (Element) doc.getObject();
					publication.inproceedings_title_id.add(new Pair(inproc.getAttribute("title"),inproc.getAttribute("key")));
				}*/
				
				
			} catch (XQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		//InProceedings 
		else if ("is_an_inproceeding".equals(is_a) ){
			publication.is_an_inproceeding = true;
			try {

				//doc.next();
				Element inproc = (Element) doc.getObject();
				publication.proceeding_title = inproc.getAttribute("proc_name");
				publication.id = inproc.getAttribute("key");
				//System.out.println(inproc);
				NodeList inproc_childs = inproc.getChildNodes();

				for (int i=0; i<= inproc_childs.getLength()-1; i++){
					String node_name = inproc_childs.item(i).getNodeName();
					String node_text = inproc_childs.item(i).getTextContent();

					switch (node_name) {
					case "author":
						publication.authors_name_id.add(new Pair(node_text,calculate_person_id(node_text)));
						break;
					case "title":
						publication.title = node_text;
						break;
					case "note":
						publication.note = node_text;
						break;
					case "pages":
						publication.pages = node_text;
						break;
					case "ee":
						publication.electronicEdition = node_text;
						break;
					case "year":
						publication.year = Integer.parseInt(node_text);
						break;
					case "crossref":
						publication.proceeding_id = node_text;
						break;
					case "booktitle":
						publication.Conference_name_id = new Pair(node_text,calculate_conference_id(node_text));
						publication.ConferenceEdition_year_id = new Pair(publication.year,calculate_conferenceEdition_id(publication.Conference_name_id.getKey(), publication.year));
						break;
					default:
						continue;
					}

					//System.out.println(node_name);
					//System.out.println(node_text);
				}




			} catch (XQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
		return publication;

	}
	
	



	/**
	 * IO-Methods
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	@Override
	public String IO_get_statistics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] IO_authors_editors_for_a_conference(String conf_id, String mode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String IO_avg_authors_per_inproceedings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String IO_count_publications_per_interval(int y1, int y2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String IO_delete_get_person_by_id(String pers_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String IO_find_author_distance_path(String name1, String name2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DivIO> IO_find_co_authors(String pers_name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String IO_find_co_authors_returns_String(String pers_name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DivIO IO_get_conf_by_id(String conf_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DivIO IO_get_confEd_by_id(String confEd_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DivIO> IO_get_conference_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DivIO> IO_get_person_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		List<DivIO> return_list = new ArrayList<DivIO>();

		String obd = "ascending";
		if ( !order_by.equals("")){
			obd = order_by.split(" ")[1];
		}


		if (boff<=0 || eoff< boff){
			boff = 1;
			eoff = 5;
		}

		//get persons, if any
		try {
			XQResultSequence doc = get_persons_by_filter_offset(filter, boff, eoff, obd);
			while (doc.next()){
				return_list.add(fill_DivIO(doc,"is_a_person"));
			}
		} catch (XQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return return_list;
	}


	@Override
	public DivIO IO_get_person_by_id(String pers_id) {
		try {
			String pers_name = pers_id.split("/")[1];
			XQResultSequence doc = get_person_by_name(pers_name);
			if (doc.next()){
				return fill_DivIO(doc,"is_a_person");
			}
		} catch (XQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fill_DivIO(null,"null");
	}


	@Override
	public List<PublicationIO> IO_get_publ_by_filter_offset(String filter, int boff, int eoff, String order_by) {

		List<PublicationIO> return_list = new ArrayList<PublicationIO>();

		if (boff<=0 || eoff< boff){
			boff = 1;
			eoff = 5;
		}

		try {

			//get proceedings, if any
			XQResultSequence doc = get_proceedings_by_filter_offset(filter, boff, eoff, order_by);
			while (doc.next()){
				return_list.add(fill_PublicationIO(doc,"is_a_proceeding"));
			}

			//get inproceedings, if any
			doc = get_inproceedings_by_filter_offset(filter, boff, eoff, order_by);
			while (doc.next()){
				return_list.add(fill_PublicationIO(doc,"is_an_inproceeding"));
			}
		} catch (XQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return return_list;
	}

	

	@Override
	public PublicationIO IO_get_publication_by_id(String publ_id){
		// TODO Auto-generated method stub
		XQResultSequence publ;
		try {
			publ = get_inproceedings_by_id(publ_id);
			//check for empty sequence			
			if(!publ.next()){
				publ = null;
			}
		} catch (XQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			publ = null;
		}
		if ( publ != null ){
			return fill_PublicationIO(publ, "is_an_inproceeding");
		}
		else {
			try {
				publ = get_proceedings_by_id(publ_id);
				//check for empty sequence			
				if(!publ.next()){
					publ = null;
				}
			} catch (XQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				publ = null;
			}
			if ( publ != null ){
				return fill_PublicationIO(publ, "is_a_proceeding");
			}
		}
		return fill_PublicationIO(null,"null");
	}



	@Override
	public List<DivIO> IO_get_publisher_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DivIO IO_get_publisher_by_id(String publ_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DivIO> IO_get_series_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DivIO IO_get_series_by_id(String series_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String IO_inproceedings_for_a_conference(String conf_id, String mode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String IO_person_is_author_and_editor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PublicationIO> IO_person_is_last_author(String pers_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PublicationIO> IO_publ_by_person_name_or_id(HashMap<String, String> args) {
		List<PublicationIO> return_list = new ArrayList<PublicationIO>();

		//check if person exists
		//boolean pers_exists;
		String pers_id = "";
		String pers_name ="";
		if( args.containsKey("id") ){
			//get person by id
			//pers_exists = exists_person_by_id(args.get("id"));
			pers_id = args.get("id");
			pers_name = pers_id.split("/")[1];
		}
		else {
			//get person by name
			//pers_exists = exists_person_by_name(args.get("name"));
			//get person id
			pers_name = args.get("name");
		}

		String publ_mode = args.get("publ");

		//get proceedings
		if ( publ_mode.equals("all") || publ_mode.equals("editored")){
			try {
				XQResultSequence publ = get_proceedings_by_Editor_name(pers_name);
				while(publ.next()){
					return_list.add(fill_PublicationIO(publ,"is_a_proceeding"));
				}
			} catch (XQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//get inproceedings
		if ( publ_mode.equals("all") || publ_mode.equals("authored")){
			try {
				XQResultSequence publ = get_inproceedings_by_Author_name(pers_name);
				while(publ.next()){
					return_list.add(fill_PublicationIO(publ,"is_an_inproceeding"));
				}
			} catch (XQException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return return_list;
	}




	@Override
	public List<DivIO> IO_publishers_whose_authors_in_interval(int y1, int y2) {
		// TODO Auto-generated method stub
		return null;
	}

}
