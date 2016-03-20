package eth.infsys.group1.dbspec;

import java.util.List;

import eth.infsys.group1.ui.fxobjs.FxConference;
import eth.infsys.group1.ui.fxobjs.FxConferenceEdition;
import eth.infsys.group1.ui.fxobjs.FxInProceedings;
import eth.infsys.group1.ui.fxobjs.FxPerson;
import eth.infsys.group1.ui.fxobjs.FxProceedings;
import eth.infsys.group1.ui.fxobjs.FxPublication;
import eth.infsys.group1.ui.fxobjs.FxPublisher;
import eth.infsys.group1.ui.fxobjs.FxSeries;
import eth.infsys.group1.xml.XMLInProceedings;
import eth.infsys.group1.xml.XMLProceedings;
import eth.infsys.group1.xmlparser.InProceedings_simple_input;
import eth.infsys.group1.xmlparser.Proceedings_simple_input;

public abstract class DBProvider<TRConference,
TRConferenceEdition,
TRInProceedings,
TRPerson,
TRProceedings,
TRPublication,
TRPublisher,
TRSeries> {

	public static final class InvalidDBRepException extends Exception {

		private static final long serialVersionUID = -3845406311284661251L;

		public final Object invalidRep;

		public InvalidDBRepException(Object invalidRep) {
			super("Invalid DB representation argument");
			this.invalidRep = invalidRep;
		}

	}

	public DBProvider() { }

	/**
	 * Uses the provided data to create a new conference
	 * and sets the DBRepresentation field of the passed FX object.
	 * @param fxObj The FX object representing the conference
	 */
	public abstract void createConference(FxConference<TRConference> fxObj);

	/**
	 * Uses the provided data to create a new conference edition
	 * and sets the DBRepresentation and ID fields of the passed FX object.
	 * @param fxObj The FX object representing the conference edition
	 * @param conference The conference
	 * @throws InvalidDBRepException If {@code conference} is not a valid conference representation
	 */
	public abstract void createConferenceEdition(FxConferenceEdition<TRConferenceEdition> fxObj,
			TRConference conference) throws InvalidDBRepException;

	/**
	 * Uses the provided data to create a new in-proceedings publication
	 * and sets the DBRepresentation and ID fields of the passed FX object.
	 * @param fxObj The FX object representing the publication
	 * @param proceedings The proceedings
	 * @throws InvalidDBRepException If {@code proceedings} is not a valid proceedings representation
	 */
	public abstract void createInProceedings(FxInProceedings<TRInProceedings> fxObj,
			TRProceedings proceedings) throws InvalidDBRepException;

	/**
	 * Uses the provided XML data and proceedings to create an inProceedings in the database.
	 * The authors are created as well if they do not exist yet.
	 * @param xmlData The XML data object representing the inProceedings
	 */
	public abstract TRInProceedings createInProceedings(XMLInProceedings xmlData,
			TRProceedings proceedings);

	/**
	 * Uses the provided data to create a new person
	 * and sets the DBRepresentation and ID fields of the passed FX object.
	 * @param fxObj The FX object representing the person
	 */
	public abstract void createPerson(FxPerson<TRPerson> fxObj);

	/**
	 * Uses the provided data to create the proceedings of the provided conference edition
	 * and sets the DBRepresentation and ID fields of the passed FX object.
	 * @param fxObj The FX object representing the proceedings
	 * @param confEdition The conference edition
	 * @throws InvalidDBRepException If {@code confEdition} is not a valid conference edition representation
	 */
	public abstract void createProceedings(FxPerson<TRPerson> fxObj,
			TRConferenceEdition confEdition) throws InvalidDBRepException;

	/**
	 * Uses the provided XML data to create a proceeding in the database.
	 * The conference, the editors, the publisher and the series are created
	 * as well if they do not exist yet.
	 * @param xmlData The XML data object representing the proceedings
	 */
	public abstract TRProceedings createProceedings(XMLProceedings xmlData);

	/**
	 * Uses the provided data to create a new publisher
	 * and sets the DBRepresentation and ID fields of the passed FX object.
	 * @param fxObj The FX object representing the publisher
	 */
	public abstract void createPublisher(FxPublisher<TRPublisher> fxObj);

	/**
	 * Uses the provided data to create a new publication series
	 * and sets the DBRepresentation and ID fields of the passed FX object.
	 * @param fxObj The FX object representing the series
	 */
	public abstract void createSeries(FxSeries<TRSeries> fxObj);

	public abstract void deleteConference(TRConference conference);
	public abstract void deleteConferenceEdition(TRConferenceEdition conferenceEdition);
	public abstract void deleteInProceedings(TRInProceedings inProceedings);
	public abstract void deletePerson(TRPerson person);
	public abstract void deleteProceedings(TRProceedings proceedings);
	public abstract void deletePublisher(TRPublisher publisher);
	public abstract void deleteSeries(TRSeries series);

	public abstract TRConference getConferenceByID(String id);
	public abstract TRConferenceEdition getConferenceEditionByID(String id);
	public abstract TRInProceedings getInProceedingsByID(String id);
	public abstract TRPerson getPersonByID(String id);
	public abstract TRProceedings getProceedingsByID(String id);
	public abstract TRPublication getPublicationByID(String id);
	public abstract TRPublisher getPublisherByID(String id);
	public abstract TRSeries getSeriesByID(String id);

	public abstract void getConferences(int startIndex, int endIndex, FxConference.SortOption sort,
			String searchTerm, List<FxConference<TRConference>> out);

	public abstract void getConferenceEditions(int startIndex, int endIndex,
			FxConferenceEdition.SortOption sort, String searchTerm,
			List<FxConferenceEdition<TRConferenceEdition>> out);

	public abstract void getConferenceEditions(int startIndex, int endIndex,
			FxConferenceEdition.SortOption sort,
			TRConference conference, String searchTerm,
			List<FxConferenceEdition<TRConferenceEdition>> out);

	public abstract void getInProceedings(int startIndex, int endIndex,
			FxInProceedings.SortOption sort, String searchTerm,
			List<FxInProceedings<TRInProceedings>> out);

	public abstract void getInProceedingsOfAuthor(int startIndex, int endIndex,
			FxInProceedings.SortOption sort, TRPerson author, String searchTerm,
			List<FxInProceedings<TRInProceedings>> out);

	public abstract void getInProceedingsOfProceedings(int startIndex, int endIndex,
			FxInProceedings.SortOption sort, TRProceedings proceedings, String searchTerm,
			List<FxInProceedings<TRInProceedings>> out);

	public abstract void getProceedings(int startIndex, int endIndex,
			FxProceedings.SortOption sort, String searchTerm,
			List<FxProceedings<TRProceedings>> out);

	public abstract void getProceedingsOfEditor(int startIndex, int endIndex,
			FxProceedings.SortOption sort, TRPerson author, String searchTerm,
			List<FxProceedings<TRProceedings>> out);

	public abstract void getPublishers(int startIndex, int endIndex,
			FxPublisher.SortOption sort, String searchTerm,
			List<FxInProceedings<TRPublisher>> out);

	public abstract void getPersons(int startIndex, int endIndex,
			FxPerson.SortOption sort, String searchTerm, List<FxPerson<TRPerson>> out);

	public abstract void getSeries(int startIndex, int endIndex,
			FxSeries.SortOption sort, String searchTerm, List<FxSeries<TRSeries>> out);

	public abstract void setConferenceTitle(TRConference conference, String title);
	public abstract void setConferenceEditionYear(TRConferenceEdition edition, int year);
	public abstract void setPersonName(TRPerson person, String name);

	public abstract void updateConferenceData(List<FxConference<TRConference>> data);
	public abstract void updateConferenceEditionData(List<FxConferenceEdition<TRConferenceEdition>> data);
	public abstract void updateInProceedingsData(List<FxInProceedings<TRInProceedings>> data);
	public abstract void updatePersonData(List<FxPerson<TRPerson>> data);
	public abstract void updateProceedingsData(List<FxProceedings<TRProceedings>> data);
	public abstract void updatePublicationData(List<FxPublication<TRPublication>> data);
	public abstract void updatePublisherData(List<FxPublisher<TRPublisher>> data);
	public abstract void updateSeriesData(List<FxSeries<TRSeries>> data);

	public abstract String batch_createInProceedings(InProceedings_simple_input args);
	public abstract String batch_createProceedings(Proceedings_simple_input args);

}
