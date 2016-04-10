package eth.infsys.group1.dbspec;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javafx.util.Pair;

@SuppressWarnings("restriction")
public abstract class DBProvider {

/*public abstract class DBProvider<TRConference,
								TRConferenceEdition,
								TRInProceedings,
								TRPerson,
								TRProceedings,
								TRPublication,
								TRPublisher,
								TRSeries> {*/

		

	
	/**
	 * Class and DB-Stuff
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	//public DBProvider() { }
	
	/**
	 * 
	 * @param dbName the name of the database to be created
	 * @param mode OPEN_DB_APPEND or OPEN_DB_OVERRIDE
	 */
	protected abstract void createDB(String dbName, int mode);	
	

	
	/**
	 * some definitions
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	public static final int OPEN_DB_APPEND = 20;
	public static final int OPEN_DB_OVERRIDE = 21;
	
	
	
	
	
	/**
	 * Batch-Loader-Methods
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	public abstract String batch_createProceedings(Proceedings_simple_input data);
	public abstract String batch_createInProceedings(InProceedings_simple_input data);
	
	
	/**
	 * IO-Methods: helper
	 * 
	 * 
	 * 
	 * 
	 */

	protected Comparator<Pair<String, String>> comparePairTitleId = new Comparator<Pair<String,String>>() {
		@Override
		public int compare(Pair<String, String> o1, Pair<String, String> o2) {
			return o1.getKey().compareTo(o2.getKey());
			//					int cmp = o1.getKey().compareTo(o2.getKey());
			//					if (cmp==0) {
			//						return o1.getValue().compareTo(o2.getValue());
			//					}
			//					return 0;
		}
	};

	protected Comparator<Pair<Integer, String>> comparePairYearId = new Comparator<Pair<Integer,String>>() {
		@Override
		public int compare(Pair<Integer, String> o1, Pair<Integer, String> o2) {
			return o1.getKey().compareTo(o2.getKey());
		}
	};

	protected Comparator<DivIO> compareDivIO_Person_name = new Comparator<DivIO>() {
		@Override
		public int compare(DivIO o1, DivIO o2) {
			return o1.Person_name.compareTo(o2.Person_name);
		}
	};
	
	
	protected Comparator<Pair<String, String>> compare_Author_id_name = new Comparator<Pair<String,String>>() {
		@Override
		public int compare(Pair<String, String> o1, Pair<String, String> o2) {
			return o1.getValue().compareTo(o2.getValue());
		}
	};
	
	protected Comparator<Pair<String, String>> compare_Pair_value = new Comparator<Pair<String,String>>() {
		@Override
		public int compare(Pair<String, String> o1, Pair<String, String> o2) {
			return o1.getValue().compareTo(o2.getValue());
		}
	};

	protected Comparator<DivIO> compareDivIO_Publisher_name = new Comparator<DivIO>() {
		@Override
		public int compare(DivIO o1, DivIO o2) {
			if (o1.Publisher_name==null || o2.Publisher_name==null){
				System.out.println("ERROR: compareDivIO_Publisher_name...");
			}
			return o1.Publisher_name.compareTo(o2.Publisher_name);
		}
	};
	
	protected Comparator<String> compare_Name = new Comparator<String>() {
		@Override
		public int compare(String o1, String o2) {
			if (o1==null || o2==null){
				System.out.println("ERROR: compareDivIO_Publisher_name...");
			}
			return o1.compareTo(o2);
		}
	};
	
	
	
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
	
	/**
	 * Statistics about the DB (#-Objects of each type...)
	 * @return some html
	 */
	public abstract String IO_get_statistics();
	

	public abstract String[] IO_authors_editors_for_a_conference(String conf_id, String mode);
	public abstract String IO_avg_authors_per_inproceedings();
	public abstract String IO_count_publications_per_interval(int y1, int y2);
	public abstract String IO_delete_get_person_by_id(String pers_id);
	public abstract String IO_find_author_distance_path(String name1, String name2);
	public abstract List<DivIO> IO_find_co_authors(String pers_name);
	public abstract String IO_find_co_authors_returns_String(String pers_name);
	public abstract DivIO IO_get_conf_by_id(String conf_id);
	public abstract DivIO IO_get_confEd_by_id(String confEd_id);
	public abstract List<DivIO> IO_get_conference_by_filter_offset(String filter, int boff, int eoff, String order_by);
	public abstract List<DivIO> IO_get_person_by_filter_offset(String filter, int boff, int eoff, String order_by);
	public abstract DivIO IO_get_person_by_id(String pers_id);
	public abstract List<PublicationIO> IO_get_publ_by_filter_offset(String filter, int boff, int eoff, String order_by);
	public abstract PublicationIO IO_get_publication_by_id(String publ_id);
	public abstract List<DivIO> IO_get_publisher_by_filter_offset(String filter, int boff, int eoff, String order_by);
	public abstract DivIO IO_get_publisher_by_id(String publ_id);
	public abstract List<DivIO> IO_get_series_by_filter_offset(String filter, int boff, int eoff, String order_by);
	public abstract DivIO IO_get_series_by_id(String series_id);
	public abstract String IO_inproceedings_for_a_conference(String conf_id, String mode);
	public abstract String IO_person_is_author_and_editor();
	
	/**
	 * Person is the last author of a publication (InProceedings)
	 * @param pers_id
	 * @return List<PublicationIO>
	 */
	public abstract List<PublicationIO> IO_person_is_last_author(String pers_id);
	public abstract List<PublicationIO> IO_publ_by_person_name_or_id(HashMap<String, String> args);
	public abstract List<DivIO> IO_publishers_whose_authors_in_interval(int y1, int y2);
	
	
	/**
	 * dummy implementation
	 * @param id
	 * @return
	 */
	//public abstract PublicationIO IO_get_inproceedings_by_id(String id)
	



	


	
	

	
	
}
