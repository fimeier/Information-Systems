package eth.infsys.group1.ui.fxobjs;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class FxPerson<TRPerson> extends FxDomainObject<TRPerson> {
	
    private StringProperty name;
	private IntegerProperty authoredPublicationCount;
	private IntegerProperty editedPublicationCount;
	private NumberBinding totalPublicationCount;

	public FxPerson(TRPerson dbRep, String id) {
		super(dbRep, id);
		this.totalPublicationCount = Bindings.add(this.authoredPublicationCount, this.editedPublicationCount);
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

    public int getAuthoredPublicationCount() {
    	return this.authoredPublicationCount.get();
    }

    public IntegerProperty getAuthoredPublicationCountProperty() {
    	return this.authoredPublicationCount;
    }

    public void setAuthoredPublicationCount(int value) {
    	this.authoredPublicationCount.set(value);
    }

    public int getEditedPublicationCount() {
    	return this.editedPublicationCount.get();
    }

    public IntegerProperty getEditedPublicationCountProperty() {
    	return this.editedPublicationCount;
    }

    public void setEditedPublicationCount(int value) {
    	this.editedPublicationCount.set(value);
    }

    public int getTotalPublicationCount() {
    	return this.totalPublicationCount.intValue();
    }

    public NumberBinding getTotalPublicationCountBinding() {
    	return this.totalPublicationCount;
    }

}
