package ids.model;

import java.util.Date;

public class JobEntity extends Entity {
	private int id;
	
	private String service;
	
	private String label;
	
	private UserEntity user;
	
	private ContextEntity context;
	
	private String agent;

	private Date created;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public ContextEntity getContext() {
		return context;
	}

	public void setContext(ContextEntity context) {
		this.context = context;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	

	
}
