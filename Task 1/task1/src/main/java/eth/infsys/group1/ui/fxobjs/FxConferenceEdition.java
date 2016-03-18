package eth.infsys.group1.ui.fxobjs;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FxConferenceEdition<TRConferenceEdition> extends FxDomainObject<TRConferenceEdition> {

	public static enum SortOption {
		//BY_NAME("Sort by Name"), BY_YEAR("Sort by Year"),
		BY_NAME_YEAR("Sort by Name and Year"),
		BY_YEAR_NAME("Sort by Year and Name"),
		BY_ID("Sort by ID");
		
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
	
	public StringProperty getConferenceNameProperty() {
		return this.conferenceName;
	}
	
	public void setConferenceName(String conferenceName) {
		this.conferenceName.set(conferenceName);
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
	
	public String getProceedingsTitle() {
		return this.proceedingsTitle.get();
	}
	
	public StringProperty getProceedingsTitleProperty() {
		return this.proceedingsTitle;
	}
	
	public void setProceedingsTitle(String value) {
		this.proceedingsTitle.set(value);
	}
}