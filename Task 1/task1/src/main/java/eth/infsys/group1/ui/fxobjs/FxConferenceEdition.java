package eth.infsys.group1.ui.fxobjs;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FxConferenceEdition<TRConferenceEdition> extends FxDomainObject<TRConferenceEdition> {

	public static class QueryOptions extends FxDomainObject.QueryOptions<SortOption> {
		
		private IntegerProperty year;
		
		public QueryOptions(int startIndex, int endIndex,
				SortOption sorting, String searchTerm, int year) {
			super(startIndex, endIndex, sorting, searchTerm);
			this.year = new SimpleIntegerProperty(year);
		}
		
		public int getYear() {
			return this.year.get();
		}
		
		public void setYear(int value) {
			this.year.set(value);
		}
		
		public IntegerProperty yearProperty() {
			return this.year;
		}
		
	}
	
	public static enum SortOption {
		
		NONE("None"),
		BY_NAME_ASC("Name Asc"), BY_NAME_DESC("Name Desc"),
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
	
	private StringProperty conferenceName;
	private IntegerProperty year;
	private StringProperty proceedingsTitle;

	public FxConferenceEdition(TRConferenceEdition dbRep, String id,
			String conferenceName, int year, String proceedingsTitle) {
		super(dbRep, id);
		this.conferenceName = new SimpleStringProperty(conferenceName);
		this.year = new SimpleIntegerProperty(year);
		this.proceedingsTitle = new SimpleStringProperty(proceedingsTitle);
	}
	
	public String getConferenceName() {
		return this.conferenceName.get();
	}
	
	public void setConferenceName(String conferenceName) {
		this.conferenceName.set(conferenceName);
	}
	
	public StringProperty conferenceNameProperty() {
		return this.conferenceName;
	}
	
	public int getYear() {
		return this.year.get();
	}
	
	public void setYear(int value) {
		this.year.set(value);
	}
	
	public IntegerProperty yearProperty() {
		return this.year;
	}
	
	public String getProceedingsTitle() {
		return this.proceedingsTitle.get();
	}
	
	public void setProceedingsTitle(String value) {
		this.proceedingsTitle.set(value);
	}
	
	public StringProperty proceedingsTitleProperty() {
		return this.proceedingsTitle;
	}
	
}