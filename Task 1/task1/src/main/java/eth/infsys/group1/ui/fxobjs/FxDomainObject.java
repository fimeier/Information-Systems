package eth.infsys.group1.ui.fxobjs;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class FxDomainObject<TR> {
	
	public static abstract class QueryOptions<TSO> {
		
		private IntegerProperty startIndex;
		private IntegerProperty endIndex;
		private ObjectProperty<TSO> sorting;
		private StringProperty searchTerm;
		
		public QueryOptions(int startIndex, int endIndex,
				TSO sorting, String searchTerm) {
			this.startIndex = new SimpleIntegerProperty(startIndex);
			this.endIndex = new SimpleIntegerProperty(endIndex);
			this.sorting = new SimpleObjectProperty<TSO>(sorting);
			this.searchTerm = new SimpleStringProperty(searchTerm);
		}
		
		/**
		 * Adds the passed value to the start index and end index.
		 * @param value An integer value (can be positive or negative)
		 */
		public void addValueToIndexes(int value) {
			this.startIndex.set(this.startIndex.get() + value);
			this.endIndex.set(this.endIndex.get() + value);
		}
		
		/**
		 * Sets the values of the start and end indexes to
		 * max(startMin, startIndex) and max(endMin, endIndex),
		 * respectively.
		 * @param startMin The minimal possible start index
		 * @param endMin The minimal possible end index
		 */
		public void raiseIndexes(int startMin, int endMin) {
			this.startIndex.set(Math.max(startMin, this.startIndex.get()));
			this.endIndex.set(Math.max(endMin, this.endIndex.get()));
		}
		
		public int getStartIndex() {
			return this.startIndex.get();
		}
		
		public void setStartIndex(int value) {
			this.startIndex.set(value);
		}
		
		public IntegerProperty startIndexProperty() {
			return this.startIndex;
		}
		
		public int getEndIndex() {
			return this.endIndex.get();
		}
		
		public void setEndIndex(int value) {
			this.endIndex.set(value);
		}
		
		public IntegerProperty endIndexProperty() {
			return this.endIndex;
		}
		
		public TSO getSortingOption() {
			return this.sorting.get();
		}
		
		public void setSortingOption(TSO value) {
			this.sorting.set(value);
		}
		
		public ObjectProperty<TSO> sortingOptionProperty() {
			return this.sorting;
		}
		
		public String getSearchTerm() {
			return this.searchTerm.get();
		}
		
		public void setSearchTerm(String value) {
			this.searchTerm.set(value);
		}
		
		public StringProperty searchTermProperty() {
			return this.searchTerm;
		}
		
	}

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
    
    public void setId(String id) {
    	this.id.set(id);
    }

    public StringProperty idProperty() {
    	return this.id;
    }
    
}
