package ids.tx.conditions;
public class EmptyCondition implements ConditionalExpression
{

	@Override
	public Boolean isEmpty() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String toString(boolean print_dones) {
		// TODO Auto-generated method stub
		return null;
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