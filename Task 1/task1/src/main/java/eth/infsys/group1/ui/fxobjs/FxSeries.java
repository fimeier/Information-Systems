package eth.infsys.group1.ui.fxobjs;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FxSeries<TRSeries> extends FxDomainObject<TRSeries> {

	public static class QueryOptions extends FxDomainObject.QueryOptions<SortOption> {
		
		public QueryOptions(int startIndex, int endIndex,
				SortOption sorting, String searchTerm) {
			super(startIndex, endIndex, sorting, searchTerm);
		}
		
	}
	
	public static enum SortOption {

		NONE("None"),
		BY_NAME_ASC("Name Asc"), BY_NAME_DESC("Title Desc"),
		BY_PUBCOUNT_ASC("Number of Publ. Asc"), BY_YEAR_DESC("Number of Publ. Desc"),
		BY_ID_ASC("ID Asc"), BY_ID_DESC("ID Desc");
		
		public final String description;
		
		SortOption(String description) {
			this.description = description;
		}
		
		@Override
		public String toString() {
			return this.description;
		}
	
	}
	
	private StringProperty name;
    private IntegerProperty publicationCount;
	
	public FxSeries(TRSeries dbRep, String id, String name) {
		super(dbRep, id);
		this.name = new SimpleStringProperty(name);
	}

	public String getName() {
    	return this.name.get();
    }

    public void setName(String name) {
    	this.name.set(name);
    }

	public StringProperty nameProperty() {
    	return this.name;
    }

    public int getPublicationCount() {
    	return this.publicationCount.get();
    }

    public void setPublications(int value) {
    	this.publicationCount.set(value);
    }

    public IntegerProperty publicationCountProperty() {
    	return this.publicationCount;
    }
    
}