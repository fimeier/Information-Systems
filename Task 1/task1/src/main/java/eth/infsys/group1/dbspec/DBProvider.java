package eth.infsys.group1.dbspec;

import java.util.List;
import eth.infsys.group1.ui.fxobjs.*;
import eth.infsys.group1.xml.XMLInProceedings;
import eth.infsys.group1.xml.XMLProceedings;

public abstract class DBProvider<TRConference,
								TRConferenceEdition,
								TRInProceedings,
								TRPerson,
								TRProceedings,
								TRPublication,
								TRPublisher,
								TRSeries> {

	public static final class ExistingIDException extends Exception {

		private static final long serialVersionUID = -3845406311284661251L;
		
		public final String id;
		
		public ExistingIDException(String id) {
			super("An object with the id '" + id + "' already exists.");
			this.id = id;
		}
		
	}

	public static final class InvalidDataException extends Exception {

		private static final long serialVersionUID = -3845406311284661251L;
		
		public final String field;
		
		public InvalidDataException(String field) {
			super("Invalid data field: '" + field + "'.");
			this.field = field;
		}
		
	}
	
	public static final class InvalidDBRepException extends Exception {

		private static final long serialVersionUID = -3845406311284661251L;
		
		public final Object invalidRep;
		public final String repDescription;
		
		public InvalidDBRepException(Object invalidRep, String repDescription) {
			super("Invalid DB representation argument: " + repDescription);
			this.invalidRep = invalidRep;
			this.repDescription = repDescription;
		}
		
	}
	
	public DBProvider() { }

	/**
	 * Uses the provided data to create a new conference
	 * and sets the DBRepresentation field of the passed FX object.
	 * @param fxObj The FX object representing the conference
	 * @throws ExistingIDException If a conference with the provided ID already exists.
	 * In this case, the data of the existing conference is copied into the {@code fxObj}.
	 * @throws InvalidDataException If a part of the provided data is not valid.
	 */
	public abstract void createConference(FxConference<TRConference> fxObj)
			throws ExistingIDException, InvalidDataException;
	
	/**
	 * Uses the provided data to create a new conference edition
	 * and sets the DBRepresentation and ID fields of the passed FX object.
	 * @param fxObj The FX object representing the conference edition
	 * @param conference The conference
	 * @throws InvalidDataException If a part of the provided data is not valid.
	 * @throws InvalidDBRepException If {@code conference} is not a valid conference representation.
	 */
	public abstract void createConferenceEdition(FxConferenceEdition<TRConferenceEdition> fxObj,
			TRConference conference) throws InvalidDataException, InvalidDBRepException;
	
	/**
	 * Uses the provided data to create a new in-proceedings publication
	 * and sets the DBRepresentation and ID fields of the passed FX object.
	 * @param fxObj The FX object representing the publication
	 * @param proceedings The proceedings
	 * @throws InvalidDataException If a part of the provided data is not valid.
	 * @throws InvalidDBRepException If {@code proceedings} is not a valid proceedings representation.
	 */
	public abstract void createInProceedings(FxInProceedings<TRInProceedings> fxObj,
			TRProceedings proceedings) throws InvalidDataException, InvalidDBRepException;
	
	/**
	 * Uses the provided XML data and proceedings to create an in-proceedings publication in the database.
	 * The authors are created as well if they do not exist yet.
	 * @param xmlData The XML data object representing the inProceedings
	 * @throws InvalidDataException If a part of the provided data is not valid.
	 */
	public abstract TRInProceedings createInProceedings(XMLInProceedings xmlData,
			TRProceedings proceedings) throws InvalidDataException;
	
	/**
	 * Uses the provided data to create a new person
	 * and sets the DBRepresentation and ID fields of the passed FX object.
	 * @param fxObj The FX object representing the person
	 * @throws InvalidDataException If a part of the provided data is not valid.
	 */
	public abstract void createPerson(FxPerson<TRPerson> fxObj) throws InvalidDataException;
	
	/**
	 * Uses the provided data to create the proceedings of the provided conference edition
	 * and sets the DBRepresentation and ID fields of the passed FX object.
	 * @param fxObj The FX object representing the proceedings
	 * @param confEdition The conference edition
	 * @throws InvalidDataException If a part of the provided data is not valid.
	 * @throws InvalidDBRepException If {@code confEdition} is not a valid conference edition representation
	 */
	public abstract void createProceedings(FxPerson<TRPerson> fxObj,
			TRConferenceEdition confEdition) throws InvalidDataException, InvalidDBRepException;
	
	/**
	 * Uses the provided XML data to create the proceedings of a conference in the database.
	 * The conference, the editors, the publisher and the series are created
	 * as well if they do not exist yet.
	 * @param xmlData The XML data object representing the proceedings
	 * @throws InvalidDataException If a part of the provided data is not valid.
	 */
	public abstract TRProceedings createProceedings(XMLProceedings xmlData) throws InvalidDataException;
	
	/**
	 * Uses the provided data to create a new publisher
	 * and sets the DBRepresentation and ID fields of the passed FX object.
	 * @param fxObj The FX object representing the publisher
	 * @throws InvalidDataException If a part of the provided data is not valid.
	 */
	public abstract void createPublisher(FxPublisher<TRPublisher> fxObj) throws InvalidDataException;
	
	/**
	 * Uses the provided data to create a new publication series
	 * and sets the DBRepresentation and ID fields of the passed FX object.
	 * @param fxObj The FX object representing the series
	 * @throws InvalidDataException If a part of the provided data is not valid.
	 */
	public abstract void createSeries(FxSeries<TRSeries> fxObj) throws InvalidDataException;

	/**
	 * Deletes the passed conference if it is valid. Also deletes the editions of this conference,
	 * their proceedings and the corresponding inProceedings.
	 * @param conference The deleted conference.
	 */
	public abstract void deleteConference(TRConference conference);
	
	/**
	 * Deletes the passed conference edition if it is valid. Also deletes their proceedings
	 * and the corresponding inProceedings.
	 * @param conferenceEdition The deleted conference edition.
	 */
	public abstract void deleteConferenceEdition(TRConferenceEdition conferenceEdition);
	
	/**
	 * Deletes the passed in-proceedings publication if it is valid.
	 * @param conferenceEdition The deleted publication.
	 */
	public abstract void deleteInProceedings(TRInProceedings inProceedings);
	
	/**
	 * Deletes the passed person if it is valid.
	 * @param conferenceEdition The deleted person.
	 */
	public abstract void deletePerson(TRPerson person);
	
	/**
	 * Deletes the passed proceedings if it is valid. Also deletes
	 * the corresponding inProceedings.
	 * @param conferenceEdition The deleted proceedings.
	 */
	public abstract void deleteProceedings(TRProceedings proceedings);
	
	/**
	 * Deletes the passed publisher if it is valid.
	 * @param conferenceEdition The deleted publisher.
	 */
	public abstract void deletePublisher(TRPublisher publisher, boolean deletePublications);
	
	/**
	 * Deletes the passed series if it is valid.
	 * @param conferenceEdition The deleted series.
	 */
	public abstract void deleteSeries(TRSeries series, boolean deletePublications);
	
	public abstract TRConference getConferenceByID(String id);
	public abstract TRConferenceEdition getConferenceEditionByID(String id);
	public abstract TRInProceedings getInProceedingsByID(String id);
	public abstract TRPerson getPersonByID(String id);
	public abstract TRProceedings getProceedingsByID(String id);
	public abstract TRPublication getPublicationByID(String id);
	public abstract TRPublisher getPublisherByID(String id);
	public abstract TRSeries getSeriesByID(String id);
	
	public abstract void getAuthors(List<FxPerson<TRPerson>> out);
	
	public abstract void getConferences(FxConference.QueryOptions options,
			List<FxConference<TRConference>> out);
	
	public abstract void getConferenceEditions(FxConferenceEdition.QueryOptions options,
			List<FxConferenceEdition<TRConferenceEdition>> out);
	
	public abstract void getConferenceEditions(TRConference conference,
			FxConferenceEdition.QueryOptions options,
			List<FxConferenceEdition<TRConferenceEdition>> out);
	
	public abstract void getInProceedings(FxInProceedings.QueryOptions options,
			List<FxInProceedings<TRInProceedings>> out);
	
	public abstract void getInProceedingsOfAuthor(TRPerson author,
			FxInProceedings.QueryOptions options,
			List<FxInProceedings<TRInProceedings>> out);
	
	public abstract void getInProceedingsOfProceedings(TRProceedings proceedings,
			FxInProceedings.QueryOptions options,
			List<FxInProceedings<TRInProceedings>> out);
	
	public abstract void getProceedings(FxProceedings.QueryOptions options,
			List<FxProceedings<TRProceedings>> out);
	
	public abstract void getProceedingsOfEditor(TRPerson author,
			FxProceedings.QueryOptions options,
			List<FxProceedings<TRProceedings>> out);
	
	public abstract void getPublishers(FxPublisher.QueryOptions options,
			List<FxInProceedings<TRPublisher>> out);
	
	public abstract void getPersons(FxPerson.QueryOptions options,
			List<FxPerson<TRPerson>> out);
	
	public abstract void getSeries(FxSeries.QueryOptions options,
			List<FxSeries<TRSeries>> out);
	
	public abstract boolean isValidConferenceID(String id);
	public abstract boolean isValidConferenceName(String name);
	public abstract boolean isValidPersonName(String name);
	public abstract boolean isValidPublisherName(String name);
	public abstract boolean isValidSeriesName(String name);
	
	public abstract void setConferenceTitle(TRConference conference, String title);
	
	public abstract void setConferenceEditionYear(TRConferenceEdition edition, int year);
	
	public abstract void setPersonName(TRPerson person, String name)
			throws InvalidDataException, InvalidDBRepException;
	
	public abstract void updateConferenceData(List<FxConference<TRConference>> data);
	public abstract void updateConferenceEditionData(List<FxConferenceEdition<TRConferenceEdition>> data);
	public abstract void updateInProceedingsData(List<FxInProceedings<TRInProceedings>> data);
	public abstract void updatePersonData(List<FxPerson<TRPerson>> data);
	public abstract void updateProceedingsData(List<FxProceedings<TRProceedings>> data);
	public abstract void updatePublicationData(List<FxPublication<TRPublication>> data);
	public abstract void updatePublisherData(List<FxPublisher<TRPublisher>> data);
	public abstract void updateSeriesData(List<FxSeries<TRSeries>> data);
}
