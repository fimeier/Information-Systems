package eth.infsys.group1.xml;

/**
 * A simple string- and integer-based representation of
 * an inProceeding as it appears in an XML file
 */
public class XMLInProceedings {
	
	private static String[] noAuthors = new String[0];
	
	public String title = null;
	public int year = 0;
	public String[] authors = noAuthors;
	public String pages = null;
	public String electronicEdition = null;
	public String note = null;
	
	public XMLInProceedings() { }

}
