package ids.tx.requirements;

import ids.tx.bpmn.*;
import ids.tx.conditions.ANDCompositeCondition;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;
import ids.tx.conditions.Predicate;
import ids.tx.conditions.WHENCondition;

public class StartEventRequirements extends EventRequirements
{
	BPMNStartEvent startEvent;
	
	public StartEventRequirements(BPMNStartEvent startEvent)
	{
		super(startEvent);
		this.startEvent = startEvent;
	}
	
	protected ConditionalExpression produceGeneratedCondition(ConditionType type)
	{
		ANDCompositeCondition generatedCondition = new ANDCompositeCondition();
		generatedCondition.addCondition(produceDataOutputCondition(type));
		generatedCondition.addCondition(produceEventDefinitionGeneratedCondition(type));
		return generatedCondition;
	}
	
	public ConditionalExpression produceForwardCondition(ConditionType type,BPMNSequenceFlow callProvenience)
	{
		if(startEvent.getEventDefinitions().isEmpty())
			return produceSuccessorCondition(type);
		else
			return super.produceForwardCondition(type, callProvenience);
	}
}