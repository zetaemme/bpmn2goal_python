package ids.tx.requirements;

import java.util.LinkedList;

import ids.tx.bpmn.*;
import ids.tx.conditions.ANDCompositeCondition;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;
import ids.tx.conditions.ORCompositeCondition;
import ids.tx.conditions.Predicate;
import ids.tx.conditions.WHENCondition;

public class EventBasedGatewayRequirements extends NodeRequirements
{
	private BPMNEventBasedGateway eventBasedGateway;
	
	public EventBasedGatewayRequirements(BPMNEventBasedGateway eventBasedGateway)
	{
		super(eventBasedGateway);
		this.eventBasedGateway = eventBasedGateway;		
	}
	
	protected ConditionalExpression producePredecessorCondition(ConditionType type)
	{
		LinkedList<ConditionalExpression> predecessorBackwardConditions = new LinkedList<ConditionalExpression>();
		
		for(BPMNSequenceFlow sequenceFlow : eventBasedGateway.getIncomings())
		{
			Predicate sequenceFlowPredicate = sequenceFlow.getCondition();
			if(sequenceFlowPredicate!=null)
			{
				ANDCompositeCondition conditionTerm = new ANDCompositeCondition();
				if(type.equals(ConditionType.EVENT))
					conditionTerm.addCondition(new WHENCondition(sequenceFlowPredicate));
				else if(type.equals(ConditionType.STATE))
					conditionTerm.addCondition(sequenceFlowPredicate);
				NodeRequirements req = factory(sequenceFlow.getSource());
				conditionTerm.addCondition(req.produceBackwardCondition(type, sequenceFlow));
				predecessorBackwardConditions.add(conditionTerm);
			}
			else
			{
				NodeRequirements req = factory(sequenceFlow.getSource());
				predecessorBackwardConditions.add(req.produceBackwardCondition(type, sequenceFlow));
			}
		}
		return new ORCompositeCondition(predecessorBackwardConditions);
	}
	
	protected ConditionalExpression produceSuccessorCondition(ConditionType type)
	{
		LinkedList<ConditionalExpression> SuccessorForwardConditions = new LinkedList<ConditionalExpression>();
		for(BPMNSequenceFlow sequenceFlow : eventBasedGateway.getOutgoings())
		{
			Predicate sequenceFlowPredicate = sequenceFlow.getCondition();
			if(sequenceFlowPredicate!=null)
			{
				ANDCompositeCondition conditionTerm = new ANDCompositeCondition();
				if(type.equals(ConditionType.EVENT))
					conditionTerm.addCondition(new WHENCondition(sequenceFlowPredicate));
				else if(type.equals(ConditionType.STATE))
					conditionTerm.addCondition(sequenceFlowPredicate);
				NodeRequirements req = factory(sequenceFlow.getTarget());
				conditionTerm.addCondition(req.produceForwardCondition(type, sequenceFlow));
				SuccessorForwardConditions.add(conditionTerm);
			}
			else
			{
				NodeRequirements req = factory(sequenceFlow.getTarget());
				SuccessorForwardConditions.add(req.produceForwardCondition(type, sequenceFlow));
			}
		}
		
		if(eventBasedGateway.getEventBasedGatewayType().equals(BPMNEventBasedGatewayType.EXCLUSIVE))
			return new ORCompositeCondition(SuccessorForwardConditions);
		else
			return new ANDCompositeCondition(SuccessorForwardConditions);
	}
	
	protected ConditionalExpression produceWaitingCondition(ConditionType type)
	{
		return null;
	}
	
	protected ConditionalExpression produceGeneratedCondition(ConditionType type)
	{
		if(eventBasedGateway.getIncomings().isEmpty())
		{
			if(type.equals(ConditionType.EVENT))
				return new WHENCondition(new Predicate("started(" + eventBasedGateway.getContainingElement().getName() + ")"));
			else if(type.equals(ConditionType.STATE))
				return new Predicate("started(" + eventBasedGateway.getContainingElement().getName() + ")");
		}
		return null;
	}
	
	public ConditionalExpression produceBackwardCondition(ConditionType type, BPMNSequenceFlow callProvenience)
	{
		ANDCompositeCondition backwardCondition = new ANDCompositeCondition();
		backwardCondition.addCondition(producePredecessorCondition(type));
		backwardCondition.addCondition(produceGeneratedCondition(type));
		if(eventBasedGateway.getEventBasedGatewayType().equals(BPMNEventBasedGatewayType.EXCLUSIVE))
			backwardCondition.addCondition(produceSuccessorUndoneTaskCondition(type,callProvenience));
		return backwardCondition;
	}
	
	public ConditionalExpression produceForwardCondition(ConditionType type,BPMNSequenceFlow callProvenience)
	{
		return produceSuccessorCondition(type);
	}
}