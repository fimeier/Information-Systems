package eth.infsys.group1.task1.dbobjs;

import ch.ethz.globis.isk.domain.*;

/**
 * Implementation of the {@link ch.ethz.globis.isk.domain.InProceedings} interface for ZooDB.
 */
public class T1InProceedings extends T1Publication implements InProceedings {
	
	private String note;
	private String pages;
	private Proceedings proceedings;

	public T1InProceedings() { }

    public String getNote() {
    	zooActivateRead();
		return this.note;
	}

    public void setNote(String note) {
    	zooActivateWrite();
    	this.note = note;
	}

    public String getPages() {
		zooActivateRead();
		return this.pages;
	}

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