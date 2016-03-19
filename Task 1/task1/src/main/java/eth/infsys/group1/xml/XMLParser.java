package eth.infsys.group1.xml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eth.infsys.group1.dbspec.DBProvider;

public final class XMLParser<TRProceedings> {

	private static String[] noContents = new String[0];
	
	private static String getTagContent(Element element, String subtag) {
		NodeList nl = element.getElementsByTagName(subtag);
		if (nl.getLength() == 0)
			return null;
		return nl.item(0).getTextContent();
	}
	
	private static String[] getTagContents(Element element, String subtag) {
		NodeList nl = element.getElementsByTagName(subtag);
		int count = nl.getLength();
		if (nl.getLength() == 0)
			return noContents;
		String[] contents = new String[count];
		for (int j = 0; j < count; j++)
			contents[j] = nl.item(j).getTextContent();
		return contents;
	}
	
	private HashMap<String, TRProceedings> proceedingsTable = new HashMap<>();
	private DBProvider<?, ?, ?, ?, TRProceedings, ?, ?, ?> dbProvider;
	
	private XMLParser(DBProvider<?, ?, ?, ?, TRProceedings, ?, ?, ?> dbProvider) {
		this.dbProvider = dbProvider;
	}
	
	public void parseXMLFile(File file) throws ParserConfigurationException, SAXException, IOException {
		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(file);
		doc.getDocumentElement().normalize();
		
		this.parseProceedings(doc);
		this.parseInProceedings(doc);
	}
	
	/**
	 * Parses the proceedings described in an XML document and stores them in the provided database.
	 * 
	 * Proceedings should be described in XML as follows:<br><br>
	 * 
	 * {@code <proceedings mdate="(not evaluated)" key="(proceedings key)">}<br>
	 * {@code     <editor>(1st editor name)</editor>}<br>
	 * {@code     <editor>(2nd editor name)</editor>}<br>
	 * {@code     <editor>...</editor>}<br>
	 * {@code     <title>(Title of the proceedings)</title>}<br>
	 * {@code     <booktitle>(Title of the conference, optional)</booktitle>}<br>
	 * {@code     <series>(Title of the series, optional)</series>}<br>
	 * {@code     <volume>(Volume of the proceedings, optional)</volume>}<br>
	 * {@code     <publisher>(Publisher of the proceedings)</publisher>}<br>
	 * {@code     <year>(Publication year)</year>}<br>
	 * {@code     <isbn>(ISBN of the proceedings, optional)</isbn>}<br>
	 * {@code     <ee>(Electronic edition of the proceedings, optional)</ee>}<br>
	 * {@code     <url>(URL of the proceedings, not evaluated)</url>}<br>
	 * {@code     <note>(Note, optional)</note>}<br>
	 * {@code </proceedings>}<br>
	 * 
	 * @param doc The XML document
	 * @param dbProvider The provider of the database in which the parsed data should be stored
	*/
	private void parseProceedings(Document doc) {
		NodeList procList = doc.getElementsByTagName("proceedings");
		int length = procList.getLength();
		for (int i = 0; i < length; i++) {
			Node procNode = procList.item(i);
			if (procNode.getNodeType() == Node.ELEMENT_NODE) {
				XMLProceedings data = new XMLProceedings();
				Element procElement = (Element)procNode;
				
				String key = procElement.getAttribute("key");
				String[] splitKey = key.split("/");
				data.conferenceID = splitKey[1];
				
				data.conferenceTitle = getTagContent(procElement, "booktitle");
				data.title = getTagContent(procElement, "title");
				data.year = Integer.valueOf(getTagContent(procElement, "year"));
				data.editors = getTagContents(procElement, "editor");
				data.volume = getTagContent(procElement, "volume");
				data.publisher = getTagContent(procElement, "publisher");
				data.series = getTagContent(procElement, "series");
				data.isbn = getTagContent(procElement, "isbn");
				data.electronicEdition = getTagContent(procElement, "ee");
				data.note = getTagContent(procElement, "note");
				
				proceedingsTable.put(key, dbProvider.createProceedings(data));
			}
		}
	}
	
	/**
	 * Parses the inProceedings described in an XML document and stores them in the provided database.
	 * 
	 * InProceedings should be described in XML as follows:<br><br>
	 * 
	 * {@code <inproceedings mdate="(not evaluated)" key="(not evaluated)">}<br>
	 * {@code     <crossref>(key of the proceedings)</crossref>}<br>
	 * {@code     <author>(1st author name)</author>}<br>
	 * {@code     <author>(2nd author name)</author>}<br>
	 * {@code     <author>...</author>}<br>
	 * {@code     <title>(Title of the publication)</title>}<br>
	 * {@code     <pages>(Pages inside the proceedings)</pages>}<br>
	 * {@code     <year>(Publication year)</year>}<br>
	 * {@code     <booktitle>(Title of the conference, not evaluated)</booktitle>}<br>
	 * {@code     <ee>(Electronic edition of the publication, optional)</ee>}<br>
	 * {@code     <url>(URL of the publication, not evaluated)</url>}<br>
	 * {@code     <note>(Note, optional)</note>}<br>
	 * {@code </inproceedings>}<br>
	 * 
	 * @param doc The XML document
	 * @param dbProvider The provider of the database in which the parsed data should be stored
	*/
	private void parseInProceedings(Document doc) {
		NodeList inprocList = doc.getElementsByTagName("inproceedings");
		int length = inprocList.getLength();
		for (int i = 0; i < length; i++) {
			Node inprocNode = inprocList.item(i);
			if (inprocNode.getNodeType() == Node.ELEMENT_NODE) {
				XMLInProceedings data = new XMLInProceedings();
				Element inProcElement = (Element)inprocNode;
				
				String procKey= getTagContent(inProcElement, "crossref");
				if (procKey == null)
					continue;
				TRProceedings proceedings = proceedingsTable.get(procKey);
				if (proceedings == null)
					continue;
				
				data.title = getTagContent(inProcElement, "title");
				data.year = Integer.valueOf(getTagContent(inProcElement, "year"));
				data.authors = getTagContents(inProcElement, "author");
				data.pages = getTagContent(inProcElement, "pages");
				data.electronicEdition = getTagContent(inProcElement, "ee");
				data.note = getTagContent(inProcElement, "note");
				
				dbProvider.createInProceedings(data, proceedings);
			}
		}
	}
	
}
