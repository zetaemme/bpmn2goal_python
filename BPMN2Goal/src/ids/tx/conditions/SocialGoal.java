package ids.tx.conditions;

public class SocialGoal extends Goal
{
	private WHEREClause itemDescriptions;
	
	public SocialGoal(String name, ConditionalExpression triggeringCondition,ActorList actors, ConditionalExpression finalState, WHEREClause itemDescriptions)
	{
		super(name,triggeringCondition,actors,finalState);
		this.itemDescriptions = itemDescriptions;
	}
	
	public String toString()
	{
		String goal = "SOCIAL GOAL " + name + " :\n";
		goal = goal + getGoalBody();
		goal = goal + itemDescriptions.toString() + "\n";
		return goal;
	}
}