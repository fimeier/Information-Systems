package eth.infsys.group1.ui.fxobjs;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public final class FxConference<TRConference> extends FxDomainObject<TRConference> {
	
	public static class QueryOptions extends FxDomainObject.QueryOptions<SortOption> {
		
		public QueryOptions(int startIndex, int endIndex,
				SortOption sorting, String searchTerm) {
			super(startIndex, endIndex, sorting, searchTerm);
		}
		
	}
	
	public static enum SortOption {
		
		NONE("None"),
		BY_NAME_ASC("Name Asc"), BY_NAME_DESC("Name Desc"),
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
	private IntegerProperty editionCount;
	
	public FxConference(TRConference dbRep, String id, String name, int editionCount) {
		super (dbRep, id);
		this.name = new SimpleStringProperty(name);		
		this.editionCount = new SimpleIntegerProperty(editionCount);
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
    
    public int getEditionCount() {
    	return this.editionCount.get();
    }
    
    public void setEditionCount(int count) {
    	this.editionCount.set(count);
    }
	
	public IntegerProperty editionCountProperty() {
		return this.editionCount;
	}
    
}