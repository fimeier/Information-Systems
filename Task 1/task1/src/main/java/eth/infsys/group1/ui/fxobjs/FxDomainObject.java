package eth.infsys.group1.ui.fxobjs;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class FxDomainObject<TR> {

	private TR dbRep;
	private StringProperty id;
	
	FxDomainObject(TR dbRep, String id) {
		this.dbRep = dbRep;
		this.id = new SimpleStringProperty(id);
	}
	
	public TR getDBRepresentation() {
		return this.dbRep;
	}
	
	public void setDBRepresentation(TR dbRep) {
		this.dbRep = dbRep;
	}
	
    public String getId() {
    	return this.id.get();
    }

    public StringProperty getIdProperty() {
    	return this.id;
    }
    
    public void setId(String id) {
    	this.id.set(id);
    }
    
}
