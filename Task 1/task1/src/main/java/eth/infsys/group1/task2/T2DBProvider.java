package eth.infsys.group1.task2;

import java.util.HashMap;
import java.util.List;

import eth.infsys.group1.dbspec.DBProvider;
import eth.infsys.group1.dbspec.DivIO;
import eth.infsys.group1.dbspec.InProceedings_simple_input;
import eth.infsys.group1.dbspec.Proceedings_simple_input;
import eth.infsys.group1.dbspec.PublicationIO;

public class T2DBProvider extends DBProvider {
	
	/**
	 * Class and DB-Stuff
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	/**
	 * Create DBProvider
	 * 
	 * @param dbName the name of the database to be created
	 * @param mode OPEN_DB_APPEND or OPEN_DB_OVERRIDE
	 */
	public T2DBProvider(String dbName, int mode) {
		createDB(dbName, mode);		
	}
	
	
	
	@Override
	protected void createDB(String dbName, int mode) {
		// By default, all database files will be created in TBD

		switch (mode) {
		case OPEN_DB_APPEND:
			

			break;

			//OPEN_DB_OVERRIDE
		default:
			

		}
	}
	
	
	

	
	/**
	 * Batch-Loader-Methods
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	@Override
	public String batch_createProceedings(Proceedings_simple_input data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String batch_createInProceedings(InProceedings_simple_input data) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * IO-Methods
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	@Override
	public String IO_get_statistics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String IO_authors_editors_for_a_conference(String conf_id, String mode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String IO_avg_authors_per_inproceedings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String IO_count_publications_per_interval(int y1, int y2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String IO_delete_get_person_by_id(String pers_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String IO_find_author_distance_path(String name1, String name2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DivIO> IO_find_co_authors(String pers_name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DivIO IO_get_conf_by_id(String conf_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DivIO IO_get_confEd_by_id(String confEd_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DivIO> IO_get_conference_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DivIO> IO_get_person_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DivIO IO_get_person_by_id(String pers_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PublicationIO> IO_get_publ_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PublicationIO IO_get_publication_by_id(String publ_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DivIO> IO_get_publisher_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DivIO IO_get_publisher_by_id(String publ_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DivIO> IO_get_series_by_filter_offset(String filter, int boff, int eoff, String order_by) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DivIO IO_get_series_by_id(String series_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String IO_inproceedings_for_a_conference(String conf_id, String mode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String IO_person_is_author_and_editor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PublicationIO> IO_person_is_last_author(String pers_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PublicationIO> IO_publ_by_person_name_or_id(HashMap<String, String> args) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<DivIO> IO_publishers_whose_authors_in_interval(int y1, int y2) {
		// TODO Auto-generated method stub
		return null;
	}



}
