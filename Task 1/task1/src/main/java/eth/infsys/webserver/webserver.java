package eth.infsys.webserver;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.xml.xquery.XQException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import eth.infsys.group1.dbspec.DBProvider;
import eth.infsys.group1.dbspec.DivIO;
import eth.infsys.group1.dbspec.InProceedings_simple_input;
import eth.infsys.group1.dbspec.Proceedings_simple_input;
import eth.infsys.group1.dbspec.PublicationIO;
import eth.infsys.group1.dbspec.WebFunc;
import eth.infsys.group1.task1.T1DBProvider;
import eth.infsys.group1.task2.T2DBProvider;
import eth.infsys.group1.task3.T3DBProvider;
import eth.infsys.group1.task4.My_timer;
import javafx.util.Pair;

@SuppressWarnings("restriction")
public class Webserver {

	static private Validator validator;


	private Boolean zooDB_implementation = false;
	private Boolean MongoDB_implementation = false;
	private Boolean BaseXDB_implementation = false;
	private String DB_Implementation="";

	private Boolean Benchmark_mode = false;

	private DBProvider myDB;
	private HttpServer server;

	//Benchmark Mode
	public Webserver(String DB_Implementation, String dbName, String User, String PW, Boolean Benchmark_mode) throws XQException{
		if ("zooDB".equals(DB_Implementation)){
			myDB = new T1DBProvider(dbName, DBProvider.OPEN_DB_APPEND);
			zooDB_implementation = true;
			this.DB_Implementation = DB_Implementation;
		}
		else if ("MongoDB".equals(DB_Implementation)){
			myDB = (T2DBProvider) new T2DBProvider(dbName, DBProvider.OPEN_DB_APPEND);
			MongoDB_implementation = true;
			this.DB_Implementation = DB_Implementation;
		}
		else if ("BaseXDB".equals(DB_Implementation)){
			myDB = (T3DBProvider) new T3DBProvider(dbName, User, PW);
			BaseXDB_implementation = true;
			this.DB_Implementation = DB_Implementation;
		}

		this.Benchmark_mode = true;	
	}

	public int benchmark(My_timer timer, String user_input){

		timer.new_run();
		timer.start();
		String response = backend(user_input);
		timer.stop();

		return response.length();
	}

	public Webserver(int Port, String DB_Implementation, String dbName) throws IOException {

		if ("zooDB".equals(DB_Implementation)){
			myDB = new T1DBProvider(dbName, DBProvider.OPEN_DB_APPEND);
			zooDB_implementation = true;
			this.DB_Implementation = DB_Implementation;
		}
		else if ("MongoDB".equals(DB_Implementation)){
			myDB = (T2DBProvider) new T2DBProvider(dbName, DBProvider.OPEN_DB_APPEND);
			MongoDB_implementation = true;
			this.DB_Implementation = DB_Implementation;
		}

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

		server = HttpServer.create(new InetSocketAddress(Port), 0);
		server.createContext("/test", new MyHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
	}

	public Webserver(int Port, String DB_Implementation, String dbName, String User, String PW) throws IOException {

		if ("BaseXDB".equals(DB_Implementation)){
			myDB = (T3DBProvider) new T3DBProvider(dbName, User, PW);
			BaseXDB_implementation = true;
			this.DB_Implementation = DB_Implementation;
		}

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

		server = HttpServer.create(new InetSocketAddress(Port), 0);
		server.createContext("/test", new MyHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
	}

	class MyHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			System.out.println("\nuser_input : "+t.getRequestURI().getQuery());
			String user_input = java.net.URLDecoder.decode(t.getRequestURI().getQuery(), "UTF-8");
			System.out.println("user_input after decoding: "+user_input);

			//System.out.println("call backend...");
			String response = "";
			try {
				response = backend(user_input);

			}
			catch(Exception e) {
				e.printStackTrace();
				response = backend("func=MAIN");
			}

			//System.out.println("returned from backend...\n");
			byte[] resp = response.getBytes();
			t.sendResponseHeaders(200, resp.length);
			OutputStream os = t.getResponseBody();
			os.write(resp);
			os.close();
		}

	}


	String readFile(String path, Charset encoding) 
			throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}


	private static Pair<String,String> parse_name_value(String arg) {
		final Pattern pattern = Pattern.compile("(?<name>^.+)=(?<value>.+)");
		Matcher matcher = pattern.matcher(arg);

		if(matcher.find()) {
			return new Pair<String,String>(matcher.group("name"),matcher.group("value"));
		} else {
			return null;
		}
	} 

	private static HashMap<String,String> get_args(String arg_in) {
		HashMap<String,String> args_out = new HashMap<String,String>();
		if (arg_in==null){
			args_out.put("func", "ERROR");
			args_out.put("error_message", "arg_count");	
			return args_out;
		}
		String[] args = arg_in.split("\\&");
		int arg_count = args.length;


		Pair<String,String> name_value = parse_name_value(args[0]);

		//func
		if ((name_value==null) || (!name_value.getKey().equals("func")) || (WebFunc.fromString(name_value.getValue())==null) ){
			args_out.put("func", "ERROR");
			args_out.put("error_message", "not_a_func");	
			return args_out;
		}

		if (arg_count != WebFunc.fromString(name_value.getValue()).arg_count ){
			args_out.put("func", "ERROR");
			args_out.put("error_message", "arg_count_missmatch");	
			return args_out;
		}


		for (int i=0; i< arg_count; i++){
			name_value = parse_name_value(args[i]);

			//ignore empty parameters for add/update
			if ( args[0].equals("func=add_inproc") || args[0].equals("func=update_inproc") || args[0].equals("func=add_proc") || args[0].equals("func=update_proc")){
				if ( name_value == null ){
					continue;
				}
			}	

			if ( name_value == null ){
				args_out.put("func", "ERROR");
				args_out.put("error_message", "wrong_argument_syntax");				
				return args_out;
			}

			args_out.put(name_value.getKey(), name_value.getValue());
		}
		return args_out;
	}

	private String backend(String user_input) {
		//System.out.println("bin im backend...");
		//System.out.println("call get args...");
		HashMap<String,String> args = get_args(user_input);
		//System.out.println("zurück von get args");


		String output = "";
		String func = args.get("func");
		String order_by = ""; //default order

		

		WebFunc wf = WebFunc.fromString(func);
		switch (wf) {
		/**
		 * data manipulation
		 * 
		 */
		case add_inproc:
			output += create_header(wf);
			output += add_inproc(args);
			output += create_footer(wf);
			break;
		case update_inproc:
			output += create_header(wf);
			output += update_inproc(args);
			output += create_footer(wf);
			break;

		case add_proc:
			output += create_header(wf);
			output += add_proc(args);
			output += create_footer(wf);
			break;
		case update_proc:
			output += create_header(wf);
			output += update_proc(args);
			output += create_footer(wf);
			break;




			/**
			 * Gui-Stuff
			 * 
			 * 
			 */
		case pupl_by_title_offset_order:
			if ( !args.containsKey("order_by") || !args.containsKey("ob_direction")){
				output += create_header(wf);
				output += get_error("wrong_args: order_by or ob_direction missing...");
				output += create_footer(wf);
				break;
			}
			order_by = args.get("order_by") + " " +args.get("ob_direction");
			//next statement must be "case pupl_by_title_offset:"

		case pupl_by_title_offset:
			if ( !args.containsKey("title") || !args.containsKey("begin-offset") || !args.containsKey("end-offset") ){
				output += create_header(wf);
				output += get_error("wrong_args: title or begin/end-offset missing...");
				output += create_footer(wf);
				break;
			}
			else {				
				output += create_header(wf);
				String filter = "";
				if (zooDB_implementation){
					filter = "title.toLowerCase().contains('"+args.get("title").toLowerCase()+"')";
				}
				if (MongoDB_implementation){
					filter = args.get("title");
				}
				if (BaseXDB_implementation){
					filter = args.get("title");
				}
				output += pupl_by_filter_offset(filter,args.get("begin-offset"),args.get("end-offset"),order_by);
				output += create_footer(wf);
				break;
			}
		case inproceeding_by_id:
			if ( !args.containsKey("key") ){
				output += create_header(wf);
				output += get_error("wrong_args: key missing...");
				output += create_footer(wf);
				break;
			}
			output += create_header(wf);
			output += inproceeding_by_id(args.get("key"));
			output += create_footer(wf);
			break;

		case proceeding_by_id:
			if ( !args.containsKey("key") ){
				output += create_header(wf);
				output += get_error("wrong_args: key missing...");
				output += create_footer(wf);
				break;
			}

			output += create_header(wf);
			output += proceeding_by_id(args.get("key"));
			output += create_footer(wf);
			break;

		case publication_by_id:
			if ( !args.containsKey("key") ){
				output += create_header(wf);
				output += get_error("wrong_args: key missing...");
				output += create_footer(wf);
				break;
			}
			output += create_header(wf);
			output += publication_by_id(args.get("key"));
			output += create_footer(wf);
			break;

		case publ_by_person_name_or_id:
			if ( !( args.containsKey("name")||args.containsKey("id")) || !args.containsKey("publ")  ){
				output += create_header(wf);
				output += get_error("wrong_args: key or id or publ missing...");
				output += create_footer(wf);
				break;
			}
			output += create_header(wf);
			output += publ_by_person_name_or_id(args);
			output += create_footer(wf);
			break;

			/**
			 * Person
			 */
		case person_by_filter_offset_order:
			if ( !args.containsKey("order_by") || !args.containsKey("ob_direction") ){
				output += create_header(wf);
				output += get_error("wrong_args: order_by or ob_direction missing...");
				output += create_footer(wf);
				break;
			}
			order_by = args.get("order_by") + " " +args.get("ob_direction");
			//next statement must be "case person_by_filter_offset:"

		case person_by_filter_offset:
			if ( !args.containsKey("name_contains") || !args.containsKey("begin-offset") || !args.containsKey("end-offset") ){
				output += create_header(wf);
				output += get_error("wrong_args: name_contains or begin/end-offset missing...");
				output += create_footer(wf);
				break;
			}
			else {				
				output += create_header(wf);
				String filter = "";
				if (zooDB_implementation){
					filter = "name.toLowerCase().contains('"+args.get("name_contains").toLowerCase()+"')";
				}
				if (MongoDB_implementation){
					filter = args.get("name_contains");
				}
				if (BaseXDB_implementation){
					filter = args.get("name_contains");
				}
				output += person_by_filter_offset(filter,args.get("begin-offset"),args.get("end-offset"),order_by);
				output += create_footer(wf);
				break;
			}
		case person_by_id:
			if ( !args.containsKey("id") ){
				output += create_header(wf);
				output += get_error("wrong_args: id missing...");
				output += create_footer(wf);
				break;
			}
			output += create_header(wf);
			output += person_by_id(args.get("id"));
			output += create_footer(wf);
			break;
		case delete_person_by_id:
			if ( !args.containsKey("id") ){
				output += create_header(wf);
				output += get_error("wrong_args: id missing...");
				output += create_footer(wf);
				break;
			}
			output += create_header(wf);
			output += delete_person_by_id(args.get("id"));
			output += create_footer(wf);
			break;

		case publisher_by_filter_offset_order:
			if ( !args.containsKey("order_by") || !args.containsKey("ob_direction") ){
				output += create_header(wf);
				output += get_error("wrong_args: order_by or ob_direction missing...");
				output += create_footer(wf);
				break;
			}
			order_by = args.get("order_by") + " " +args.get("ob_direction");
			//next statement must be "case publisher_by_filter_offset:"

		case publisher_by_filter_offset:
			if ( !args.containsKey("name_contains") || !args.containsKey("begin-offset") || !args.containsKey("end-offset") ){
				output += create_header(wf);
				output += get_error("wrong_args: name_contains or begin/end-offset missing...");
				output += create_footer(wf);
				break;
			}
			else {				
				output += create_header(wf);
				String filter = "";
				if (zooDB_implementation){
					filter = "name.toLowerCase().contains('"+args.get("name_contains").toLowerCase()+"')";
				}
				if (MongoDB_implementation){
					filter = args.get("name_contains");
				}
				if (BaseXDB_implementation){
					filter = args.get("name_contains");
				}
				output += publisher_by_filter_offset(filter,args.get("begin-offset"),args.get("end-offset"),order_by);
				output += create_footer(wf);
				break;
			}
		case publisher_by_id:
			if ( !args.containsKey("id") ){
				output += create_header(wf);
				output += get_error("wrong_args: id missing...");
				output += create_footer(wf);
				break;
			}
			output += create_header(wf);
			output += publisher_by_id(args.get("id"));
			output += create_footer(wf);
			break;

		case series_by_filter_offset_order:
			if ( !args.containsKey("order_by") || !args.containsKey("ob_direction") ){
				output += create_header(wf);
				output += get_error("wrong_args: order_by or ob_direction missing...");
				output += create_footer(wf);
				break;
			}
			order_by = args.get("order_by") + " " +args.get("ob_direction");
			//next statement must be "case series_by_filter_offset:"

		case series_by_filter_offset:
			if ( !args.containsKey("name_contains") || !args.containsKey("begin-offset") || !args.containsKey("end-offset") ){
				output += create_header(wf);
				output += get_error("wrong_args: name_contains or begin/end-offset missing...");
				output += create_footer(wf);
				break;
			}
			else {				
				output += create_header(wf);
				String filter = "";
				if (zooDB_implementation){
					filter = "name.toLowerCase().contains('"+args.get("name_contains").toLowerCase()+"')";
				}
				if (MongoDB_implementation){
					filter = args.get("name_contains");
				}
				if (BaseXDB_implementation){
					filter = args.get("name_contains");
				}
				output += series_by_filter_offset(filter,args.get("begin-offset"),args.get("end-offset"),order_by);
				output += create_footer(wf);
				break;
			}
		case series_by_id:
			if ( !args.containsKey("id") ){
				output += create_header(wf);
				output += get_error("wrong_args: id missing...");
				output += create_footer(wf);
				break;
			}
			output += create_header(wf);
			output += series_by_id(args.get("id"));
			output += create_footer(wf);
			break;
		case confEd_by_id:
			if ( !args.containsKey("id") ){
				output += create_header(wf);
				output += get_error("wrong_args: id missing...");
				output += create_footer(wf);
				break;
			}
			output += create_header(wf);
			output += confEd_by_id(args.get("id"));
			output += create_footer(wf);
			break;
		case conf_by_id:
			if ( !args.containsKey("id") ){
				output += create_header(wf);
				output += get_error("wrong_args: id missing...");
				output += create_footer(wf);
				break;
			}
			output += create_header(wf);
			output += conf_by_id(args.get("id"));
			output += create_footer(wf);
			break;	
		case conf_by_filter_offset_order:
			if ( !args.containsKey("order_by") || !args.containsKey("ob_direction") ){
				output += create_header(wf);
				output += get_error("wrong_args: order_by or ob_direction missing...");
				output += create_footer(wf);
				break;
			}
			order_by = args.get("order_by") + " " +args.get("ob_direction");
			//next statement must be "case conf_by_filter_offset:"

		case conf_by_filter_offset:
			if ( !args.containsKey("name_contains") || !args.containsKey("begin-offset") || !args.containsKey("end-offset") ){
				output += create_header(wf);
				output += get_error("wrong_args: name_contains or begin/end-offset missing...");
				output += create_footer(wf);
				break;
			}
			else {				
				output += create_header(wf);
				String filter = "";
				if (zooDB_implementation){
					filter = "name.toLowerCase().contains('"+args.get("name_contains").toLowerCase()+"')";
				}
				if (MongoDB_implementation){
					filter = args.get("name_contains");
				}
				if (BaseXDB_implementation){
					filter = args.get("name_contains");
				}
				output += conference_by_filter_offset(filter,args.get("begin-offset"),args.get("end-offset"),order_by);
				output += create_footer(wf);
				break;
			}

			/**
			 * Queries
			 */
		case find_co_authors:
			if ( !args.containsKey("name")){
				output += create_header(wf);
				output += get_error("wrong_args: name missing...");
				output += create_footer(wf);
				break;
			}
			output += create_header(wf);
			output += find_co_authors(args.get("name"));
			output += create_footer(wf);
			break;

		case find_author_distance_path:
			if ( !args.containsKey("name1") || !args.containsKey("name2")){
				output += create_header(wf);
				output += get_error("wrong_args: name1 or name2 missing...");
				output += create_footer(wf);
				break;
			}
			output += create_header(wf);
			output += find_author_distance_path(args.get("name1"),args.get("name2"));
			output += create_footer(wf);
			break;

		case avg_authors_per_inproceedings:
			output += create_header(wf);
			output += avg_authors_per_inproceedings();
			output += create_footer(wf);
			break;

		case count_publications_per_interval:
			if ( !args.containsKey("year1") || !args.containsKey("year2")){
				output += create_header(wf);
				output += get_error("wrong_args: year1 or year2 is missing...");
				output += create_footer(wf);
				break;
			}
			else {				
				output += create_header(wf);
				output += count_publications_per_interval(args.get("year1"),args.get("year2"));
				output += create_footer(wf);
				break;
			}

		case inproceedings_for_a_conference:
			if ( !args.containsKey("id")||!args.containsKey("mode") ){
				output += create_header(wf);
				output += get_error("wrong_args: id or mode missing...");
				output += create_footer(wf);
				break;
			}
			output += create_header(wf);
			output += inproceedings_for_a_conference(args.get("id"), args.get("mode"));
			output += create_footer(wf);
			break;

		case authors_editors_for_a_conference:
			if ( !args.containsKey("id")||!args.containsKey("mode") ){
				output += create_header(wf);
				output += get_error("wrong_args: id or mode missing...");
				output += create_footer(wf);
				break;
			}
			output += create_header(wf);
			output += authors_editors_for_a_conference(args.get("id"), args.get("mode"));
			output += create_footer(wf);
			break;	

		case person_is_author_and_editor:
			output += create_header(wf);
			output += person_is_author_and_editor();
			output += create_footer(wf);
			break;	

		case person_is_last_author:
			if ( !args.containsKey("id") ){
				output += create_header(wf);
				output += get_error("wrong_args: id missing...");
				output += create_footer(wf);
				break;
			}
			output += create_header(wf);
			output += person_is_last_author(args.get("id"));
			output += create_footer(wf);
			break;

		case publishers_whose_authors_in_interval:
			if ( !args.containsKey("year1") || !args.containsKey("year2")){
				output += create_header(wf);
				output += get_error("wrong_args: year1 or year2 is missing...");
				output += create_footer(wf);
				break;
			}
			else {				
				output += create_header(wf);
				output += publishers_whose_authors_in_interval(args.get("year1"),args.get("year2"));
				output += create_footer(wf);
				break;
			}

			/**
			 * Navigation and other stuff	
			 */

		case get_statistics:

			output += create_header(wf);
			output += get_statistics();
			output += create_footer(wf);
			break;

		case PAGE:
			if ( !args.containsKey("name")){
				output += create_header(wf);
				output += get_error("wrong_args: number missing...");
				output += create_footer(wf);
				break;
			}
			output += create_header(wf);
			output += get_page(args.get("name"));
			output += create_footer(wf);
			break;

		case MAIN:
			output += create_header(WebFunc.MAIN);
			output += get_main();
			output += create_footer(WebFunc.MAIN);
			break;
		case ERROR:
			output += create_header(WebFunc.ERROR);
			output += get_error(args.get("error_message"));
			output += create_footer(WebFunc.MAIN);
			break;

		default:
			output += create_header(WebFunc.MAIN);
			output += get_main();
			output += "<br>default case<br>";
			output += create_footer(WebFunc.MAIN);

		}
		//System.out.println(output);
		return output;
	}


	private String publishers_whose_authors_in_interval(String year1, String year2) {
		String Output ="";
		int y1, y2;
		try {
			y1 = Integer.valueOf(year1);
			y2 = Integer.valueOf(year2);

			if (y2<y1){
				Output += "<br>wrong intervall";
				return Output;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			y1 = 0;
			y2 = 0;

			Output += "<br>wrong intervall";
			return Output;

		}

		List<DivIO> publishers = myDB.IO_publishers_whose_authors_in_interval(y1, y2);
		for (DivIO publi: publishers){
			Output += publi.get_all();
		} 
		return Output;
	}

	private String get_statistics() {
		String Output ="";
		Output = myDB.IO_get_statistics();
		return Output;
	}

	private String person_is_last_author(String pers_id) {
		String Output = "<h3>(Task 13) person is last author</h3>";

		Output += "<p>Author with <b>id="+pers_id+"</b> is the last author in the following inproceedings:<p>";

		List<PublicationIO> publs = myDB.IO_person_is_last_author(pers_id);
		if (publs.isEmpty()){
			Output += "no publications found..";
		}
		else {
			for (PublicationIO publ: publs){
				Output += publ.get_all();
			}
		}
		return Output;
	}

	private String person_is_author_and_editor() {
		return myDB.IO_person_is_author_and_editor();
	}

	private String authors_editors_for_a_conference(String conf_id, String mode) {
		String Output ="";

		if (mode.equals("count")){
			String[] count = myDB.IO_authors_editors_for_a_conference(conf_id, mode);
			//Output += myDB.IO_authors_editors_for_a_conference(conf_id, mode);
			Output += count[0];
			return Output;
		}
		if (mode.equals("retrieve")){

			String[] editors_authors = myDB.IO_authors_editors_for_a_conference(conf_id, mode);
			Output += "<p></p>";
			Output += "<div>";
			Output += "<!-- Nav tabs -->";
			Output += "<ul class='nav nav-tabs' role='tablist'>";
			Output += "<li role='presentation' class='active'>";
			Output += "<a href='#Editors' aria-controls='home' role='tab' data-toggle='tab'>Editors</a></li>";
			Output += "<li role='presentation'><a href='#Authors' aria-controls='profile' role='tab' data-toggle='tab'>Authors</a>";
			Output += "</li>";
			Output += "</ul>";
			Output += "<!-- Tab panes -->";
			Output += "<div class='tab-content'>";

			Output += "<div role='tabpanel' class='tab-pane active' id='Editors'>";
			Output += "<!-- START Editors-->";
			Output += editors_authors[0];

			if ("".equals(editors_authors[0])){
				Output += "<h3>no editors...</h3>";
			}
			Output += "<!-- END Editors-->";
			Output += "</div>";

			Output += "<div role='tabpanel' class='tab-pane' id='Authors'>";
			Output += "<!-- START Authors-->";
			Output += editors_authors[1];
			if ("".equals(editors_authors[1])){
				Output += "<h3>no authors...</h3>";
			}
			Output += "<!-- END Authors-->";
			Output += "</div>";

			Output += "<!-- END inPROCEEDINGS--></div></div></div>";

			return Output;
		}
		else{
			Output+= "<br>wrong mode...<br>";
			return Output;
		}
	}

	private String inproceedings_for_a_conference(String conf_id, String mode) {
		String Output ="";
		if (mode.equals("retrieve")|| mode.equals("count")){
			Output ="";
			Output += myDB.IO_inproceedings_for_a_conference(conf_id, mode);
			return Output;
		}
		else{
			Output+= "<br>wrong mode...<br>";
			return Output;
		}
	}

	private String count_publications_per_interval(String year1, String year2) {
		String Output ="";
		int y1, y2;
		try {
			y1 = Integer.valueOf(year1);
			y2 = Integer.valueOf(year2);

			if (y2<y1){
				Output += "<br>wrong intervall";
				return Output;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			y1 = 0;
			y2 = 0;

			Output += "<br>wrong intervall";
			return Output;

		}

		Output += myDB.IO_count_publications_per_interval(y1, y2);

		return Output;
	}


	private String avg_authors_per_inproceedings() {
		String output = "";
		output = myDB.IO_avg_authors_per_inproceedings();

		return output;
	}

	private String find_author_distance_path(String name1, String name2) {
		String output = "";
		output += myDB.IO_find_author_distance_path(name1, name2);

		return output;
	}

	private String series_by_filter_offset(String filter, String beginoffset, String endoffset, String order_by) {
		int boff, eoff;
		try {
			boff = Integer.valueOf(beginoffset);
			eoff = Integer.valueOf(endoffset);
		}
		catch(Exception e) {
			e.printStackTrace();
			boff = 0;
			eoff = 0;
		}


		String Output = "<p></p>";//<p>filter="+filter+"<br>beginoffset="+boff+"<br>endoffset="+eoff+" order="+order_by+"</p>";
		String argument_next = "";
		String argument_prev = "";

		int offset = Math.abs(boff - eoff)+1;

		if ( !order_by.equals("")){
			String ob = order_by.split(" ")[0];
			String obd = order_by.split(" ")[1];
			argument_prev = "/test/?func=series_by_filter_offset_order&name_contains="+filter+"&begin-offset="+(boff-offset)+"&end-offset="+(eoff-offset)+"&order_by="+ob+"&ob_direction="+obd;
			argument_next = "/test/?func=series_by_filter_offset_order&name_contains="+filter+"&begin-offset="+(boff+offset)+"&end-offset="+(eoff+offset)+"&order_by="+ob+"&ob_direction="+obd;
		}
		else{
			argument_prev = "/test/?func=series_by_filter_offset&name_contains="+filter+"&begin-offset="+(boff-offset)+"&end-offset="+(eoff-offset);
			argument_next = "/test/?func=series_by_filter_offset&name_contains="+filter+"&begin-offset="+(boff+offset)+"&end-offset="+(eoff+offset);
		}
		if ( DB_Implementation.equals("zooDB")){
			argument_next = "";
			argument_prev = "";
		}

		Output += "<div>";
		Output += "<!-- Nav tabs -->";
		Output += "<ul class='nav nav-tabs' role='tablist'>";
		Output += "<li role='presentation' class='active'><a href='#Series' aria-controls='home' role='tab' data-toggle='tab'>Series</a></li>";
		Output += "<li role='presentation' style='float:right'><a href='"+argument_next+"' aria-controls='profile' role='tab'>next</a>";
		Output += "<li role='presentation' style='float:right'><a href='"+argument_prev +"' aria-controls='profile' role='tab'>previous</a>";
		Output += "</li>";
		Output += "</ul>";
		Output += "<!-- Tab panes -->";
		Output += "<div class='tab-content'>";
		Output += "<div role='tabpanel' class='tab-pane active' id='Series'>";
		Output += "<!-- START Series-->";

		List<DivIO> series = myDB.IO_get_series_by_filter_offset(filter, boff, eoff, order_by);
		for (DivIO ser: series){
			Output += ser.get_all();
		} 

		Output += "<!-- END Series-->";
		Output += "</div></div></div>";

		return Output;





	}

	private String series_by_id(String series_id) {
		DivIO series = myDB.IO_get_series_by_id(series_id);

		String output = series.get_all();
		return output;
	}

	private String publisher_by_filter_offset(String filter, String beginoffset, String endoffset, String order_by) {
		int boff, eoff;
		try {
			boff = Integer.valueOf(beginoffset);
			eoff = Integer.valueOf(endoffset);
		}
		catch(Exception e) {
			e.printStackTrace();
			boff = 0;
			eoff = 0;
		}


		String Output = "<p></p>";//<p>filter="+filter+"<br>beginoffset="+boff+"<br>endoffset="+eoff+" order="+order_by+"</p>";

		String argument_next = "";
		String argument_prev = "";

		int offset = Math.abs(boff - eoff)+1;

		if ( !order_by.equals("")){
			String ob = order_by.split(" ")[0];
			String obd = order_by.split(" ")[1];
			argument_prev = "/test/?func=publisher_by_filter_offset_order&name_contains="+filter+"&begin-offset="+(boff-offset)+"&end-offset="+(eoff-offset)+"&order_by="+ob+"&ob_direction="+obd;
			argument_next = "/test/?func=publisher_by_filter_offset_order&name_contains="+filter+"&begin-offset="+(boff+offset)+"&end-offset="+(eoff+offset)+"&order_by="+ob+"&ob_direction="+obd;
		}
		else{
			argument_prev = "/test/?func=publisher_by_filter_offset&name_contains="+filter+"&begin-offset="+(boff-offset)+"&end-offset="+(eoff-offset);
			argument_next = "/test/?func=publisher_by_filter_offset&name_contains="+filter+"&begin-offset="+(boff+offset)+"&end-offset="+(eoff+offset);
		}
		if ( DB_Implementation.equals("zooDB")){
			argument_next = "";
			argument_prev = "";
		}

		Output += "<div>";
		Output += "<!-- Nav tabs -->";
		Output += "<ul class='nav nav-tabs' role='tablist'>";
		Output += "<li role='presentation' class='active'><a href='#Publishers' aria-controls='home' role='tab' data-toggle='tab'>Publishers</a></li>";
		Output += "<li role='presentation' style='float:right'><a href='"+argument_next+"' aria-controls='profile' role='tab'>next</a>";
		Output += "<li role='presentation' style='float:right'><a href='"+argument_prev +"' aria-controls='profile' role='tab'>previous</a>";
		Output += "</li>";
		Output += "</ul>";
		Output += "<!-- Tab panes -->";
		Output += "<div class='tab-content'>";
		Output += "<div role='tabpanel' class='tab-pane active' id='Publishers'>";
		Output += "<!-- START Publishers-->";

		List<DivIO> publishers = myDB.IO_get_publisher_by_filter_offset(filter, boff, eoff, order_by);
		for (DivIO publisher: publishers){
			Output += publisher.get_all();
		} 

		Output += "<!-- END Publishers-->";
		Output += "</div></div></div>";

		return Output;
	}

	private String publisher_by_id(String publ_id) {
		DivIO publisher = myDB.IO_get_publisher_by_id(publ_id);

		String output = publisher.get_all();
		return output;
	}

	private String find_co_authors(String name) {
		String output = "";

		if (zooDB_implementation){
			output += "<p>The co-authors (and their publications) of <b>"+name+"</b> are:</p>";

			List<DivIO> persons = myDB.IO_find_co_authors(name);
			for (DivIO person: persons){
				output += person.get_all();
			} 		
		}
		if (MongoDB_implementation){
			output = "<p>The co-authors and <font color='red'>the common publications</font> with <b>"+name+"</b> are:</p>";

			output += myDB.IO_find_co_authors_returns_String(name);
		}

		if (BaseXDB_implementation){
			output += "<p>The co-authors (and their publications) of <b>"+name+"</b> are:</p>";

			List<DivIO> persons = myDB.IO_find_co_authors(name);
			for (DivIO person: persons){
				output += person.get_all();
			} 		
		}



		return output;
	}

	private  String person_by_id(String id) {
		DivIO pers = myDB.IO_get_person_by_id(id);

		String output = pers.get_all();
		return output;
	}
	private String delete_person_by_id(String id) {
		String output = myDB.IO_delete_get_person_by_id(id);

		return output;
	}

	private String conf_by_id(String id) {
		DivIO conf = myDB.IO_get_conf_by_id(id);

		String output = conf.get_all();
		return output;
	}

	private String confEd_by_id(String id) {
		//DivIO confEd = myDB.IO_get_confEd_by_id(id);

		//int id_length = id.length();
		//String Conference_id = id.substring(0, id_length-4);
		//int year = Integer.valueOf(id.substring(id_length-4));

		//DivIO confEd = ((T2DBProvider) myDB).IO_get_conferenceEdition_by_year(Conference_id,year);
		DivIO confEd = myDB.IO_get_confEd_by_id(id);
		String output = confEd.get_all();
		return output;
	}

	private String conference_by_filter_offset(String filter, String beginoffset, String endoffset, String order_by) {
		int boff, eoff;
		try {
			boff = Integer.valueOf(beginoffset);
			eoff = Integer.valueOf(endoffset);
		}
		catch(Exception e) {
			e.printStackTrace();
			boff = 0;
			eoff = 0;
		}

		String Output = "<p></p>";//<p>filter="+filter+"<br>beginoffset="+boff+"<br>endoffset="+eoff+" order="+order_by+"</p>";

		String argument_next = "";
		String argument_prev = "";

		int offset = Math.abs(boff - eoff)+1;

		if ( !order_by.equals("")){
			String ob = order_by.split(" ")[0];
			String obd = order_by.split(" ")[1];
			argument_prev = "/test/?func=conf_by_filter_offset_order&name_contains="+filter+"&begin-offset="+(boff-offset)+"&end-offset="+(eoff-offset)+"&order_by="+ob+"&ob_direction="+obd;
			argument_next = "/test/?func=conf_by_filter_offset_order&name_contains="+filter+"&begin-offset="+(boff+offset)+"&end-offset="+(eoff+offset)+"&order_by="+ob+"&ob_direction="+obd;
		}
		else{
			argument_prev = "/test/?func=conf_by_filter_offset&name_contains="+filter+"&begin-offset="+(boff-offset)+"&end-offset="+(eoff-offset);
			argument_next = "/test/?func=conf_by_filter_offset&name_contains="+filter+"&begin-offset="+(boff+offset)+"&end-offset="+(eoff+offset);
		}
		if ( DB_Implementation.equals("zooDB")){
			argument_next = "";
			argument_prev = "";
		}

		Output += "<div>";
		Output += "<!-- Nav tabs -->";
		Output += "<ul class='nav nav-tabs' role='tablist'>";
		Output += "<li role='presentation' class='active'><a href='#Conferences' aria-controls='home' role='tab' data-toggle='tab'>Conferences</a></li>";
		Output += "<li role='presentation' style='float:right'><a href='"+argument_next+"' aria-controls='profile' role='tab'>next</a>";
		Output += "<li role='presentation' style='float:right'><a href='"+argument_prev +"' aria-controls='profile' role='tab'>previous</a>";
		Output += "</li>";
		Output += "</ul>";
		Output += "<!-- Tab panes -->";
		Output += "<div class='tab-content'>";
		Output += "<div role='tabpanel' class='tab-pane active' id='Conferences'>";
		Output += "<!-- START Conferences-->";

		List<DivIO> confs = myDB.IO_get_conference_by_filter_offset(filter, boff, eoff, order_by);
		for (DivIO conf: confs){
			Output += conf.get_all();
		} 
		Output += "<!-- END Conferences-->";
		Output += "</div></div></div>";

		return Output;

	}

	private String person_by_filter_offset(String filter, String beginoffset, String endoffset, String order_by) {
		int boff, eoff;
		try {
			boff = Integer.valueOf(beginoffset);
			eoff = Integer.valueOf(endoffset);
		}
		catch(Exception e) {
			e.printStackTrace();
			boff = 0;
			eoff = 0;
		}

		String Output = "<p></p>";//<p>filter="+filter+"<br>beginoffset="+boff+"<br>endoffset="+eoff+" order="+order_by+"</p>";

		String argument_next = "";
		String argument_prev = "";

		int offset = Math.abs(boff - eoff)+1;

		if ( !order_by.equals("")){
			String ob = order_by.split(" ")[0];
			String obd = order_by.split(" ")[1];
			argument_prev = "/test/?func=person_by_filter_offset_order&name_contains="+filter+"&begin-offset="+(boff-offset)+"&end-offset="+(eoff-offset)+"&order_by="+ob+"&ob_direction="+obd;
			argument_next = "/test/?func=person_by_filter_offset_order&name_contains="+filter+"&begin-offset="+(boff+offset)+"&end-offset="+(eoff+offset)+"&order_by="+ob+"&ob_direction="+obd;
		}
		else{
			argument_prev = "/test/?func=person_by_filter_offset&name_contains="+filter+"&begin-offset="+(boff-offset)+"&end-offset="+(eoff-offset);
			argument_next = "/test/?func=person_by_filter_offset&name_contains="+filter+"&begin-offset="+(boff+offset)+"&end-offset="+(eoff+offset);
		}
		if ( DB_Implementation.equals("zooDB")){
			argument_next = "";
			argument_prev = "";
		}

		Output += "<div>";
		Output += "<!-- Nav tabs -->";
		Output += "<ul class='nav nav-tabs' role='tablist'>";
		Output += "<li role='presentation' class='active'><a href='#Persons' aria-controls='home' role='tab' data-toggle='tab'>Persons</a></li>";
		Output += "<li role='presentation' style='float:right'><a href='"+argument_next+"' aria-controls='profile' role='tab'>next</a>";
		Output += "<li role='presentation' style='float:right'><a href='"+argument_prev +"' aria-controls='profile' role='tab'>previous</a>";
		Output += "</li>";
		Output += "</ul>";
		Output += "<!-- Tab panes -->";
		Output += "<div class='tab-content'>";
		Output += "<div role='tabpanel' class='tab-pane active' id='Persons'>";
		Output += "<!-- START Persons-->";

		List<DivIO> persons = myDB.IO_get_person_by_filter_offset(filter, boff, eoff, order_by);
		for (DivIO person: persons){
			Output += person.get_all();
		} 
		Output += "<!-- END Persons-->";
		Output += "</div></div></div>";


		return Output;
	}

	private String publ_by_person_name_or_id(HashMap<String, String> args) {
		String Output = "<p></p>";//<p><b>filter</b>="+args+"<br></p>";

		//Output += "<a href='#first_proc'>go to Proceedings</a>, <a href='#first_inproc'>go to InProceedings</a><br>";

		List<PublicationIO> publs = myDB.IO_publ_by_person_name_or_id(args);
		Boolean first_proc = false;
		Boolean first_inproc = false;
		if (publs.isEmpty()){
			Output += "no publications found..";
		}
		else {
			Output += "<div>";
			Output += "<!-- Nav tabs -->";
			Output += "<ul class='nav nav-tabs' role='tablist'>";
			Output += "<li role='presentation' class='active'>";
			Output += "<a href='#Proceedings' aria-controls='home' role='tab' data-toggle='tab'>Proceedings</a></li>";
			Output += "<li role='presentation'><a href='#inProceedings' aria-controls='profile' role='tab' data-toggle='tab'>inProceedings</a>";
			//Output += "<li role='presentation' style='float:right'><a href='"+argument_next+"' aria-controls='profile' role='tab'>next</a>";
			//Output += "<li role='presentation' style='float:right'><a href='"+argument_prev +"' aria-controls='profile' role='tab'>previous</a>";
			Output += "</li>";
			Output += "</ul>";
			Output += "<!-- Tab panes -->";
			Output += "<div class='tab-content'>";
			Output += "<div role='tabpanel' class='tab-pane active' id='Proceedings'>";
			Output += "<!-- START PROCEEDINGS-->";
			for (PublicationIO publ: publs){
				if ( (publ.is_a_proceeding == true) && (first_proc == false) ){
					first_proc = true;
					//Output += "<h4 id='first_proc'>Proceedings</h4>";
				}
				if ( (publ.is_an_inproceeding == true) && (first_inproc == false) ){
					first_inproc = true;
					//Output += "<h4 id='first_inproc'>InProceedings</h4>";
					if (!first_proc){
						Output += "<h3> no proceedings...</h3>";
					}
					Output += "<!-- END PROCEEDINGS-->";
					Output += "</div>";
					Output += "<div role='tabpanel' class='tab-pane' id='inProceedings'>";
					Output += "<!-- START inPROCEEDINGS-->";
				}
				Output += publ.get_all();
			}
			//closes proceedings
			if (args.get("publ").equals("editored")){
				Output += "<!-- END PROCEEDINGS-->";
				Output += "</div>";
				Output += "<div role='tabpanel' class='tab-pane' id='inProceedings'>";
				Output += "<!-- START inPROCEEDINGS-->";
				Output += "<h3> no inroceedings...</h3>";

			}
			Output += "<!-- END inPROCEEDINGS--></div></div></div>";


		}

		return Output;

	}

	private String pupl_by_filter_offset(String filter, String beginoffset, String endoffset, String order_by) {
		int boff, eoff;
		try {
			boff = Integer.valueOf(beginoffset);
			eoff = Integer.valueOf(endoffset);
		}
		catch(Exception e) {
			e.printStackTrace();
			boff = 0;
			eoff = 0;
		}

		String Output = "<p></p>";//<p>filter="+filter+"<br>beginoffset="+boff+"<br>endoffset="+eoff+"</p>";

		List<PublicationIO> publs = myDB.IO_get_publ_by_filter_offset(filter, boff, eoff, order_by);

		if (zooDB_implementation){
			Output += "<div>";
			for (PublicationIO publ: publs){
				Output += publ.get_all();
			}
			Output += "</div>";
		}
		if (MongoDB_implementation || BaseXDB_implementation){
			Boolean first_proc = false;
			Boolean first_inproc = false;
			int proc_count = 0;
			int inproc_count = 0;
			//String temp_output="";
			if (publs.isEmpty()){
				Output += "no publications found..";
			}
			else {

				int offset = Math.abs(boff - eoff)+1;

				String argument_prev ="";
				String argument_next ="";
				if ( !order_by.equals("")){
					String ob = order_by.split(" ")[0];
					String obd = order_by.split(" ")[1];
					argument_prev = "/test/?func=pupl_by_title_offset_order&title="+filter+"&begin-offset="+(boff-offset)+"&end-offset="+(eoff-offset)+"&order_by="+ob+"&ob_direction="+obd;
					argument_next = "/test/?func=pupl_by_title_offset_order&title="+filter+"&begin-offset="+(boff+offset)+"&end-offset="+(eoff+offset)+"&order_by="+ob+"&ob_direction="+obd;
				}
				else{
					argument_prev = "/test/?func=pupl_by_title_offset&title="+filter+"&begin-offset="+(boff-offset)+"&end-offset="+(eoff-offset);
					argument_next = "/test/?func=pupl_by_title_offset&title="+filter+"&begin-offset="+(boff+offset)+"&end-offset="+(eoff+offset);
				}

				Output += "<div>";
				Output += "<!-- Nav tabs -->";
				Output += "<ul class='nav nav-tabs' role='tablist'>";
				Output += "<li role='presentation' class='active'>";
				Output += "<a href='#Proceedings' aria-controls='home' role='tab' data-toggle='tab'>Proceedings</a></li>";
				Output += "<li role='presentation'><a href='#inProceedings' aria-controls='profile' role='tab' data-toggle='tab'>inProceedings</a>";
				Output += "<li role='presentation' style='float:right'><a href='"+argument_next+"' aria-controls='profile' role='tab'>next</a>";
				Output += "<li role='presentation' style='float:right'><a href='"+argument_prev +"' aria-controls='profile' role='tab'>previous</a>";
				Output += "</li>";
				Output += "</ul>";
				Output += "<!-- Tab panes -->";
				Output += "<div class='tab-content'>";
				Output += "<div role='tabpanel' class='tab-pane active' id='Proceedings'>";
				Output += "<!-- START PROCEEDINGS-->";
				for (PublicationIO publ: publs){
					if ( publ.is_a_proceeding){
						proc_count++;
						if (first_proc == false){
							first_proc = true;
							//temp_output += "<h4 id='first_proc'>Proceedings</h4>";
						}
					}
					else if (publ.is_an_inproceeding){
						inproc_count++;
						if (first_inproc == false){
							if (proc_count==0){
								Output += "<h3>no proceedings found...</h3>";
							}
							Output += "<!-- END PROCEEDINGS-->";
							Output += "</div>";
							Output += "<div role='tabpanel' class='tab-pane' id='inProceedings'>";
							Output += "<!-- START inPROCEEDINGS-->";
							first_inproc = true;
							//temp_output += "<h4 id='first_inproc'>InProceedings</h4>";
						}
					}
					Output += publ.get_all();
				}
				//closes proceedings
				if (inproc_count==0){
					Output += "<!-- END PROCEEDINGS-->";
					Output += "</div>";
					Output += "<div role='tabpanel' class='tab-pane' id='inProceedings'>";
					Output += "<!-- START inPROCEEDINGS-->";
					Output += "<h3> no inproceedings...</h3>";
				}
				Output += "<!-- END inPROCEEDINGS--></div></div></div>";
			}
		}

		return Output;

	}

	public String get_error(String error_code) {
		String Output = "<br>" +error_code +"<br>";
		return Output;
	}


	private String get_main() {
		String path = "../task1/src/main/java/eth/infsys/webserver/index.html";
		String output = "";
		try {
			output = readFile(path, Charset.defaultCharset());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}


	private String get_page(String name) {
		String path = "../task1/src/main/java/eth/infsys/webserver/page_"+name+".html";
		String output = "";
		try {
			output = readFile(path, Charset.defaultCharset());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			output = get_main();
		}
		return output;
	}

	private String create_header(WebFunc wf){
		String path1 = "../task1/src/main/java/eth/infsys/webserver/header_part1.html";
		String path2 = "../task1/src/main/java/eth/infsys/webserver/header_part2.html";
		String output = "";
		try {
			output = readFile(path1, Charset.defaultCharset());
			output += DB_Implementation;
			output += readFile(path2, Charset.defaultCharset());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}

	//private String create_header(WebFunc wf){
	/*switch (wf) {
		case inproceeding_by_id:
			return "<html><head><meta charset='utf-8'></head><body><a href='/test/?func=MAIN'>HOME</a>";
		case proceeding_by_id:
			return "<html><head><meta charset='utf-8'></head><body><a href='/test/?func=MAIN'>HOME</a>";
		case publication_by_id:
			return "<html><head><meta charset='utf-8'></head><body><a href='/test/?func=MAIN'>HOME</a>";
		}*/
	//return "<html><head><meta http-equiv='content-type' content='text/html; charset=UTF-8'></head><body><h1>"+DB_Implementation+"</h1><a href='/test/?func=MAIN'>HOME</a><FORM><INPUT Type='button' VALUE='Back' onClick='history.go(-1);return true;'></FORM><br>";
	//}
	private String create_footer(WebFunc wf){
		/*switch (wf) {
		case inproceeding_by_id:
			return "</body></html>";
		case proceeding_by_id:
			return "</body></html>";
		case publication_by_id:
			return "</body></html>";
		}*/
		return "</div></body></html>";
	}

	private String inproceeding_by_id(String id) {
		PublicationIO publ = null;
		if (zooDB_implementation){
			publ = myDB.IO_get_publication_by_id(id);
		}
		if (MongoDB_implementation){
			publ = ((T2DBProvider) myDB).IO_get_inproceedings_by_id(id);
		}
		if (BaseXDB_implementation){
			publ = myDB.IO_get_publication_by_id(id);
		}

		String output = publ.get_all();
		return output;
	}

	private String proceeding_by_id(String id) {
		PublicationIO publ = null;
		if (zooDB_implementation){
			publ = myDB.IO_get_publication_by_id(id);
		}
		if (MongoDB_implementation){
			publ = ((T2DBProvider) myDB).IO_get_proceedings_by_id(id);
		}
		if (BaseXDB_implementation){
			publ = myDB.IO_get_publication_by_id(id);
		}

		String output = publ.get_all();
		return output;
	}

	private String publication_by_id(String id) {
		PublicationIO publ = myDB.IO_get_publication_by_id(id);

		String output = publ.get_all();
		return output;
	}


	/**
	 * data manipulations
	 * @param <T>
	 * 
	 * 
	 * 
	 * 
	 */
	private <T> String getConstraintViolationOutput(Set<ConstraintViolation<T>> constraintViolations){
		String Output ="";

		int numFailedConstraints = constraintViolations.size();
		if (numFailedConstraints > 0){
			Output += "<h3>There are some problems with the input!!!</h3>";
			Output += "<ol type='1'>";
			for( ConstraintViolation<T> constVio: 	constraintViolations){
				String mess = constVio.getMessage();
				String path = constVio.getPropertyPath().toString();
				Output += "<li> Field = "+path+"; "+mess+"</li>";
			}
			Output += "</ol><hr>";
		}
		else {
			Output += "There are no problems...<br>";
		}
		return Output;
	}

	private <T> HashMap<String,String> getConstraintViolationHashMap(Set<ConstraintViolation<T>> constraintViolations){
		//get "all problem fields and messages as HashMap"
		HashMap<String,String> problemFieldsMessages = new HashMap<String, String>();
		for( ConstraintViolation<T> constVio: 	constraintViolations){
			problemFieldsMessages.put(constVio.getPropertyPath().toString(), constVio.getMessage());
		}
		return problemFieldsMessages;
	}


	private String add_inproc(HashMap<String, String> args) {

		String Output ="";

		InProceedings_simple_input inproc = new InProceedings_simple_input(args, "add", myDB);

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( inproc );

		int numFailedConstraints = constraintViolations.size();
		if (numFailedConstraints > 0){

			//get "problems as html output"	
			Output += getConstraintViolationOutput(constraintViolations);

			Output += "<!-- Beispiel Fehler --><form><div class='form-group'>";
			Output += "<label for='add_inproc'>add inproceedings</label>";
			Output += "<input type='hidden' id='add_inproc' name='func' value='add_inproc'>";

			Output += createFormBodyWithAnnotations(createInprocAttributInputList(inproc, args), constraintViolations);


			Output += "<button type='submit' class='btn btn-default'>add inproceedings</button>";
			Output += "</div></form>";

		} else {
			//create inproceedings....
			Output += "<h3>Inproceedings created...</h3><hr>";
			inproc.mode = "log";
			Output += myDB.batch_createInProceedings(inproc);
			
			Output += "<hr>";
			
			Output += myDB.IO_get_publication_by_id(inproc.id).get_all();
			//Output += (new PublicationIO(inproc)).get_all();
		}

		return Output;
	}

	private String update_inproc(HashMap<String, String> args) {

		String Output ="";

		InProceedings_simple_input inproc = new InProceedings_simple_input(args, "update", myDB);

		Set<ConstraintViolation<InProceedings_simple_input>> constraintViolations = validator.validate( inproc );

		int numFailedConstraints = constraintViolations.size();
		if (numFailedConstraints > 0){
			//get "problems as html output"	
			Output += getConstraintViolationOutput(constraintViolations);

			Output += "<form><div class='form-group'>";
			Output += "<label for='update_inproc'>update inproceedings</label>";
			Output += "<input type='hidden' id='update_inproc' name='func' value='update_inproc'>";

			Output += createFormBodyWithAnnotations(createInprocAttributInputList(inproc, args), constraintViolations);


			Output += "<button type='submit' class='btn btn-default'>update inproceedings</button>";
			Output += "</div></form>";
			
			
		} else {
			//update inproceedings....

			//tbd

			Output += "<h3>Inproceedings updated...</h3><hr>";
			//print input
			Output += (new PublicationIO(inproc)).get_all();
		}

		return Output;
	}

	private String add_proc(HashMap<String, String> args) {

		String Output ="";

		Proceedings_simple_input proc = new Proceedings_simple_input(args, "add", myDB);

		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( proc );

		int numFailedConstraints = constraintViolations.size();
		if (numFailedConstraints > 0){
			//get "problems as html output"	
			Output += getConstraintViolationOutput(constraintViolations);

			Output += "<form><div class='form-group'>";
			Output += "<label for='add_proc'>add proceedings</label>";
			Output += "<input type='hidden' id='add_proc' name='func' value='add_proc'>";

			Output += createFormBodyWithAnnotations(createProcAttributInputList(proc, args), constraintViolations);


			Output += "<button type='submit' class='btn btn-default'>add proceedings</button>";
			Output += "</div></form>";
		} else {
			//create proceedings....
			Output += "<h3>Proceedings created...</h3><hr>";
			proc.mode = "log";
			Output += myDB.batch_createProceedings(proc);
			
			Output += "<hr>";
			
			Output += myDB.IO_get_publication_by_id(proc.id).get_all();
			//Output += (new PublicationIO(proc)).get_all();
		}

		return Output;
	}

	private String update_proc(HashMap<String, String> args) {

		String Output ="";

		Proceedings_simple_input proc = new Proceedings_simple_input(args, "update", myDB);

		Set<ConstraintViolation<Proceedings_simple_input>> constraintViolations = validator.validate( proc );

		int numFailedConstraints = constraintViolations.size();
		if (numFailedConstraints > 0){
			//get "problems as html output"	
			Output += getConstraintViolationOutput(constraintViolations);

			Output += "<form><div class='form-group'>";
			Output += "<label for='update_proc'>update proceedings</label>";
			Output += "<input type='hidden' id='update_proc' name='func' value='update_proc'>";

			Output += createFormBodyWithAnnotations(createProcAttributInputList(proc, args), constraintViolations);


			Output += "<button type='submit' class='btn btn-default'>update proceedings</button>";
			Output += "</div></form>";
		} else {
			//update proceedings....

			//tbd

			Output += "<h3>Proceedings updated...</h3><hr>";
			Output += (new PublicationIO(proc)).get_all();
		}

		return Output;
	}



	private <T> String createFormBodyWithAnnotations(List<Pair<String,String>> attributInputList, Set<ConstraintViolation<T>> constraintViolations){
		String Output ="";

		HashMap<String,String> problemFieldsMessages = getConstraintViolationHashMap(constraintViolations);

		for (Pair<String,String> attributInput: attributInputList){
			String attribut = attributInput.getKey();
			String input = attributInput.getValue();
			if (problemFieldsMessages.containsKey(attribut)){
				String tooltip = problemFieldsMessages.get(attribut);
				Output += getHasError(input, tooltip);
			}
			else {
				Output += getHasNoError(input);
			}
		}
		return Output;
	}

	private List<Pair<String,String>> createInprocAttributInputList(InProceedings_simple_input inproc, HashMap<String, String> args){

		List<Pair<String,String>> attributInputList = new ArrayList<Pair<String,String>>();

		String temp = "";
		
		if ( (temp = inproc.title) == null ){
			temp = "";
		}	
		attributInputList.add(new Pair("title","type='text' class='form-control' placeholder='title' name='title' value='"+temp+"'>"));
		
		if ( (temp = inproc.id) == null ){
			temp = "";
		}
		attributInputList.add(new Pair("id","type='text' class='form-control' placeholder='id/key' name='id' value='"+temp+"'>"));
		
		//year cannot be null
		if ( inproc.year == 0 ){
			temp = "";
			attributInputList.add(new Pair("year","type='text' class='form-control' placeholder='year' name='year' value='"+temp+"'>"));
		}
		else {
			attributInputList.add(new Pair("year","type='text' class='form-control' placeholder='year' name='year' value='"+inproc.year+"'>"));
		}
		
		
		if ( (temp = inproc.electronicEdition) == null ){
			temp = "";
		}
		attributInputList.add(new Pair("electronicEdition","type='text' class='form-control' placeholder='ee' name='ee' value='"+temp+"'>"));
		
		if ( (temp = inproc.conferenceName) == null ){
			temp = "";
		}
		attributInputList.add(new Pair("conferenceName","type='text' class='form-control' placeholder='conference' name='conference' value='"+temp+"'>"));
		
		if ( (temp = args.get("authors")) == null ){
			temp = "";
		}
		attributInputList.add(new Pair("authors","type='text' class='form-control' placeholder='author 1;author2;...;author n' name='authors' value='"+temp+"'>"));
		
		if ( (temp = inproc.note) == null ){
			temp = "";
		}
		attributInputList.add(new Pair("note","type='text' class='form-control' placeholder='note = {Draft|Submitted|Accepted|Published}' name='note' value='"+temp+"'>"));
		
		if ( (temp = inproc.pages) == null ){
			temp = "";
		}
		attributInputList.add(new Pair("pages","type='text' class='form-control' placeholder='pages' name='pages' value='"+temp+"'>"));
		
		if ( (temp = inproc.crossref) == null ){
			temp = "";
		}
		attributInputList.add(new Pair("crossref","type='text' class='form-control' placeholder='proceedings id/key' name='proc_id' value='"+temp+"'>"));

		return attributInputList;
	}
	
	
	private List<Pair<String,String>> createProcAttributInputList(Proceedings_simple_input proc, HashMap<String, String> args){

		List<Pair<String,String>> attributInputList = new ArrayList<Pair<String,String>>();

		String temp = "";
		
		if ( (temp = proc.title) == null ){
			temp = "";
		}	
		attributInputList.add(new Pair("title","type='text' class='form-control' placeholder='title' name='title' value='"+temp+"'>"));
		
		if ( (temp = proc.id) == null ){
			temp = "";
		}
		attributInputList.add(new Pair("id","type='text' class='form-control' placeholder='id/key' name='id' value='"+temp+"'>"));
		
		//year cannot be null
		attributInputList.add(new Pair("year","type='text' class='form-control' placeholder='year' name='year' value='"+proc.year+"'>"));
		
		if ( (temp = proc.electronicEdition) == null ){
			temp = "";
		}
		attributInputList.add(new Pair("electronicEdition","type='text' class='form-control' placeholder='ee' name='ee' value='"+temp+"'>"));
		
		
		if ( (temp = proc.conferenceName) == null ){
			temp = "";
		}
		attributInputList.add(new Pair("conferenceName","type='text' class='form-control' placeholder='conference' name='conference' value='"+temp+"'>"));
		
		//conferenceEdition cannot be null
		attributInputList.add(new Pair("conferenceEdition","type='text' class='form-control' placeholder='conference-edition' name='conference-edition' value='"+proc.conferenceEdition+"'>"));
		
		if ( (temp = args.get("editors")) == null ){
			temp = "";
		}
		attributInputList.add(new Pair("editors","type='text' class='form-control' placeholder='editor 1;editor 2;...;editor n' name='editors' value='"+temp+"'>"));
		
		if ( (temp = proc.note) == null ){
			temp = "";
		}
		attributInputList.add(new Pair("note","type='text' class='form-control' placeholder='note' name='note' value='"+temp+"'>"));
		
		//number cannot be null
		attributInputList.add(new Pair("number","type='text' class='form-control' placeholder='number' name='number' value='"+proc.number+"'>"));
		
		
		if ( (temp = proc.publisher) == null ){
			temp = "";
		}
		attributInputList.add(new Pair("publisher","type='text' class='form-control' placeholder='publisher' name='publisher' value='"+temp+"'>"));
		
		if ( (temp = proc.isbn) == null ){
			temp = "";
		}
		attributInputList.add(new Pair("isbn","type='text' class='form-control' placeholder='isbn' name='isbn' value='"+temp+"'>"));
		
		if ( (temp = proc.volume) == null ){
			temp = "";
		}
		attributInputList.add(new Pair("volume","type='text' class='form-control' placeholder='volume' name='volume' value='"+temp+"'>"));
		
		if ( (temp = proc.series) == null ){
			temp = "";
		}
		attributInputList.add(new Pair("series","type='text' class='form-control' placeholder='series' name='series' value='"+temp+"'>"));

		return attributInputList;
	}





	private String getHasError(String form, String tooltip){
		String Output = "<div class='form-group has-error has-feedback'>";
		Output += "<input data-toggle='tooltip' data-placement='bottom' title='"+tooltip+"' "+form;
		Output += "<span class='glyphicon glyphicon-remove form-control-feedback'></span></div>";
		return Output;
	}

	private String getHasNoError(String form){
		String Output = "<div class='form-group has-success has-feedback'>";
		Output += "<input data-toggle='tooltip' data-placement='bottom' title='OK' "+form;
		Output += "<span class='glyphicon glyphicon-ok form-control-feedback'></span></div>";
		return Output;

	}


}