package eth.infsys.group1.task3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQResultSequence;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import eth.infsys.group1.dbspec.DBProvider;
import eth.infsys.group1.dbspec.DivIO;
import eth.infsys.group1.dbspec.InProceedings_simple_input;
import eth.infsys.group1.dbspec.Proceedings_simple_input;
import eth.infsys.group1.dbspec.PublicationIO;
import javafx.util.Pair;
import net.xqj.basex.BaseXXQDataSource;

@SuppressWarnings("restriction")
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
	public T3DBProvider(String dbName, String User, String PW){
		try {
			this.dbName = dbName;
			this.xqs = new BaseXXQDataSource();
			xqs.setProperty("serverName", "localhost");
			xqs.setProperty("port", "1984");
			this.conn = xqs.getConnection(User, PW);
		} catch (XQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	

	private XQResultSequence exists_proceedings_by_id(String id) throws XQException {
		String xqueryString = "let $proc_id := '"+id+"' let $proc_exists :=for $proc in doc('"+dbName+"')//proceedings[@key = $proc_id ] return count($proc) return <proceedings key='{$proc_id}' exists='{$proc_exists}'></proceedings>";
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
		String xqueryString =" let $all_person := for $person in distinct-values(doc('"+dbName+"')//inproceedings/author | doc('"+dbName+"')//proceedings/editor ) where contains(upper-case($person),upper-case('"+filter+"')) order by $person "+obd+" return $person for $i in ("+boff+" to "+eoff+") let $person := $all_person[$i] where $person != '' let $procs := doc('"+dbName+"')//proceedings[editor = $person] let $inprocs := doc('"+dbName+"')//inproceedings[author = $person] let $name := $person return (<person name= '{$name}'> {for $x in $procs order by $x/title return <proc_key_title key='{$x/@key}' title='{$x/title}'></proc_key_title>} {for $x in $inprocs order by $x/title return <inproc_key_title key='{$x/@key}' title='{$x/title}'></inproc_key_title>} </person>)";

		XQExpression xqe = conn.createExpression();
		return xqe.executeQuery(xqueryString);
	}

	private XQResultSequence get_confEd_by_name(String conf_name, String confEd_name) throws XQException {
		//String xqueryString = "let $procs := doc('"+dbName+"')//proceedings[editor = '"+confEd_name+"'] let $inprocs := doc('"+dbName+"')//inproceedings[author = '"+confEd_name+"']let $name := '"+confEd_name+"' return (<person name= '{$name}'> {for $x in $procs order by $x/title return <proc_key_title key='{$x/@key}' title='{$x/title}'></proc_key_title>} {for $x in $inprocs order by $x/title return <inproc_key_title key='{$x/@key}' title='{$x/title}'></inproc_key_title>} </person>)";
		String xqueryString = "for $proc in doc('"+dbName+"')//proceedings where $proc/booktitle = '"+conf_name+"' let $proc_id := $proc/@key where doc('"+dbName+"')//inproceedings[crossref =  $proc_id]/year = '"+confEd_name+"' return <confEd_key_title key='conferenceedition/"+conf_name+"/"+confEd_name+"' title='"+confEd_name+"'> {$proc/booktitle} <proc_key_title key='{$proc_id}' title='{$proc/title}'></proc_key_title></confEd_key_title>";
		XQExpression xqe = conn.createExpression();
		return xqe.executeQuery(xqueryString);
	}

	private XQResultSequence get_conf_by_name(String conf_name) throws XQException {
		String xqueryString = "let $conf := '"+conf_name+"' let $all_proc_id := for $proc in doc('"+dbName+"')//proceedings where $proc/booktitle = $conf return $proc/@key let $all_confEd := for $proc_id in  $all_proc_id let $inproc := doc('"+dbName+"')//inproceedings[crossref =  $proc_id] return $inproc[1]/year return <conf_key_title key='conference/{$conf}' title='{$conf}'>{for $confEd in $all_confEd order by $confEd return <confEd_key_title key='conferenceedition/{$conf}/{$confEd}' title='{$confEd}'></confEd_key_title>}</conf_key_title>";
		XQExpression xqe = conn.createExpression();
		return xqe.executeQuery(xqueryString);
	}


	private XQResultSequence get_conference_by_filter_offset(String filter, int boff, int eoff, String obd) throws XQException {
		String xqueryString = "let $filter := '"+filter+"' let $all_conferences :=  for $conf in distinct-values(doc('"+dbName+"')//proceedings/booktitle) where contains(upper-case($conf),upper-case($filter)) order by $conf "+obd+" return $conf for $i in ("+boff+" to "+eoff+") let $conf := $all_conferences[$i] where  $conf !='' let $all_proc_id := for $proc in doc('"+dbName+"')//proceedings where $proc/booktitle = $conf return $proc/@key let $all_confEd := for $proc_id in  $all_proc_id let $inproc := doc('"+dbName+"')//inproceedings[crossref =  $proc_id] return $inproc[1]/year return <conf_key_title key='conference/{$conf}' title='{$conf}'> {for $confEd in $all_confEd order by $confEd return <confEd_key_title key='conferenceedition/{$conf}/{$confEd}' title='{$confEd}'></confEd_key_title>}</conf_key_title>";
		XQExpression xqe = conn.createExpression();
		return xqe.executeQuery(xqueryString);
	}

	private XQResultSequence get_publisher_by_name(String publ_name) throws XQException {
		String xqueryString = "let $publ_name := '"+publ_name+"' return <publisher_key_title key='publisher/{$publ_name}' title='{$publ_name}'> {for $proc in doc('"+dbName+"')//proceedings where $proc/publisher = $publ_name order by $proc/title let $proc_id := $proc/@key return <proc_key_title key='{$proc_id}' title='{$proc/title}'></proc_key_title>}</publisher_key_title>";
		XQExpression xqe = conn.createExpression();
		return xqe.executeQuery(xqueryString);
	}

	private XQResultSequence get_publisher_by_filter_offset(String filter, int boff, int eoff, String obd) throws XQException {
		String xqueryString = "let $filter := '"+filter+"' let $all_publishers :=  for $publ in distinct-values(doc('"+dbName+"')//proceedings/publisher) where contains(upper-case($publ),upper-case($filter)) order by $publ "+obd+" return $publ for $i in ("+boff+" to "+eoff+") let $publ_name := $all_publishers[$i] where $publ_name !='' return <publisher_key_title key='publisher/{$publ_name}' title='{$publ_name}'> {for $proc in doc('"+dbName+"')//proceedings where $proc/publisher = $publ_name order by $proc/title let $proc_id := $proc/@key return <proc_key_title key='{$proc_id}' title='{$proc/title}'></proc_key_title>}</publisher_key_title>";
		XQExpression xqe = conn.createExpression();
		return xqe.executeQuery(xqueryString);
	}


	private XQResultSequence get_series_by_name(String series_name) throws XQException {
		String xqueryString = "let $series_name := '"+series_name+"' return <series_key_title key='series/{$series_name}' title='{$series_name}'> {for $proc in doc('"+dbName+"')//proceedings where $proc/series = $series_name order by $proc/title let $proc_id := $proc/@key return <proc_key_title key='{$proc_id}' title='{$proc/title}'></proc_key_title>}</series_key_title>";
		XQExpression xqe = conn.createExpression();
		return xqe.executeQuery(xqueryString);
	}

	private XQResultSequence get_series_by_filter_offset(String filter, int boff, int eoff, String obd) throws XQException {
		//String xqueryString = "let $series_name := '"+series_name+"' return <series_key_title key='series/{$series_name}' title='{$series_name}'> {for $proc in doc('"+dbName+"')//proceedings where $proc/series = $series_name order by $proc/title let $proc_id := $proc/@key return <proc_key_title key='{$proc_id}' title='{$proc/title}'></proc_key_title>}</series_key_title>";
		String xqueryString = "let $filter := '"+filter+"' let $all_series :=  for $series in distinct-values(doc('"+dbName+"')//proceedings/series) where contains(upper-case($series),upper-case($filter)) order by $series "+obd+" return $series for $i in ("+boff+" to "+eoff+") let $series_name := $all_series[$i] where $series_name !='' return <series_key_title key='series/{$series_name}' title='{$series_name}'> {for $proc in doc('"+dbName+"')//proceedings where $proc/series = $series_name order by $proc/title let $proc_id := $proc/@key return <proc_key_title key='{$proc_id}' title='{$proc/title}'></proc_key_title>} </series_key_title>";
		XQExpression xqe = conn.createExpression();
		return xqe.executeQuery(xqueryString);
	}

	/**
	 * Special-Queries
	 * @throws XQException 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	private XQResultSequence executeQuery_myquery(String xqueryString) throws XQException {
		XQExpression xqe = conn.createExpression();
		return xqe.executeQuery(xqueryString);
	}

	private XQResultSequence find_co_authors(String pers_name) throws XQException {
		//let $filter := '"+filter+"' let $all_series :=  for $series in distinct-values(doc('"+dbName+"')//proceedings/series) where contains(upper-case($series),upper-case($filter)) order by $series "+obd+" return $series for $i in ("+boff+" to "+eoff+") let $series_name := $all_series[$i] where $series_name !='' return <series_key_title key='series/{$series_name}' title='{$series_name}'> {for $proc in doc('"+dbName+"')//proceedings where $proc/series = $series_name order by $proc/title let $proc_id := $proc/@key return <proc_key_title key='{$proc_id}' title='{$proc/title}'></proc_key_title>} </series_key_title>";
		String xqueryString = "let $author := '"+pers_name+"' let $all_inprocs := for $inproc in doc('"+dbName+"')//inproceedings where $inproc/author = $author return $inproc for $coauthor in distinct-values($all_inprocs/author) order by $coauthor where  $coauthor != $author let $procs := doc('"+dbName+"')//proceedings[editor = $coauthor] let $inprocs := doc('"+dbName+"')//inproceedings[author = $coauthor] let $name := $coauthor  return (<person name= '{$name}'> {for $x in $procs order by $x/title return <proc_key_title key='{$x/@key}' title='{$x/title}'></proc_key_title>} {for $x in $inprocs order by $x/title return <inproc_key_title key='{$x/@key}' title='{$x/title}'></inproc_key_title>} </person>)";
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

		else if ( "is_a_series".equals(is_a) ){
			divobj.is_a_series = true;

			try {
				Element series = (Element) doc.getObject();
				divobj.id = series.getAttribute("key");
				divobj.Series_name = series.getAttribute("title");
				NodeList childs = series.getChildNodes();
				for (int i=0; i<= childs.getLength()-1; i++){
					String node_name = childs.item(i).getNodeName();

					switch (node_name) {
					case "proc_key_title":					
						Element proc = (Element) childs.item(i);

						String proceedings_id = proc.getAttribute("key");
						String proceedings_title = proc.getAttribute("title");
						divobj.Series_publications_title_id.add(new Pair<String, String>(proceedings_title, proceedings_id));
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
		else if ( "is_a_publisher".equals(is_a) ){
			divobj.is_a_publisher = true;
			try {
				Element publ = (Element) doc.getObject();
				divobj.id = publ.getAttribute("key");
				divobj.Publisher_name = publ.getAttribute("title");
				NodeList childs = publ.getChildNodes();
				for (int i=0; i<= childs.getLength()-1; i++){
					String node_name = childs.item(i).getNodeName();

					switch (node_name) {
					case "proc_key_title":					
						Element proc = (Element) childs.item(i);

						String proceedings_id = proc.getAttribute("key");
						String proceedings_title = proc.getAttribute("title");
						divobj.Publisher_publications_title_id.add(new Pair<String, String>(proceedings_title, proceedings_id));
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

		else if ( "is_a_conference".equals(is_a) ){
			divobj.is_a_conference = true;
			try {
				Element conf = (Element) doc.getObject();
				divobj.id = conf.getAttribute("key");
				divobj.Conference_name = conf.getAttribute("title");
				NodeList childs = conf.getChildNodes();
				for (int i=0; i<= childs.getLength()-1; i++){
					String node_name = childs.item(i).getNodeName();

					switch (node_name) {
					case "confEd_key_title":					
						Element confEd = (Element) childs.item(i);
						try {
							String ConferenceEdition_id = confEd.getAttribute("key");
							int year = Integer.parseInt(confEd.getAttribute("title"));
							divobj.Conference_editions_year_id.add(new Pair<Integer, String>(year, confEd.getAttribute("key")));
						}
						catch (NumberFormatException e) {
							//ignore-entry
						}
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

		//ConferenceEdition
		else if ( "is_a_conference_edition".equals(is_a) ){
			divobj.is_a_conference_edition = true;
			try {
				Element confEd = (Element) doc.getObject();
				divobj.id = confEd.getAttribute("key");
				divobj.ConferenceEdition_year = Integer.parseInt(confEd.getAttribute("title"));
				NodeList childs = confEd.getChildNodes();
				for (int i=0; i<= childs.getLength()-1; i++){
					String node_name = childs.item(i).getNodeName();

					switch (node_name) {
					case "booktitle":
						divobj.ConferenceEdition_conference_name = childs.item(i).getTextContent();
						divobj.ConferenceEdition_conference_id = calculate_conference_id(divobj.ConferenceEdition_conference_name);
						break;
					case "proc_key_title":					
						Element proc = (Element) childs.item(i);
						divobj.ConferenceEditions_proceedings_id = proc.getAttribute("key");
						divobj.ConferenceEditions_proceedings_title = proc.getAttribute("title");
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
					case "isbn":
						publication.isbn = node_text;
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
						publication.ConferenceEdition_year_id = new Pair<Integer, String>(temp,calculate_conferenceEdition_id(publication.Conference_name_id.getKey(), temp));
						break;
					case "editor":
						publication.editors_name_id.add(new Pair<String, String>(node_text,calculate_person_id(node_text)));
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
		String out[]= new String[2]; out[0]=""; out[1]="";
		String Output ="";
		String Conference_id = conf_id.split("/")[1];
		String Conference_name = Conference_id;
		int count_editors = 0;
		int count_authors = 0;
		int total = 0;

		if (mode.equals("count")){
			try {
				String xqueryString = "let $conf := '"+Conference_id+"' let $count_editors := count(distinct-values(doc('"+dbName+"')//proceedings[booktitle = $conf]/editor)) let $count_authors := count(distinct-values(doc('"+dbName+"')//inproceedings[booktitle = $conf]/author)) return <result count_editors='{$count_editors}' count_authors='{$count_authors}' ></result>";
				XQResultSequence doc = executeQuery_myquery(xqueryString);
				if (doc.next()){
					Element result = (Element) doc.getObject();
					count_editors = Integer.parseInt(result.getAttribute("count_editors"));
					count_authors = Integer.parseInt(result.getAttribute("count_authors"));
					total = count_authors + count_editors;
					Output += "<br>there are <b>"+count_authors+" authors</b> and <b>"+count_editors+" editors</b> (total="+total+") for the conference <a href='/test/?func=conf_by_id&id="+conf_id+"'>"+Conference_name+"</a><br>";
					out[0]=Output;
					return out;
				}
			} catch (XQException e) {
				e.printStackTrace();
			}
		}

		else {
			//get editors
			try {
				String xqueryString = "let $conf := '"+Conference_id+"' for $editor in distinct-values(doc('"+dbName+"')//proceedings[booktitle = $conf]/editor) order by $editor return <editor key='person/{$editor}' name='{$editor}'></editor>";
				XQResultSequence doc = executeQuery_myquery(xqueryString);
				while (doc.next()){
					Element result = (Element) doc.getObject();
					String Editor_id = result.getAttribute("key");
					String Editor_name = result.getAttribute("name");
					out[0]+= "<a href='/test?func=person_by_id&id="+Editor_id+"'>"+Editor_name+"</a> ";
				}
			} catch (XQException e) {
				e.printStackTrace();
			}



			//get authors
			try {
				String xqueryString = "let $conf := '"+Conference_id+"' for $author in distinct-values(doc('"+dbName+"')//inproceedings[booktitle = $conf]/author) order by $author return <author key='person/{$author}' name='{$author}'></author>";
				XQResultSequence doc = executeQuery_myquery(xqueryString);
				while (doc.next()){
					Element result = (Element) doc.getObject();
					String Author_id = result.getAttribute("key");
					String Author_name = result.getAttribute("name");
					out[1]+= "<a href='/test?func=person_by_id&id="+Author_id+"'>"+Author_name+"</a> ";
				}
			} catch (XQException e) {
				e.printStackTrace();
			}
			return out;
		}

		//Error...
		Output += "<br>Error...<br>";
		out[0]=Output;
		return out;
	}


	@Override
	public String IO_avg_authors_per_inproceedings() {
		int count_inprocs = 0;
		int count_authors = 0;
		double avg = 0;
		//get count_inprocs and count_authors
		try {
			String xqueryString = "let $all_inprocs :=  doc('"+dbName+"')//inproceedings let $count_inproc := count($all_inprocs) let $count_authors := count($all_inprocs/author) return <result count_inprocs='{$count_inproc}' count_authors='{$count_authors}'></result>";
			XQResultSequence doc = executeQuery_myquery(xqueryString);
			//Node shit = doc.getNode();
			if (doc.next()){
				Element result = (Element) doc.getObject();

				count_inprocs = Integer.parseInt(result.getAttribute("count_inprocs"));
				count_authors = Integer.parseInt(result.getAttribute("count_authors"));

				avg = (double) count_authors / (double) count_inprocs;
			}
		} catch (XQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String Output = "<br>there are <b>"+count_inprocs+" inproceedings</b> in total with an average of <b>"+String.valueOf(avg)+" authors per inproceedings</b><br>";
		return Output;
	}



	@Override
	public String IO_count_publications_per_interval(int y1, int y2) {
		String Output = "";


		int count_proceedings = 0;
		int count_inproceedings = 0;
		int total = 0;

		try {
			String xqueryString = "let $year1 := '"+y1+"' let $year2 := '"+y2+"' let $count_inprocs := count(doc('"+dbName+"')//inproceedings[$year1 <= *:year][*:year <= $year2]) let $count_procs := count(doc('"+dbName+"')//proceedings[$year1 <= *:year][*:year <= $year2]) return <result count_procs='{$count_procs}' count_inprocs='{$count_inprocs}' ></result>";
			XQResultSequence doc = executeQuery_myquery(xqueryString);
			if (doc.next()){
				Element result = (Element) doc.getObject();
				count_proceedings = Integer.parseInt(result.getAttribute("count_procs"));
				count_inproceedings = Integer.parseInt(result.getAttribute("count_inprocs"));
				total = count_proceedings + count_inproceedings;
			}
		} catch (XQException e) {
			e.printStackTrace();
		}


		Output += "<br>the number of publications in the interval ["+y1+","+y2+"] is: "+total+"<br>";
		Output += "count_proceedings: "+count_proceedings+"<br>";
		Output += "count_inproceedings: "+count_inproceedings+"<br>";

		return Output;
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
		List<DivIO> return_list = new ArrayList<DivIO>();

		//get persons, if any
		try {
			XQResultSequence doc = find_co_authors(pers_name);
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
	public String IO_find_co_authors_returns_String(String pers_name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DivIO IO_get_conf_by_id(String conf_id) {
		try {
			String conf_name = conf_id.split("/")[1];
			XQResultSequence doc = get_conf_by_name(conf_name);
			if (doc.next()){
				return fill_DivIO(doc,"is_a_conference");
			}
		} catch (XQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fill_DivIO(null,"null");
	}



	@Override
	public DivIO IO_get_confEd_by_id(String confEd_id) {
		try {
			String conf_name = confEd_id.split("/")[1];
			String confEd_name = confEd_id.split("/")[2];

			XQResultSequence doc = get_confEd_by_name(conf_name, confEd_name);
			if (doc.next()){
				return fill_DivIO(doc,"is_a_conference_edition");
			}
		} catch (XQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fill_DivIO(null,"null");
	}



	@Override
	public List<DivIO> IO_get_conference_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		List<DivIO> return_list = new ArrayList<DivIO>();

		String obd = "ascending";
		if ( !order_by.equals("")){
			obd = order_by.split(" ")[1];
		}
		if (boff<=0 || eoff< boff){
			boff = 1;
			eoff = 5;
		}

		//get conference, if any
		try {
			XQResultSequence doc = get_conference_by_filter_offset(filter, boff, eoff, obd);
			while (doc.next()){
				return_list.add(fill_DivIO(doc,"is_a_conference"));
			}
		} catch (XQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return return_list;
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
	public boolean IO_exists_inproceedings_id(String id){
		System.out.println("T3DBProvider: IO_exists_inproceedings_id...");
		XQResultSequence publ;
		try {
			publ = get_inproceedings_by_id(id);
			//check for empty sequence			
			if(!publ.next()){
				return false;
			}
			else {
				return true;
			}
		} catch (XQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			publ = null;
		}
		return false;
	}
	

	@Override
	public boolean IO_exists_proceedings_id(String id) {
		System.out.println("T3DBProvider: IO_exists_proceedings_id...");
		XQResultSequence publ;
		try {
			publ = exists_proceedings_by_id(id);
			//check for empty sequence			
			if(publ.next()){
				Element proc = (Element) publ.getObject();
				if ( proc.getAttribute("exists").equals("1")){
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		} catch (XQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
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
		List<DivIO> return_list = new ArrayList<DivIO>();

		String obd = "ascending";
		if ( !order_by.equals("")){
			obd = order_by.split(" ")[1];
		}
		if (boff<=0 || eoff< boff){
			boff = 1;
			eoff = 5;
		}

		//get conference, if any
		try {
			XQResultSequence doc = get_publisher_by_filter_offset(filter, boff, eoff, obd);
			while (doc.next()){
				return_list.add(fill_DivIO(doc,"is_a_publisher"));
			}
		} catch (XQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return return_list;
	}


	@Override
	public DivIO IO_get_publisher_by_id(String publ_id) {
		try {
			String publ_name = publ_id.split("/")[1];
			XQResultSequence doc = get_publisher_by_name(publ_name);
			if (doc.next()){
				return fill_DivIO(doc,"is_a_publisher");
			}
		} catch (XQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fill_DivIO(null,"null");
	}


	@Override
	public List<DivIO> IO_get_series_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		List<DivIO> return_list = new ArrayList<DivIO>();

		String obd = "ascending";
		if ( !order_by.equals("")){
			obd = order_by.split(" ")[1];
		}
		if (boff<=0 || eoff< boff){
			boff = 1;
			eoff = 5;
		}

		//get conference, if any
		try {
			XQResultSequence doc = get_series_by_filter_offset(filter, boff, eoff, obd);
			while (doc.next()){
				return_list.add(fill_DivIO(doc,"is_a_series"));
			}
		} catch (XQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return return_list;
	}



	@Override
	public DivIO IO_get_series_by_id(String series_id) {
		try {
			String series_name = series_id.split("/")[1];
			XQResultSequence doc = get_series_by_name(series_name);
			if (doc.next()){
				return fill_DivIO(doc,"is_a_series");
			}
		} catch (XQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fill_DivIO(null,"null");
	}


	@Override
	public String IO_inproceedings_for_a_conference(String conf_id, String mode) {
		String Output ="";
		String Conference_id = conf_id.split("/")[1];
		String Conference_name = Conference_id;
		int count_inprocs = 0;

		if (mode.equals("count")){
			try {
				String xqueryString = "let $conf := '"+Conference_id+"' return <result count_inprocs='{count(for $inproc in doc('"+dbName+"')//inproceedings where $inproc/booktitle = $conf return $inproc)}'></result>";
				XQResultSequence doc = executeQuery_myquery(xqueryString);
				if (doc.next()){
					Element result = (Element) doc.getObject();
					count_inprocs = Integer.parseInt(result.getAttribute("count_inprocs"));
				}
			} catch (XQException e) {
				e.printStackTrace();
			}
			Output += "<br>there are <b>"+count_inprocs+" inproceedings</b> for the conference <a href='/test/?func=conf_by_id&id="+conf_id+"'>"+ Conference_name+"</a><br>";
			return Output;
		}
		else if (mode.equals("retrieve")) {
			try {
				Output += "<br>the inproceedings for the conference <a href='/test/?func=conf_by_id&id="+conf_id+"'>"+ Conference_name+"</a> are:<br><br>";

				String xqueryString = "let $conf := '"+Conference_id+"' for $inproc in doc('"+dbName+"')//inproceedings where $inproc/booktitle = $conf order by $inproc/title return (<inproc_key_title key='{$inproc/@key}' title='{$inproc/title}'></inproc_key_title>)";
				XQResultSequence doc = executeQuery_myquery(xqueryString);

				while (doc.next()){
					Element result = (Element) doc.getObject();

					String Inproceedings_id = result.getAttribute("key");
					String Inproceedings_title = result.getAttribute("title");

					Output += "<a href='/test/?func=inproceeding_by_id&key="+Inproceedings_id+"'>"+Inproceedings_title+"</a><br>";
				}
				return Output;
			} catch (XQException e) {
				e.printStackTrace();
			}
		}
		return "<br>Error...<br>";
	}

	@Override
	public String IO_person_is_author_and_editor() {
		String Output = "";
		StringBuilder temp = new StringBuilder();
		StringBuilder Jumplist = new StringBuilder();

		Jumplist.append("<b>Jumplist: </b>");

		int k = 0;

		try {
			String xqueryString = "let $result := for $proc in doc('"+dbName+"')//proceedings for $editor in $proc/editor let $inprocs_per_editor := doc('"+dbName+"')//inproceedings[author = $editor][crossref = $proc/@key] for $inproc in $inprocs_per_editor return <editor_author id='{$editor}' name='{$editor}' proc_key='{$proc/@key}' proc_title='{$proc/title}' inproc_key='{$inproc/@key}' inproc_title='{$inproc/title}'></editor_author> for $res in $result order by $res/@name return $res";
			XQResultSequence doc = executeQuery_myquery(xqueryString);

			String Person_id= "";
			String Person_name="";

			Boolean is_first_person = true;
			String last_Person_id = "no last person";

			String Proceeding_id ="";
			String Proceeding_title ="";

			String Inproceeding_id="";
			String Inproceeding_title="";

			while (doc.next()){
				k++;
				Element result = (Element) doc.getObject();

				Person_id=result.getAttribute("id");
				//29.5.2016: ev ist der Fehler noch an anderen Orten
				Person_id = "person/"+Person_id;
				Person_name=result.getAttribute("name");

				Proceeding_id =result.getAttribute("proc_key");
				Proceeding_title =result.getAttribute("proc_title");

				Inproceeding_id=result.getAttribute("inproc_key");
				Inproceeding_title=result.getAttribute("inproc_title");

				//new Person
				if ( ! last_Person_id.equals(Person_id) ){
					//close the list for last person: not for the first person
					if (!is_first_person){
						//last entry for person
						temp.append("</ol><hr>");
					} else {
						is_first_person = false;
					}

					//start new List for the new Person

					//add person to jumplist - first time
					Jumplist.append("<a href='#"+Person_id+"'>"+Person_name + "</a>, ");

					//first time header
					temp.append("<h4 id='"+Person_id+"'><a href='/test/?func=person_by_id&id=" +Person_id+ "'>"+Person_name+"</a></h4>");
					temp.append("<ol type='1'>");
				}
				//add entry for person
				temp.append("<li><a href='/test/?func=proceeding_by_id&key=" + Proceeding_id+"'>"+Proceeding_title+ "</a> / <a href='/test/?func=inproceeding_by_id&key=" + Inproceeding_id+"'>"+Inproceeding_title+ "</a></li>");

				last_Person_id = Person_id;
			}
		} catch (XQException e) {
			e.printStackTrace();
		}

		Output += "Statistic: inner loop... "+k+"<br>"+Jumplist + "<br>" + temp;
		return Output;
	}

	@Override
	public List<PublicationIO> IO_person_is_last_author(String pers_id) {
		List<PublicationIO> return_list = new ArrayList<PublicationIO>();	

		try {
			String pers_name = pers_id.split("/")[1];

			String xqueryString = "let $author := '"+pers_name+"' for $inproc in doc('"+dbName+"')//inproceedings where ($inproc/author)[last()] =  $author order by $inproc/title return $inproc";
			XQResultSequence doc = executeQuery_myquery(xqueryString);

			while (doc.next()){
				return_list.add(fill_PublicationIO(doc,"is_an_inproceeding"));
			}

			return return_list;

		} catch (XQException e) {
			e.printStackTrace();
		}
		return return_list;
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
		List<DivIO> return_list = new ArrayList<>();

		try {
			String xqueryString = "let $year1 := '"+y1+"' let $year2 := '"+y2+"' let $all_crossrefs := distinct-values(doc('"+dbName+"')//inproceedings[$year1 <= *:year][*:year <= $year2]/crossref) let $all_publishers := distinct-values(for $crossref in $all_crossrefs return doc('"+dbName+"')//proceedings[@key=$crossref]/publisher) for $publ_name in $all_publishers order by $publ_name return <publisher_key_title key='publisher/{$publ_name}' title='{$publ_name}'> {for $proc in doc('"+dbName+"')//proceedings where $proc/publisher = $publ_name order by $proc/title let $proc_id := $proc/@key return <proc_key_title key='{$proc_id}' title='{$proc/title}'></proc_key_title>} </publisher_key_title>";
			XQResultSequence doc = executeQuery_myquery(xqueryString);

			while (doc.next()){
				return_list.add(fill_DivIO(doc,"is_a_publisher"));
			}
			
			return return_list;
			
		} catch (XQException e) {
			e.printStackTrace();
		}

		return return_list;
	}

	
	
	@Override
	public String IO_get_isbn_by_proc_id(String publ_id) {
		XQResultSequence publ;
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
			PublicationIO temp = fill_PublicationIO(publ, "is_a_proceeding");
			String isbn = temp.isbn;
			return isbn;
		}
		else {
			return "Error getting old isbn...";
		}
	}

	

}
