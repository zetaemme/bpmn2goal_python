package ids.model;

public class GoalEntity extends Entity {

	private int id;
	
	private String pack;
	
	private String name;
	
	private String goal_description;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}
	
	public String getGoalDescription() {
		return goal_description;
	}

	public void setGoalDescription(String goal_description) {
		this.goal_description = goal_description;
	}

	
}