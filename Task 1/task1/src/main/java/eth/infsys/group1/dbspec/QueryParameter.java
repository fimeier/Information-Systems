package eth.infsys.group1.dbspec;

public enum QueryParameter {
	SORT_BY_NAME("order by name"),
	SORT_BY_YEAR("order by year"),
	SORT_BY_TITLE("order by title"),
	SORT_BY_DEFAULT(""),
	
	FILTER_BY_NAME("name"),
	FILTER_BY_YEAR("year"),

	FILTER_BY_TITLE("title"),
	
	
/**
 * where clause operators
 */
	EQ("=="),
	NE("!="),
	G(">"),
	L("<"),
	GE(">="),
	LE("<="),


	;
	
	public final String db_query;
	
	QueryParameter(String db_query) {
		this.db_query = db_query;
	}
	
	private static final QueryParameter[] values = QueryParameter.values();
	
	static public QueryParameter fromInt(int type) {
		return values[type];
	}

	static public QueryParameter fromString(String type) {
		return QueryParameter.valueOf(type);
	}
	

}
