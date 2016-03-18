package eth.infsys.group1.ui;

import java.util.List;
import java.util.Set;

import eth.infsys.group1.task1.dbobjs.Person;
import eth.infsys.group1.task1.dbobjs.Proceedings;

public abstract class DBProvider<TRConference,
								TRConferenceEdition,
								TRInProceedings,
								TRPerson,
								TRProceedings,
								TRPublication,
								TRPublisher,
								TRSeries> {

	public static final int SORT_BY_NAME = 1;
	public static final int SORT_BY_TITLE = 5;
	public static final int SORT_BY_YEAR = 7;
	
	public static final int OPEN_DB_APPEND = 20;
	public static final int OPEN_DB_OVERRIDE = 21;

	
	public DBProvider() {
	}
	
	public abstract TRProceedings createProceeding(String id, String title, Set<String> editors, int year, String electronicEdition, String note, int number, String publisher, String volume, String isbn, String series, int conferenceEdition, String conferenceName);
	public abstract TRInProceedings createInProceedings(String id, String title, int year, String electronicEdition, List<String> authors, String note, String pages, List<Proceedings> proceedings);
	
	
	public abstract String getTitle(TRInProceedings inProceedings);
	public abstract List<TRConference> getConferences(int startIndex, int endIndex, int sort);
	public abstract List<TRConferenceEdition> getConferenceEditions(int startIndex, int endIndex, int sort, TRConference conference);
	public abstract List<TRInProceedings> getInProceedings(int startIndex, int endIndex, int sort);
	public abstract List<TRInProceedings> getInProceedingsOfAuthor(int startIndex, int endIndex, int sort, TRPerson author);
	public abstract List<TRInProceedings> getInProceedingsOfProceedings(int startIndex, int endIndex, int sort, TRProceedings proceedings);
	public abstract List<TRProceedings> getProceedings(int startIndex, int endIndex, int sort);
	public abstract List<TRProceedings> getProceedingsOfAuthor(int startIndex, int endIndex, int sort, Person author);
	public abstract List<TRPublisher> getPublishers(int startIndex, int endIndex, int sort);
	public abstract List<TRPerson> getPersons(int startIndex, int endIndex, int sort);
	public abstract List<TRPerson> getPersons(int startIndex, int endIndex, int sort, String nameFilter);
	public abstract List<TRSeries> getSeries(int startIndex, int endIndex, int sort);

	

	
	
}
