package ids.model;

import java.util.Date;

public class ContextEntity extends Entity {

	private int id;
	
	private Date created;
	
	private boolean complete;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	
	
}
