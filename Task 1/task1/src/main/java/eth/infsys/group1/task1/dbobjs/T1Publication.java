package eth.infsys.group1.task1.dbobjs;

import ch.ethz.globis.isk.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the {@link ch.ethz.globis.isk.domain.Publication} interface for ZooDB.
 */
public class T1Publication extends T1DomainObject implements Publication {

	private String title;
	private List<Person> authors;
	private Set<Person> editors;
	private int year;
	private String electronicEdition;

	public T1Publication() { }
	
    public String getTitle() {
    	zooActivateRead();
		return this.title;
	}

    public void setTitle(String title) {
    	zooActivateWrite();
    	this.title = title;
    }

    public List<Person> getAuthors() {
    	zooActivateRead();
		return Collections.unmodifiableList(this.authors);
	}

    public void setAuthors(List<Person> authors) {
    	zooActivateWrite();
    	//this.authors.clear();  probably a empty List
    	this.authors = authors;

	}

    public Set<Person> getEditors() {
    	zooActivateRead();
		return Collections.unmodifiableSet(this.editors);
	}

    public void setEditors(Set<Person> editors) {
    	zooActivateWrite();
    	//this.editors.clear(); probably a empty Set
    	this.editors = editors;
	}

    public int getYear() {
    	zooActivateRead();
		return this.year;
	}

    public void setYear(int year) {
    	zooActivateWrite();
    	this.year = year;
	}

    public String getElectronicEdition() {
    	zooActivateRead();
		return this.electronicEdition;
	}

    public void setElectronicEdition(String electronicEdition) {
    	zooActivateWrite();
    	this.electronicEdition = electronicEdition;
	}
    
}