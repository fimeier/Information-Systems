package eth.infsys.group1.dbspec;

public enum WebFunc {
	publication_by_id(2), 			//func=publication_by_id#key=<key-value>
	publication_by_person(2),		//func=publication_by_person#name=value	
	pupl_by_title_offset(4), 		//func=publication_by_filter#title=<title-value>#begin-offset=<value>#end-offset=<value>
	pupl_by_title_offset_order(6),	//func=publication_by_filter#title=<title-value>#begin-offset=<value>#end-offset=<value>#order_by=<value>#ob_direction
	inproceeding_by_id(2), 			//func=inproceeding_by_id#key=<key-value>
	//inproceeding_by_filter(4), 	//func=inproceeding_by_filter#title=<title-value>#begin-offset=<value>#begin-offset=<value>
	proceeding_by_id(2), 			//func=proceeding_by_id#key=<key-value>
	//proceeding_by_filter(4), 		//func=proceeding_by_filter#title=<title-value>#begin-offset=<value>#begin-offset=<value>
	find_co_authors_order_by(2),	//func=find_co_authors_order_by#name=value
	
	//
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
