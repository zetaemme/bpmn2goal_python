package ids.tx.conditions;

public class AFTERSINCECompositeCondition implements EventCondition
{
	private String duration = "";
	private ConditionalExpression tailCondition;
	
	public AFTERSINCECompositeCondition(String duration,ConditionalExpression tailCondition)
	{
		this.duration = duration;
		this.tailCondition = tailCondition;
	}
	
	public String toString()
	{
		return "AFTER " + duration + " SINCE " + tailCondition.toString();
	}
	
	public Boolean isEmpty()
	{
		if(tailCondition == null)
			return true;
		else
			return (duration.isEmpty() && tailCondition.isEmpty());
	}

	@Override
	public String toString(boolean print_dones) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public boolean requiresDone() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String internalString() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ConditionalExpression optimize() {
		return this;
	}

}