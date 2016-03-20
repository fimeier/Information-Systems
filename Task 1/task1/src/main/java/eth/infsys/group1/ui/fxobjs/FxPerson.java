package eth.infsys.group1.ui.fxobjs;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FxPerson<TRPerson> extends FxDomainObject<TRPerson> {
	
	public static class QueryOptions extends FxDomainObject.QueryOptions<SortOption> {
		
		public QueryOptions(int startIndex, int endIndex,
				SortOption sorting, String searchTerm) {
			super(startIndex, endIndex, sorting, searchTerm);
		}
		
	}
	
	public static enum SortOption {

		NONE("None"),
		BY_ID_ASC("ID Asc"), BY_ID_DESC("ID Desc"),
		BY_NAME_ASC("Name Asc"), BY_NAME_DESC("Name Desc"),
		BY_AUTHORED_PUBLS_ASC("Authored Publications Asc"),
		BY_AUTHORED_PUBLS_DESC("Authored Publications Desc"),
		BY_EDITED_PUBLS_ASC("Edited Publications Asc"),
		BY_EDITED_PUBLS_DESC("Edited Publications Desc");
		
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
	private IntegerProperty authoredPublicationCount;
	private IntegerProperty editedPublicationCount;
	private NumberBinding totalPublicationCount;

	public FxPerson(TRPerson dbRep, String id, String name) {
		super(dbRep, id);
		this.name = new SimpleStringProperty(name);
		this.totalPublicationCount = Bindings.add(this.authoredPublicationCount, this.editedPublicationCount);
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

    public int getAuthoredPublicationCount() {
    	return this.authoredPublicationCount.get();
    }

    public void setAuthoredPublicationCount(int value) {
    	this.authoredPublicationCount.set(value);
    }

    public IntegerProperty authoredPublicationCountProperty() {
    	return this.authoredPublicationCount;
    }

    public int getEditedPublicationCount() {
    	return this.editedPublicationCount.get();
    }

    public void setEditedPublicationCount(int value) {
    	this.editedPublicationCount.set(value);
    }

    public IntegerProperty editedPublicationCountProperty() {
    	return this.editedPublicationCount;
    }

    public int getTotalPublicationCount() {
    	return this.totalPublicationCount.intValue();
    }

    public NumberBinding totalPublicationCountBinding() {
    	return this.totalPublicationCount;
    }

}
