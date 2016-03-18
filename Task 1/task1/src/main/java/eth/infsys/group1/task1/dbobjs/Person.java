package eth.infsys.group1.task1.dbobjs;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Person extends DomainObject {

	/**
	 * Should only be used by the database
	 */
	protected Person() { }
	
	public Person(String name) {
		this.name = name;
		this.setId(name);
	}
	
    private String name;
	private Set<Publication> authoredPublications = new HashSet<>();
	private Set<Publication> editedPublications = new HashSet<>();

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
    	zooActivateWrite();
    	this.authoredPublications = new HashSet<>(authoredPublications);
    }
    public boolean addAuthoredPublications(Publication e) {
    	zooActivateWrite();
    	return this.authoredPublications.add(e);
    }

    public Set<Publication> getEditedPublications() {
    	zooActivateRead();
    	return Collections.unmodifiableSet(this.editedPublications);
    }

    public void setEditedPublications(Set<Publication> editedPublications) {
    	zooActivateWrite();
    	this.editedPublications = new HashSet<>(editedPublications);
    }
    
    public boolean addEditedPublications(Publication e) {
    	zooActivateWrite();
    	return this.editedPublications.add(e);
    }

}
