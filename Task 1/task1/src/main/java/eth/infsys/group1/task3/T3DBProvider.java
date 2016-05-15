package eth.infsys.group1.task3;

import java.util.HashMap;
import java.util.List;

import javax.xml.stream.XMLStreamReader;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQItemType;
import javax.xml.xquery.XQResultSequence;

import org.bson.Document;
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



	private XQResultSequence get_proceedings_by_id(String id) throws XQException {
		//String xqueryString = "let $proc := doc('"+dbName+"')//proceedings[@key = 'conf/gbrpr/2005' ] let $proc_id := $proc/@key let $inproc := doc('"+dbName+"')//inproceedings[crossref =  $proc_id] return ($proc,for $x in $inproc order by $x/title return ($x/title,$x/@key))";
		//String xqueryString = "let $proc := doc('"+dbName+"')//proceedings[@key = 'conf/gbrpr/2005' ] let $proc_id := $proc/@key let $inproc := doc('"+dbName+"')//inproceedings[crossref =  $proc_id] return ($proc,for $x in $inproc order by $x/title return <inproc_key_title key='{$x/@key}' title='{$x/title}'></inproc_key_title>)";

		String xqueryString = "let $proc := doc('"+dbName+"')//proceedings[@key = 'conf/gbrpr/2005' ] let $proc_id := $proc/@key let $inproc := doc('"+dbName+"')//inproceedings[crossref =  $proc_id] return (<proceedings confEd='{$inproc[1]/year}'> {$proc/(@*, *)}</proceedings>,for $x in $inproc order by $x/title return <inproc_key_title key='{$x/@key}' title='{$x/title}'></inproc_key_title>)";

		
		//String xqueryString = "let $proc := doc('"+dbName+"')//proceedings[@key = '"+id+"' ]" +
			//	"let $proc_id := $proc/@key" +
				//"let $inproc := doc('"+dbName+"')//inproceedings[crossref =  $proc_id]" +
				//"return ($proc,for $x in $inproc order by $x/title return ($x/title,$x/@key))";
		XQExpression xqe = conn.createExpression();
		return xqe.executeQuery(xqueryString);
	}

	private XQResultSequence get_inproceedings_by_id(String id) throws XQException {
		String xqueryString = "for $inproc in doc('"+dbName+"')//inproceedings[@key = '"+id+"' ] let $proc_id := $inproc/crossref let $proc_name := doc('"+dbName+"')//proceedings[@key = $proc_id]/title  return <inproceedings proc_name='{ $proc_name}'> {$inproc/(@*, *)} </inproceedings>";

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
						publication.year = Integer.parseInt(node_text);
						break;
					case "ee":
						publication.electronicEdition = node_text;
						break;
					case "booktitle":
						publication.Conference_name_id = new Pair(node_text,calculate_conference_id(node_text));
						int temp = Integer.parseInt(proc.getAttribute("confEd"));
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
					
					default:
						continue;
					}
				}
				while ( doc.next() ){
					int a = 55;
					Element inproc = (Element) doc.getObject();
					publication.inproceedings_title_id.add(new Pair(inproc.getAttribute("title"),inproc.getAttribute("key")));
				}
				
				
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DivIO IO_get_person_by_id(String pers_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PublicationIO> IO_get_publ_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DivIO> IO_publishers_whose_authors_in_interval(int y1, int y2) {
		// TODO Auto-generated method stub
		return null;
	}

}
