package eth.infsys.group1.ui.fxobjs;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FxInProceedings<TRInProceedings> extends FxPublication<TRInProceedings> {

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

	public List<String> getAuthors() {
		return this.authors;
	}
	
	public void setAuthors(List<String> values) {
		this.authors.clear();
		this.authors.addAll(values);
	}
	
    public String getNote() {
		return this.note.get();
	}

    public StringProperty getNoteProperty() {
		return this.note;
	}

    public void setNote(String note) {
    	this.note.set(note);
	}

    public String getPages() {
		return this.pages.get();
	}
    
    public StringProperty getPagesProperty() {
    	return this.pages;
    }

    public void setPages(String pages) {
    	this.pages.set(pages);
	}

    public String getProceedingsTitle() {
    	return this.proceedingsTitle.get();
	}

    public StringProperty getProceedingsTitleProperty() {
    	return this.proceedingsTitle;
	}

    public void setProceedingsTitle(String value) {
    	this.proceedingsTitle.set(value);
	}
    
}