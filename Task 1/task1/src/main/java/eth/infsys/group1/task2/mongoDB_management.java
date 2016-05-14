package eth.infsys.group1.task2;

import eth.infsys.group1.dbspec.DBProvider;

public class mongoDB_management {

	public static void main(String[] args) {
		
		//String MongodbName ="myDBLP";	
		//String MongodbName ="myDBLPv2";
		String MongodbName ="myDBLP_manipulate";

		//T2DBProvider myDB = new T2DBProvider(MongodbName, DBProvider.OPEN_DB_OVERRIDE);
		T2DBProvider myDB = new T2DBProvider(MongodbName, DBProvider.OPEN_DB_APPEND);
		
		myDB.createIndexes("all");
		/*
		myDB.createIndexes("Publications");
		myDB.createIndexes("Conferences");
		myDB.createIndexes("Persons");
		myDB.createIndexes("Publishers");
		myDB.createIndexes("Series");*/

	}

}
