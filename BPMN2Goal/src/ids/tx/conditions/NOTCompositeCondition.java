package ids.tx.conditions;

public class NOTCompositeCondition implements ConditionalExpression
{
	private ConditionalExpression condition;
		
	public NOTCompositeCondition(ConditionalExpression condition)
	{
		this.condition = condition;
	}
	
	public String toString()
	{	
		if(condition!=null)
		{
			String outputCondition = condition.toString();
			if(!outputCondition.equals(""))
				return "NOT " + outputCondition;
		}
		return "";
	}
	
	
	public String toString(boolean dones)
	{	
		if(condition!=null)
		{
			String outputCondition = condition.toString(dones);
			if(!outputCondition.equals(""))
				return "NOT " + outputCondition;
		}
		return "";
	}

	
	public Boolean isEmpty()
	{
		if(condition == null)
			return true;
		else
			return condition.isEmpty();
	}

	@Override
	public boolean requiresDone() {
		return true;
	}

	@Override
	public String internalString() {
		String outputCondition="";
		if(condition!=null)
		{
			outputCondition = condition.internalString();
			if(!outputCondition.equals(""))
				return "not( " + outputCondition+")";
		}
		return outputCondition;
	}
	
	@Override
	public ConditionalExpression optimize() {
		return this;
	}

}