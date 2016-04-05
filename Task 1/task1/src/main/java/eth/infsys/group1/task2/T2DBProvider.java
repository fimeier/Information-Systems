package eth.infsys.group1.task2;

import com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.elemMatch;
import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Updates.addToSet;
import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import static java.util.Arrays.asList;
import com.mongodb.client.model.Sorts.*;
import com.mongodb.client.model.TextSearchOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiConsumer;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonReader;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

import eth.infsys.group1.dbspec.DBProvider;
import eth.infsys.group1.dbspec.DivIO;
import eth.infsys.group1.dbspec.InProceedings_simple_input;
import eth.infsys.group1.dbspec.Proceedings_simple_input;
import eth.infsys.group1.dbspec.PublicationIO;
import eth.infsys.group1.task1.dbobjs.InProceedings;
import eth.infsys.group1.task1.dbobjs.Person;
import eth.infsys.group1.task1.dbobjs.Proceedings;
import javafx.util.Pair;

@SuppressWarnings("restriction")
public class T2DBProvider extends DBProvider {

	/**
	 * Class and DB-Stuff
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	private MongoClient mongoClient;
	private MongoDatabase db;
	private String dbName;


	/**
	 * Create DBProvider
	 * 
	 * @param dbName the name of the database to be created
	 * @param mode OPEN_DB_APPEND or OPEN_DB_OVERRIDE
	 */
	public T2DBProvider(String dbName, int mode) {
		this.dbName = dbName;
		createDB(dbName, mode);		
	}



	@Override
	protected void createDB(String dbName, int mode) {
		// By default, all database files will be created in TBD

		switch (mode) {
		case OPEN_DB_APPEND:

			this.mongoClient = new MongoClient();
			this.db = mongoClient.getDatabase(dbName);
			this.dbName = dbName;


			break;

			//OPEN_DB_OVERRIDE
		default:
			this.mongoClient = new MongoClient();
			this.db = mongoClient.getDatabase(dbName);
			this.dbName = dbName;

			db.drop();

			db.getCollection("Publications").createIndex(new Document("inproceedings._id",1).append("unique", true));//     createIndex();
			//db.getCollection("Publications").createIndex({"_id":1,"inproceedings._id"});//     createIndex();

			//db.getCollection("Publications").createIndex({"inproceedings._id",1}, { unique: true } )
			//db.getCollection("Publications").createIndex(new Document("_id",1).append("inproceedings._id",1).append(partialFilterExpression: {unique: true }} )
		}
	}

	public void closeDB(){
		mongoClient.close();
	}

	protected void createIndexes(String on_collection){
		boolean all = false;
		switch(on_collection){
		case "all":
			all = true;
			//no break			
		case "Publications":
			//unique
			db.getCollection("Publications").createIndex(new Document("_id",1).append("unique", true));
			db.getCollection("Publications").createIndex(new Document("inproceedings.Inproceedings_id",1).append("unique", true));
			db.getCollection("Publications").createIndex(new Document("_id",1).append("inproceedings.Inproceedings_id",1).append("unique", true));

			//text index
			//db.getCollection("Publications").createIndex(new Document("title","text").append("inproceedings.Inproceedings_title", "text"));
			db.getCollection("Publications").createIndex(new Document("inproceedings.Inproceedings_title", "text"));

			
			//for performance
			db.getCollection("Publications").createIndex(new Document("title",1));
			db.getCollection("Publications").createIndex(new Document("inproceedings.Inproceedings_title",1));
			
			db.getCollection("Publications").createIndex(new Document("editors.Editor_id",1));
			db.getCollection("Publications").createIndex(new Document("editors.Editor_name",1));
			
			db.getCollection("Publications").createIndex(new Document("inproceedings.authors.Author_id",1));
			db.getCollection("Publications").createIndex(new Document("inproceedings.authors.Author_name",1));
			


			if (!all){
				break;
			}
		case "Conferences":
			//unique
			db.getCollection("Conferences").createIndex(new Document("_id",1).append("unique", true));
			db.getCollection("Conferences").createIndex(new Document("editions.ConferenceEdition_id",1).append("unique", true));
			db.getCollection("Conferences").createIndex(new Document("_id",1).append("editions.ConferenceEdition_id",1).append("unique", true));

			
			//text
			db.getCollection("Conferences").createIndex(new Document("name", "text"));

			
			//for performance
			db.getCollection("Conferences").createIndex(new Document("name",1));
			db.getCollection("Conferences").createIndex(new Document("name",-1));
			db.getCollection("Conferences").createIndex(new Document("editions.year",1));
			db.getCollection("Conferences").createIndex(new Document("editions.year",-1));

			if (!all){
				break;
			}


		case "Persons":
			//unique
			db.getCollection("Persons").createIndex(new Document("_id",1).append("unique", true));
			
			//text
			db.getCollection("Persons").createIndex(new Document("name", "text"));

			
			//for performance
			db.getCollection("Persons").createIndex(new Document("name",1));
			db.getCollection("Persons").createIndex(new Document("name",-1));

			if (!all){
				break;
			}
		case"Publishers":
			//unique
			db.getCollection("Publishers").createIndex(new Document("_id",1).append("unique", true));
			

			//text
			db.getCollection("Publishers").createIndex(new Document("name", "text"));

			
			//for performance
			db.getCollection("Publishers").createIndex(new Document("name",1));
			db.getCollection("Publishers").createIndex(new Document("name",-1));

			if (!all){
				break;
			}
		case"Series":
			//unique
			db.getCollection("Series").createIndex(new Document("_id",1).append("unique", true));
			

			//text
			db.getCollection("Series").createIndex(new Document("name", "text"));

			//for performance
			db.getCollection("Series").createIndex(new Document("name",1));
			db.getCollection("Series").createIndex(new Document("name",-1));

			break;
		}

	}


	/**
	 * ID-generator-Methods
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	public static String calculate_person_id(String name){
		return "person/" + name;		
	}

	public static String calculate_conference_id(String conferenceName){
		return "conference/" + conferenceName;
	}

	public static String calculate_conferenceEdition_id(String conferenceName, int conferenceEdition){
		return "conferenceedition/" + conferenceName + "/" + conferenceEdition;
	}

	public static String calculate_publisher_id(String name){
		return "publisher/" + name;
	}

	static public String calculate_series_id(String name){
		return "series/" + name;
	}









	/**
	 * Batch-Loader-Methods
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	@SuppressWarnings("restriction")
	@Override
	public String batch_createProceedings(Proceedings_simple_input args) {
		PublicationIO proc_input = new PublicationIO(args);

		Document proc = get_proceedings_by_id(proc_input.id);
		//return existing proceeding
		if ( ! (proc == null) ){
			String id = proc.getString("_id");
			String title = proc.getString("title");
			System.out.println("proceeding already exists... id=" + id + " and title=" + title);
			return id;
		}
		//create new proceeding
		System.out.println("create new proceeding...");

		//Conference
		Document conf = get_conference_by_id(proc_input.Conference_id);
		if ( conf == null ){
			conf = create_conference(proc_input);
			System.out.println("Conference(" + proc_input.Conference_id + ") created...");
		}
		else {
			System.out.println("Conference(" + proc_input.Conference_id + ") already exists...");
		}

		//create ConferenceEdition and add proceedings
		if (!exists_conferenceEdition_by_id(proc_input.ConferenceEdition_id)){
			Document confEd = create_conferenceEdition(proc_input);
			System.out.println("ConferenceEdition(" + proc_input.ConferenceEdition_id + ") created...");
		}
		else {
			//impliziert, dass das proceeding bereits hinzugefügt wurde... sollte man nie sehen 
			System.out.println("ERROR: ConferenceEdition(" + proc_input.Conference_id + ") already exists... 66666666666666666");
		}

		//create Editors and add proceedings
		for (Pair<String, String> editor_name_id: proc_input.editors_name_id){
			//String person_id = Person.calculate_person_id(editor_name);
			Document editor = get_person_by_id(editor_name_id.getValue());
			if ( editor == null ){
				//DivIO input_args = new DivIO(); input_args.is_a_person=true;
				//input_args.set_Person_add_Pair(editor_name, null, new Pair<String,String>(args.title,args.id));
				editor = create_editor_add_proceedings(proc_input, editor_name_id);
				System.out.println("Editor (" + editor_name_id.getKey() + ") created...");
			}
			else {
				System.out.println("Editor(" + editor_name_id.getKey() + ") already exists... add proceedings");
				person_add_proceedings(editor_name_id.getValue(), proc_input.title, proc_input.id);
			}
		}


		//create Publisher and add proceedings
		Document publ = get_publisher_by_id(proc_input.publisher_id);
		if ( publ == null ){
			publ = create_publisher_add_proceedings(proc_input);
			System.out.println("Publisher (" + proc_input.publisher_name + ") created...");
		}
		else {
			System.out.println("Publisher(" + args.publisher + ") already exists...");
			publisher_add_proceedings(proc_input);
		}

		//create Series and add proceedings
		Document serie = get_series_by_id(proc_input.series_id);
		if ( serie == null ){
			serie = create_serie_add_proceedings(proc_input);
			System.out.println("Series (" + proc_input.series_name + ") created...");
		}
		else {
			System.out.println("Series (" + proc_input.series_name + ") already exists...");
			series_add_proceedings(proc_input);

		}


		//create Proceedings
		Document doc = create_proceedings(proc_input);
		db.getCollection("Publications").insertOne(doc);

		return proc_input.id;
	}




	@SuppressWarnings("restriction")
	@Override
	public String batch_createInProceedings(InProceedings_simple_input args) {
		PublicationIO inproc_input = new PublicationIO(args);

		Document inproc = get_inproceedings_by_id(inproc_input);
		//return existing inproceedings
		if ( !(inproc == null) ){	
			//Document inproclein = inproc.get("inproceedings");
			//System.out.println(inproc.toJson());
			String id = inproc_input.id;//inproc.getString("inproceedings.Inproceedings_id");
			String title = inproc_input.title;//inproc.getString("inproceedings.Inproceedings_title");
			System.out.println("inproceeding already exists... id=" + id + " and title=" + title);
			return id;
		}

		//check if Proceedings is existing
		Document proc = get_proceedings_by_id(inproc_input.proceeding_id);
		if ( proc == null ){
			System.out.println("ERROR: Proceeding with id=" + inproc_input.proceeding_id + " is not existing. Error Inproceeding creation... 66666666666");
			return null;
		}

		//create new inproceedings
		System.out.println("create new inproceedings for proceedings with id=" + inproc_input.proceeding_id);


		//create Authors and add inproceedings
		for (Pair<String, String> author_name_id: inproc_input.authors_name_id){
			Document author = get_person_by_id(author_name_id.getValue());
			if ( author == null ){
				author = create_author_add_inproceedings(inproc_input, author_name_id);
				System.out.println("Author (" + author_name_id.getKey() + ") created...");
			}
			else {
				System.out.println("Author(" + author_name_id.getKey() + ") already exists... add inproceedings");
				person_add_inproceedings(author_name_id.getValue(), inproc_input.title, inproc_input.id);
			}
		}

		//MongoCollection<Document> collection = db.getCollection("Publications");

		Document doc = create_inproceedings(inproc_input);// XML_to_Bson.InProceedings_to_Bson_Document(args);

		Bson filter = eq("_id", inproc_input.proceeding_id);
		db.getCollection("Publications").updateOne(filter, addToSet("inproceedings", doc));			

		return inproc_input.id;
	}




	/**
	 * Helper-Methods
	 * 
	 */

	@SuppressWarnings("restriction")
	private Document create_inproceedings(PublicationIO inproc_input){
		//Authors
		List<Document> authors = new ArrayList<Document>();
		for (Pair<String, String> author_name_id: inproc_input.authors_name_id){
			authors.add(new Document("Author_id", author_name_id.getValue())
					.append("Author_name", author_name_id.getKey()));
		}
		Document document = new Document("Inproceedings_id", (String) inproc_input.id)
				.append("Inproceedings_title", (String) inproc_input.title)
				.append("year", (int) inproc_input.year)
				.append("electronicEdition", (String) inproc_input.electronicEdition )
				.append("authors", authors)
				.append("note", (String) inproc_input.note)
				.append("pages", (String) inproc_input.pages);

		return document;
	}

	@SuppressWarnings("restriction")
	private Document create_proceedings(PublicationIO proc_input) {
		//Editors
		List<Document> editors = new ArrayList<Document>();
		for (Pair<String, String> editor_name_id: proc_input.editors_name_id){
			editors.add(new Document("Editor_id", editor_name_id.getValue())
					.append("Editor_name", editor_name_id.getKey()));
		}

		return new Document("_id", (String) proc_input.id)
				.append("title", (String) proc_input.title)
				.append("year", (int) proc_input.year)
				.append("electronicEdition", (String) proc_input.electronicEdition)
				.append("Conference_id", proc_input.Conference_id)
				.append("Conference_name", proc_input.Conference_name)
				
				.append("ConferenceEdition_id", (String) proc_input.ConferenceEdition_id)
				.append("ConferenceEdition_year", (int) proc_input.ConferenceEdition_year)

				
				.append("editors", editors)
				.append("note", (String) proc_input.note)
				.append("number", (int) proc_input.number)
				.append("Publisher_id", (String) proc_input.publisher_id)
				.append("Publisher_name", (String) proc_input.publisher_name)
				.append("volume", (String) proc_input.volume)
				.append("isbn", (String) proc_input.isbn)
				.append("Series_id",(String) proc_input.series_id)
				.append("Series_name",(String) proc_input.series_name);
	}

	private void series_add_proceedings(PublicationIO proc_input) {
		Bson filter = eq("_id", proc_input.series_id);
		Document publications = new Document("proceedings_id", (String) proc_input.id)
				.append("proceedings_title", (String) proc_input.title);

		db.getCollection("Series").updateOne(filter, addToSet("publications", publications));
	}

	private Document create_serie_add_proceedings(PublicationIO proc_input) {
		List<Document> publications = new ArrayList<Document>();
		publications.add(new Document("proceedings_id", (String) proc_input.id)
				.append("proceedings_title", (String) proc_input.title));

		Document doc = new Document("_id", (String) proc_input.series_id)
				.append("name", (String) proc_input.series_name)
				.append("publications", publications);

		db.getCollection("Series").insertOne(doc);
		return doc;
	}



	private Document get_series_by_id(String series_id) {
		Bson filter = eq("_id", series_id);
		Document doc = db.getCollection("Series").find(filter).first();
		return doc;
	}


	private void publisher_add_proceedings(PublicationIO proc_input) {
		Bson filter = eq("_id", proc_input.publisher_id);
		Document publications = new Document("proceedings_id", (String) proc_input.id)
				.append("proceedings_title", (String) proc_input.title);

		db.getCollection("Publishers").updateOne(filter, addToSet("publications", publications));	
	}

	private Document create_publisher_add_proceedings(PublicationIO proc_input) {
		List<Document> publications = new ArrayList<Document>();
		publications.add(new Document("proceedings_id", (String) proc_input.id)
				.append("proceedings_title", (String) proc_input.title));

		Document doc = new Document("_id", (String) proc_input.publisher_id)
				.append("name", (String) proc_input.publisher_name)
				.append("publications", publications);

		db.getCollection("Publishers").insertOne(doc);
		return doc;
	}


	private Document get_publisher_by_id(String publ_id) {
		Bson filter = eq("_id", publ_id);
		Document publisher = db.getCollection("Publishers").find(filter).first();
		return publisher;
	}


	private void person_add_inproceedings(String pers_id, String Inproceedings_title, String Inproceedings_id) {
		Bson filter = eq("_id", pers_id);
		Document authoredPublications = new Document("Inproceedings_id", (String) Inproceedings_id)
				.append("Inproceedings_title", (String) Inproceedings_title);

		db.getCollection("Persons").updateOne(filter, addToSet("authoredPublications", authoredPublications));
	}


	@SuppressWarnings("restriction")
	private Document create_author_add_inproceedings(PublicationIO inproc_input, Pair<String, String> author_name_id) {

		//Authors
		List<Document> authoredPublications = new ArrayList<Document>();
		authoredPublications.add(new Document("Inproceedings_id", (String) inproc_input.id)
				.append("Inproceedings_title", (String) inproc_input.title));


		Document doc = new Document("_id", (String) author_name_id.getValue())
				.append("name", (String) author_name_id.getKey())
				.append("authoredPublications",authoredPublications);

		db.getCollection("Persons").insertOne(doc);

		return doc;
	}

	private void person_add_proceedings(String pers_id, String proceedings_title, String proceedings_id) {
		Bson filter = eq("_id", pers_id);
		Document editedPublication = new Document("proceedings_id", (String) proceedings_id)
				.append("proceedings_title", (String) proceedings_title);

		db.getCollection("Persons").updateOne(filter, addToSet("editedPublications", editedPublication));
	}


	@SuppressWarnings("restriction")
	private Document create_editor_add_proceedings(PublicationIO proc_input, Pair<String, String> editor_name_id) {

		//Editors
		List<Document> editedPublications = new ArrayList<Document>();
		editedPublications.add(new Document("proceedings_id", (String) proc_input.id)
				.append("proceedings_title", (String) proc_input.title));


		Document doc = new Document("_id", (String) editor_name_id.getValue())
				.append("name", (String) editor_name_id.getKey())
				.append("editedPublications",editedPublications);

		db.getCollection("Persons").insertOne(doc);

		return doc;
	}

	private Document get_person_by_id(String person_id) {
		Bson filter = eq("_id", person_id);
		Document pers = db.getCollection("Persons").find(filter).first();
		return pers;
	}
	private Document get_person_by_name(String Person_name) {
		Bson filter = eq("name", Person_name);
		Document pers = db.getCollection("Persons").find(filter).first();
		return pers;
	}

	private Document create_conferenceEdition(PublicationIO proc_input) {
		Bson filter = eq("_id", proc_input.Conference_id);

		Document doc = new Document("ConferenceEdition_id", (String) proc_input.ConferenceEdition_id)
				.append("year", (int) proc_input.ConferenceEdition_year)
				.append("proceedings_id", (String) proc_input.id)
				.append("proceedings_title", proc_input.title);

		db.getCollection("Conferences").updateOne(filter, addToSet("editions", doc));

		return doc;
	}

	/**
	 * for batch loader: does conferenceEdition exist??
	 * @param Conference_id
	 * @param year
	 * @return
	 */
	private Boolean exists_conferenceEdition_by_id(String confEd_id) {
		Bson filter = eq("editions.ConferenceEdition_id", confEd_id);
		Document doc = db.getCollection("Conferences").find(filter).first();
		return (doc != null);	
	}
	
	private Boolean exists_person_by_id(String Person_id) {
		Bson filter = eq("_id", Person_id);
		Document doc = db.getCollection("Persons").find(filter).first();
		return (doc != null);	
	}
	private Boolean exists_person_by_name(String Person_name) {
		Bson filter = eq("name", Person_name);
		Document doc = db.getCollection("Persons").find(filter).first();
		return (doc != null);	
	}






	private Document get_conferenceEdition_by_id(String confEd_id) {
		Bson filter = eq("editions.ConferenceEdition_id", confEd_id);
		Bson projection = fields(include("name"),elemMatch("editions", Filters.eq("ConferenceEdition_id", confEd_id)));
		Document confEd = db.getCollection("Conferences").find(filter).projection(projection).first();

		int a = 5;
		return confEd;
	}

	private Document get_conferenceEdition_by_year(String Conference_id, int year) {

		//Bson filter = Filters.and(Filters.eq("_id", Conference_id),elemMatch("editions", Filters.eq("year", year)));
		//Bson projection = include("editions");
		//Document confEd = db.getCollection("Conferences").find(filter).projection(projection).first();

		Bson filter = eq("_id", Conference_id);
		Bson projection = fields(include("name"),elemMatch("editions", Filters.eq("year", year)));
		Document confEd = db.getCollection("Conferences").find(filter).projection(projection).first();

		return confEd;		
	}

	private Document create_conference(PublicationIO proc_input) {
		Document conf = new Document("_id", (String) proc_input.Conference_id)
				.append("name", (String) proc_input.Conference_name);
		db.getCollection("Conferences").insertOne(conf);
		return conf;
	}

	private Document get_conference_by_id(String conf_id) {
		Bson filter = eq("_id", conf_id);
		Document proc = db.getCollection("Conferences").find(filter).first();
		return proc;
	}


	private Document get_proceedings_by_id(String proc_id) {
		Bson filter = eq("_id", proc_id);
		Bson projection = exclude("inproceedings.year", "inproceedings.electronicEdition","inproceedings.authors","inproceedings.note","inproceedings.pages");
		Document proc = db.getCollection("Publications").find(filter).projection(projection).sort(Sorts.ascending("inproceedings.Inproceedings_title")).first();
		return proc;
	}
	
	private FindIterable<Document>  get_proceedings_by_Editor_id(String Editor_id) {
		Bson filter = eq("editors.Editor_id", Editor_id);
		Bson projection = exclude("inproceedings.year", "inproceedings.electronicEdition","inproceedings.authors","inproceedings.note","inproceedings.pages");
		return db.getCollection("Publications").find(filter).projection(projection);
	}

	//"old" method for batch loader
	private Document get_inproceedings_by_id(PublicationIO inproc_input) {
		Bson filter = Filters.and(Filters.eq("_id", inproc_input.proceeding_id),elemMatch("inproceedings", Filters.eq("Inproceedings_id", inproc_input.id)));
		//Bson filter = elemMatch("inproceedings", Filters.eq("_id", inproc_input));
		//Bson filter = Filters.eq("inproceedings._id", inproc_id);
		Bson projection = fields(include("inproceedings"), excludeId());
		Document proc = db.getCollection("Publications").find(filter).projection(projection).first();
		return proc;
	}

	private Document get_inproceedings_by_id(String inproc_id) {
		Bson filter = eq("inproceedings.Inproceedings_id", inproc_id);
		Bson projection = fields(include("_id","title","Conference_id","Conference_name","ConferenceEdition_id","ConferenceEdition_year"),elemMatch("inproceedings", Filters.eq("Inproceedings_id", inproc_id)));
		Document proc = db.getCollection("Publications").find(filter).projection(projection).first();
		return proc;
	}


	private FindIterable<Document> get_inproceedings_by_Author_id(String pers_id) {
		Bson filter = eq("inproceedings.authors.Author_id", pers_id);
		Bson projection = fields(include("_id","title","Conference_id","Conference_name","ConferenceEdition_id","ConferenceEdition_year"),elemMatch("inproceedings", Filters.eq("authors.Author_id", pers_id)));
		return db.getCollection("Publications").find(filter).projection(projection);
	}

	private  AggregateIterable<Document> get_inproceedings_by_filter_offset(String filter_by, int skip_in, int lim, String order_by) {
		/*
		db.getCollection('Publications').aggregate([
		{$match: { $text: { $search: "fOrMal" } } },
		{$unwind: {path: "$inproceedings"}} ,
		{$match: {"inproceedings.Inproceedings_title":{ $regex: "fORmAl", $options:"i"}}},
		])*/
		switch(order_by){

		case "title ascending":
			return db.getCollection("Publications").aggregate(
					asList(
							new Document("$match", new Document("$text", new Document("$search", filter_by))),
							new Document("$unwind", new Document("path", "$inproceedings")),
							new Document("$match", new Document("inproceedings.Inproceedings_title", new Document("$regex",filter_by).append("$options", "i"))),
							Aggregates.sort(Sorts.ascending("inproceedings.Inproceedings_title")),
							Aggregates.skip(skip_in),
							Aggregates.limit(lim),
							Aggregates.project(include("_id","title","Conference_id","Conference_name","ConferenceEdition_id","ConferenceEdition_year","inproceedings"))
							)
					);
		case "title descending":
			return db.getCollection("Publications").aggregate(
					asList(
							new Document("$match", new Document("$text", new Document("$search", filter_by))),
							new Document("$unwind", new Document("path", "$inproceedings")),
							new Document("$match", new Document("inproceedings.Inproceedings_title", new Document("$regex",filter_by).append("$options", "i"))),
							Aggregates.sort(Sorts.descending("inproceedings.Inproceedings_title")),
							Aggregates.skip(skip_in),
							Aggregates.limit(lim),
							Aggregates.project(include("_id","title","Conference_id","Conference_name","ConferenceEdition_id","ConferenceEdition_year","inproceedings"))
							)
					);
		case "year ascending":
			return db.getCollection("Publications").aggregate(
					asList(
							new Document("$match", new Document("$text", new Document("$search", filter_by))),
							new Document("$unwind", new Document("path", "$inproceedings")),
							new Document("$match", new Document("inproceedings.Inproceedings_title", new Document("$regex",filter_by).append("$options", "i"))),
							Aggregates.sort(Sorts.ascending("inproceedings.year")),
							Aggregates.skip(skip_in),
							Aggregates.limit(lim),
							Aggregates.project(include("_id","title","Conference_id","Conference_name","ConferenceEdition_id","ConferenceEdition_year","inproceedings"))
							)
					);
		case "year descending":
			return db.getCollection("Publications").aggregate(
					asList(
							new Document("$match", new Document("$text", new Document("$search", filter_by))),
							new Document("$unwind", new Document("path", "$inproceedings")),
							new Document("$match", new Document("inproceedings.Inproceedings_title", new Document("$regex",filter_by).append("$options", "i"))),
							Aggregates.sort(Sorts.descending("inproceedings.year")),
							Aggregates.skip(skip_in),
							Aggregates.limit(lim),
							Aggregates.project(include("_id","title","Conference_id","Conference_name","ConferenceEdition_id","ConferenceEdition_year","inproceedings"))
							)
					);
		default:
			return db.getCollection("Publications").aggregate(
					asList(
							new Document("$match", new Document("$text", new Document("$search", filter_by))),
							new Document("$unwind", new Document("path", "$inproceedings")),
							new Document("$match", new Document("inproceedings.Inproceedings_title", new Document("$regex",filter_by).append("$options", "i"))),
							Aggregates.skip(skip_in),
							Aggregates.limit(lim),
							Aggregates.project(include("_id","title","Conference_id","Conference_name","ConferenceEdition_id","ConferenceEdition_year","inproceedings"))
							)
					); 
		}
	}


	private FindIterable<Document> get_proceedings_by_filter_offset(String filter_by, int skip_in, int limit_in, String order_by) {
		String regex =filter_by; //"seventh";//"/"+filter_by+"/i";
		
		Document filter = new Document("title", 
				new Document("$regex", regex).append("$options","i"));

		Bson projection = exclude("inproceedings.year", "inproceedings.electronicEdition","inproceedings.authors","inproceedings.note","inproceedings.pages");

		switch(order_by){

		case "title ascending":
			return db.getCollection("Publications").find(filter).projection(projection).skip(skip_in).limit(limit_in).sort(Sorts.ascending("title"));
		case "title descending":
			return db.getCollection("Publications").find(filter).projection(projection).skip(skip_in).limit(limit_in).sort(Sorts.descending("title"));
		case "year ascending":
			return db.getCollection("Publications").find(filter).projection(projection).skip(skip_in).limit(limit_in).sort(Sorts.ascending("year"));
		case "year descending":
			return db.getCollection("Publications").find(filter).projection(projection).skip(skip_in).limit(limit_in).sort(Sorts.descending("year"));
		default:
			return db.getCollection("Publications").find(filter).projection(projection).skip(skip_in).limit(limit_in);
		}
	}
	
	private FindIterable<Document> get_persons_by_filter_offset(String filter_by, int skip_in, int limit_in, String order_by) {
		switch(order_by){

		case "name ascending":
			return db.getCollection("Persons").find(
					new Document("$text", new Document("$search", filter_by)))
					.sort(Sorts.ascending("name"))
					.skip(skip_in)
					.limit(limit_in);
		case "name descending":
			return db.getCollection("Persons").find(
					new Document("$text", new Document("$search", filter_by)))
					.sort(Sorts.descending("name"))
					.skip(skip_in)
					.limit(limit_in);
		default:
			//return db.getCollection("Person").find(Filters.text("Maier"));
			
			return db.getCollection("Persons").find(
					new Document("$text", new Document("$search", filter_by)))
					.skip(skip_in)
					.limit(limit_in);							
		}
	}
	
	private FindIterable<Document> get_conferences_by_filter_offset(String filter_by, int skip_in, int limit_in, String order_by) {
		switch(order_by){

		case "name ascending":
			return db.getCollection("Conferences").find(
					new Document("$text", new Document("$search", filter_by)))
					.sort(Sorts.ascending("name"))
					.skip(skip_in)
					.limit(limit_in);
		case "name descending":
			return db.getCollection("Conferences").find(
					new Document("$text", new Document("$search", filter_by)))
					.sort(Sorts.descending("name"))
					.skip(skip_in)
					.limit(limit_in);
		default:
			return db.getCollection("Conferences").find(
					new Document("$text", new Document("$search", filter_by)))
					.skip(skip_in)
					.limit(limit_in);							
		}
	}


	private FindIterable<Document> get_publishers_by_filter_offset(String filter_by, int skip_in, int limit_in, String order_by) {
		String filter_ext = "/"+filter_by+"/";

		switch(order_by){
		

		case "name ascending":
			return db.getCollection("Publishers").find(
					new Document("$text", new Document("$search", filter_by)))
					.sort(Sorts.ascending("name"))
					.skip(skip_in)
					.limit(limit_in);
		case "name descending":
			return db.getCollection("Publishers").find(
					new Document("$text", new Document("$search", "\""+filter_by+"\"")))
					.sort(Sorts.descending("name"))
					.skip(skip_in)
					.limit(limit_in);
		default:
			return db.getCollection("Publishers").find(
					new Document("name",filter_ext));
					
			//sowas ähnliches für Publisher etc.... damit auch Teilwörter gefunden werden
			//db.getCollection('Publishers').find({'name': /he/})
					
					/*
					new Document("$text", new Document("$search", filter_ext))
				//	new Document("$match", new Document("inproceedings.Inproceedings_title", new Document("$regex",filter_by).append("$options", "i")))
					)
					//.filter(Filters.text(filter_by))
					.skip(skip_in)
					.limit(limit_in);		*/					
		}
	}
	

	private FindIterable<Document> get_series_by_filter_offset(String filter_by, int skip_in, int limit_in, String order_by) {
		//String filter_ext = "/"+filter_by+"/";

		switch(order_by){


		case "name ascending":
			return db.getCollection("Series").find(
					new Document("$text", new Document("$search", filter_by)))
					.sort(Sorts.ascending("name"))
					.skip(skip_in)
					.limit(limit_in);
		case "name descending":
			return db.getCollection("Series").find(
					new Document("$text", new Document("$search", filter_by)))
					.sort(Sorts.descending("name"))
					.skip(skip_in)
					.limit(limit_in);
		default:
			return db.getCollection("Series").find(
					new Document("$text", new Document("$search", filter_by)))
					.skip(skip_in)
					.limit(limit_in);					
		}
	}
	
	
	private AggregateIterable<Document> find_co_authors(String pers_name) {
		return db.getCollection("Publications").aggregate(asList(
				Aggregates.match(eq("inproceedings.authors.Author_name", pers_name)),
				Aggregates.unwind("$inproceedings"),
				Aggregates.match(eq("inproceedings.authors.Author_name", pers_name)),
				Aggregates.project(fields(
						include("inproceedings.Inproceedings_id","inproceedings.Inproceedings_title","inproceedings.authors.Author_id","inproceedings.authors.Author_name"),
						exclude("_id")))
				));

	}

/*
	new Document("$unwind", new Document("path", "$inproceedings")),
	new Document("$match", new Document("inproceedings.Inproceedings_title", new Document("$regex",filter_by).append("$options", "i"))),
	Aggregates.sort(Sorts.ascending("inproceedings.Inproceedings_title")),
	Aggregates.skip(skip_in),
	Aggregates.limit(lim),
	Aggregates.project(include("_id","title","Conference_id","Conference_name","ConferenceEdition_id","ConferenceEdition_year","inproceedings"))
	)
	*/

	/**
	 * IO-Methods: helper
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	@SuppressWarnings("restriction")
	private PublicationIO fast_fill_PublicationIO(Document doc, String is_a){
		PublicationIO publication = new PublicationIO();
		if ( doc == null ){
			publication.is_empty = true;
			return publication;
		}

		//Proceedings
		else if ("is_a_proceeding".equals(is_a) ){
			publication.is_a_proceeding = true;
			System.out.println(doc.toJson());

			BsonReader r = new JsonReader(doc.toJson());
			r.readStartDocument();

			String current_name = r.readName();
			BsonType current_type;

			//if ( "_id".equals( current_name) ){
				publication.id = r.readString();
				//	if ( (current_type = r.readBsonType()) != BsonType.ARRAY ){
				//	current_name = r.readName();
				//	}
				//}
				//	if ( "title".equals( current_name) ){
				publication.title = r.readString();
				//	if ( (current_type = r.readBsonType()) != BsonType.ARRAY ){
				//	current_name = r.readName();
				//	}			
				//}
				//if ( "year".equals( current_name) ){
				publication.year = r.readInt32();
				//if ( (current_type = r.readBsonType()) != BsonType.ARRAY ){
				//current_name = r.readName();
				//}			
				//	}
				//if ( "electronicEdition".equals( current_name ) ){
				publication.electronicEdition = r.readString();
				//	if ( (current_type = r.readBsonType()) != BsonType.ARRAY ){
				//current_name = r.readName();
				//}
				//}

			String conf_id ="";
			String conf_name="";
			//if ( "Conference_id".equals( current_name) ){
				conf_id = r.readString();
				current_name = r.readName();
				//	if ( "Conference_name".equals( current_name) ){
					conf_name = r.readString();
					//	current_name = r.readName();
					publication.Conference_name_id = new Pair<String, String>(conf_name,conf_id);
					//	}
					//	}
					//if ( "ConferenceEdition_id".equals( current_name) ){
				String confEdid = r.readString();
				//	current_name = r.readName();
				//	if ( "ConferenceEdition_year".equals( current_name) ){
					int year = r.readInt32();
					//	current_name = r.readName();
					publication.ConferenceEdition_year_id = new Pair<Integer, String>(year,confEdid);
					//}
					//}

			//read [] editors
					//if ( "editors".equals( current_name ) ){
				r.readStartArray();
				while (r.readBsonType() != BsonType.END_OF_DOCUMENT) {
					r.readStartDocument();

					String Editor_id = r.readString();
					String Editor_name = r.readString();

					publication.editors_name_id.add(new Pair<String, String>(Editor_name, Editor_id));					
					r.readEndDocument();
				}
				r.readEndArray();
				//current_name = r.readName();
				//}

			//read the other fields up to []inproceedings
				//if ( "note".equals( current_name) ){
				publication.note = r.readString();
				//current_name = r.readName();
				//}
				//if ( "number".equals( current_name ) ){
				publication.number = r.readInt32();
				//	current_name = r.readName();
				//	}
				//if ( "Publisher_id".equals( current_name ) ){
				publication.publisher_id = r.readString();
				//	current_name = r.readName();
				//	}
				//if ( "Publisher_name".equals( current_name ) ){
				publication.publisher_name = r.readString();
				//	current_name = r.readName();
				//}
				//if ( "volume".equals( current_name ) ){
				publication.volume = r.readString();
				//	current_name = r.readName();
				//}
				//if ( "isbn".equals( current_name ) ){
				publication.isbn = r.readString();
				//	current_name = r.readName();
				//}
				//if ( "Series_id".equals( current_name ) ){
				publication.series_id = r.readString();
				//	current_name = r.readName();
				//}
				//if ( "Series_name".equals( current_name ) ){
				publication.series_name = r.readString();
				//	current_name = r.readName();
				//}
			//read [] inproceedings
				//if ( "inproceedings".equals( current_name ) ){
				r.readStartArray();
				while (r.readBsonType() != BsonType.END_OF_DOCUMENT) {
					r.readStartDocument();

					String Inproceedings_id = r.readString();
					String Inproceedings_title = r.readString();

					publication.inproceedings_title_id.add(new Pair<String, String>(Inproceedings_title, Inproceedings_id));
					r.readEndDocument();
				}
				r.readEndArray();
				//}
			publication.inproceedings_title_id.sort(comparePairTitleId);
		}


		//InProceedings - wahrscheinlich i.o.
		else if ("is_an_inproceeding".equals(is_a) ){
			publication.is_an_inproceeding = true;

			//System.out.println(doc.toJson());

			BsonReader r = new JsonReader(doc.toJson());
			r.readStartDocument();

			String current_name = r.readName();
			BsonType current_type;

			//if ( "_id".equals( current_name) ){
				publication.proceeding_id = r.readString();
				//	current_name = r.readName();
				//}
				//	if ( "title".equals( current_name) ){
				publication.proceeding_title = r.readString();
				//	current_name = r.readName();
				//}

			String conf_id ="";
			String conf_name="";
			//	if ( "Conference_id".equals( current_name) ){
				conf_id = r.readString();
				//current_name = r.readName();
				//	if ( "Conference_name".equals( current_name) ){
					conf_name = r.readString();
					//	current_name = r.readName();
					publication.Conference_name_id = new Pair<String, String>(conf_name,conf_id);
					//}
					//}
					//if ( "ConferenceEdition_id".equals( current_name) ){
				String confEdid = r.readString();
				current_name = r.readName();
				//	if ( "ConferenceEdition_year".equals( current_name) ){
					int year = r.readInt32();
						current_name = r.readName();
					publication.ConferenceEdition_year_id = new Pair<Integer, String>(year,confEdid);
					//	}
					//}

			//read []inproceedings
			if ("inproceedings".equals(current_name)){
				/**
				 * for inproceeding_by_id needed
				 * for pupl_by_title_offset not
				 * maybe change inproceeding_by_id so that it doesnt return an array
				 */
				boolean is_array = false;
				if ( r.getCurrentBsonType() == BsonType.ARRAY ){
					r.readStartArray();
					is_array = true;
				}
				r.readStartDocument();
				current_type = r.readBsonType();
				//if (current_type != BsonType.END_OF_DOCUMENT){
					publication.id = r.readString();
					//	current_type = r.readBsonType();
					//	if (current_type != BsonType.END_OF_DOCUMENT){
						publication.title = r.readString();
						//	current_type = r.readBsonType();
						//	if (current_type != BsonType.END_OF_DOCUMENT){
							publication.year = r.readInt32();
							//	current_type = r.readBsonType();
							//	if (current_type != BsonType.END_OF_DOCUMENT){
								publication.electronicEdition = r.readString();
								//		current_type = r.readBsonType();
								//		}
								//	}
								//	}
								//}

				//read []authors
								//	if ( current_type == BsonType.ARRAY && "authors".equals( current_name = r.readName()) ){
					r.readStartArray();
					while (r.readBsonType() != BsonType.END_OF_DOCUMENT) {
						r.readStartDocument();

						String Author_id = r.readString();
						String Author_name = r.readString();

						publication.authors_name_id.add(new Pair<String, String>(Author_name, Author_id));

						r.readEndDocument();
					}
					r.readEndArray();
					//}

					//	if (current_type != BsonType.END_OF_DOCUMENT){
					publication.note = r.readString();
					//		current_type = r.readBsonType();
					//		if (current_type != BsonType.END_OF_DOCUMENT){
						publication.pages = r.readString();
						//		}
						//	}
				r.readEndDocument();
				if ( is_array ){
					r.readEndArray();				
				}
			}
		}
		return publication;
	}


	@SuppressWarnings("restriction")
	private PublicationIO fill_PublicationIO(Document doc, String is_a){
		PublicationIO publication = new PublicationIO();
		if ( doc == null ){
			publication.is_empty = true;
			return publication;
		}

		//Proceedings
		else if ("is_a_proceeding".equals(is_a) ){
			publication.is_a_proceeding = true;
			System.out.println(doc.toJson());

			BsonReader r = new JsonReader(doc.toJson());
			r.readStartDocument();

			String current_name = r.readName();
			BsonType current_type;

			if ( "_id".equals( current_name) ){
				publication.id = r.readString();
				//	if ( (current_type = r.readBsonType()) != BsonType.ARRAY ){
				current_name = r.readName();
				//	}
			}
			if ( "title".equals( current_name) ){
				publication.title = r.readString();
				//	if ( (current_type = r.readBsonType()) != BsonType.ARRAY ){
				current_name = r.readName();
				//	}			
			}
			if ( "year".equals( current_name) ){
				publication.year = r.readInt32();
				//if ( (current_type = r.readBsonType()) != BsonType.ARRAY ){
				current_name = r.readName();
				//}			
			}
			if ( "electronicEdition".equals( current_name ) ){
				publication.electronicEdition = r.readString();
				//	if ( (current_type = r.readBsonType()) != BsonType.ARRAY ){
				current_name = r.readName();
				//}
			}

			String conf_id ="";
			String conf_name="";
			if ( "Conference_id".equals( current_name) ){
				conf_id = r.readString();
				current_name = r.readName();
				if ( "Conference_name".equals( current_name) ){
					conf_name = r.readString();
					current_name = r.readName();
					publication.Conference_name_id = new Pair<String, String>(conf_name,conf_id);
				}
			}
			if ( "ConferenceEdition_id".equals( current_name) ){
				String confEdid = r.readString();
				current_name = r.readName();
				if ( "ConferenceEdition_year".equals( current_name) ){
					int year = r.readInt32();
					current_name = r.readName();
					publication.ConferenceEdition_year_id = new Pair<Integer, String>(year,confEdid);
				}
			}

			//read [] editors
			if ( "editors".equals( current_name ) ){
				r.readStartArray();
				while (r.readBsonType() != BsonType.END_OF_DOCUMENT) {
					r.readStartDocument();

					String Editor_id = r.readString();
					String Editor_name = r.readString();

					publication.editors_name_id.add(new Pair<String, String>(Editor_name, Editor_id));					
					r.readEndDocument();
				}
				r.readEndArray();
				current_name = r.readName();
			}

			//read the other fields up to []inproceedings
			if ( "note".equals( current_name) ){
				publication.note = r.readString();
				current_name = r.readName();
			}
			if ( "number".equals( current_name ) ){
				publication.number = r.readInt32();
				current_name = r.readName();
			}
			if ( "Publisher_id".equals( current_name ) ){
				publication.publisher_id = r.readString();
				current_name = r.readName();
			}
			if ( "Publisher_name".equals( current_name ) ){
				publication.publisher_name = r.readString();
				current_name = r.readName();
			}
			if ( "volume".equals( current_name ) ){
				publication.volume = r.readString();
				current_name = r.readName();
			}
			if ( "isbn".equals( current_name ) ){
				publication.isbn = r.readString();
				current_name = r.readName();
			}
			if ( "Series_id".equals( current_name ) ){
				publication.series_id = r.readString();
				current_name = r.readName();
			}
			if ( "Series_name".equals( current_name ) ){
				publication.series_name = r.readString();
				current_name = r.readName();
			}
			//read [] inproceedings
			if ( "inproceedings".equals( current_name ) ){
				r.readStartArray();
				while (r.readBsonType() != BsonType.END_OF_DOCUMENT) {
					r.readStartDocument();

					String Inproceedings_id = r.readString();
					String Inproceedings_title = r.readString();

					publication.inproceedings_title_id.add(new Pair<String, String>(Inproceedings_title, Inproceedings_id));
					r.readEndDocument();
				}
				r.readEndArray();
			}
			publication.inproceedings_title_id.sort(comparePairTitleId);
		}


		//InProceedings - wahrscheinlich i.o.
		else if ("is_an_inproceeding".equals(is_a) ){
			publication.is_an_inproceeding = true;

			//System.out.println(doc.toJson());

			BsonReader r = new JsonReader(doc.toJson());
			r.readStartDocument();

			String current_name = r.readName();
			BsonType current_type;

			if ( "_id".equals( current_name) ){
				publication.proceeding_id = r.readString();
				current_name = r.readName();
			}
			if ( "title".equals( current_name) ){
				publication.proceeding_title = r.readString();
				current_name = r.readName();
			}

			String conf_id ="";
			String conf_name="";
			if ( "Conference_id".equals( current_name) ){
				conf_id = r.readString();
				current_name = r.readName();
				if ( "Conference_name".equals( current_name) ){
					conf_name = r.readString();
					current_name = r.readName();
					publication.Conference_name_id = new Pair<String, String>(conf_name,conf_id);
				}
			}
			if ( "ConferenceEdition_id".equals( current_name) ){
				String confEdid = r.readString();
				current_name = r.readName();
				if ( "ConferenceEdition_year".equals( current_name) ){
					int year = r.readInt32();
					current_name = r.readName();
					publication.ConferenceEdition_year_id = new Pair<Integer, String>(year,confEdid);
				}
			}

			//read []inproceedings
			if ("inproceedings".equals(current_name)){
				/**
				 * for inproceeding_by_id needed
				 * for pupl_by_title_offset not
				 * maybe change inproceeding_by_id so that it doesnt return an array
				 */
				boolean is_array = false;
				if ( r.getCurrentBsonType() == BsonType.ARRAY ){
					r.readStartArray();
					is_array = true;
				}
				r.readStartDocument();
				current_type = r.readBsonType();
				if (current_type != BsonType.END_OF_DOCUMENT){
					publication.id = r.readString();
					current_type = r.readBsonType();
					if (current_type != BsonType.END_OF_DOCUMENT){
						publication.title = r.readString();
						current_type = r.readBsonType();
						if (current_type != BsonType.END_OF_DOCUMENT){
							publication.year = r.readInt32();
							current_type = r.readBsonType();
							if (current_type != BsonType.END_OF_DOCUMENT){
								publication.electronicEdition = r.readString();
								current_type = r.readBsonType();
							}
						}
					}
				}

				//read []authors
				if ( current_type == BsonType.ARRAY && "authors".equals( current_name = r.readName()) ){
					r.readStartArray();
					while (r.readBsonType() != BsonType.END_OF_DOCUMENT) {
						r.readStartDocument();

						String Author_id = r.readString();
						String Author_name = r.readString();

						publication.authors_name_id.add(new Pair<String, String>(Author_name, Author_id));

						r.readEndDocument();
					}
					r.readEndArray();
				}

				if (current_type != BsonType.END_OF_DOCUMENT){
					publication.note = r.readString();
					current_type = r.readBsonType();
					if (current_type != BsonType.END_OF_DOCUMENT){
						publication.pages = r.readString();
					}
				}
				r.readEndDocument();
				if ( is_array ){
					r.readEndArray();				
				}
			}
		}
		return publication;
	}

	@SuppressWarnings("restriction")
	private DivIO fill_DivIO(Document doc, String is_a) {

		DivIO divobj = new DivIO();
		if ( doc == null ){
			divobj.is_empty = true;
			return divobj;
		}
		else {
			//System.out.println(doc.toJson());
		}

		//Conference 
		/*
		 * ConferenceEdition_id angepasst
		 * allenfalls kann man die ganzen Infos zu den Proceedings weglassen
		 */
		if ( "is_a_conference".equals(is_a) ){
			divobj.is_a_conference = true;
			BsonReader r = new JsonReader(doc.toJson());
			r.readStartDocument();

			String current_name = r.readName();		
			if ( "_id".equals( current_name) ){
				divobj.id = r.readString();
				current_name = r.readName();
			}
			if ( "name".equals( current_name) ){
				divobj.Conference_name = r.readString();
				if (r.readBsonType() != BsonType.END_OF_DOCUMENT){
					current_name = r.readName();
				}
				else {
					r.readEndDocument();
				}
			}

			if ("editions".equals(current_name)){
				r.readStartArray();
				while (r.readBsonType() != BsonType.END_OF_DOCUMENT) {
					r.readStartDocument();

					String ConferenceEdition_id = r.readString();
					int year = r.readInt32();
					String proceedings_id = r.readString();
					String proceedings_title = r.readString();
					divobj.Conference_editions_year_id.add(new Pair<Integer, String>(year, ConferenceEdition_id));

					r.readEndDocument();
				}
				r.readEndArray();
				if (r.readBsonType() != BsonType.END_OF_DOCUMENT){
					current_name = r.readName();
				}
				else {
					r.readEndDocument();
				}
			}
			divobj.Conference_editions_year_id.sort(comparePairYearId);

			return divobj;
		}

		//ConferenceEdition -- ConferenceEdition_id angepasst
		else if ( "is_a_conference_edition".equals(is_a) ){
			divobj.is_a_conference_edition = true;

			BsonReader r = new JsonReader(doc.toJson());
			r.readStartDocument();

			String current_name = r.readName();
			if ( "_id".equals( current_name) ){
				divobj.ConferenceEdition_conference_id = r.readString();
				current_name = r.readName();
			}
			if ( "name".equals( current_name) ){
				divobj.ConferenceEdition_conference_name = r.readString();
				if (r.readBsonType() != BsonType.END_OF_DOCUMENT){
					current_name = r.readName();
				}
				else {
					r.readEndDocument();
				}
			}
			//der Inhalt eines Arrays.. aber jeweils nur "1"-Eintrag
			if ("editions".equals(current_name)){
				r.readStartArray();
				while (r.readBsonType() != BsonType.END_OF_DOCUMENT) {
					r.readStartDocument();
					divobj.id = r.readString();
					divobj.ConferenceEdition_year = r.readInt32();
					divobj.ConferenceEditions_proceedings_id = r.readString();
					divobj.ConferenceEditions_proceedings_title = r.readString();				
					r.readEndDocument();
				}
				r.readEndArray();
				if (r.readBsonType() != BsonType.END_OF_DOCUMENT){
					current_name = r.readName();
				}
				else {
					r.readEndDocument();
				}
			}		
			return divobj;
		}

		//Person -- current_name angepasst
		else if ( "is_a_person".equals(is_a) ){
			divobj.is_a_person = true;

			BsonReader r = new JsonReader(doc.toJson());
			r.readStartDocument();

			String current_name =r.readName();
			if ( "_id".equals( current_name ) ){
				divobj.id = r.readString();
				current_name = r.readName();
			}
			if ( "name".equals( current_name ) ){
				divobj.Person_name = r.readString();
				current_name = r.readName();
			}

			if ("editedPublications".equals(current_name)){
				r.readStartArray();
				while (r.readBsonType() != BsonType.END_OF_DOCUMENT) {
					r.readStartDocument();
					String proceedings_id = r.readString();
					String proceedings_title = r.readString();
					r.readEndDocument();
					divobj.Person_editedPublications_title_id.add(new Pair<String, String>(proceedings_title, proceedings_id));
					/*System.out.println("proceedings_id"+proceedings_id);
					System.out.println("proceedings_title"+proceedings_title);*/
				}
				r.readEndArray();

				if (r.readBsonType() != BsonType.END_OF_DOCUMENT){
					current_name = r.readName();
				} else {
					r.readEndDocument();
				}
			}
			divobj.Person_editedPublications_title_id.sort(comparePairTitleId);

			if ("authoredPublications".equals(current_name)){
				r.readStartArray();
				while (r.readBsonType() != BsonType.END_OF_DOCUMENT) {
					r.readStartDocument();
					String Inproceedings_id = r.readString();
					String Inproceedings_title = r.readString();
					divobj.Person_authoredPublications_title_id.add(new Pair<String, String>(Inproceedings_title, Inproceedings_id));
					r.readEndDocument();
					/*System.out.println("Inproceedings_id"+Inproceedings_id);
					System.out.println("Inproceedings_title"+Inproceedings_title);*/
				}
				r.readEndArray();

				if (r.readBsonType() != BsonType.END_OF_DOCUMENT){
					current_name = r.readName();
				}
				else {
					r.readEndDocument();
				}
			}
			divobj.Person_authoredPublications_title_id.sort(comparePairTitleId);

			return divobj;
		}


		//Publisher ok -- current_name angepasst
		else if ( "is_a_publisher".equals(is_a) ){
			divobj.is_a_publisher = true;	

			BsonReader r = new JsonReader(doc.toJson());
			r.readStartDocument();

			String current_name = r.readName();
			if ( "_id".equals( current_name) ){
				divobj.id = r.readString();
				current_name =r.readName();
			}
			if ( "name".equals( current_name) ){
				divobj.Publisher_name = r.readString();
				if (r.readBsonType() != BsonType.END_OF_DOCUMENT){
					current_name = r.readName();
				}
				else {
					r.readEndDocument();
				}
			}

			if ("publications".equals(current_name)){
				r.readStartArray();
				while (r.readBsonType() != BsonType.END_OF_DOCUMENT) {
					r.readStartDocument();
					String proceedings_id = r.readString();
					String proceedings_title = r.readString();
					r.readEndDocument();
					divobj.Publisher_publications_title_id.add(new Pair<String, String>(proceedings_title, proceedings_id));
				}
				r.readEndArray();

				if (r.readBsonType() != BsonType.END_OF_DOCUMENT){
					current_name = r.readName();
				}
				else {
					r.readEndDocument();
				}
			}
			divobj.Publisher_publications_title_id.sort(comparePairTitleId);
			return divobj;
		}
		//Series ok -- current_name angepasst
		else if ( "is_a_series".equals(is_a) ){
			divobj.is_a_series = true;

			BsonReader r = new JsonReader(doc.toJson());
			r.readStartDocument();

			String current_name = r.readName();
			if ( "_id".equals( current_name) ){
				divobj.id = r.readString();
				current_name = r.readName();
			}
			if ( "name".equals( current_name) ){
				divobj.Series_name = r.readString();
				if (r.readBsonType() != BsonType.END_OF_DOCUMENT){
					current_name = r.readName();
				}
				else {
					r.readEndDocument();
				}
			}

			if ("publications".equals(current_name)){
				r.readStartArray();
				while (r.readBsonType() != BsonType.END_OF_DOCUMENT) {
					r.readStartDocument();
					String proceedings_id = r.readString();
					String proceedings_title = r.readString();
					r.readEndDocument();
					divobj.Series_publications_title_id.add(new Pair<String, String>(proceedings_title, proceedings_id));
				}
				r.readEndArray();
			}
			divobj.Series_publications_title_id.sort(comparePairTitleId);
			if (r.readBsonType() != BsonType.END_OF_DOCUMENT){
				current_name = r.readName();
			}
			else {
				r.readEndDocument();
			}

			return divobj;
		}
		return divobj;
	}



	/**
	 * IO-Methods
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	@Override
	public String IO_get_statistics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String IO_authors_editors_for_a_conference(String conf_id, String mode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String IO_avg_authors_per_inproceedings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String IO_count_publications_per_interval(int y1, int y2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String IO_delete_get_person_by_id(String pers_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String IO_find_author_distance_path(String name1, String name2) {
		String Output="";
		
		String pers1 = name1;
		String pers2 = name2;
		if ( !exists_person_by_name(name1)){
			Output = "<br>name1 (="+ name1 + ") or name2 (="+ name2 + ") is not a person<br>";
			return Output;
		}
		if ( !exists_person_by_name(name2)){
			Output = "<br>name1 (="+ name1 + ") or name2 (="+ name2 + ") is not a person<br>";
			return Output;
		}
		
		
		
		return Output;
	}
	
	@Override
	//do not use it
	public List<DivIO> IO_find_co_authors(String pers_name) {
		// TODO Auto-generated method stub
		return null;
	}	
	@Override
	public String IO_find_co_authors_returns_String(String pers_name) {
		HashMap<Pair<String,String>,List<Pair<String,String>>> common_inprocs = new HashMap<Pair<String,String>,List<Pair<String,String>>>();
		List<Pair<String,String>> Author_id_name_sort = new ArrayList<Pair<String,String>>();
		String Output ="";
		//check if person exists
		if (!exists_person_by_name(pers_name)){
			Output+="person not found...";
			return Output;
		}

		String Inproceedings_id ="";
		String Inproceedings_title ="";
		String Author_id="";
		String Author_name="";
		for (Document doc: find_co_authors(pers_name)){
			System.out.println(doc.toJson());


			BsonReader r = new JsonReader(doc.toJson());
			r.readStartDocument();

			String current_name = r.readName();
			BsonType current_type;

			if ("inproceedings".equals(current_name)){
				r.readStartDocument();
				current_type = r.readBsonType();
				if (current_type != BsonType.END_OF_DOCUMENT){
					Inproceedings_id = r.readString();
					current_type = r.readBsonType();
					if (current_type != BsonType.END_OF_DOCUMENT){
						Inproceedings_title = r.readString();
						current_type = r.readBsonType();
					}
				}

				Pair<String,String> Inproc_id_title = new Pair<String,String>(Inproceedings_id,Inproceedings_title);
				//read []authors
				if ( current_type == BsonType.ARRAY && "authors".equals( current_name = r.readName()) ){
					r.readStartArray();
					while (r.readBsonType() != BsonType.END_OF_DOCUMENT) {
						r.readStartDocument();

						Author_id = r.readString();
						Author_name = r.readString();

						Pair<String,String> Author_id_name = new Pair<String,String>(Author_id,Author_name);

						List<Pair<String,String>> existing_list = common_inprocs.get(Author_id_name);
						if(existing_list!=null){
							//add to list
							existing_list.add(Inproc_id_title);
						} else {
							//create new Author entry
							List<Pair<String,String>> new_list = new ArrayList<Pair<String,String>>();
							new_list.add(Inproc_id_title);
							common_inprocs.put(Author_id_name, new_list);
							//for sort list
							Author_id_name_sort.add(Author_id_name);
							;
						}

						r.readEndDocument();
					}
					r.readEndArray();
				}
			}
		}
		Author_id_name_sort.sort(compare_Author_id_name);
		Output += "<b>Jumplist:</b> ";
		for (Pair<String,String> Author_id_name: Author_id_name_sort){
			Output += "<a href='#"+Author_id_name.getKey()+"'>"+Author_id_name.getValue() + "</a>, ";
		}
		Output += "<br>";
		
		
		final String[] temp = new String[1];
		temp[0] = "";
		

		BiConsumer<Pair<String, String>,List<Pair<String, String>>> action = new BiConsumer<Pair<String, String>,List<Pair<String, String>>>(){

			@Override
			public void accept(Pair<String, String> Author_id_name, List<Pair<String, String>> Inproc_id_title) {
				if (Author_id_name.getValue().equals(pers_name)){
					return;
				}
				temp[0] += "<h4 id='"+Author_id_name.getKey()+"'><a href='/test/?func=person_by_id&id=" +Author_id_name.getKey()+ "'>"+Author_id_name.getValue()+"</a></h4>";
				//temp[0] += Author_id_name.getValue() +" is co-author of "+pers_name+" in the following inproceedings: <br>";
				temp[0] += "<ol type='1'>";
				for (Pair<String,String> inprTid: Inproc_id_title){
					String title = inprTid.getValue().substring(0, Math.min(200,inprTid.getKey().length()));
					String id = inprTid.getKey();
					temp[0] += "<li>"+title + " (<a href='/test/?func=inproceeding_by_id&key=" + id + "'>"+id+"</a>)</li>";
				}
				temp[0] += "</ol>";				
				temp[0] += "<br><br>";
			}

		};
		common_inprocs.forEach(action);
		Output += temp[0];
		/**
		 * indices erstellen
		 * und query plan anschauen
		 */
		return Output;
	}



	@Override
	public DivIO IO_get_conf_by_id(String conf_id) {
		return fill_DivIO(get_conference_by_id(conf_id),"is_a_conference");
	}


	@Override 
	public DivIO IO_get_confEd_by_id(String confEd_id) {
		return fill_DivIO(get_conferenceEdition_by_id(confEd_id),"is_a_conference_edition");
	}


	public DivIO IO_get_conferenceEdition_by_year(String Conference_id, int year) {
		return fill_DivIO(get_conferenceEdition_by_year(Conference_id,year),"is_a_conference_edition");
	}

	@Override
	public List<DivIO> IO_get_conference_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		List<DivIO> return_list = new ArrayList<DivIO>();

		int limit = 0;
		int skip = 0;
		if (boff==0 || eoff< boff){
			limit = 5;
			skip = 0;
		}
		else {
			limit = eoff - boff + 1;
			skip = boff -1;
		}

		//get conferences, if any
		for (Document doc: get_conferences_by_filter_offset(filter, skip, limit, order_by)){
				return_list.add(fill_DivIO(doc,"is_a_conference"));
		}

		return return_list;
	}



	@Override
	public List<DivIO> IO_get_person_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		List<DivIO> return_list = new ArrayList<DivIO>();

		int limit = 0;
		int skip = 0;
		if (boff==0 || eoff< boff){
			limit = 5;
			skip = 0;
		}
		else {
			limit = eoff - boff + 1;
			skip = boff -1;
		}

		//get persons, if any
		for (Document doc: get_persons_by_filter_offset(filter, skip, limit, order_by)){
				return_list.add(fill_DivIO(doc,"is_a_person"));
		}

		return return_list;
	}

	



	@Override
	public DivIO IO_get_person_by_id(String pers_id) {
		//Document pers = get_person_by_id(pers_id);
		return fill_DivIO(get_person_by_id(pers_id),"is_a_person");
	}

	@Override
	/**
	 * !!!!!!!!!! auf fast fill gestellt
	 */
	public List<PublicationIO> IO_get_publ_by_filter_offset(String filter, int boff, int eoff, String order_by) {

		List<PublicationIO> return_list = new ArrayList<PublicationIO>();

		int limit = 0;
		int skip = 0;
		if (boff==0 || eoff< boff){
			limit = 5;
			skip = 0;
		}
		else {
			limit = eoff - boff + 1;
			skip = boff -1;
		}

		//get proceedings, if any
		for (Document doc: get_proceedings_by_filter_offset(filter, skip, limit, order_by)){
				return_list.add(fill_PublicationIO(doc,"is_a_proceeding"));
		}

		//get inproceedings, if any
		//AggregateIterable<Document> slow = get_inproceedings_by_filter_offset(filter, boff, eoff, order_by);
		//System.out.println("have doc's from db...");
		//for (Document doc: slow){
		for (Document doc: get_inproceedings_by_filter_offset(filter, skip, limit, order_by)){	
			return_list.add(fill_PublicationIO(doc,"is_an_inproceeding"));
		}
		return return_list;
	}

	

	@Override
	public PublicationIO IO_get_publication_by_id(String publ_id) {
		// TODO Auto-generated method stub
		Document inproc = get_inproceedings_by_id(publ_id);
		if ( inproc != null ){
			return fill_PublicationIO(inproc, "is_an_inproceeding");
		}
		else {
			Document proc = get_proceedings_by_id(publ_id);
			if ( proc != null ){
				return fill_PublicationIO(proc, "is_a_proceeding");
			}
		}
		return fill_PublicationIO(null,"null");
	}

	public PublicationIO IO_get_inproceedings_by_id(String id) {
		return fill_PublicationIO(get_inproceedings_by_id(id),"is_an_inproceeding");
	}

	public PublicationIO IO_get_proceedings_by_id(String id) {
		return fill_PublicationIO(get_proceedings_by_id(id),"is_a_proceeding");
	}


	@Override
	public List<DivIO> IO_get_publisher_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		List<DivIO> return_list = new ArrayList<DivIO>();

		int limit = 0;
		int skip = 0;
		if (boff==0 || eoff< boff){
			limit = 5;
			skip = 0;
		}
		else {
			limit = eoff - boff + 1;
			skip = boff -1;
		}

		//get publishers, if any
		for (Document doc: get_publishers_by_filter_offset(filter, skip, limit, order_by)){
				return_list.add(fill_DivIO(doc,"is_a_publisher"));
		}

		return return_list;
	}


	@Override
	public DivIO IO_get_publisher_by_id(String publ_id) {
		return fill_DivIO(get_publisher_by_id(publ_id),"is_a_publisher");
	}

	@Override
	public List<DivIO> IO_get_series_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		List<DivIO> return_list = new ArrayList<DivIO>();

		int limit = 0;
		int skip = 0;
		if (boff==0 || eoff< boff){
			limit = 5;
			skip = 0;
		}
		else {
			limit = eoff - boff + 1;
			skip = boff -1;
		}

		//get series, if any
		for (Document doc: get_series_by_filter_offset(filter, skip, limit, order_by)){
				return_list.add(fill_DivIO(doc,"is_a_series"));
		}

		return return_list;
	}


	@Override
	public DivIO IO_get_series_by_id(String series_id) {
		return fill_DivIO(get_series_by_id(series_id),"is_a_series");
	}

	@Override
	public String IO_inproceedings_for_a_conference(String conf_id, String mode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String IO_person_is_author_and_editor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PublicationIO> IO_person_is_last_author(String pers_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PublicationIO> IO_publ_by_person_name_or_id(HashMap<String, String> args) {
		List<PublicationIO> return_list = new ArrayList<PublicationIO>();
	
		//check if person exists
		boolean pers_exists;
		
		String pers_id = "";

		if( args.containsKey("id") ){
			//get person by id
			pers_exists = exists_person_by_id(args.get("id"));
			pers_id = args.get("id");
		}
		else {
			//get person by name
			pers_exists = exists_person_by_name(args.get("name"));
			//get person id
			if (pers_exists){
				pers_id = get_person_by_name(args.get("name")).get("_id").toString();
				
				
				
			}
		}

		if ( ! pers_exists ){
			//return empty list
			return return_list;
		}
				
		String publ_mode = args.get("publ");
		
		//get proceedings
		if ( publ_mode.equals("all") || publ_mode.equals("editored")){
			for (Document doc: get_proceedings_by_Editor_id(pers_id)){
				return_list.add(fill_PublicationIO(doc,"is_a_proceeding"));
			}
			
			/*			
			Block<Document> call_fill_PublicationIO_proc = new Block<Document>() {
			     @Override
			     public void apply(final Document doc) {
						return_list.add(fill_PublicationIO(doc,"is_a_proceeding"));
			     }
			};
			get_proceedings_by_Editor_id(pers_id).forEach(call_fill_PublicationIO_proc);
			*/
		}
		
		//get inproceedings
		if ( publ_mode.equals("all") || publ_mode.equals("authored")){
			for (Document doc: get_inproceedings_by_Author_id(pers_id)){
				return_list.add(fill_PublicationIO(doc,"is_an_inproceeding"));
			}
		}
			
		
		return return_list;
	}





	@Override
	public List<DivIO> IO_publishers_whose_authors_in_interval(int y1, int y2) {
		// TODO Auto-generated method stub
		return null;
	}








	







}
