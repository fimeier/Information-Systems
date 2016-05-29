package eth.infsys.webserver;

public class mongoDB_Web_Start {

	public static void main(String[] args) throws Exception {

		/**
		 * Task 2 - MongoDB
		 * 
		 *
		 *
		 */

		//String dbName ="myDBLP";
		String dbName ="myDBLPv2";
		//String dbName ="myDBLP_manipulate";



		int Port = 8080;
		String DB_Implementation = "MongoDB";

		System.out.println("Start webserver for mongoDB="+dbName);
		Webserver web_MongoDB = new Webserver(Port, DB_Implementation, dbName);
		System.out.println("Return from: Start webserver for mongoDB="+dbName);

	}
}



