package eth.infsys.group1.task2;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.WriteError;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.*;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.annotation.Generated;

import static java.util.Arrays.asList;


public class retarded_Test {
	
	public static MongoClient mongoClient;
	public static MongoDatabase db;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		mongoClient = new MongoClient();
		db = mongoClient.getDatabase("test");

	
			try {
				insert_document();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (MongoWriteException a) {
				if (a.getCode()==11000)
					System.out.println("Error: id already exists");
				else
					System.out.println("Error: other problem: "+ a.getMessage());
			}
			
			try {
				insert_my_document();
			} catch (MongoWriteException a) {
				if (a.getCode()==11000)
					System.out.println("Error: id already exists");
				else
					System.out.println("Error: other problem: "+ a.getMessage());
			}
			
			
			
			mongoClient.close();
	
	}

	private static void insert_my_document() {
	MongoCollection<Document> collection = db.getCollection("bitches");
	FindIterable<Document> iter= collection.find();
	long countdocs = collection.count();
	
	Document document = new Document();
	//document.append("_id", "sauhure");
	document.append("ficken", "fotze");
	document.append("geld", (double) 234);


	collection.insertOne(document);
	
	Document myDoc = collection.find().first();
	System.out.println(myDoc.toJson());	
	

	myDoc = collection.find(eq("_id", "sauhure")).first();
	System.out.println(myDoc.toJson());
	
	
	//myDoc = collection.find(and(gt("i", 50), lte("i", 100))).first();
	//System.out.println(myDoc.toJson());
	
	Bson filter = and(gt("i", 50), lte("i", 100));
	
	System.out.println(Filters.and(filter));
	
	//String shit = filter.Render().ToString();
	
	//System.out.println(filter.);

		
	}

	private static void insert_document() throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
		db.getCollection("restaurants").insertOne(
		        new Document("address",
		                new Document()
		                        .append("street", "22 Avenue")
		                        .append("zipcode", "10075")
		                        .append("building", "1480")
		                        .append("coord", asList(-73.9557413, 40.7720266)))
		                .append("borough", "Manhattan")
		                .append("cuisine", "Italian")
		                .append("grades", asList(
		                        new Document()
		                                .append("date", format.parse("2014-10-01T00:00:00Z"))
		                                .append("grade", "A")
		                                .append("score", 11),
		                        new Document()
		                                .append("date", format.parse("2014-01-16T00:00:00Z"))
		                                .append("grade", "B")
		                                .append("score", 17)))
		                .append("name", "Vella")
		                .append("restaurant_id", "41704620")
		                .append("_id","asdasd"));
		
	}

}
