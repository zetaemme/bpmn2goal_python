package ids.tx.conditions;

public interface ConditionalExpression
{
	public Boolean isEmpty();
	public String toString(boolean print_dones);
	public boolean requiresDone();
	
	public String internalString();
	
	public ConditionalExpression optimize();
}