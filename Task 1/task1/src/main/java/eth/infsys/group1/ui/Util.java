package eth.infsys.group1.ui;

import java.util.Optional;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public final class Util {
	
	public static GridPane create2ColumnGrid(int rows, Node... elements) {
		assert(elements.length == 2 * rows);
		
		GridPane grid = new GridPane();
		grid.getColumnConstraints().add(new ColumnConstraints());
		grid.getColumnConstraints().add(new ColumnConstraints());
		for (int i = 0; i < rows; i++)
			grid.getRowConstraints().add(new RowConstraints(30));
		
		for (int i = 0, e = 0; i < rows; i++) {
			for (int j = 0; j < 2; j++, e++) {
				Node n = elements[e];
				if (n != null) {
					GridPane.setMargin(n, new Insets(5));
					grid.add(n, j, i);
				}
			}
		}
		
		return grid;
	}
	
	public static GridPane create3ColumnGrid(int rows, double c3Width, Node... elements) {
		assert(elements.length == 3 * rows);
		
		GridPane grid = new GridPane();
		grid.getColumnConstraints().add(new ColumnConstraints());
		grid.getColumnConstraints().add(new ColumnConstraints());
		ColumnConstraints cc3 = new ColumnConstraints(c3Width);
		cc3.setHalignment(HPos.CENTER);
		grid.getColumnConstraints().add(cc3);
		for (int i = 0; i < rows; i++)
			grid.getRowConstraints().add(new RowConstraints(30));
		
		for (int i = 0, e = 0; i < rows; i++) {
			for (int j = 0; j < 3; j++, e++) {
				Node n = elements[e];
				if (n != null)
					grid.add(n, j, i);
			}
		}
		
		return grid;
	}
	
	public static Optional<ButtonType> showExistingIDAlert(String id) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Info: Existing ID");
		alert.setContentText("An object with this ID already exists: '" + id + "'.");
		return alert.showAndWait();
	}

	public static Optional<ButtonType> showInvalidDataAlert(String field) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error: Invalid Data");
		alert.setContentText("A part of the provided data was invalid: '"
				+ field + "'. The object could not be created/edited.");
		return alert.showAndWait();
	}

	public static Optional<ButtonType> showInvalidDBRepAlert(String repDescription) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error: Invalid " + repDescription);
		alert.setContentText("The provided " + repDescription +
				" was invalid. The object could not be created/edited.");
		return alert.showAndWait();
	}
	
}
