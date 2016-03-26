package eth.infsys.group1.dbspec;

public enum WebFunc {
	publisher_by_id(2),						//func=publisher_by_id#id=<id-value>
	publisher_by_filter_offset(4),			//func=publisher_by_filter_offset#name_contains=<value>#begin-offset=<value>#end-offset=<value>
	publisher_by_filter_offset_order(6),	//func=publisher_by_filter_offset_order#name_contains=<value>#begin-offset=<value>#end-offset=<value>#order_by=<value>#ob_direction
	series_by_id(2),						//func=series_by_id#id=<id-value>
	series_by_filter_offset(4),				//func=series_by_filter_offset#name_contains=<value>#begin-offset=<value>#end-offset=<value>
	series_by_filter_offset_order(6),		//func=series_by_filter_offset_order#name_contains=<value>#begin-offset=<value>#end-offset=<value>#order_by=<value>#ob_direction
	
	person_by_id(2),						//func=person_by_id#id=<id-value>
	person_by_filter_offset(4),				//func=person_by_filter_offset#name_contains=<value>#begin-offset=<value>#end-offset=<value>
	person_by_filter_offset_order(6),		//func=person_by_filter_offset_order#name_contains=<value>#begin-offset=<value>#end-offset=<value>#order_by=<value>#ob_direction
	delete_person_by_id(2),					//func=delete_person_by_id#id=<id-value>
	
	conf_by_filter_offset(4),				//func=conf_by_filter_offset#name_contains=<value>#begin-offset=<value>#end-offset=<value>
	conf_by_filter_offset_order(6),			//func=conf_by_filter_offset_order#name_contains=<value>#begin-offset=<value>#end-offset=<value>#order_by=<value>#ob_direction
	publication_by_id(2), 					//func=publication_by_id#key=<key-value>
	publ_by_person_name_or_id(3),			//func=publication_by_person#name(or id)=value#publ=<all,authored,edited>	
	conf_by_id(2),							//func=confEd_by_id#id=<id-value>
	confEd_by_id(2),						//func=confEd_by_id#id=<id-value>
	pupl_by_title_offset(4), 				//func=publication_by_filter#title=<title-value>#begin-offset=<value>#end-offset=<value>
	pupl_by_title_offset_order(6),			//func=publication_by_filter#title=<title-value>#begin-offset=<value>#end-offset=<value>#order_by=<value>#ob_direction
	inproceeding_by_id(2), 					//func=inproceeding_by_id#key=<key-value>
	//inproceeding_by_filter(4), 			//func=inproceeding_by_filter#title=<title-value>#begin-offset=<value>#begin-offset=<value>
	proceeding_by_id(2), 					//func=proceeding_by_id#key=<key-value>
	//proceeding_by_filter(4), 				//func=proceeding_by_filter#title=<title-value>#begin-offset=<value>#begin-offset=<value>
	find_co_authors(2),						//func=find_co_authors#name=value
	find_author_distance_path(3),			//func=find_author_distance_path#name1=value1#name2=value2
	avg_authors_per_inproceedings(1),		//func=avg_authors_per_inproceedings
	count_publications_per_interval(3),		//func=count_publications_per_interval&year1=<value>&year2=<value>
	count_inproceedings_for_a_conference(2),//func=count_inproceedings_for_a_conference&id=<value>
	count_authors_editors_for_a_conference(2), //func=count_authors_editors_for_a_conference&id=<value>
	//
	PAGE(2),	//func=PAGE&name=<value>
	MAIN(1), //func
	ERROR(1),
	;

	public final int arg_count;

	WebFunc(int arg_count) {
		this.arg_count = arg_count;
	}

	private static final WebFunc[] values = WebFunc.values();

	static public WebFunc fromInt(int type) {
		return values[type];
	}


	public static WebFunc fromString(String type) {
		try {
			return WebFunc.valueOf(type);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}
