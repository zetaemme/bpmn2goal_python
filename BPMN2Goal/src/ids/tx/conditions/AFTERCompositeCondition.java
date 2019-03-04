package ids.tx.conditions;

public class AFTERCompositeCondition implements EventCondition
{
	private ConditionalExpression headCondition;
	private ConditionalExpression tailCondition;
		
	public AFTERCompositeCondition(ConditionalExpression headCondition,ConditionalExpression tailCondition)
	{
		this.headCondition = headCondition;
		this.tailCondition = tailCondition;
	}
	
	public ConditionalExpression getHeadCondition()
	{
		return headCondition;
	}
	
	public ConditionalExpression getTailCondition()
	{
		return tailCondition;
	}
	
	public String toString()
	{
		return headCondition.toString() + " AFTER " + tailCondition.toString();
	}
	
	public Boolean isEmpty()
	{
		if(headCondition == null || tailCondition == null)
			return true;
		else
			return (headCondition.isEmpty() && tailCondition.isEmpty()); 
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