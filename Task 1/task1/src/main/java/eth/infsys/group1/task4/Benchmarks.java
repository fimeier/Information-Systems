package eth.infsys.group1.task4;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xquery.XQException;

import eth.infsys.webserver.Webserver;
import javafx.util.Pair;


@SuppressWarnings("restriction")
public class Benchmarks {

	//Parameters

	//after server start....
	private static int sleep_time = 10000; //4000;
	//"num cold runs"
	private static int cold_runs = 5;
	//offset for query_num
	private static int offset_cold_run = 1000;
	private static int num_runs = 30;

	private static String dbName;
	private static String User = "admin";
	private static String PW = "admin";
	private static Boolean Benchmark_mode = true;

	/** Queries as defined in static html
	 * 
	 * 
	 * 
	 */
	static String q1 = "publication_by_id&key=conf/gbrpr/CuissartH05";
	static String q2 = "func=pupl_by_title_offset&title=Algorithm&begin-offset=1&end-offset=5";
	static String q3 = "func=pupl_by_title_offset_order&title=intel&begin-offset=1&end-offset=5&order_by=title&ob_direction=descending";
	static String q4 = "func=find_co_authors&name=Moira C. Norrie";
	static String q5 = "func=find_author_distance_path&name1=A. W. Roscoe&name2=Angel J. Rico-Diaz";
	static String q6 = "func=avg_authors_per_inproceedings";
	static String q7 = "func=count_publications_per_interval&year1=2005&year2=2008";
	static String q8 = "func=inproceedings_for_a_conference&id=conference/IEEE Conference on Computational Complexity&mode=count";
	static String q9 = "func=authors_editors_for_a_conference&id=conference/IEEE Conference on Computational Complexity&mode=count";
	static String q10 = "func=authors_editors_for_a_conference&id=conference/IEEE Conference on Computational Complexity&mode=retrieve";
	static String q11 = "func=inproceedings_for_a_conference&id=conference/IEEE Conference on Computational Complexity&mode=retrieve";
	static String q12 = "func=person_is_author_and_editor";
	static String q13 = "func=person_is_last_author&id=person/Moira C. Norrie";
	static String q14 = "func=publishers_whose_authors_in_interval&year1=2005&year2=2008";





	public static void main(String[] args) throws InterruptedException, XQException {

		List<List<Pair<Integer,Pair<Double,Integer>>>> all_results = new ArrayList<>();

		String [] implementations = new String[3];
		implementations[0] = "zooDB";
		implementations[1] = "MongoDB";
		implementations[2] = "BaseXDB";

		//do the benchmarks
		for (String DB_Implementation: implementations){
			List<Pair<Integer,Pair<Double,Integer>>> results = benchmark_db(DB_Implementation);
			all_results.add(results);
		}

		//print the benchmark overview
		int run = 1;
		int i=0;
		for (List<Pair<Integer,Pair<Double,Integer>>> res: all_results){
			System.out.println("---------------------new implementation---------------------");
			System.out.println("run#Implementation#Query-Num#timings#responsesize#");
			for (Pair<Integer,Pair<Double,Integer>> qx_avg_size: res){
				int query_num = qx_avg_size.getKey();
				double avg = qx_avg_size.getValue().getKey();
				double size = qx_avg_size.getValue().getValue();
				System.out.println(run+"#"+implementations[i]+"#"+query_num+"#"+avg+"#"+size);
			}
			i++;
		}
	}

	private static List<Pair<Integer,Pair<Double,Integer>>> benchmark_db(String DB_Implementation) throws XQException, InterruptedException{

		String user_input = "";
		int response_length = 0;

		My_timer timer = new My_timer();
		Webserver Benchmark_Server = null;


		List<Pair<Integer,Pair<Double,Integer>>> results = new ArrayList<>();
		List<Pair<Integer,String>> queries = new ArrayList<>();
		queries.add(new Pair<Integer, String>(1,q1));
		queries.add(new Pair<Integer, String>(2,q2));
		queries.add(new Pair<Integer, String>(3,q3));
		queries.add(new Pair<Integer, String>(4,q4));
		queries.add(new Pair<Integer, String>(5,q5));
		queries.add(new Pair<Integer, String>(6,q6));
		queries.add(new Pair<Integer, String>(7,q7));
		queries.add(new Pair<Integer, String>(8,q8));
		queries.add(new Pair<Integer, String>(9,q9));
		queries.add(new Pair<Integer, String>(10,q10));
		queries.add(new Pair<Integer, String>(11,q11));
		queries.add(new Pair<Integer, String>(12,q12));
		queries.add(new Pair<Integer, String>(13,q13));
		queries.add(new Pair<Integer, String>(14,q14));


		switch(DB_Implementation){
		case "zooDB":
			dbName = "Project1_test.zdb";
			System.out.println("Start webserver for zooDB="+dbName +" in Benchmark-Mode");
			Benchmark_Server = new Webserver(DB_Implementation, dbName, User, PW, Benchmark_mode);
			Thread.sleep(sleep_time);
			break;
		case "MongoDB":
			dbName = "myDBLPv2";
			System.out.println("Start webserver for MongoDB="+dbName +" in Benchmark-Mode");
			Benchmark_Server = new Webserver(DB_Implementation, dbName, User, PW, Benchmark_mode);
			Thread.sleep(sleep_time);
			break;
		case "BaseXDB":
			dbName = "big_xml";
			System.out.println("Start webserver for BaseXDB="+dbName +" in Benchmark-Mode");
			Benchmark_Server = new Webserver(DB_Implementation, dbName, User, PW, Benchmark_mode);
			Thread.sleep(sleep_time);
			break;
		default:
			System.out.println("Error: wrong DB_Implementation...");
			System.exit(666);
		}

		for(Pair<Integer,String> qn_query:  queries){
			int query_num = qn_query.getKey();
			user_input = qn_query.getValue();

			timer.reset();

			System.out.println("--------Executing Query-"+query_num+" for "+DB_Implementation+"--------");


			//no cold runs
			System.out.println("<cold runs>");
			for(int i = 0; i < cold_runs; i++){
				response_length = Benchmark_Server.benchmark(timer, user_input);
				System.out.println("last_run="+timer.get_last_run()+ " (runs="+timer.get_runs()+")");
			}
			System.out.println("</cold runs>");
			//add max cold runtime
			Pair<Integer,Pair<Double,Integer>> max_cold_qx = new Pair<Integer, Pair<Double, Integer>>(query_num+offset_cold_run,new Pair<Double, Integer>(timer.get_max_run(),response_length));
			results.add(max_cold_qx);
			//reset timer
			timer.reset();

			//do the benchmarks
			for(int i = 0; i < num_runs; i++){
				response_length = Benchmark_Server.benchmark(timer, user_input);
				System.out.println("last_run="+timer.get_last_run()+ " (runs="+timer.get_runs()+")");
			}

			//add avg run_time
			Pair<Integer,Pair<Double,Integer>> res_qx = new Pair<Integer, Pair<Double, Integer>>(query_num,new Pair<Double, Integer>(timer.get_avg_run(),response_length));
			results.add(res_qx);
		}

		System.out.println("all completed...");

		return results;
	}

}
