package eth.infsys.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import eth.infsys.group1.dbspec.*;

import eth.infsys.group1.task1.T1DBProvider;
import eth.infsys.group1.xmlparser.DivIO;
import eth.infsys.group1.xmlparser.PublicationIO;
import javafx.util.Pair;

public class webserver {

	static private T1DBProvider myDB;
	static private HttpServer server;
	public static void main(String[] args) throws Exception {
		//Choose DB
		//String dbName = "Project1_ZooDB_new.zdb";
		String dbName = "Project1_ZooDB_updated_confEd_keys.zdb";

		//T1DBProvider myDB = new T1DBProvider(dbName, T1DBProvider.OPEN_DB_OVERRIDE);
		myDB = new T1DBProvider(dbName, T1DBProvider.OPEN_DB_APPEND);


		server = HttpServer.create(new InetSocketAddress(8000), 0);
		server.createContext("/test", new MyHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
	}

	static class MyHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			
			String user_input = java.net.URLDecoder.decode(t.getRequestURI().getQuery(), "UTF-8");
			System.out.println("user_input after decoding: "+user_input);
			
			System.out.println("call backend...");
			String response = "";
			try {
				response = backend(user_input);

			}
			catch(Exception e) {
				e.printStackTrace();
			}

			System.out.println("returned from backend...");
			byte[] resp = response.getBytes();
			t.sendResponseHeaders(200, resp.length);
			OutputStream os = t.getResponseBody();
			os.write(resp);
			os.close();
		}

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
			if ( name_value == null ){
				args_out.put("func", "ERROR");
				args_out.put("error_message", "wrong_argument_syntax");				
				return args_out;
			}
			args_out.put(name_value.getKey(), name_value.getValue());
		}
		return args_out;
	}

	static private String backend(String user_input) {
		System.out.println("bin im backend...");
		System.out.println("call get args...");
		HashMap<String,String> args = get_args(user_input);
		System.out.println("zurück von get args");


		String output = "";
		String func = args.get("func");
		String order_by = ""; //default order

		String key ="";


		WebFunc wf = WebFunc.fromString(func);
		switch (wf) {
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
				String filter = "title.toLowerCase().contains('"+args.get("title").toLowerCase()+"')";
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
			output += publ_by_person(args);
			output += create_footer(wf);
			break;
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
				String filter = "name.toLowerCase().contains('"+args.get("name_contains").toLowerCase()+"')";
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
				String filter = "name.toLowerCase().contains('"+args.get("name_contains").toLowerCase()+"')";
				output += conference_by_filter_offset(filter,args.get("begin-offset"),args.get("end-offset"),order_by);
				output += create_footer(wf);
				break;
			}

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
		case MAIN:
			//output += create_header(WebFunc.MAIN);
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
			//output += get_main();
			output += "<br>default case<br>";
			output += create_footer(WebFunc.MAIN);

		}
		//System.out.println(output);
		return output;
	}

	private static String find_co_authors(String name) {
		String output = "";
		List<DivIO> persons = myDB.IO_find_co_authors(name);
		for (DivIO person: persons){
			output += person.get_all() + "<br><br>";
		} 

		return output;
	}

	private static String person_by_id(String id) {
		DivIO pers = myDB.IO_get_person_by_id(id);

		String output = pers.get_all();
		return output;
	}

	private static String conf_by_id(String id) {
		DivIO conf = myDB.IO_get_conf_by_id(id);

		String output = conf.get_all();
		return output;
	}

	private static String confEd_by_id(String id) {
		DivIO confEd = myDB.IO_get_confEd_by_id(id);

		String output = confEd.get_all();
		return output;
	}

	private static String conference_by_filter_offset(String filter, String beginoffset, String endoffset, String order_by) {
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

		String Output = "<br>filter="+filter+"<br>beginoffset="+boff+"<br>endoffset="+eoff+" order="+order_by+"<br>";

		List<DivIO> confs = myDB.IO_get_conference_by_filter_offset(filter, boff, eoff, order_by);
		for (DivIO conf: confs){
			Output += conf.get_all() + "<br><br>";
		} 
		return Output;
	}

	private static String person_by_filter_offset(String filter, String beginoffset, String endoffset, String order_by) {
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

		String Output = "<br>filter="+filter+"<br>beginoffset="+boff+"<br>endoffset="+eoff+" order="+order_by+"<br>";

		
		List<DivIO> persons = myDB.IO_get_person_by_filter_offset(filter, boff, eoff, order_by);
		for (DivIO person: persons){
			Output += person.get_all() + "<br><br>";
		} 
		
		return Output;
	}

	private static String publ_by_person(HashMap<String, String> args) {
		String Output = "<br>filter="+args+"<br>";
		
		
		List<PublicationIO> publs = myDB.IO_get_publ_by_person(args);
		for (PublicationIO publ: publs){
			Output += publ.get_all() + "<br><br>";
		}
		Output += "blub";
		
		return Output;

	}

	private static String pupl_by_filter_offset(String filter, String beginoffset, String endoffset, String order_by) {
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

		String Output = "<br>filter="+filter+"<br>beginoffset="+boff+"<br>endoffset="+eoff+"<br>";

		List<PublicationIO> publs = myDB.IO_get_publ_by_filter_offset(filter, boff, eoff, order_by);
		for (PublicationIO publ: publs){
			Output += publ.get_all() + "<br><br>";
		} 
		return Output;
	}

	public static String get_error(String error_code) {
		String Output = "<br>" +error_code +"<br>";
		return Output;
	}


	private static String get_main() {
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

	private static String create_header(WebFunc wf){
		switch (wf) {
		case inproceeding_by_id:
			return "<html><head><meta charset='utf-8'></head><body><a href='/test/?func=MAIN'>HOME</a>";
		case proceeding_by_id:
			return "<html><head><meta charset='utf-8'></head><body><a href='/test/?func=MAIN'>HOME</a>";
		case publication_by_id:
			return "<html><head><meta charset='utf-8'></head><body><a href='/test/?func=MAIN'>HOME</a>";
		}
		return "<html><head><meta charset='utf-8'></head><body><a href='/test/?func=MAIN'>HOME</a>";
	}
	private static String create_footer(WebFunc wf){
		switch (wf) {
		case inproceeding_by_id:
			return "</body></html>";
		case proceeding_by_id:
			return "</body></html>";
		case publication_by_id:
			return "</body></html>";
		}
		return "</body></html>";
	}

	//private static String inproceeding_by_id(String id, QueryParameter sort_by) {
	private static String inproceeding_by_id(String id) {
		PublicationIO publ = myDB.IO_get_publication_by_id(id);
		String output = publ.get_all();
		return output;
	}

	private static String proceeding_by_id(String id) {
		PublicationIO publ = myDB.IO_get_publication_by_id(id);
		String output = publ.get_all();
		return output;
	}

	private static String publication_by_id(String id) {
		PublicationIO publ = myDB.IO_get_publication_by_id(id);

		String output = publ.get_all();
		return output;
	}


	static String readFile(String path, Charset encoding) 
			throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

}