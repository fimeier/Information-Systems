package eth.infsys.webserver;

public class start_webserver_mongoDB {

	public static void main(String[] args) throws Exception {

		/**
		 * Task 2 - MongoDB
		 * 
		 *
		 *
		 */

		String dbName ="myDBLP";		


		int Port = 8080;
		String DB_Implementation = "MongoDB";

		System.out.println("Start webserver for zooDB="+dbName);
		Webserver web_zooDB = new Webserver(Port, DB_Implementation, dbName);
		System.out.println("Return from: Start webserver for zooDB="+dbName);

	}
}



