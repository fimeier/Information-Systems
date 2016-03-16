package eth.infsys.group1.task1.dbobjs;

import ch.ethz.globis.isk.domain.*;

/**
 * Implementation of the {@link ch.ethz.globis.isk.domain.InProceedings} interface for ZooDB.
 */
public class InProceedings extends Publication implements InProceedings {
	
	private String note;
	private String pages;
	private Proceedings proceedings;

	public InProceedings() { }

    public String getNote() {
    	zooActivateRead();
		return this.note;
	}
    
    /**
     *<pages> from XML-File
     */
    public void setNote(String note) {
    	zooActivateWrite();
    	this.note = note;
	}

    public String getPages() {
		zooActivateRead();
		return this.pages;
	}

/**
 *<pages> from XML-File
 */
    public void setPages(String pages) {
    	zooActivateWrite();
    	this.pages = pages;
	}

    public Proceedings getProceedings() {
    	zooActivateRead();
		return this.proceedings;
	}

    public void setProceedings(Proceedings proceedings) {
    	zooActivateWrite();
    	this.proceedings = proceedings;
	}
    
}