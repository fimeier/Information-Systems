package eth.infsys.group1.task1;

import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;

import eth.infsys.group1.task1.dbobjs.*;
import eth.infsys.group1.ui.DBProvider;

public class T1DBProvider extends
		DBProvider<T1Conference, T1ConferenceEdition, T1InProceedings, T1Person, T1Proceedings, T1Publication, T1Publisher, T1Series> {

	private PersistenceManager persistenceManager;
	
	public T1DBProvider(PersistenceManager persistenceManager) {
		
	}

	@Override
	public T1InProceedings createInProceedings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle(T1InProceedings inProceedings) {
		return inProceedings.getTitle();
	}

	@Override
	public List<T1Conference> getConferences(int startIndex, int endIndex, int sort) {
		switch (sort) {
		case SORT_BY_NAME:
		default:
			
		}
	}

	@Override
	public List<T1ConferenceEdition> getConferenceEditions(int startIndex, int endIndex, int sort,
			T1Conference conference) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T1InProceedings> getInProceedings(int startIndex, int endIndex, int sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T1InProceedings> getInProceedings(int startIndex, int endIndex, int sort, T1Person authorFilter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T1Proceedings> getProceedings(int startIndex, int endIndex, int sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T1Proceedings> getProceedings(int startIndex, int endIndex, int sort, T1Person authorFilter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T1Publisher> getPublishers(int startIndex, int endIndex, int sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T1Person> getPersons(int startIndex, int endIndex, int sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T1Person> getPersons(int startIndex, int endIndex, int sort, String nameFilter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T1Series> getSeries(int startIndex, int endIndex, int sort) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Set<T1Publication> getAuthoredPublications(T1Person person) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Set<T1Publication> getEditedPublications(T1Person person) {
		// TODO Auto-generated method stub
		return null;
	}

}
