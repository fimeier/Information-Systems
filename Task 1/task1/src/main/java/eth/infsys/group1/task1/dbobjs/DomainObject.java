package eth.infsys.group1.task1.dbobjs;

import ch.ethz.globis.isk.domain.DomainObject;

import org.zoodb.api.impl.ZooPC;

/**
 * Implementation of the {@link ch.ethz.globis.isk.domain.DomainObject} interface for ZooDB.
 */
public abstract class DomainObject extends ZooPC implements DomainObject {

	private String id;
		
	public DomainObject() { }
	
	public DomainObject(String id) {
		this.id = id;
	}
	
    public String getId() {
        zooActivateRead();
    	return this.id;
    }

    public void setId(String id) {
    	zooActivateWrite();
    	this.id = id;
    }
    
}
