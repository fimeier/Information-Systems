package eth.infsys.group1.dbspec;

import eth.infsys.group1.xmlparser.InProceedings_simple_input;
import eth.infsys.group1.xmlparser.Proceedings_simple_input;

public abstract class DBProvider<TRConference,
								TRConferenceEdition,
								TRInProceedings,
								TRPerson,
								TRProceedings,
								TRPublication,
								TRPublisher,
								TRSeries> {




	public static final class InvalidDBRepException extends Exception {

		private static final long serialVersionUID = -3845406311284661251L;
		
		public final Object invalidRep;
		public final String repDescription;
		
		public InvalidDBRepException(Object invalidRep, String repDescription) {
			super("Invalid DB representation argument: " + repDescription);
			this.invalidRep = invalidRep;
			this.repDescription = repDescription;
		}
		
	}
	
	public DBProvider() { }

	
	
	

	public abstract String batch_createProceedings(Proceedings_simple_input data);
	public abstract String batch_createInProceedings(InProceedings_simple_input data);
	
	
}
