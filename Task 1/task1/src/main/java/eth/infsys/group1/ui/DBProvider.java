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
	
	public DBProvider() {
	}

	public abstract void createConference(FxConference<TRConference> fxObj);
	public abstract void createConferenceEdition(FxConferenceEdition<TRConferenceEdition> fxObj, TRConference conference);
	public abstract void createInProceedings(FxInProceedings<TRInProceedings> fxObj, TRProceedings proceedings);
	public abstract void createPerson(FxPerson<TRPerson> fxObj);
	public abstract void createProceedings(FxPerson<TRPerson> fxObj);
	public abstract void createPublisher(FxPublisher<TRPublisher> fxObj);
	public abstract void createSeries(FxSeries<TRSeries> fxObj);
	
	public abstract String getTitle(TRInProceedings inProceedings);
	
	public abstract void getConferences(int startIndex, int endIndex, int sort,
			List<FxConference<TRConference>> out);
	
	public abstract void getConferenceEditions(int startIndex, int endIndex, int sort,
			List<FxConferenceEdition<TRConferenceEdition>> out);
	
	public abstract void getConferenceEditions(int startIndex, int endIndex, int sort,
			TRConference conference, List<FxConferenceEdition<TRConferenceEdition>> out);
	
	public abstract List<TRInProceedings> getInProceedings(int startIndex, int endIndex, int sort);
	public abstract List<TRInProceedings> getInProceedingsOfAuthor(int startIndex, int endIndex, int sort, TRPerson author);
	public abstract List<TRInProceedings> getInProceedingsOfProceedings(int startIndex, int endIndex, int sort, TRProceedings proceedings);
	public abstract List<TRProceedings> getProceedings(int startIndex, int endIndex, int sort);
	public abstract List<TRProceedings> getProceedingsOfAuthor(int startIndex, int endIndex, int sort, TRPerson author);
	public abstract List<TRPublisher> getPublishers(int startIndex, int endIndex, int sort);
	public abstract List<TRPerson> getPersons(int startIndex, int endIndex, int sort);
	public abstract List<TRPerson> getPersons(int startIndex, int endIndex, int sort, String nameFilter);
	public abstract List<TRSeries> getSeries(int startIndex, int endIndex, int sort);	
	
}
