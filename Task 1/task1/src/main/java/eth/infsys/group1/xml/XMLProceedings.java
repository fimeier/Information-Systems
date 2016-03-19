package eth.infsys.group1.xml;

/**
 * A simple string- and integer-based representation of
 * a proceeding as it appears in an XML file
 */
public class XMLProceedings {
	
	private static String[] noEditors = new String[0];
	
	public String conferenceID = null;
	public String conferenceTitle = null;
	public String title = null;
	public int year = 0;
	public String[] editors = noEditors;
	public String volume = null;
	public String publisher = null;
	public String series = null;
	public String isbn = null;
	public String electronicEdition = null;
	public String note = null;
	
	public XMLProceedings() { }

}
