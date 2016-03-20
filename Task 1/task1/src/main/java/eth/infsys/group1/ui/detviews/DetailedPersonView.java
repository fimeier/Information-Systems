package eth.infsys.group1.ui.detviews;

import java.util.Optional;

import eth.infsys.group1.dbspec.DBProvider;
import eth.infsys.group1.dbspec.DBProvider.InvalidDBRepException;
import eth.infsys.group1.dbspec.DBProvider.InvalidDataException;
import eth.infsys.group1.ui.FxObjEventHandler;
import eth.infsys.group1.ui.Util;
import eth.infsys.group1.ui.fxobjs.FxPerson;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DetailedPersonView<TRPerson, TRProceedings, TRInProceedings> extends BorderPane {

	private DBProvider<?, ?, TRInProceedings, TRPerson, TRProceedings, ?, ?, ?> dbProvider;
	
	private ObjectProperty<FxPerson<TRPerson>> person = new SimpleObjectProperty<>();

	private Label idLabel = new Label();
	private Label nameLabel = new Label();
	
	private FxObjEventHandler<FxPerson<TRPerson>> onDeletionHandler;
	
	public DetailedPersonView(DBProvider<?, ?, TRInProceedings, TRPerson, TRProceedings, ?, ?, ?> dbProvider,
			FxObjEventHandler<FxPerson<TRPerson>> onDeletionHandler) {
		this.dbProvider = dbProvider;
		this.onDeletionHandler = onDeletionHandler;
		
		BooleanBinding noPerson = Bindings.equal(person, (Object)null);
		
		Button nameEditButton = new Button("Edit");
		nameEditButton.setOnMouseClicked((event) -> nameEditButtonClick(event));
		nameEditButton.disableProperty().bind(noPerson);
		
		GridPane grid = Util.create3ColumnGrid(2, 50, 
				new Label("ID: "), this.idLabel, null,
				new Label("Name: "), this.nameLabel, nameEditButton);
		
		TitledPane tpProc = new TitledPane("Proceedings", null); //TODO
		
		TitledPane tpInProc = new TitledPane("InProceedings", null); //TODO
		
		Accordion pubAccordion = new Accordion(tpProc, tpInProc);
		
		this.setCenter(new VBox(grid, pubAccordion));
		
		Button deleteButton = new Button("Delete");
		deleteButton.setOnMouseClicked((event) -> deleteButtonClick(event));
		deleteButton.disableProperty().bind(noPerson);
		
		this.setBottom(new ToolBar(deleteButton));
	}

	public FxPerson<TRPerson> getPerson() {
		return this.person.get();
	}
	
	public ObjectProperty<FxPerson<TRPerson>> personProperty() {
		return this.person;
	}
	
	public void setPerson(FxPerson<TRPerson> value) {
		this.person.set(value);
	}
	
	public void deleteButtonClick(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY) {
			FxPerson<TRPerson> deletedPerson = this.getPerson();
			
			if (deletedPerson != null) {
				Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
				confirmationAlert.setTitle("Object Deletion");
				confirmationAlert.setContentText(
						"Are you sure that you want to delete the person '"
						+ deletedPerson.getName() + "' ? This cannot be undone.");
				
				Optional<ButtonType> result = confirmationAlert.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					this.dbProvider.deletePerson(deletedPerson.getDBRepresentation());
					this.onDeletionHandler.handle(deletedPerson);
				}
			}
		}
	}
	
	public void nameEditButtonClick(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY) {
			TextInputDialog dialog = new TextInputDialog(this.person.getName());
			Optional<String> newName = dialog.showAndWait();
			if (newName.isPresent()) {
				try {
					this.dbProvider.setPersonName(this.getPerson().getDBRepresentation(), newName.get());
				} catch (InvalidDataException e) {
					Util.showInvalidDataAlert("name");
				} catch (InvalidDBRepException e) {
					Util.showInvalidDBRepAlert(e.repDescription);
				}
			}
		}
	}
	
}
