package ids.tx.conditions;

public class MESSAGERECEIVEDCondition implements StateCondition
{
	private String message = "";
	private String sourceActor = "";
	
	public MESSAGERECEIVEDCondition(String message, String sourceActor)
	{
		this.message = message;
		this.sourceActor = sourceActor;
	}
	
	public String toString()
	{
		return "MESSAGE " + message + " RECEIVED FROM " + sourceActor; 
	}
	
	public Boolean isEmpty()
	{
		return (message.equals("") && sourceActor.equals(""));
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
		return "received(" + message + "," + sourceActor+")"; 
	}
	
	@Override
	public ConditionalExpression optimize() {
		return this;
	}

}