package eth.infsys.group1.xmlparser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eth.infsys.group1.dbspec.DBProvider;
import javafx.util.Pair;

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

	private HashSet<String> proceedingsTable = new HashSet<>();
	private HashMap<String, Pair<String,Integer>> confEditions = new HashMap<>();


	private DBProvider<?, ?, ?, ?, TRProceedings, ?, ?, ?> dbProvider;

	public XMLParser(DBProvider<?, ?, ?, ?, TRProceedings, ?, ?, ?> dbProvider) {
		this.dbProvider = dbProvider;
	}

	public void parseXMLFile(File file) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.   newDocumentBuilder();
		Document doc = docBuilder.   parse(file);		
		doc.getDocumentElement().normalize();		

		this.parseConfEditions(doc);
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
		System.out.println("XMLParser: ---------------2. Create Proceedings...");
		NodeList procList = doc.getElementsByTagName("proceedings");
		int length = procList.getLength();
		for (int i = 0; i < length; i++) {
			Node procNode = procList.item(i);
			if (procNode.getNodeType() == Node.ELEMENT_NODE) {
				Proceedings_simple_input data = new Proceedings_simple_input();
				Element procElement = (Element)procNode;


				data.id = procElement.getAttribute("key");
				String safe_key = data.id;

				Pair<String,Integer> confEd = confEditions.get(data.id);
				if (confEd == null) {
					System.out.println("XMLParser: Proceedings (" + data.id +") finds no confEd in HashMap");
					continue;
				}

				data.title = getTagContent(procElement, "title");
				data.editors = getTagContents(procElement, "editor");
				data.year = Integer.valueOf(getTagContent(procElement, "year"));
				data.electronicEdition = getTagContent(procElement, "ee");
				data.note = getTagContent(procElement, "note");
				String numberAsString = getTagContent(procElement, "number");
				if (numberAsString != null){
					data.number = Integer.valueOf(getTagContent(procElement, "number"));
				}
				else {
					data.number = 0;
				}
				data.publisher = getTagContent(procElement, "publisher");
				/*if (data.publisher==null){
					System.out.println("data.publisher==null");
				}*/
				data.volume = getTagContent(procElement, "volume");
				data.isbn = getTagContent(procElement, "isbn");
				data.series = getTagContent(procElement, "series");
				/*if (data.series==null){
					System.out.println("data.series==null");
				}*/

				data.conferenceName = confEd.getKey();
				data.conferenceEdition = confEd.getValue();
				
				//more conditions besides that the ConfEd must exist
				if (data.id==null||data.title==null||data.year==0){
					continue;				
				}
				
				String ret_val = dbProvider.batch_createProceedings(data);
				if (ret_val.equals(safe_key)) {
					proceedingsTable.add(ret_val);
				}
				else {
					System.out.println("XMLParser: FATAL-ERROR: Proceedings key<=>id missmatch.. delete DB :-)");
					return;
				}
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
		System.out.println("XMLParser: ---------------3. Create InProceedings...");
		NodeList inprocList = doc.getElementsByTagName("inproceedings");
		int length = inprocList.getLength();
		int not_created = length;
		for (int i = 0; i < length; i++) {
			Node inprocNode = inprocList.item(i);
			if (inprocNode.getNodeType() == Node.ELEMENT_NODE) {
				InProceedings_simple_input data = new InProceedings_simple_input();
				Element inProcElement = (Element)inprocNode;

				data.id = inProcElement.getAttribute("key");
				String safe_key = data.id;
				data.title = getTagContent(inProcElement, "title");
				data.year = Integer.valueOf(getTagContent(inProcElement, "year"));
				data.electronicEdition = getTagContent(inProcElement, "ee");
				data.authors = getTagContents(inProcElement, "author");
				data.note = getTagContent(inProcElement, "note");
				data.pages = getTagContent(inProcElement, "pages");
				data.crossref = getTagContent(inProcElement, "crossref");
				data.conferenceEdition = data.year;
				data.conferenceName = getTagContent(inProcElement, "booktitle");

				//String procKey= getTagContent(inProcElement, "crossref");
				if (data.crossref == null){
					System.out.println("XMLParser: InProceedings (" + data.id +") has no crossref...");
					continue;
				}
				Boolean proc_exists = proceedingsTable.contains(data.crossref);
				if (proc_exists == false) {
					System.out.println("XMLParser: InProceedings (" + data.id +") not created, needs Proceedings with key="+ data.crossref);
					continue;
				}
				
				//Conditions
				if (data.id==null||data.title==null||data.year==0){
					continue;				
				}

				not_created--;

				String ret_val = dbProvider.batch_createInProceedings(data);

				if (! ret_val.equals(safe_key)) {
					System.out.println("XMLParser: FATAL-ERROR: Proceedings key<=>id missmatch.. delete DB :-)");
					return;
				}
			}
		}
		System.out.println("XMLParser: InProceedings in xml =" + length +" .... not created=" + not_created);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void parseConfEditions(Document doc) {
		System.out.println("XMLParser: ---------------1. Fill ConfEdition-Map...");
		NodeList inprocList = doc.getElementsByTagName("inproceedings");
		int length = inprocList.getLength();
		for (int i = 0; i < length; i++) {
			Node inprocNode = inprocList.item(i);
			if (inprocNode.getNodeType() == Node.ELEMENT_NODE) {
				Element inProcElement = (Element)inprocNode;
				//String id = inProcElement.getAttribute("key");
				int year = Integer.valueOf(getTagContent(inProcElement, "year"));
				String crossref = getTagContent(inProcElement, "crossref");
				int conferenceEdition = year;
				String conferenceName = getTagContent(inProcElement, "booktitle");

				if (crossref == null || year == 0 || conferenceName == null){
					continue;
				}
				Pair<String,Integer> confEd = confEditions.get(crossref);

				if (confEd == null) {
					System.out.println("XMLParser: ("+ conferenceName +"," +conferenceEdition +") added to ConfEdition-Map...");
					confEditions.put(crossref, new Pair(conferenceName, conferenceEdition));
				}
			}
		}
	}	
}