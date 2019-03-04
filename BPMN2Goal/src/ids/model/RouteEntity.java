package ids.model;

public class RouteEntity extends Entity {
	private int id;
	
	private String service_path;
	
	private String agent;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getService_path() {
		return service_path;
	}

	public void setService_path(String service_path) {
		this.service_path = service_path;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}
	
	
}
