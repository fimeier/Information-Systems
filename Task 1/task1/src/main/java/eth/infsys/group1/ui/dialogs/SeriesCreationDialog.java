package eth.infsys.group1.ui.dialogs;

import eth.infsys.group1.dbspec.DBProvider;
import eth.infsys.group1.dbspec.DBProvider.InvalidDataException;
import eth.infsys.group1.ui.Util;
import eth.infsys.group1.ui.fxobjs.FxSeries;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SeriesCreationDialog<TRSeries> extends DomainObjectCreationDialog<TRSeries, FxSeries<TRSeries>> {

	private DBProvider<?, ?, ?, ?, ?, ?, ?, TRSeries> dbProvider;
	
	private TextField nameText = new TextField();
	
	public SeriesCreationDialog(DBProvider<?, ?, ?, ?, ?, ?, ?, TRSeries> dbProvider) {
		super("Series creation", "Create a new series");
		this.dbProvider = dbProvider;
		this.dataValidityProperty().bind(Bindings.createBooleanBinding(
				() -> (dbProvider.isValidSeriesName(nameText.getText())),
				nameText.textProperty()));
		
		this.getDialogPane().setContent(Util.create2ColumnGrid(1, 
				new Label("Name: "), nameText));
	}

	@Override
	protected FxSeries<TRSeries> createObject() {
		FxSeries<TRSeries> fxObj = new FxSeries<TRSeries>(null, null, nameText.getText());
		try {
			this.dbProvider.createSeries(fxObj);
			return fxObj;
		} catch (InvalidDataException e) {
			Util.showInvalidDataAlert(e.field);
			return null;
		}
	}

}
