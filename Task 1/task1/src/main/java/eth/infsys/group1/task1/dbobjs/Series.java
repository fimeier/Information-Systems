package eth.infsys.group1.task1.dbobjs;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Series extends DomainObject {

    private String name;
	private Set<Publication> publications = new HashSet<>();
    
	/**
	 * Should only be used by the database
	 */
    protected Series() { }
    
    public Series(String name) {
    	this.name = name;
    	if (this.name==null){
    		System.out.println("create series/null...");
    		this.name = "null";
		}
    	this.setId(calculate_series_id(name));
    }
    
	//example "series/Lecture Notes in Computer Science"
	static public String calculate_series_id(String name){
		return "series/" + name;
	}


	public String getName() {
    	zooActivateRead();
		return this.name;
	}

    public void setName(String name) {
    	zooActivateWrite();
    	this.name = name;
    }

    public boolean addPublication(Publication e) {
    	zooActivateWrite();
    	return this.publications.add(e);
    }
    
    public boolean addPublications(Collection<? extends Publication> e) {
    	zooActivateWrite();
    	return this.publications.addAll(e);
    }
    
    public Set<Publication> getPublications() {
    	zooActivateRead();
		return Collections.unmodifiableSet(this.publications);
	}
    
    public boolean removePublication(Object e) {
    	zooActivateWrite();
    	return this.publications.remove(e);
    }
    
    public boolean removePublications(Collection<?> e) {
    	zooActivateWrite();
    	return this.publications.removeAll(e);
    }

    public void setPublications(Set<Publication> publications) {
    	zooActivateWrite();
    	this.publications = new HashSet<>(publications);
    }
    
}