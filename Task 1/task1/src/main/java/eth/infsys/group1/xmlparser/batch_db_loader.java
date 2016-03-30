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
		//String dbName = "Project1_ZooDB_updated_confEd_keys.zdb";
		//String dbName = "Project1_ZooDB_updated_assesmenttask1.zdb";
		//String dbName = "Project1_4mil.zdb";
		

		//AssesmentTask1.xml
		String dbName = "Project1_empty.zdb";


		//big db inkl Ass.data
		//String dbName = "Project1_test.zdb";



		T1DBProvider myDB = new T1DBProvider(dbName, T1DBProvider.OPEN_DB_APPEND);
		//T1DBProvider myDB = new T1DBProvider(dbName, T1DBProvider.OPEN_DB_OVERRIDE);
		
		//!!!!!!!change override


		load_input(myDB);
		//same call again is not a problem...
		//load_input(myDB);

		//queries(myDB);

		//String arg = null;
		//String publ_id = Publisher.calculate_publisher_id(arg);

		myDB.closeDB();

	}

	private static void load_input(T1DBProvider myDB) throws ParserConfigurationException, SAXException, IOException {
		XMLParser<Proceedings> myParser = new XMLParser<>(myDB);
		
		String dblp_data = "../task1/src/main/java/eth/infsys/group1/xmlparser/AssesmentTask1.xml";
		//String dblp_data = "../task1/src/main/java/eth/infsys/group1/xmlparser/20 468 407 bis 24 468 423.xml";


		File file = new File(dblp_data);
		myParser.parseXMLFile(file);
	}


	
	
	
}

