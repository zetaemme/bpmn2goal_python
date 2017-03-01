package ids.tx.requirements;

import ids.tx.bpmn.*;
import ids.tx.conditions.ANDCompositeCondition;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;
import ids.tx.conditions.Predicate;
import ids.tx.conditions.WHENCondition;

public class ParallelGatewayRequirements extends NodeRequirements
{
	private BPMNParallelGateway parallelGateway;
	
	public ParallelGatewayRequirements(BPMNParallelGateway parallelGateway)
	{
		super(parallelGateway);
		this.parallelGateway = parallelGateway;		
	}
	
	protected ConditionalExpression producePredecessorCondition(ConditionType type)
	{
		ANDCompositeCondition predecessorCondition = new ANDCompositeCondition();
		for(BPMNSequenceFlow sequenceFlow : parallelGateway.getIncomings())
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
				predecessorCondition.addCondition(conditionTerm);
			}
			else
			{
				NodeRequirements req = factory(sequenceFlow.getSource());
				predecessorCondition.addCondition(req.produceBackwardCondition(type, sequenceFlow));
			}
		}
		return predecessorCondition;
	}
	
	protected ConditionalExpression produceSuccessorCondition(ConditionType type)
	{
		ANDCompositeCondition successorCondition = new ANDCompositeCondition();
		for(BPMNSequenceFlow sequenceFlow : parallelGateway.getOutgoings())
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
				successorCondition.addCondition(conditionTerm);
			}
			else
			{
				NodeRequirements req = factory(sequenceFlow.getTarget());
				successorCondition.addCondition(req.produceForwardCondition(type, sequenceFlow));
			}
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
		return producePredecessorCondition(type);
	}
	
	public ConditionalExpression produceForwardCondition(ConditionType type,BPMNSequenceFlow callProvenience)
	{
		return produceSuccessorCondition(type);
	}
}