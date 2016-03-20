package eth.infsys.group1.ui.fxobjs;

import java.util.Set;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

public class FxProceedings<TRProceedings> extends FxPublication<TRProceedings> {

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
	
	private ObservableSet<String> editors;
	private StringProperty note;
	private IntegerProperty number;
	private StringProperty publisher;
	private StringProperty volume;
	private StringProperty isbn;
	private StringProperty seriesTitle;
	private StringProperty conferenceName;
	private IntegerProperty conferenceYear;
	private StringBinding conferenceTitleWithYear;
	private IntegerProperty inProceedingsCount;
	
	public FxProceedings(TRProceedings dbRep, String id, String title,
			int year, String electronicEdition, Set<String> editors,
			String note, int number, String publisher, String volume, String isbn,
			String seriesTitle, String conferenceName, int conferenceYear,
			int inProceedingsCount) {
		super(dbRep, id, title, year, electronicEdition);
		this.editors = FXCollections.observableSet(editors);
		this.note = new SimpleStringProperty(note);
		this.number = new SimpleIntegerProperty(number);
		this.publisher = new SimpleStringProperty(publisher);
		this.volume = new SimpleStringProperty(volume);
		this.isbn = new SimpleStringProperty(isbn);
		this.seriesTitle = new SimpleStringProperty(seriesTitle);
		this.conferenceName = new SimpleStringProperty(conferenceName);
		this.conferenceYear = new SimpleIntegerProperty(year);
		this.conferenceTitleWithYear = Bindings.createStringBinding(
				() -> this.conferenceName.get() + this.conferenceYear.get(),
				this.conferenceName, this.conferenceYear);
		this.inProceedingsCount = new SimpleIntegerProperty(inProceedingsCount);
	}

	public ObservableSet<String> getEditors() {
		return this.editors;
	}
	
	public void setEditors(Set<String> value) {
		this.editors.clear();
		this.editors.addAll(value);
	}
	
    public String getNote() {
    	return this.note.get();
	}

    public void setNote(String value) {
    	this.note.set(value);
	}
	
    public StringProperty noteProperty() {
    	return this.note;
	}

    public int getNumber() {
    	return this.number.get();
    }

    public void setNumber(int value) {
    	this.number.set(value);
    }
    
    public IntegerProperty numberProperty() {
    	return this.number;
    }

    public String getPublisher() {
    	return this.publisher.get();
    }

    public void setPublisher(String value) {
    	this.publisher.set(value);
    }

    public StringProperty publisherProperty() {
    	return this.publisher;
    }

    public String getVolume() {
    	return this.volume.get();
    }

    public void setVolume(String value) {
    	this.volume.set(value);
    }

    public StringProperty volumeProperty() {
    	return this.volume;
    }

    public String getIsbn() {
    	return this.isbn.get();
    }

    public void setIsbn(String value) {
    	this.isbn.set(value);
    }
    
    public StringProperty isbnProperty() {
    	return this.isbn;
    }

    public String getSeriesTitle() {
    	return this.seriesTitle.get();
    }

    public void setSeriesTitle(String value) {
    	this.seriesTitle.set(value);
    }

    public StringProperty seriesTitleProperty() {
    	return this.seriesTitle;
    }

    public String getConferenceName() {
    	return this.conferenceName.get();
    }

    public void setConferenceName(String value) {
    	this.conferenceName.set(value);
    }

    public StringProperty conferenceNameProperty() {
    	return this.conferenceName;
    }

    public int getConferenceYear() {
    	return this.conferenceYear.get();
    }

    public void setConferenceYear(int value) {
    	this.conferenceYear.set(value);
    }

    public IntegerProperty conferenceYearProperty() {
    	return this.conferenceYear;
    }

    public String getConferenceTitleWithYear() {
    	return this.conferenceTitleWithYear.get();
    }

    public StringBinding conferenceTitleWithYearBinding() {
    	return this.conferenceTitleWithYear;
    }

    public int getPublicationCount() {
    	return this.inProceedingsCount.get();
    }

    public void setPublicationCount(int value) {
    	this.inProceedingsCount.set(value);
    }

    public IntegerProperty publicationCountProperty() {
    	return this.inProceedingsCount;
    }

}