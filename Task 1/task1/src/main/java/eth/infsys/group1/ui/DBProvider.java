package eth.infsys.group1.ui;

import java.util.List;
import eth.infsys.group1.ui.fxobjs.*;

public abstract class DBProvider<TRConference,
								TRConferenceEdition,
								TRInProceedings,
								TRPerson,
								TRProceedings,
								TRPublication,
								TRPublisher,
								TRSeries> {

	public static final int SORT_BY_ID = 2;
	public static final int SORT_BY_NAME = 3;
	public static final int SORT_BY_TITLE = 5;
	public static final int SORT_BY_YEAR = 7;

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
	 */
	public abstract void createConferenceEdition(FxConferenceEdition<TRConferenceEdition> fxObj,
			TRConference conference);
	
	/**
	 * Uses the provided data to create a new in-proceedings publication
	 * and sets the DBRepresentation and ID fields of the passed FX object.
	 * @param fxObj The FX object representing the publication
	 * @param proceedings The proceedings
	 */
	public abstract void createInProceedings(FxInProceedings<TRInProceedings> fxObj,
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
	 * @param fxObj The FX object representing the person
	 * @param confEdition The conference edition
	 */
	public abstract void createProceedings(FxPerson<TRPerson> fxObj,
			TRConferenceEdition confEdition);
	
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
	
	public abstract TRConference getConferenceByID(String id);
	public abstract TRConferenceEdition getConferenceEditionByID(String id);
	public abstract TRInProceedings getInProceedingsByID(String id);
	public abstract TRPerson getPersonByID(String id);
	public abstract TRProceedings getProceedingsByID(String id);
	public abstract TRPublication getPublicationByID(String id);
	public abstract TRPublisher getPublisherByID(String id);
	public abstract TRSeries getSeriesByID(String id);
	
	public abstract void getConferences(int startIndex, int endIndex, int sort,
			String searchTerm, List<FxConference<TRConference>> out);
	
	public abstract void getConferenceEditions(int startIndex, int endIndex, int sort,
			String searchTerm, List<FxConferenceEdition<TRConferenceEdition>> out);
	
	public abstract void getConferenceEditions(int startIndex, int endIndex, int sort,
			TRConference conference, String searchTerm,
			List<FxConferenceEdition<TRConferenceEdition>> out);
	
	public abstract void getInProceedings(int startIndex, int endIndex, int sort,
			String searchTerm, List<FxInProceedings<TRInProceedings>> out);
	
	public abstract void getInProceedingsOfAuthor(int startIndex, int endIndex, int sort,
			TRPerson author, String searchTerm, List<FxInProceedings<TRInProceedings>> out);
	
	public abstract void getInProceedingsOfProceedings(int startIndex, int endIndex, int sort,
			TRProceedings proceedings, String searchTerm,
			List<FxInProceedings<TRInProceedings>> out);
	
	public abstract void getProceedings(int startIndex, int endIndex, int sort,
			String searchTerm, List<FxInProceedings<TRProceedings>> out);
	
	public abstract void getProceedingsOfEditor(int startIndex, int endIndex, int sort,
			TRPerson author, String searchTerm,
			List<FxInProceedings<TRProceedings>> out);
	
	public abstract void getPublishers(int startIndex, int endIndex, int sort,
			String searchTerm, List<FxInProceedings<TRPublisher>> out);
	
	public abstract void getPersons(int startIndex, int endIndex, int sort,
			String searchTerm, List<FxInProceedings<TRPerson>> out);
	
	public abstract void getSeries(int startIndex, int endIndex, int sort,
			String searchTerm, List<FxSeries<TRSeries>> out);

}
