package eth.infsys.group1.dbspec;

public enum WebFunc {
	publication_by_id(2), 	//func=inproceeding_by_id#key=<key-value>
	inproceeding_by_id(2), 	//func=inproceeding_by_id#key=<key-value>
	proceeding_by_id(2), 	//func=inproceeding_by_id#key=<key-value>
	//
	pupl_by_title_offset(5), //func#id#sort#begin-offset#end-offset
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
