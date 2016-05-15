package eth.infsys.webserver;

public class baseX_Web_Start {

	public static void main(String[] args) throws Exception {

		/**
		 * Task 3 - BaseXDB
		 * 
		 *
		 *
		 */

		String dbName = "big_xml";
	    String User = "admin";
	    String PW = "admin";

		int Port = 8888;
		String DB_Implementation = "BaseXDB";

		System.out.println("Start webserver for BaseXDB="+dbName);
		Webserver web_BaseXDB = new Webserver(Port, DB_Implementation, dbName, User, PW);
		System.out.println("Return from: Start webserver for BaseXDB="+dbName);

	}
}



