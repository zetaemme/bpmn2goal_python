package ids.tx.requirements;

import java.util.LinkedList;

import ids.tx.bpmn.*;
import ids.tx.conditions.ANDCompositeCondition;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;
import ids.tx.conditions.ORCompositeCondition;

public abstract class EventRequirements extends NodeRequirements
{
private BPMNEvent event;
	
	public EventRequirements(BPMNEvent event)
	{
		super(event);
		this.event = event;		
	}
	
	protected ConditionalExpression produceEventDefinitionWaitingCondition(ConditionType type)
	{
		LinkedList<ConditionalExpression> eventWaitingConditions = new LinkedList<ConditionalExpression>();		
		for(BPMNEventDefinition eventDefinition : event.getEventDefinitions())
		{
			EventDefinitionRequirements eventDefReq = EventDefinitionRequirements.factory(eventDefinition);
			eventWaitingConditions.add(eventDefReq.produceEventDefinitionWaitingCondition(event.getNodeType(),type));
		}
		if(event.getParallelMultiple() == true)
			return new ANDCompositeCondition(eventWaitingConditions);
		else
			return new ORCompositeCondition(eventWaitingConditions);
	}
	
	protected ConditionalExpression produceWaitingCondition(ConditionType type)
	{
		ANDCompositeCondition waitingCondition = new ANDCompositeCondition();
		waitingCondition.addCondition(produceDataInputCondition(type));	
		waitingCondition.addCondition(produceEventDefinitionWaitingCondition(type));
		return waitingCondition;
	}
	
	protected ConditionalExpression produceEventDefinitionGeneratedCondition(ConditionType type)
	{
		LinkedList<ConditionalExpression> eventGeneratedConditions = new LinkedList<ConditionalExpression>();		
		for(BPMNEventDefinition eventDefinition : event.getEventDefinitions())
		{
			EventDefinitionRequirements eventDefReq = EventDefinitionRequirements.factory(eventDefinition);
			eventGeneratedConditions.add(eventDefReq.produceEventDefinitionGeneratedCondition(event.getNodeType(),type));
		}
		if(event.getParallelMultiple() == true)
			return new ANDCompositeCondition(eventGeneratedConditions);
		else
			return new ORCompositeCondition(eventGeneratedConditions);
		
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
		return produceWaitingCondition(type);
	}

	public ConditionalExpression produceBackwardCondition(ConditionType type,BPMNSequenceFlow callProvenience)
	{
		return produceGeneratedCondition(type);
	}
}