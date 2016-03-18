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

	public Set<String> getEditors() {
		return this.editors;
	}
	
	public void setEditors(Set<String> value) {
		this.editors.clear();
		this.editors.addAll(value);
	}
	
    public String getNote() {
    	return this.note.get();
	}
	
    public StringProperty getNoteProperty() {
    	return this.note;
	}

    public void setNote(String value) {
    	this.note.set(value);
	}

    public int getNumber() {
    	return this.number.get();
    }
    
    public IntegerProperty getNumberProperty() {
    	return this.number;
    }

    public void setNumber(int value) {
    	this.number.set(value);
    }

    public String getPublisher() {
    	return this.publisher.get();
    }

    public StringProperty getPublisherProperty() {
    	return this.publisher;
    }

    public void setPublisher(String value) {
    	this.publisher.set(value);
    }

    public String getVolume() {
    	return this.volume.get();
    }

    public StringProperty getVolumeProperty() {
    	return this.volume;
    }

    public void setVolume(String value) {
    	this.volume.set(value);
    }

    public String getIsbn() {
    	return this.isbn.get();
    }
    
    public StringProperty getIsbnProperty() {
    	return this.isbn;
    }

    public void setIsbn(String value) {
    	this.isbn.set(value);
    }

    public String getSeriesTitle() {
    	return this.seriesTitle.get();
    }

    public StringProperty getSeriesTitleProperty() {
    	return this.seriesTitle;
    }

    public void setSeriesTitle(String value) {
    	this.seriesTitle.set(value);
    }

    public String getConferenceName() {
    	return this.conferenceName.get();
    }

    public StringProperty getConferenceNameProperty() {
    	return this.conferenceName;
    }

    public void setConferenceName(String value) {
    	this.conferenceName.set(value);
    }

    public int getConferenceYear() {
    	return this.conferenceYear.get();
    }

    public IntegerProperty getConferenceYearProperty() {
    	return this.conferenceYear;
    }

    public void setConferenceYear(int value) {
    	this.conferenceYear.set(value);
    }

    public String getConferenceTitleWithYear() {
    	return this.conferenceTitleWithYear.get();
    }

    public StringBinding getConferenceTitleWithYearBinding() {
    	return this.conferenceTitleWithYear;
    }

    public int getPublicationCount() {
    	return this.inProceedingsCount.get();
    }

    public IntegerProperty getPublicationCountProperty() {
    	return this.inProceedingsCount;
    }

    public void setPublicationCount(int value) {
    	this.inProceedingsCount.set(value);
    }

}