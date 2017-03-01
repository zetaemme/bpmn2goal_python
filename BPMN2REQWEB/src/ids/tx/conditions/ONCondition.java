package ids.tx.conditions;

public class ONCondition implements EventCondition
{
	private String date = "";
		
	public ONCondition(String date)
	{
		this.date = date;
	}
	
	public String toString()
	{
			return "ON " + date;	
	}
	
	public Boolean isEmpty()
	{
		return date.isEmpty();
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