package eth.infsys.group1.ui.dialogs;

import eth.infsys.group1.dbspec.DBProvider;
import eth.infsys.group1.dbspec.DBProvider.InvalidDataException;
import eth.infsys.group1.ui.Util;
import eth.infsys.group1.ui.fxobjs.FxPerson;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PersonCreationDialog<TRPerson> extends DomainObjectCreationDialog<TRPerson, FxPerson<TRPerson>> {

	private DBProvider<?, ?, ?, TRPerson, ?, ?, ?, ?> dbProvider;
	
	private TextField nameText = new TextField();
	
	public PersonCreationDialog(DBProvider<?, ?, ?, TRPerson, ?, ?, ?, ?> dbProvider) {
		super("Person creation", "Create a new person");
		this.dbProvider = dbProvider;
		this.dataValidityProperty().bind(Bindings.createBooleanBinding(
				() -> (dbProvider.isValidPersonName(nameText.getText())),
				nameText.textProperty()));
		
		this.getDialogPane().setContent(Util.create2ColumnGrid(1, 
				new Label("Name: "), nameText));
	}

	@Override
	protected FxPerson<TRPerson> createObject() {
		FxPerson<TRPerson> fxObj = new FxPerson<TRPerson>(null, null, nameText.getText());
		try {
			this.dbProvider.createPerson(fxObj);
			return fxObj;
		} catch (InvalidDataException e) {
			Util.showInvalidDataAlert(e.field);
			return null;
		}
	}

}
