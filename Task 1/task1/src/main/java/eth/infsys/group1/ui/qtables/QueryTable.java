package eth.infsys.group1.ui.qtables;

import java.util.Collection;

import eth.infsys.group1.ui.fxobjs.FxDomainObject;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;

public abstract class QueryTable<TR, TFx extends FxDomainObject<TR>,
				TQO extends FxDomainObject.QueryOptions<?>> extends BorderPane {
	
	private ObservableList<TFx> displayedData = FXCollections.observableArrayList();
	
	private final TQO queryOptions;
	
	private TableView<TFx> table;
	
	private ToolBar navToolBar;
	private Button prevResButton = new Button("<");
	private Button nextResButton = new Button(">");
	private Button refreshButton = new Button("⟳");
	private TextField startIndexText = new TextField();
	private TextField endIndexText = new TextField();
	
	private ToolBar efToolBar;
	
	public QueryTable() {
		this.queryOptions = getInitialQueryOptions();
		
		this.table = new TableView<TFx>(displayedData);
		this.table.getColumns().setAll(createTableColumns());
		this.table.setSortPolicy((t) -> true); //deactivate default sorting
		
		this.setCenter(table);
		
		this.prevResButton.setOnMouseClicked((event) -> prevResButtonClicked(event));
		this.nextResButton.setOnMouseClicked((event) -> nextResButtonClicked(event));
		this.refreshButton.setOnMouseClicked((event) -> refreshButtonClicked(event));
		
		this.startIndexText.textProperty().bindBidirectional(queryOptions.startIndexProperty(),
				new NumberStringConverter());
		this.startIndexText.setPrefWidth(50);
		
		this.endIndexText.textProperty().bindBidirectional(queryOptions.endIndexProperty(),
				new NumberStringConverter());
		this.endIndexText.setPrefWidth(50);
		
		this.navToolBar = new ToolBar(prevResButton, startIndexText, new Label("-"),
				endIndexText, nextResButton, refreshButton);
		
		this.efToolBar = this.createEfToolBar();
		
		this.setBottom(efToolBar == null ? navToolBar : new VBox(navToolBar, efToolBar));
		
	}
	
	public void doQuery() {
		this.navToolBar.setDisable(true);
		if (this.efToolBar != null)
			this.efToolBar.setDisable(true);
		this.queryOptions.raiseIndexes(1, 1);
		this.displayedData.clear();
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				doQuery(queryOptions, displayedData);
				
				navToolBar.setDisable(false);
				if (efToolBar != null)
					efToolBar.setDisable(false);
			}
		});
	}
	
	public ObservableList<TFx> getDisplayedData() {
		return this.displayedData;
	}
	
	public TQO getQueryOptions() {
		return this.queryOptions;
	}
	
	public TFx getSelectedItem() {
		return this.table.getSelectionModel().getSelectedItem();
	}

	public void selectItem(TFx item) {
		this.table.getSelectionModel().select(item);;
	}
	
	public ReadOnlyObjectProperty<TFx> selectedItemProperty() {
		return this.table.getSelectionModel().selectedItemProperty();
	}
	
	public void updateData() {
		this.updateData(displayedData);
	}
	
	// ===METHODS IMPLEMENTED BY SUBCLASSES===
	
	protected abstract ToolBar createEfToolBar();
	
	protected abstract Collection<? extends TableColumn<TFx, ?>> createTableColumns();
	
	protected abstract void doQuery(TQO options, ObservableList<TFx> out);
	
	protected abstract TQO getInitialQueryOptions();
	
	protected abstract void updateData(ObservableList<TFx> data);
	
	// ===EVENT HANDLERS===
	
	private void prevResButtonClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY) {
			this.queryOptions.addValueToIndexes(-10);
			doQuery();
		}
	}
	
	private void nextResButtonClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY) {
			this.queryOptions.addValueToIndexes(10);
			doQuery();
		}
	}
	
	private void refreshButtonClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.PRIMARY) {
			doQuery();
		}
	}
	
}
