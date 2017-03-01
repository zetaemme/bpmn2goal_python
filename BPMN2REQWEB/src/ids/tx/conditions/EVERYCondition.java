package ids.tx.conditions;

public class EVERYCondition implements EventCondition
{
	private String recurringInterval = "";
		
	public EVERYCondition(String recurringInterval)
	{
		this.recurringInterval = recurringInterval;
	}
	
	public String toString()
	{
		return "EVERY " + recurringInterval;
	}
	
	public Boolean isEmpty()
	{
		return recurringInterval.isEmpty();
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