package ids.tx.conditions;

public class MESSAGESENTCondition implements StateCondition
{
	private String message = "";
	private String targetActor = "";
	
	public MESSAGESENTCondition(String message, String targetActor)
	{
		this.message = message;
		this.targetActor = targetActor;
	}
	
	public String toString()
	{
		return "MESSAGE " + message + " SENT TO " + targetActor; 
	}
	
	public Boolean isEmpty()
	{
		return (message.equals("") && targetActor.equals(""));
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
		return "sent(" + message + "," + targetActor+")"; 
	}
	
	@Override
	public ConditionalExpression optimize() {
		return this;
	}

}