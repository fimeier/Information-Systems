package eth.infsys.group1.ui.fxobjs;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FxSeries<TRSeries> extends FxDomainObject<TRSeries> {

	public static enum SortOption {
		BY_NAME("Sort by Name"),
		BY_PUBCOUNT("Sort by Number of Publications"),
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
	
	private StringProperty name;
    private IntegerProperty publicationCount;
	
	public FxSeries(TRSeries dbRep, String id, String name) {
		super(dbRep, id);
		this.name = new SimpleStringProperty(name);
	}

	public String getName() {
    	return this.name.get();
    }

	public StringProperty getNameProperty() {
    	return this.name;
    }

    public void setName(String name) {
    	this.name.set(name);
    }

    public int getPublicationCount() {
    	return this.publicationCount.get();
    }

    public IntegerProperty getPublicationCountProperty() {
    	return this.publicationCount;
    }

    public void setPublications(int value) {
    	this.publicationCount.set(value);
    }
    
}