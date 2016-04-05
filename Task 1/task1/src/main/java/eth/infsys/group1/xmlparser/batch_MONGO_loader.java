package eth.infsys.group1.xmlparser;


import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import eth.infsys.group1.dbspec.DBProvider;
import eth.infsys.group1.task1.dbobjs.Proceedings;
import eth.infsys.group1.task2.T2DBProvider;

public class batch_MONGO_loader {


	
	

	public static void main(String[] args) {
		
		
		String MongodbName ="myDBLP";		
		//T2DBProvider myDB = new T2DBProvider(MongodbName, DBProvider.OPEN_DB_OVERRIDE);
		T2DBProvider myDB = new T2DBProvider(MongodbName, DBProvider.OPEN_DB_APPEND);


		XMLParser<Proceedings> myParser = new XMLParser<>(myDB);

		String dblp_data = "../task1/src/main/java/eth/infsys/group1/xmlparser/AssesmentTask1.xml";
		//String dblp_data = "../task1/src/main/java/eth/infsys/group1/xmlparser/dblp_part_neu.xml";
		//String dblp_data = "../task1/src/main/java/eth/infsys/group1/xmlparser/20 468 407 bis 24 468 423.xml";

		File file = new File(dblp_data);
		
	
		
		try {
			myParser.parseXMLFile(file);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		myDB.closeDB();


		
		
	}
}












