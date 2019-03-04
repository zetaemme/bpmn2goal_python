package ids.tx.conditions;

public class IndividualGoal extends Goal
{	
	public IndividualGoal(String name, ConditionalExpression triggeringCondition,ActorList actors, ConditionalExpression finalState)
	{
		super(name,triggeringCondition,actors,finalState);
	}
	
	public String toString()
	{
		String goal = "GOAL " + name + " : \n";
		goal = goal + getGoalBody();
		return goal;
	}
}