package eth.infsys.group1.task1.dbobjs;

import ch.ethz.globis.isk.domain.*;

import java.util.Collections;
import java.util.Set;

/**
 * Implementation of the {@link ch.ethz.globis.isk.domain.Series} interface for ZooDB.
 */
public class T1Series extends T1DomainObject implements Series {

    private String name;
	private Set<Publication> publications;
    
    public T1Series() { }

	public String getName() {
    	zooActivateRead();
		return this.name;
	}

    public void setName(String name) {
    	zooActivateWrite();
    	this.name = name;
    }

    public Set<Publication> getPublications() {
    	zooActivateRead();
		return Collections.unmodifiableSet(this.publications);
	}

    public void setPublications(Set<Publication> publications) {
    	zooActivateWrite();
    	this.publications.clear();
    	this.publications.addAll(publications);
    }
    
}