package eth.infsys.group1.ui.dialogs;

import eth.infsys.group1.dbspec.DBProvider;
import eth.infsys.group1.ui.fxobjs.FxPerson;
import eth.infsys.group1.ui.qtables.PersonQueryTable;

public class PersonSelectionDialog<TRPerson> extends QTableSelectionDialog<TRPerson, FxPerson<TRPerson>> {

	public PersonSelectionDialog(DBProvider<?, ?, ?, TRPerson, ?, ?, ?, ?> dbProvider) {
		super("Person Selection", "Please select a person",
				PersonQueryTable.createTableWithCreateToolBar(dbProvider));
	}

}
