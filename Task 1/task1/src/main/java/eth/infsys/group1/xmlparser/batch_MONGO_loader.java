package eth.infsys.group1.xmlparser;


import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteError;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import eth.infsys.group1.dbspec.DBProvider;
import eth.infsys.group1.task1.T1DBProvider;
import eth.infsys.group1.task1.dbobjs.Proceedings;
import eth.infsys.group1.task2.T2DBProvider;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.xml.sax.SAXException;

import static com.mongodb.client.model.Filters.*;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.annotation.Generated;
import javax.xml.parsers.ParserConfigurationException;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;

public class batch_MONGO_loader {


	
	

	public static void main(String[] args) {
		
		
		String MongodbName ="myDBLP";		
		T2DBProvider myDB = new T2DBProvider(MongodbName, DBProvider.OPEN_DB_OVERRIDE);
		//T2DBProvider myDB = new T2DBProvider(myMONGO, DBProvider.OPEN_DB_APPEND);


		XMLParser<Proceedings> myParser = new XMLParser<>(myDB);

		//String dblp_data = "../task1/src/main/java/eth/infsys/group1/xmlparser/AssesmentTask1.xml";
		String dblp_data = "../task1/src/main/java/eth/infsys/group1/xmlparser/dblp_part_neu.xml";
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












