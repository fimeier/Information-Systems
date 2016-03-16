package eth.infsys.group1.task1.dbobjs;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Conference extends DomainObject {
	
	private String name;
	private Set<ConferenceEdition> editions = new HashSet<>();
	
	/**
	 * Should only be used by the database
	 */
	protected Conference() { }

	public Conference(String name) {
		this.name = name;
	}

	public String getName() {
    	zooActivateRead();
    	return this.name;
    }

    public void setName(String name) {
    	zooActivateWrite();
    	this.name = name;
    }
    
    public boolean addEdition(ConferenceEdition e) {
    	zooActivateWrite();
    	return this.editions.add(e);
    }
    
    public boolean addEditions(Collection<? extends ConferenceEdition> e) {
    	zooActivateWrite();
    	return this.editions.addAll(e);
    }

    public Set<ConferenceEdition> getEditions() {
    	zooActivateRead();
    	return Collections.unmodifiableSet(this.editions);
    }
    
    public boolean removeEdition(Object e) {
    	zooActivateWrite();
    	return this.editions.remove(e);
    }
    
    public boolean removeEditions(Collection<?> e) {
    	zooActivateWrite();
    	return this.editions.removeAll(e);
    }

    public void setEditions(Set<ConferenceEdition> editions) {
    	zooActivateWrite();
    	this.editions = new HashSet<>(editions);
    }
    
}