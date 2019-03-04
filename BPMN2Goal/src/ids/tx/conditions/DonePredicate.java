package ids.tx.conditions;
public class DonePredicate implements StateCondition
{
	private String activity = "";
	
	public DonePredicate(String activity)
	{
		this.activity = activity;
	}
	
	public String toString()
	{
		return "done("+activity+")";
	}
	
	public Boolean isEmpty()
	{
		return activity.equals("");
	}

	@Override
	public String toString(boolean print_dones) {
		String output="";
		if (print_dones==true) {
			output+=toString();
		}
		return output;
	}

	@Override
	public boolean requiresDone() {
		return true;
	}

	@Override
	public String internalString() {
		return "done("+activity+")";
	}
	
	@Override
	public ConditionalExpression optimize() {
		if (activity.isEmpty()) return new EmptyCondition();
		return this;
	}

}