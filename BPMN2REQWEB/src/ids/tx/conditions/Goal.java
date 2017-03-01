package ids.tx.conditions;

public abstract class  Goal
{
	protected String name = "";
	protected ConditionalExpression triggeringCondition;
	protected ActorList actors;
	protected ConditionalExpression finalState;
	
	public Goal(String name, ConditionalExpression triggeringCondition,ActorList actors, ConditionalExpression finalState)
	{
		this.name = name;
		this.triggeringCondition = triggeringCondition;
		this.actors = actors;
		this.finalState = finalState;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String geOLDGoalBody()
	{
		String goalDescription = triggeringCondition.toString() + "\n";
		goalDescription = goalDescription + actors.toString() + " SHALL ADDRESS\n";
		goalDescription = goalDescription + finalState.toString() + "\n";
		return goalDescription;
	}

	public String getGoalBody()
	{
		String goalDescription = ""; 
		//goalDescription += triggeringCondition.toString() + "\n";
		goalDescription += "WHEN "+triggeringCondition.optimize().toString() + "\n";
		goalDescription = goalDescription + actors.toString() + " SHALL ADDRESS\n";
		//goalDescription = goalDescription + finalState.toString() + "\n";
		goalDescription = goalDescription + finalState.optimize().toString() + "\n";
		return goalDescription;
	}
	
	public ConditionalExpression getTriggeringCondition()
	{
		return triggeringCondition;
	}
	
	public ActorList getActors()
	{
		return actors;
	}
	
	public ConditionalExpression getFinalState()
	{
		return finalState;
	}
	
	public abstract String toString();
}