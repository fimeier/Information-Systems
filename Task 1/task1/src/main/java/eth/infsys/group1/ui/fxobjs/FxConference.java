package eth.infsys.group1.ui.fxobjs;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public final class FxConference<TRConference> extends FxDomainObject<TRConference> {
	
	private StringProperty name;
	private IntegerProperty editionCount;
	
	public FxConference(TRConference dbRep, String id, String name, int editionCount) {
		super (dbRep, id);
		this.name = new SimpleStringProperty(name);		
		this.editionCount = new SimpleIntegerProperty(editionCount);
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
    
    public int getEditionCount() {
    	return this.editionCount.get();
    }
	
	public IntegerProperty getEditionCountProperty() {
		return this.editionCount;
	}
    
    public void setEditionCount(int count) {
    	this.editionCount.set(count);
    }
    
}