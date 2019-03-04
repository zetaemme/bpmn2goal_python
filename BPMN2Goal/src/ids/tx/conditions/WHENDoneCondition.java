package ids.tx.conditions;

public class WHENDoneCondition implements EventCondition
{
	private String activity;
		
	public WHENDoneCondition(String activity)
	{
		this.activity = activity;
	}
	
	public String toString()
	{
		if(!activity.equals(""))
			return "done("+activity+")";
		return "";
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
		if(!activity.equals(""))
			return "done("+activity+")";
		return "";
	}

	@Override
	public ConditionalExpression optimize() {
		return this;
	}

}