package eth.infsys.group1.task1.dbobjs;

import ch.ethz.globis.isk.domain.*;

import java.util.Collections;
import java.util.Set;

/**
 * Implementation of the {@link ch.ethz.globis.isk.domain.Conference} interface for ZooDB.
 */
public class T1Conference extends T1DomainObject implements Conference {
	
	private String name;
	private Set<ConferenceEdition> editions;
	
	public T1Conference() { }

	public String getName() {
    	zooActivateRead();
    	return this.name;
    }

    public void setName(String name) {
    	zooActivateWrite();
    	this.name = name;
    }

    public Set<ConferenceEdition> getEditions() {
    	zooActivateRead();
    	return Collections.unmodifiableSet(this.editions);
    }

    public void setEditions(Set<ConferenceEdition> editions) {
    	zooActivateWrite();
    	this.editions.clear();
    	this.editions.addAll(editions);
    }
    
}