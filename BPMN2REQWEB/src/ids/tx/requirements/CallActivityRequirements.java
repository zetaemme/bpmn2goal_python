package ids.tx.requirements;

import ids.tx.bpmn.*;
import ids.tx.conditions.ANDCompositeCondition;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;
import ids.tx.conditions.Predicate;
import ids.tx.conditions.WHENCondition;

public class CallActivityRequirements extends ActivityRequirements
{	
	BPMNCallActivity callActivity;
	
	public CallActivityRequirements(BPMNCallActivity callActivity)
	{
		super(callActivity);
		this.callActivity = callActivity;
	}
	
	protected ConditionalExpression produceStandardGeneratedCondition(ConditionType type)
	{
		ANDCompositeCondition standardCondition = new ANDCompositeCondition();
		standardCondition.addCondition(super.produceStandardGeneratedCondition(type));
		if(type.equals(ConditionType.EVENT))
			if(callActivity.getCalledElement()!=null)
				standardCondition.addCondition(new WHENCondition(new Predicate("thrown(" + callActivity.getCalledElement().getName() + "call)")));
		else if(type.equals(ConditionType.STATE))
			if(callActivity.getCalledElement()!=null)
				standardCondition.addCondition(new Predicate("thrown(" + callActivity.getCalledElement().getName() + "call)"));
		return standardCondition;
	}
}