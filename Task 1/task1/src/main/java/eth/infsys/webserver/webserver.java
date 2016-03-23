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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import eth.infsys.group1.dbspec.*;

import eth.infsys.group1.task1.T1DBProvider;
import eth.infsys.group1.xmlparser.PublicationIO;
import javafx.util.Pair;

public class webserver {

	static private T1DBProvider myDB;
	static private HttpServer server;
	public static void main(String[] args) throws Exception {
		//Choose DB
		String dbName = "Project1_ZooDB_new.zdb";
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

			//String[] args = get_args(t.getRequestURI().getQuery());
			String user_input = t.getRequestURI().getQuery();
			System.out.println("user_input 777="+user_input);

			//seems to "auto-decode" the input
			//java.net.URLDecoder.decode(user_input, "UTF-8");

			System.out.println("call backend...");

			String response = "";
			try {
				response = backend(user_input);

			}
			catch(Exception e) {
				e.printStackTrace();
			}

			byte[] resp = response.getBytes();
			t.sendResponseHeaders(200, resp.length);
			OutputStream os = t.getResponseBody();
			os.write(resp);
			os.close();
		}
		
	}
	
	private static Pair<String,String> parse_name_value(String arg) {
        final Pattern pattern = Pattern.compile("(?<name>^[a-zA-Z0-9]+)=(?<value>.+)");
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
				args_out.put("error_message", "wrong_arguments");				
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
		
		String key ="";
		
		
		



		WebFunc wf = WebFunc.fromString(func);
		switch (wf) {
		case inproceeding_by_id:
			output += create_header(wf);
			output += inproceeding_by_id(args.get("key"));
			output += create_footer(wf);
			break;
		case proceeding_by_id:
			output += create_header(wf);
			output += proceeding_by_id(args.get("key"));
			output += create_footer(wf);
			break;
		case publication_by_id:
			output += create_header(wf);
			output += publication_by_id(args.get("key"));
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
			output += create_footer(WebFunc.MAIN);

		}
		System.out.println(output);
		return output;
	}


	public static String get_error(String error_code) {
		String Output = "<br>" +error_code +"<br>";
		return Output;
	}


	private static String get_main() {
		String path = "../task1/src/test/java/eth/infsys/group1/task1/index.html";
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