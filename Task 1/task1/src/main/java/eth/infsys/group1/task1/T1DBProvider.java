package eth.infsys.group1.task1;

import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;

import eth.infsys.group1.task1.dbobjs.*;
import eth.infsys.group1.ui.DBProvider;

public class T1DBProvider extends
		DBProvider<Conference, ConferenceEdition, InProceedings, Person, Proceedings, Publication, Publisher, Series> {

	private PersistenceManager persistenceManager;
	
	public T1DBProvider(PersistenceManager persistenceManager) {
		
	}

	@Override
	public InProceedings createInProceedings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle(InProceedings inProceedings) {
		return inProceedings.getTitle();
	}

	@Override
	public List<Conference> getConferences(int startIndex, int endIndex, int sort) {
		switch (sort) {
		case SORT_BY_NAME:
		default:
			
		}
	}

	@Override
	public List<ConferenceEdition> getConferenceEditions(int startIndex, int endIndex, int sort,
			Conference conference) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InProceedings> getInProceedings(int startIndex, int endIndex, int sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InProceedings> getInProceedings(int startIndex, int endIndex, int sort, Person authorFilter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Proceedings> getProceedings(int startIndex, int endIndex, int sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Proceedings> getProceedings(int startIndex, int endIndex, int sort, Person authorFilter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Publisher> getPublishers(int startIndex, int endIndex, int sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Person> getPersons(int startIndex, int endIndex, int sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Person> getPersons(int startIndex, int endIndex, int sort, String nameFilter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Series> getSeries(int startIndex, int endIndex, int sort) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Set<Publication> getAuthoredPublications(Person person) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Set<Publication> getEditedPublications(Person person) {
		// TODO Auto-generated method stub
		return null;
	}

}
