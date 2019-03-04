package ids.tx.conditions;
public class Predicate implements StateCondition
{
	String predicate = "";
	
	public Predicate(String predicate)
	{
		this.predicate = predicate;
	}
	
	public String toString()
	{
		return predicate;
	}
	
	public Boolean isEmpty()
	{
		return predicate.isEmpty();
	}

	@Override
	public String toString(boolean print_dones) {
		return toString();
	}

	@Override
	public boolean requiresDone() {
		return false;
	}

	@Override
	public String internalString() {
		return "state("+predicate+")";
	}
	
	@Override
	public ConditionalExpression optimize() {
		if (predicate.isEmpty()) return new EmptyCondition();
		return this;
	}

}