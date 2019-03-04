package ids.tx.conditions;

public class WHENCondition implements EventCondition
{
	private StateCondition state;
		
	public WHENCondition(StateCondition state)
	{
		this.state = state;
	}
	
	public String toString()
	{
		String stateCondition = state.toString();
		if(!stateCondition.equals(""))
			return state.toString();
		return "";
	}
	
	public Boolean isEmpty()
	{
		return state.isEmpty();
	}

	@Override
	public String toString(boolean print_dones) {
		return state.toString(print_dones);
	}

	@Override
	public boolean requiresDone() {
		return state.requiresDone();
	}

	@Override
	public String internalString() {
		return "when("+state.internalString()+")";
	}
	
	@Override
	public ConditionalExpression optimize() {
		return this;
	}

}