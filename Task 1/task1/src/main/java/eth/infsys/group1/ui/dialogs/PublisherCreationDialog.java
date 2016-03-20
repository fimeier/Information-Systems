package eth.infsys.group1.ui.dialogs;

import eth.infsys.group1.dbspec.DBProvider;
import eth.infsys.group1.dbspec.DBProvider.InvalidDataException;
import eth.infsys.group1.ui.Util;
import eth.infsys.group1.ui.fxobjs.FxPublisher;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PublisherCreationDialog<TRPublisher> extends DomainObjectCreationDialog<TRPublisher, FxPublisher<TRPublisher>> {

	private DBProvider<?, ?, ?, ?, ?, ?, TRPublisher, ?> dbProvider;
	
	private TextField nameText = new TextField();
	
	public PublisherCreationDialog(DBProvider<?, ?, ?, ?, ?, ?, TRPublisher, ?> dbProvider) {
		super("Publisher creation", "Create a new publisher");
		this.dbProvider = dbProvider;
		this.dataValidityProperty().bind(Bindings.createBooleanBinding(
				() -> (dbProvider.isValidPublisherName(nameText.getText())),
				nameText.textProperty()));
		
		this.getDialogPane().setContent(Util.create2ColumnGrid(1, 
				new Label("Name: "), nameText));
	}

	@Override
	protected FxPublisher<TRPublisher> createObject() {
		FxPublisher<TRPublisher> fxObj = new FxPublisher<TRPublisher>(null, null, nameText.getText());
		try {
			this.dbProvider.createPublisher(fxObj);
			return fxObj;
		} catch (InvalidDataException e) {
			Util.showInvalidDataAlert(e.field);
			return null;
		}
	}

}
