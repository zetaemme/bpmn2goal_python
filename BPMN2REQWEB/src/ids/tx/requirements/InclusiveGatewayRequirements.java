package ids.tx.requirements;

import java.util.LinkedList;

import ids.tx.bpmn.*;
import ids.tx.conditions.ANDCompositeCondition;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;
import ids.tx.conditions.NOTCompositeCondition;
import ids.tx.conditions.ORCompositeCondition;
import ids.tx.conditions.Predicate;
import ids.tx.conditions.WHENCondition;

public class InclusiveGatewayRequirements extends NodeRequirements
{
private BPMNInclusiveGateway inclusiveGateway;
	
	public InclusiveGatewayRequirements(BPMNInclusiveGateway inclusiveGateway)
	{
		super(inclusiveGateway);
		this.inclusiveGateway = inclusiveGateway;		
	}
	
	protected ConditionalExpression produceSuccessorCondition(ConditionType type)
	{
		ANDCompositeCondition successorCondition = new ANDCompositeCondition();
		LinkedList<ConditionalExpression> conditionalSuccessorConditions = new LinkedList<ConditionalExpression>();
		for(BPMNSequenceFlow sequenceFlow : inclusiveGateway.getOutgoings())
		{
			if(sequenceFlow != inclusiveGateway.getDefaultSequenceFlow())
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
					conditionalSuccessorConditions.add(conditionTerm);
				}
				else
				{
					NodeRequirements targetReq = factory(sequenceFlow.getTarget());
					successorCondition.addCondition(targetReq.produceForwardCondition(type, sequenceFlow));
				}
			}
		}
		ORCompositeCondition allCombinationSuccessorCondition = new ORCompositeCondition();
		for(int i=1 ; i<=conditionalSuccessorConditions.size() ; i++)
			allCombinationSuccessorCondition.addAllCondition(generateCombination(conditionalSuccessorConditions,i));
		
		if(inclusiveGateway.getDefaultSequenceFlow() != null)
		{
			ANDCompositeCondition nonDefaultSequenceFlowCondition = new ANDCompositeCondition();
			for(BPMNSequenceFlow sequenceFlow : inclusiveGateway.getOutgoings())
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
			NodeRequirements defReq = factory(inclusiveGateway.getDefaultSequenceFlow().getTarget()); 
			nonDefaultSequenceFlowCondition.addCondition(defReq.produceForwardCondition(type, inclusiveGateway.getDefaultSequenceFlow()));	
			allCombinationSuccessorCondition.addCondition(nonDefaultSequenceFlowCondition);
		}
		
		successorCondition.addCondition(allCombinationSuccessorCondition);
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