package ids.model;

import java.util.Date;

public class ManualTaskEntity extends Entity {
	private int id;
	
	private String label;
	
	private String description;
	
	private UserEntity user;
	
	private ContextEntity context;
	
	private Date created;
	
	private Date closed;
	
	private int progress;
	
	private String service;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getClosed() {
		return closed;
	}

	public void setClosed(Date closed) {
		this.closed = closed;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public ContextEntity getContext() {
		return context;
	}

	public void setContext(ContextEntity context) {
		this.context = context;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}
	
}
