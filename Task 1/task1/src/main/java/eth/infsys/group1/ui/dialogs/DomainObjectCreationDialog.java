package eth.infsys.group1.ui.dialogs;

import eth.infsys.group1.ui.fxobjs.FxDomainObject;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;

public abstract class DomainObjectCreationDialog<TR, TFx extends FxDomainObject<TR>> extends Dialog<TFx> {
	
	private BooleanProperty dataValidity = new SimpleBooleanProperty(false);
	
	public DomainObjectCreationDialog(String title, String header) {
		this.setTitle(title);
		this.setHeaderText(header);
		
		ButtonType createButtonType = new ButtonType("Create", ButtonData.OK_DONE);
		this.getDialogPane().getButtonTypes().setAll(createButtonType, ButtonType.CANCEL);
		
		Node selectButton = this.getDialogPane().lookupButton(createButtonType);
		selectButton.disableProperty().bind(dataValidity.not());
		
		this.setResultConverter((btnType) -> (btnType == createButtonType) ? createObject() : null);
	}
	
	protected BooleanProperty dataValidityProperty() {
		return this.dataValidity;
	}
	
	protected abstract TFx createObject();
	
}
