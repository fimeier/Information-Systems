package eth.infsys.group1.ui.fxobjs;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FxInProceedings<TRInProceedings> extends FxPublication<TRInProceedings> {

	public static class QueryOptions extends FxPublication.QueryOptionBase<SortOption> {
		
		public QueryOptions(int startIndex, int endIndex,
				SortOption sorting, String searchTerm, int year) {
			super(startIndex, endIndex, sorting, searchTerm, year);
		}
		
	}
	
	public static enum SortOption {

		NONE("None"),
		BY_TITLE_ASC("Title Asc"), BY_TITLE_DESC("Title Desc"),
		BY_YEAR_ASC("Year Asc"), BY_YEAR_DESC("Year Desc"),
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
	
	private ObservableList<String> authors;
	private StringProperty note;
	private StringProperty pages;
	private StringProperty proceedingsTitle;

	public FxInProceedings(TRInProceedings dbRep, String id,
			String title, int year, String electronicEdition,
			List<String> authors, String proceedingsTitle,
			String pages, String note) {
		super(dbRep, id, title, year, electronicEdition);
		this.authors = FXCollections.observableArrayList(authors);
		this.proceedingsTitle = new SimpleStringProperty(proceedingsTitle);
		this.pages = new SimpleStringProperty(pages);
		this.note = new SimpleStringProperty(note);
	}

	public ObservableList<String> getAuthors() {
		return this.authors;
	}
	
	public void setAuthors(List<String> values) {
		this.authors.clear();
		this.authors.addAll(values);
	}
	
    public String getNote() {
		return this.note.get();
	}

    public void setNote(String note) {
    	this.note.set(note);
	}

    public StringProperty noteProperty() {
		return this.note;
	}

    public String getPages() {
		return this.pages.get();
	}

    public void setPages(String pages) {
    	this.pages.set(pages);
	}
    
    public StringProperty pagesProperty() {
    	return this.pages;
    }

    public String getProceedingsTitle() {
    	return this.proceedingsTitle.get();
	}

    public void setProceedingsTitle(String value) {
    	this.proceedingsTitle.set(value);
	}

    public StringProperty proceedingsTitleProperty() {
    	return this.proceedingsTitle;
	}
    
}