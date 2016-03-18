package eth.infsys.group1.ui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * The main window of our application
 */
public class MainPane<TRConference,
					  TRConferenceEdition,
					  TRInProceedings,
					  TRPerson,
					  TRProceedings,
					  TRPublication,
					  TRPublisher,
					  TRSeries> extends TabPane {

	public MainPane(DBProvider<TRConference, TRConferenceEdition, TRInProceedings,
			TRPerson, TRProceedings, TRPublication, TRPublisher, TRSeries> dbProvider) {
        PersonOverviewPane<TRPerson> personListPane = new PersonOverviewPane<TRPerson>(dbProvider);
		Tab personTab = new Tab("Persons", personListPane);
        personTab.setClosable(false);
        personTab.setOnSelectionChanged(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				if (personTab.isSelected())
					personListPane.updateData();
			}
		});
        this.getTabs().add(personTab);
	}

}
