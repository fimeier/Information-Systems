package eth.infsys.group1.task1.dbobjs;

import ch.ethz.globis.isk.domain.*;

import java.util.Collections;
import java.util.Set;

/**
 * Implementation of the {@link ch.ethz.globis.isk.domain.Person} interface for ZooDB.
 */
public class T1Person extends T1DomainObject implements Person {

	public T1Person() { }
	
    private String name;
	private Set<Publication> authoredPublications;
	private Set<Publication> editedPublications;

	public String getName() {
    	zooActivateRead();
    	return this.name;
    }

    public void setName(String name) {
    	zooActivateWrite();
    	this.name = name;
    }

    public Set<Publication> getAuthoredPublications() {
    	zooActivateRead();
    	return Collections.unmodifiableSet(this.authoredPublications);
    }

    public void setAuthoredPublications(Set<Publication> authoredPublications) {
    	this.authoredPublications.clear();
    	this.authoredPublications.addAll(authoredPublications);
    }

    public Set<Publication> getEditedPublications() {
    	zooActivateRead();
    	return Collections.unmodifiableSet(this.editedPublications);
    }

    public void setEditedPublications(Set<Publication> editedPublications) {
    	zooActivateWrite();
    	this.editedPublications.clear();
    	this.editedPublications.addAll(editedPublications);
    }

}
