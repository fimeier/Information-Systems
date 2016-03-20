package eth.infsys.group1.ui.dialogs;

import eth.infsys.group1.dbspec.DBProvider;
import eth.infsys.group1.dbspec.DBProvider.ExistingIDException;
import eth.infsys.group1.dbspec.DBProvider.InvalidDataException;
import eth.infsys.group1.ui.Util;
import eth.infsys.group1.ui.fxobjs.FxConference;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ConferenceCreationDialog<TRConference> extends DomainObjectCreationDialog<TRConference, FxConference<TRConference>> {

	private DBProvider<TRConference, ?, ?, ?, ?, ?, ?, ?> dbProvider;

	private TextField IDText = new TextField();
	private TextField nameText = new TextField();
	
	public ConferenceCreationDialog(DBProvider<TRConference, ?, ?, ?, ?, ?, ?, ?> dbProvider) {
		super("Conference creation", "Create a new conference");
		this.dbProvider = dbProvider;
		this.dataValidityProperty().bind(Bindings.createBooleanBinding(
				() -> (dbProvider.isValidConferenceID(IDText.getText()) &&
						dbProvider.isValidConferenceName(nameText.getText())),
				IDText.textProperty(), nameText.textProperty()));
		
		this.getDialogPane().setContent(Util.create2ColumnGrid(2, 
				new Label("ID: "), IDText,
				new Label("Name: "), nameText));
	}

	@Override
	protected FxConference<TRConference> createObject() {
		if (nameText.getText().equals(""))
			nameText.setText(IDText.getText().toUpperCase());
		
		FxConference<TRConference> fxObj = new FxConference<TRConference>
			(null, IDText.getText(), nameText.getText(), 0);
		try {
			this.dbProvider.createConference(fxObj);
			return fxObj;
		} catch (ExistingIDException e) {
			Util.showExistingIDAlert(e.id);
			return fxObj;
		} catch (InvalidDataException e) {
			Util.showInvalidDataAlert(e.field);
			return null;
		}
	}

}
