package ids.tx.requirements;

import ids.tx.bpmn.*;
import ids.tx.conditions.ANDCompositeCondition;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;
import ids.tx.conditions.NOTCompositeCondition;
import ids.tx.conditions.ORCompositeCondition;
import ids.tx.conditions.Predicate;
import ids.tx.conditions.WHENCondition;

public class ExclusiveGatewayRequirements extends NodeRequirements
{
	private BPMNExclusiveGateway exclusiveGateway;
	
	public ExclusiveGatewayRequirements(BPMNExclusiveGateway exclusiveGateway)
	{
		super(exclusiveGateway);
		this.exclusiveGateway = exclusiveGateway;		
	}
	
	protected ConditionalExpression produceSuccessorCondition(ConditionType type)
	{
		ORCompositeCondition successorCondition = new ORCompositeCondition();
		for(BPMNSequenceFlow sequenceFlow : exclusiveGateway.getOutgoings())
		{
			if(sequenceFlow != exclusiveGateway.getDefaultSequenceFlow())
			{
				Predicate sequenceFlowPredicate = sequenceFlow.getCondition();
				if(sequenceFlowPredicate!=null)
				{
					ANDCompositeCondition conditionTerm = new ANDCompositeCondition();
					if(type.equals(ConditionType.EVENT))
						conditionTerm.addCondition(new WHENCondition(sequenceFlowPredicate));
					else if(type.equals(ConditionType.STATE))
						conditionTerm.addCondition(sequenceFlowPredicate);
					NodeRequirements targetReq = factory(sequenceFlow.getTarget()); 
					conditionTerm.addCondition(targetReq.produceForwardCondition(type, sequenceFlow));
					successorCondition.addCondition(conditionTerm);
				}
				else
				{
					NodeRequirements targetReq = factory(sequenceFlow.getTarget());
					successorCondition.addCondition(targetReq.produceForwardCondition(type, sequenceFlow));
				}
			}
		}
		if(exclusiveGateway.getDefaultSequenceFlow() != null)
		{
			ANDCompositeCondition nonDefaultSequenceFlowCondition = new ANDCompositeCondition();
			for(BPMNSequenceFlow sequenceFlow : exclusiveGateway.getOutgoings())
			{
				Predicate sequenceFlowPredicate = sequenceFlow.getCondition();
				if(sequenceFlowPredicate!=null)
				{
					if(type.equals(ConditionType.EVENT))
						nonDefaultSequenceFlowCondition.addCondition(new NOTCompositeCondition(new WHENCondition(sequenceFlowPredicate)));
					else if(type.equals(ConditionType.STATE))
						nonDefaultSequenceFlowCondition.addCondition(new NOTCompositeCondition(sequenceFlowPredicate));
				}
			}
			NodeRequirements defReq = factory(exclusiveGateway.getDefaultSequenceFlow().getTarget());
			nonDefaultSequenceFlowCondition.addCondition(defReq.produceForwardCondition(type, exclusiveGateway.getDefaultSequenceFlow()));	
			successorCondition.addCondition(nonDefaultSequenceFlowCondition);
		}
		return successorCondition;
	}
	
	protected ConditionalExpression produceWaitingCondition(ConditionType type)
	{
		return null;
	}
	
	protected ConditionalExpression produceGeneratedCondition(ConditionType type)
	{
		return null;
	}
	
	public ConditionalExpression produceBackwardCondition(ConditionType type, BPMNSequenceFlow callProvenience)
	{
		ANDCompositeCondition backwardCondition = new ANDCompositeCondition();
		backwardCondition.addCondition(producePredecessorCondition(type));
		backwardCondition.addCondition(produceSuccessorUndoneTaskCondition(type,callProvenience));
		return backwardCondition;
	}
	
	public ConditionalExpression produceForwardCondition(ConditionType type,BPMNSequenceFlow callProvenience)
	{
		return produceSuccessorCondition(type);
	}
}
