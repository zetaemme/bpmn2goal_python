package ids.tx.requirements;

import ids.tx.bpmn.*;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;

public class EndEventRequirements extends EventRequirements
{
	BPMNEndEvent endEvent;
	
	public EndEventRequirements(BPMNEndEvent endEvent)
	{
		super(endEvent);
		this.endEvent = endEvent;
	}
	
	public ConditionalExpression produceBackwardCondition(ConditionType type,BPMNSequenceFlow callProvenience)
	{
		if(endEvent.getEventDefinitions().isEmpty())
			return producePredecessorCondition(type);
		else
			return super.produceBackwardCondition(type, callProvenience);
	}
}