package eth.infsys.group1.xmlparser;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import eth.infsys.group1.task1.T1DBProvider;
import eth.infsys.group1.task1.dbobjs.Proceedings;

public class batch_db_loader {


	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

		//Choose DB
		String dbName = "Project1_ZooDB_new.zdb";
		//T1DBProvider myDB = new T1DBProvider(dbName, T1DBProvider.OPEN_DB_OVERRIDE);
		T1DBProvider myDB = new T1DBProvider(dbName, T1DBProvider.OPEN_DB_OVERRIDE);



		load_input(myDB);
		//same call again is not a problem...
		//load_input(myDB);

		queries();



		myDB.closeDB();

	}

	private static void load_input(T1DBProvider myDB) throws ParserConfigurationException, SAXException, IOException {
		XMLParser<Proceedings> myParser = new XMLParser<>(myDB);
		String dblp_data = "../task1/src/main/java/eth/infsys/group1/xmlparser/dblp_part.xml";
		//String myfile = "C:\\Users\\Filip\\Downloads\\dblp.xml";

		File file = new File(dblp_data);
		myParser.parseXMLFile(file);
	}

	private static void queries() {
		System.out.println("Task 1: ....");


	}

}
