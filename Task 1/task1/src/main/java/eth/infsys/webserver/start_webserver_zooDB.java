package eth.infsys.webserver;

public class start_webserver_zooDB {

	public static void main(String[] args) throws Exception {

		/**
		 * Task 1 - ZooDB
		 * 
		 *
		 *
		 */
		//String dbName = "Project1_empty.zdb"; //AssesmentTask1.xml
		String dbName = "Project1_test.zdb"; //big db inkl Ass.data
		//String dbName = "Project1_empty546.zdb";
		
		int Port = 8000;
		String DB_Implementation = "zooDB";

		System.out.println("Start webserver for zooDB="+dbName);
		Webserver web_zooDB = new Webserver(Port, DB_Implementation, dbName);
		System.out.println("Return from: Start webserver for zooDB="+dbName);
	}
}
