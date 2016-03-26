package eth.infsys.group1.xmlparser;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import eth.infsys.group1.dbspec.QueryParameter;
import eth.infsys.group1.dbspec.WebFunc;
import eth.infsys.group1.task1.T1DBProvider;
import eth.infsys.group1.task1.dbobjs.InProceedings;
import eth.infsys.group1.task1.dbobjs.Proceedings;

public class batch_db_loader {


	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

		//Choose DB
		//String dbName = "Project1_ZooDB_updated_confEd_keys.zdb";
		//String dbName = "Project1_ZooDB_updated_assesmenttask1.zdb";
		String dbName = "Project1_4mil.zdb";


		//T1DBProvider myDB = new T1DBProvider(dbName, T1DBProvider.OPEN_DB_OVERRIDE);
		T1DBProvider myDB = new T1DBProvider(dbName, T1DBProvider.OPEN_DB_APPEND);
		
//!!!!!!!change override


		load_input(myDB);
		//same call again is not a problem...
		//load_input(myDB);

		//queries(myDB);



		myDB.closeDB();

	}

	private static void load_input(T1DBProvider myDB) throws ParserConfigurationException, SAXException, IOException {
		XMLParser<Proceedings> myParser = new XMLParser<>(myDB);
		//String dblp_data = "../task1/src/main/java/eth/infsys/group1/xmlparser/dblp_part.xml";
		//String dblp_data = "../task1/src/main/java/eth/infsys/group1/xmlparser/dblp_part_neu.xml";
		//String dblp_data = "../task1/src/main/java/eth/infsys/group1/xmlparser/AssesmentTask1.xml";
		String dblp_data = "../task1/src/main/java/eth/infsys/group1/xmlparser/20 468 407 bis 24 468 423.xml";


		File file = new File(dblp_data);
		myParser.parseXMLFile(file);
	}

	private static void queries(T1DBProvider myDB) {
		System.out.println("Task 1: ....");
		
		InProceedings inproc;
		InProceedings inproc2;
		long start, stop;
		long start2, stop2;
		

		//myDB.tr_begin();
		//myDB.tr_commit();

		String id;
		PublicationIO publ;
		/*
		System.out.print("-----------------------------\n");
		id = "conf/isola/PodpecanZL10";	
		publ = myDB.IO_get_publication_by_id(id);
		publ.print_all();
		
		System.out.print("-----------------------------\n");
		id = "conf/isola/Zimmermann04a";	
		publ = myDB.IO_get_publication_by_id(id);
		publ.print_all();
		
		String s = "SORT_BY_TITLE";
		
		QueryParameter qp = QueryParameter.fromString(s);
		System.out.print(qp.db_query);
		
*/

	}
	
	
	
	
	/**
	 * IO-Helper-Methods
	 */
	
	
	
	
	
}

