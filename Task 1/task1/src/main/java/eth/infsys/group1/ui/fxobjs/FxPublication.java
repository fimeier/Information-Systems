package eth.infsys.group1.ui.fxobjs;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FxPublication<TR> extends FxDomainObject<TR> {

	public static enum SortOption {
		//BY_TITLE("Sort by Title"), BY_YEAR("Sort by Year"),
		BY_TITLE_YEAR("Sort by Title and Year"),
		BY_YEAR_TITLE("Sort by Year and Title"),
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

    public StringProperty getTitleProperty() {
    	return this.title;
    }
    
    public void setTitle(String title) {
    	this.title.set(title);
    }
    
    public int getYear() {
    	return this.year.get();
	}
    
    public IntegerProperty getYearProperty() {
    	return this.year;
	}

    public void setYear(int year) {
    	this.year.set(year);
	}

    public String getElectronicEdition() {
		return this.electronicEdition.get();
	}
    
    public StringProperty getElectronicEditionProperty() {
    	return this.electronicEdition;
    }

    public void setElectronicEdition(String electronicEdition) {
    	this.electronicEdition.set(electronicEdition);
	}
    
}