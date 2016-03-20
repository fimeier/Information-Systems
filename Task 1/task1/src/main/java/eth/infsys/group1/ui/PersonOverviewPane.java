package eth.infsys.group1.ui;

import java.util.Optional;

import eth.infsys.group1.dbspec.DBProvider;
import eth.infsys.group1.ui.fxobjs.FxPerson;
import eth.infsys.group1.ui.fxobjs.FxPerson.SortOption;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;

public class PersonOverviewPane<TRPerson> extends BorderPane {
	
	private final DBProvider<?, ?, ?, TRPerson, ?, ?, ?, ?> dbProvider;
	private ObservableList<FxPerson<TRPerson>> displayedData;
	
	private TextField searchNewText = new TextField();
	private Button newButton = new Button("New");
	private Button searchButton = new Button("Search");
	private String lastSearchTerm = "";

	private Button prevResButton = new Button("<");
	private Button nextResButton = new Button(">");
	private Button refreshButton = new Button("⟳");
	
	private ChoiceBox<FxPerson.SortOption> sortCB = new ChoiceBox<>();
	
	private TextField startIndexText = new TextField();
	private TextField endIndexText = new TextField();
	
	private IntegerProperty startIndex = new SimpleIntegerProperty(1);
	private IntegerProperty endIndex = new SimpleIntegerProperty(10);
	//private IntegerProperty totalResultCount = new SimpleIntegerProperty(0);
	
	private TextField nameEditText = new TextField();
	private BooleanProperty isEditingName = new SimpleBooleanProperty(false);
	private Label nameLabel = new Label("");
	
	private TableView<FxPerson<TRPerson>> personTable;
	private ReadOnlyObjectProperty<FxPerson<TRPerson>> selectedPerson;
	
	@SuppressWarnings("unchecked")
	public PersonOverviewPane(DBProvider<?, ?, ?, TRPerson, ?, ?, ?, ?> dbProvider) {
		this.dbProvider = dbProvider;
		this.displayedData = FXCollections.observableArrayList();

		BorderPane tablePane = new BorderPane(personTable);
		
		TableView<FxPerson<TRPerson>> personTable = new TableView<>(this.displayedData);
		TableColumn<FxPerson<TRPerson>, String> IDColumn = new TableColumn<>("ID");
		IDColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FxPerson<TRPerson>,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<FxPerson<TRPerson>, String> param) {
				return param.getValue().getIdProperty();
			}
		});
		TableColumn<FxPerson<TRPerson>, String> NameColumn = new TableColumn<>("Name");
		NameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FxPerson<TRPerson>,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<FxPerson<TRPerson>, String> param) {
				return param.getValue().getNameProperty();
			}
		});
		personTable.getColumns().setAll(IDColumn, NameColumn);
		this.selectedPerson = personTable.getSelectionModel().selectedItemProperty();
		this.selectedPerson.addListener(new ChangeListener<FxPerson<TRPerson>>() {
			@Override
			public void changed(ObservableValue<? extends FxPerson<TRPerson>> observable,
					FxPerson<TRPerson> oldValue, FxPerson<TRPerson> newValue) {
				nameLabel.setText(newValue.getName());
			}
		});
		
		prevResButton.setOnMouseClicked((event) -> prevResButtonClicked(event));
		nextResButton.setOnMouseClicked((event) -> nextResButtonClicked(event));
		//Label trLabel = new Label();
		//trLabel.textProperty().bindBidirectional(totalResultCount, new NumberStringConverter());
		sortCB.setItems(FXCollections.observableArrayList(SortOption.BY_NAME, SortOption.BY_ID));
		ToolBar pTableToolBar = new ToolBar(prevResButton, startIndexText, new Label("-"),
				endIndexText, nextResButton, refreshButton/*, new Label("Total: "), trLabel*/, sortCB);
		
		tablePane.setBottom(pTableToolBar);
		
		BorderPane personInfoPane = new BorderPane();
		GridPane personInfoGrid = new GridPane();
		personInfoGrid.getColumnConstraints().add(new ColumnConstraints());
		personInfoGrid.getColumnConstraints().add(new ColumnConstraints());
		ColumnConstraints cc3 = new ColumnConstraints(50);
		cc3.setHalignment(HPos.CENTER);
		personInfoGrid.getColumnConstraints().add(cc3);
		
		RowConstraints rc = new RowConstraints(30);
		personInfoGrid.getRowConstraints().add(rc);
		personInfoGrid.getRowConstraints().add(rc);
		personInfoGrid.getRowConstraints().add(rc);
		personInfoGrid.getRowConstraints().add(rc);
		
		Label ndLabel = new Label("Name:");
		GridPane.setMargin(ndLabel, new Insets(5.0));
		nameLabel.visibleProperty().bind(Bindings.not(isEditingName));
		GridPane.setMargin(nameLabel, new Insets(5.0));
		GridPane.setMargin(nameEditText, new Insets(5.0));
		Button nameEdit = new Button("Edit");
		nameEdit.visibleProperty().bind(Bindings.not(isEditingName));
		nameEdit.setOnMouseClicked((event) -> nameEditButtonClicked(event));
		Button nameOK = new Button("OK");
		nameOK.visibleProperty().bind(isEditingName);
		nameOK.setOnMouseClicked((event) -> nameOKButtonClicked(event));
		
		
		personInfoGrid.add(ndLabel, 0, 0);
		
		personInfoPane.setCenter(personInfoGrid);
		
		Button deleteButton = new Button("Delete");
		deleteButton.disableProperty().bind(Bindings.notEqual(selectedPerson, (Object)null));
		deleteButton.setOnMouseClicked((event) -> deletePersonButtonClicked(event));
		
		personInfoPane.setBottom(new ToolBar(deleteButton));
		
		this.setCenter(new SplitPane(tablePane, personInfoPane));
		
		startIndexText.textProperty().bindBidirectional(this.startIndex, new NumberStringConverter());
		endIndexText.textProperty().bindBidirectional(this.endIndex, new NumberStringConverter());

		this.searchNewText.setPromptText("Person name");
		newButton.setOnMouseClicked((event) -> newButtonClicked(event));
		searchButton.setOnMouseClicked((event) -> searchButtonClicked(event));
		ToolBar searchToolBar = new ToolBar(searchNewText, newButton, searchButton);
		this.setBottom(searchToolBar);
	}
	
	private void newButtonClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY) {
			TextInputDialog tid = new TextInputDialog(searchNewText.getText());
			tid.setTitle("New Person");
			tid.setHeaderText("Create a new person");
			tid.setContentText("Person name:");
			Optional<String> result = tid.showAndWait();
			if (result.isPresent()) {
				FxPerson<TRPerson> fxPerson = new FxPerson<TRPerson>(null, null, result.get());
				this.dbProvider.createPerson(fxPerson);
				this.displayedData.add(fxPerson);
				this.personTable.getSelectionModel().select(fxPerson);
			}
		}
	}
	
	private void prevResButtonClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY) {
			startIndex.subtract(10);
			endIndex.subtract(10);
			doSearch(lastSearchTerm, false);
		}
	}
	
	private void nextResButtonClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY) {
			startIndex.add(10);
			endIndex.add(10);
			doSearch(lastSearchTerm, false);
		}
	}
	
	private void nameEditButtonClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY) {
			this.nameEditText.setText(this.selectedPerson.getName());
			this.isEditingName.set(true);
		}
	}
	
	private void nameOKButtonClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY) {
			this.isEditingName.set(false);
			this.dbProvider.setPersonName(this.selectedPerson.get().getDBRepresentation(),
					this.nameEditText.getText());
		}
	}
	
	private void deletePersonButtonClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY) {
			FxPerson<TRPerson> selected = this.selectedPerson.get();
			this.displayedData.remove(selected);
			this.dbProvider.deletePerson(selected.getDBRepresentation());
		}
	}
	
	private void doSearch(String searchTerm, boolean resetStartEndIndex) {
		this.displayedData.clear();
		setNavControlsDisabled(true);
		this.lastSearchTerm = searchTerm;
		if (resetStartEndIndex) {
			startIndex.set(1);
			endIndex.set(10);
		}
		else {
			startIndex.set(Math.max(1, startIndex.get()));
			endIndex.set(Math.max(1, endIndex.get()));
			startIndex.set(Math.min(startIndex.get(), endIndex.get()));
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				dbProvider.getPersons(startIndex.get(), endIndex.get(),
						sortCB.getSelectionModel().getSelectedItem(), searchTerm, 
						displayedData);
				setNavControlsDisabled(false);
			}
		});
	}
	
	private void setNavControlsDisabled(boolean b) {
		newButton.setDisable(b);
		searchButton.setDisable(b);
		prevResButton.setDisable(b);
		nextResButton.setDisable(b);
		refreshButton.setDisable(b);
		startIndexText.setDisable(b);
		endIndexText.setDisable(b);
	}

	public void updateData() {
		this.dbProvider.updatePersonData(this.displayedData);
		FxPerson<TRPerson> selected = this.selectedPerson.get();
		if (selected != null)
			nameLabel.setText(selected.getName());
	}

}
