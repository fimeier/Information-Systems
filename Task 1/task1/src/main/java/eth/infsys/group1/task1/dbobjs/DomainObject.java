package eth.infsys.group1.task1.dbobjs;

import org.zoodb.api.impl.ZooPC;

public abstract class DomainObject extends ZooPC {

	private String id;
	
	public DomainObject() { }
	
    public String getId() {
        zooActivateRead();
    	return this.id;
    }

    public void setId(String id) {
    	zooActivateWrite();
    	this.id = id;
    }
    
}
