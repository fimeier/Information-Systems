package eth.infsys.group1.ui.fxobjs;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FxPublication<TR> extends FxDomainObject<TR> {

	public static class QueryOptionBase<TSO> extends FxDomainObject.QueryOptions<TSO> {
		
		private IntegerProperty year;
		
		public QueryOptionBase(int startIndex, int endIndex,
				TSO sorting, String searchTerm, int year) {
			super(startIndex, endIndex, sorting, searchTerm);
			this.year = new SimpleIntegerProperty(year);
		}
		
		public int getYear() {
			return this.year.get();
		}
		
		public IntegerProperty getYearProperty() {
			return this.year;
		}
		
		public void setYear(int value) {
			this.year.set(value);
		}
		
	}
	
	public static class QueryOptions extends QueryOptionBase<SortOption> {
		
		public QueryOptions(int startIndex, int endIndex,
				SortOption sorting, String searchTerm, int year) {
			super(startIndex, endIndex, sorting, searchTerm, year);
		}
		
	}
	
	public static enum SortOption {

		NONE("None"),
		BY_TITLE_ASC("Title Asc"), BY_TITLE_DESC("Title Desc"),
		BY_YEAR_ASC("Year Asc"), BY_YEAR_DESC("Year Desc"),
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
	
	private StringProperty title;
	private IntegerProperty year;
	private StringProperty electronicEdition;

	public FxPublication(TR dbRep, String id,
			String title, int year, String electronicEdition) {
		super(dbRep, id);
		this.title = new SimpleStringProperty(title);
		this.year = new SimpleIntegerProperty(year);
		this.electronicEdition = new SimpleStringProperty(electronicEdition);
	}
	
    public String getTitle() {
		return this.title.get();
	}
    
    public void setTitle(String title) {
    	this.title.set(title);
    }

    public StringProperty titleProperty() {
    	return this.title;
    }
    
    public int getYear() {
    	return this.year.get();
	}

    public void setYear(int year) {
    	this.year.set(year);
	}
    
    public IntegerProperty yearProperty() {
    	return this.year;
	}

    public String getElectronicEdition() {
		return this.electronicEdition.get();
	}

    public void setElectronicEdition(String electronicEdition) {
    	this.electronicEdition.set(electronicEdition);
	}
    
    public StringProperty electronicEditionProperty() {
    	return this.electronicEdition;
    }
    
}