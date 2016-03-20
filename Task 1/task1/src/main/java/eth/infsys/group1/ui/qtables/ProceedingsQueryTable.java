package eth.infsys.group1.ui.qtables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import eth.infsys.group1.dbspec.DBProvider;
import eth.infsys.group1.ui.FxObjEventHandler;
import eth.infsys.group1.ui.dialogs.PersonCreationDialog;
import eth.infsys.group1.ui.dialogs.PersonSelectionDialog;
import eth.infsys.group1.ui.fxobjs.FxPerson;
import eth.infsys.group1.ui.fxobjs.FxProceedings;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ToolBar;

public abstract class ProceedingsQueryTable<TRProceedings>
		extends QueryTable<TRProceedings, FxProceedings<TRProceedings>, FxProceedings.QueryOptions> {

	private static class SearchPQT<TRProceedings> extends ProceedingsQueryTable<TRProceedings> {
		
		private TextField searchText = new TextField();

		public SearchPQT(DBProvider<?, ?, ?, ?, TRProceedings, ?, ?, ?> dbProvider) {
			super(dbProvider);
		}

		@Override
		protected ToolBar createEfToolBar() {
			TextField searchText = new TextField();
			searchText.setPromptText("Person name");
			
			Button searchButton = new Button("Search");
			searchButton.setOnMouseClicked((event) -> searchButtonClicked(event));
			
			return new ToolBar(searchText);
		}
		
		private void searchButtonClicked(MouseEvent event) {
			if (event.getButton() == MouseButton.PRIMARY) {
				this.getQueryOptions().setSearchTerm(this.searchText.getText());
				this.getQueryOptions().setStartIndex(1);
				this.getQueryOptions().setEndIndex(10);
				this.doQuery();
			}
		}
		
	}
	
	private static class AddRemovePQT<TRPerson> extends SearchPQT<TRPerson> {

		private FxObjEventHandler<FxPerson<TRPerson>> addButtonHandler;
		private FxObjEventHandler<FxPerson<TRPerson>> removeButtonHandler;
		
		private PersonSelectionDialog<TRPerson> pSelectionDialog;
		
		public AddRemovePQT(DBProvider<?, ?, ?, TRPerson, ?, ?, ?, ?> dbProvider,
				FxObjEventHandler<FxPerson<TRPerson>> addButtonHandler,
				FxObjEventHandler<FxPerson<TRPerson>> removeButtonHandler) {
			super(dbProvider);
			this.addButtonHandler = addButtonHandler;
			this.removeButtonHandler = removeButtonHandler;
			this.pSelectionDialog = new PersonSelectionDialog<>(dbProvider);
		}

		@Override
		protected ToolBar createEfToolBar() {
			ToolBar tb = super.createEfToolBar();
			
			Button addButton = new Button("Add...");
			addButton.setOnMouseClicked((event) -> addButtonClicked(event));
			tb.getItems().add(addButton);
			
			Button removeButton = new Button("Remove");
			removeButton.setOnMouseClicked((event) -> removeButtonClicked(event));
			removeButton.disableProperty().bind(Bindings.equal(this.selectedItemProperty(), (Object)null));
			tb.getItems().add(removeButton);
			
			return tb;
		}
		
		private void addButtonClicked(MouseEvent event) {
			if (event.getButton() == MouseButton.PRIMARY) {
				pSelectionDialog.updateData();
				Optional<FxPerson<TRPerson>> result = pSelectionDialog.showAndWait();
				
				result.ifPresent((fxObj) -> addButtonHandler.handle(fxObj));
			}
		}
		
		private void removeButtonClicked(MouseEvent event) {
			if (event.getButton() == MouseButton.PRIMARY) {
				FxPerson<TRPerson> selectedItem = this.getSelectedItem();
				if (selectedItem != null)
					this.removeButtonHandler.handle(selectedItem);
			}
		}
		
	}
	
	private static class AddRemoveViewPQT<TRPerson> extends AddRemovePQT<TRPerson> {

		private FxObjEventHandler<FxPerson<TRPerson>> viewButtonHandler;
		
		public AddRemoveViewPQT(DBProvider<?, ?, ?, TRPerson, ?, ?, ?, ?> dbProvider,
				FxObjEventHandler<FxPerson<TRPerson>> addButtonHandler,
				FxObjEventHandler<FxPerson<TRPerson>> removeButtonHandler,
				FxObjEventHandler<FxPerson<TRPerson>> viewButtonHandler) {
			super(dbProvider, addButtonHandler, removeButtonHandler);
			this.viewButtonHandler = viewButtonHandler;
		}

		@Override
		protected ToolBar createEfToolBar() {
			ToolBar tb = super.createEfToolBar();
			
			Button viewButton = new Button("View");
			viewButton.setOnMouseClicked((event) -> viewButtonClicked(event));
			viewButton.disableProperty().bind(Bindings.equal(this.selectedItemProperty(), (Object)null));
			tb.getItems().add(viewButton);
			
			return tb;
		}
		
		private void viewButtonClicked(MouseEvent event) {
			if (event.getButton() == MouseButton.PRIMARY && this.viewButtonHandler != null) {
				FxPerson<TRPerson> selectedItem = this.getSelectedItem();
				if (selectedItem != null)
					this.viewButtonHandler.handle(selectedItem);
			}
		}
		
	}

	private static class CreatePQT<TRPerson> extends SearchPQT<TRPerson> {
		
		public CreatePQT(DBProvider<?, ?, ?, TRPerson, ?, ?, ?, ?> dbProvider) {
			super(dbProvider);
		}

		@Override
		protected ToolBar createEfToolBar() {
			ToolBar tb = super.createEfToolBar();
			
			Button createButton = new Button("Create...");
			createButton.setOnMouseClicked((event) -> createButtonClicked(event));
			tb.getItems().add(createButton);
			
			return tb;
		}
		
		private void createButtonClicked(MouseEvent event) {
			if (event.getButton() == MouseButton.PRIMARY) {
				Dialog<FxPerson<TRPerson>> creationDialog =
						new PersonCreationDialog<>(this.dbProvider());
				Optional<FxPerson<TRPerson>> result = creationDialog.showAndWait();
				
				if (result.isPresent()) {
					this.getDisplayedData().add(result.get());
					this.selectItem(result.get());
				}
			}
		}
		
	}

	public static <TRPerson> ProceedingsQueryTable<TRPerson> createTableWithAddRemoveToolBar(
			DBProvider<?, ?, ?, TRPerson, ?, ?, ?, ?> dbProvider,
			FxObjEventHandler<FxPerson<TRPerson>> addButtonHandler,
			FxObjEventHandler<FxPerson<TRPerson>> removeButtonHandler) {
		return new AddRemovePQT<TRPerson>(dbProvider, addButtonHandler, removeButtonHandler);
	}

	public static <TRPerson> ProceedingsQueryTable<TRPerson> createTableWithAddRemoveViewToolBar(
			DBProvider<?, ?, ?, TRPerson, ?, ?, ?, ?> dbProvider,
			FxObjEventHandler<FxPerson<TRPerson>> addButtonHandler,
			FxObjEventHandler<FxPerson<TRPerson>> removeButtonHandler,
			FxObjEventHandler<FxPerson<TRPerson>> viewButtonHandler) {
		return new AddRemoveViewPQT<TRPerson>(dbProvider, addButtonHandler,
				removeButtonHandler, viewButtonHandler);
	}
	
	public static <TRPerson> ProceedingsQueryTable<TRPerson> createTableWithCreateToolBar(
			DBProvider<?, ?, ?, TRPerson, ?, ?, ?, ?> dbProvider) {
		return new CreatePQT<TRPerson>(dbProvider);
	}
	
	private DBProvider<?, ?, ?, TRPerson, ?, ?, ?, ?> dbProvider;
	
	public ProceedingsQueryTable(DBProvider<?, ?, ?, TRPerson, ?, ?, ?, ?> dbProvider) {
		this.dbProvider = dbProvider;
	}
	
	protected DBProvider<?, ?, ?, TRPerson, ?, ?, ?, ?> dbProvider() {
		return this.dbProvider;
	}

	@Override
	protected Collection<? extends TableColumn<FxPerson<TRPerson>, ?>> createTableColumns() {
		ArrayList<TableColumn<FxPerson<TRPerson>, ?>> columns = new ArrayList<>();
		
		TableColumn<FxPerson<TRPerson>, String> IDColumn = new TableColumn<>("ID");
		IDColumn.setCellValueFactory((param) -> param.getValue().idProperty());
		IDColumn.sortTypeProperty().addListener(new ChangeListener<SortType>() {
			@Override
			public void changed(ObservableValue<? extends SortType> observable, SortType oldValue, SortType newValue) {
				FxPerson.QueryOptions qo = getQueryOptions();
				if (newValue == SortType.ASCENDING) {
					qo.setSortingOption(FxPerson.SortOption.BY_ID_ASC);
					doQuery();
				}
				else if (newValue == SortType.DESCENDING) {
					qo.setSortingOption(FxPerson.SortOption.BY_ID_DESC);
					doQuery();
				}
				else if (qo.getSortingOption() == FxPerson.SortOption.BY_ID_ASC
							|| qo.getSortingOption() == FxPerson.SortOption.BY_ID_DESC)
					qo.setSortingOption(FxPerson.SortOption.NONE);
			}
		});
		columns.add(IDColumn);
		
		TableColumn<FxPerson<TRPerson>, String> NameColumn = new TableColumn<>("Name");
		NameColumn.setCellValueFactory((param) -> param.getValue().nameProperty());
		NameColumn.sortTypeProperty().addListener(new ChangeListener<SortType>() {
			@Override
			public void changed(ObservableValue<? extends SortType> observable, SortType oldValue, SortType newValue) {
				FxPerson.QueryOptions qo = getQueryOptions();
				if (newValue == SortType.ASCENDING) {
					qo.setSortingOption(FxPerson.SortOption.BY_NAME_ASC);
					doQuery();
				}
				else if (newValue == SortType.DESCENDING) {
					qo.setSortingOption(FxPerson.SortOption.BY_NAME_DESC);
					doQuery();
				}
				else if (qo.getSortingOption() == FxPerson.SortOption.BY_NAME_ASC
							|| qo.getSortingOption() == FxPerson.SortOption.BY_NAME_DESC)
					qo.setSortingOption(FxPerson.SortOption.NONE);
			}
		});
		columns.add(NameColumn);
		
		TableColumn<FxPerson<TRPerson>, Number> APCColumn = new TableColumn<>("Authored");
		APCColumn.setCellValueFactory((param) -> param.getValue().authoredPublicationCountProperty());
		APCColumn.sortTypeProperty().addListener(new ChangeListener<SortType>() {
			@Override
			public void changed(ObservableValue<? extends SortType> observable, SortType oldValue, SortType newValue) {
				FxPerson.QueryOptions qo = getQueryOptions();
				if (newValue == SortType.ASCENDING) {
					qo.setSortingOption(FxPerson.SortOption.BY_AUTHORED_PUBLS_ASC);
					doQuery();
				}
				else if (newValue == SortType.DESCENDING) {
					qo.setSortingOption(FxPerson.SortOption.BY_AUTHORED_PUBLS_DESC);
					doQuery();
				}
				else if (qo.getSortingOption() == FxPerson.SortOption.BY_AUTHORED_PUBLS_ASC
							|| qo.getSortingOption() == FxPerson.SortOption.BY_AUTHORED_PUBLS_DESC)
					qo.setSortingOption(FxPerson.SortOption.NONE);
			}
		});
		columns.add(APCColumn);
		
		TableColumn<FxPerson<TRPerson>, Number> EPCColumn = new TableColumn<>("Edited");
		EPCColumn.setCellValueFactory((param) -> param.getValue().editedPublicationCountProperty());
		EPCColumn.sortTypeProperty().addListener(new ChangeListener<SortType>() {
			@Override
			public void changed(ObservableValue<? extends SortType> observable, SortType oldValue, SortType newValue) {
				FxPerson.QueryOptions qo = getQueryOptions();
				if (newValue == SortType.ASCENDING) {
					qo.setSortingOption(FxPerson.SortOption.BY_EDITED_PUBLS_ASC);
					doQuery();
				}
				else if (newValue == SortType.DESCENDING) {
					qo.setSortingOption(FxPerson.SortOption.BY_EDITED_PUBLS_DESC);
					doQuery();
				}
				else if (qo.getSortingOption() == FxPerson.SortOption.BY_EDITED_PUBLS_ASC
							|| qo.getSortingOption() == FxPerson.SortOption.BY_EDITED_PUBLS_DESC)
					qo.setSortingOption(FxPerson.SortOption.NONE);
			}
		});
		columns.add(EPCColumn);
		
		return columns;
	}

	@Override
	protected void doQuery(FxPerson.QueryOptions options, ObservableList<FxPerson<TRPerson>> out) {
		dbProvider.getPersons(options, out);
	}

	@Override
	protected FxPerson.QueryOptions getInitialQueryOptions() {
		return new FxPerson.QueryOptions(1, 10, FxPerson.SortOption.NONE, "");
	}

	@Override
	protected void updateData(ObservableList<FxPerson<TRPerson>> data) {
		this.dbProvider.updatePersonData(data);
	}
	
}
