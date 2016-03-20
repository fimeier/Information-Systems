package eth.infsys.group1.ui.dialogs;

import eth.infsys.group1.ui.fxobjs.FxDomainObject;
import eth.infsys.group1.ui.qtables.QueryTable;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;

public abstract class QTableSelectionDialog<TR, TFx extends FxDomainObject<TR>> extends Dialog<TFx> {

	private QueryTable<TR, TFx, ?> queryTable;
	
	public QTableSelectionDialog(
			String title, String header, QueryTable<TR, TFx, ?> qtable) {
		this.setTitle(title);
		this.setHeaderText(header);
		
		this.queryTable = qtable;
		this.getDialogPane().setContent(qtable);
		
		ButtonType selectButtonType = new ButtonType("Select", ButtonData.OK_DONE);
		this.getDialogPane().getButtonTypes().setAll(selectButtonType, ButtonType.CANCEL);
		
		Node selectButton = this.getDialogPane().lookupButton(selectButtonType);
		selectButton.disableProperty().bind(Bindings.equal(qtable.selectedItemProperty(), (Object)null));
		
		this.setResultConverter((btnType) -> (btnType == selectButtonType) ? qtable.getSelectedItem() : null);
	}
	
	public void updateData() {
		this.queryTable.updateData();
	}
	
}
