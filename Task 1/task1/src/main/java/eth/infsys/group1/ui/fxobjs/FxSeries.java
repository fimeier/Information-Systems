package eth.infsys.group1.ui.fxobjs;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FxSeries<TRSeries> extends FxDomainObject<TRSeries> {

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