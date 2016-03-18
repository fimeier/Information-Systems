package eth.infsys.group1.ui.fxobjs;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FxInProceedings<TRInProceedings> extends FxPublication<TRInProceedings> {
	
	private StringProperty note;
	private StringProperty pages;
	private StringProperty proceedingsTitle;

	public FxInProceedings(TRInProceedings dbRep, String id,
			String title, int year, String electronicEdition,
			String proceedingsTitle, String pages, String note) {
		super(dbRep, id, title, year, electronicEdition);
		this.proceedingsTitle = new SimpleStringProperty(proceedingsTitle);
		this.pages = new SimpleStringProperty(pages);
		this.note = new SimpleStringProperty(note);
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