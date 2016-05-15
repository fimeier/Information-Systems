package eth.infsys.group1.task3;

import javax.xml.xquery.*;

import baseX.examples.ExecutingCommands;
import eth.infsys.group1.dbspec.DBProvider;
import eth.infsys.group1.task1.T1DBProvider;

import javax.xml.namespace.QName;
import net.xqj.basex.BaseXXQDataSource;

public class baseX_Test {

	public static void main(String[] args) throws XQException
	  {
		/*
	    XQDataSource xqs = new BaseXXQDataSource();
	    xqs.setProperty("serverName", "localhost");
	    xqs.setProperty("port", "1984");

	    // Change USERNAME and PASSWORD values
	    XQConnection conn = xqs.getConnection("admin", "admin");

	    XQPreparedExpression xqpe =
	    conn.prepareExpression("declare variable $x as xs:string external; $x");

	    xqpe.bindString(new QName("x"), "Hello World!", null);

	    XQResultSequence rs = xqpe.executeQuery();

	    while(rs.next())
	      System.out.println(rs.getItemAsString(null));
	    

	    conn.close();
	    
	    String args2[]= {"localhost","1984","admin","admin"};
	    
	    ExecutingCommands.main(args2);
	    */
	    String dbName = "big_xml";
	    String User = "admin";
	    String PW = "admin";
	    T3DBProvider myDB = new T3DBProvider(dbName, User, PW);
	    myDB.testx();
	  }

}
