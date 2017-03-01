package ids.tx.requirements;

import java.util.LinkedList;

import ids.tx.bpmn.*;
import ids.tx.conditions.ANDCompositeCondition;
import ids.tx.conditions.ActorList;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;
import ids.tx.conditions.DonePredicate;
import ids.tx.conditions.EmptyCondition;
import ids.tx.conditions.MESSAGERECEIVEDCondition;
import ids.tx.conditions.MESSAGESENTCondition;
import ids.tx.conditions.NOTCompositeCondition;
import ids.tx.conditions.ORCompositeCondition;
import ids.tx.conditions.Predicate;
import ids.tx.conditions.WHENCondition;
import ids.tx.conditions.WHENDoneCondition;

public abstract class ActivityRequirements extends NodeRequirements
{
private BPMNActivity activity;
	
	public ActivityRequirements(BPMNActivity activity)
	{
		super(activity);
		this.activity = activity;		
	}
	
	protected ConditionalExpression produceMessageReceivedCondition(ConditionType type)
	{
		ORCompositeCondition messageReceivedCondition = new ORCompositeCondition();
		if(type.equals(ConditionType.EVENT))
		{
			for(BPMNMessageFlow messageFlow : activity.getIncomingMessageFlows())
			{
				InteractionNodeRequirements sourceReq = new InteractionNodeRequirements(messageFlow.getSource());
				messageReceivedCondition.addCondition(new WHENCondition(new MESSAGERECEIVEDCondition(messageFlow.getMessage().printSpec(),sourceReq.getInternalActors().toString())));
			}
		}
		return messageReceivedCondition;
	}
	
	protected ConditionalExpression produceMessageSentCondition(ConditionType type)
	{
		ANDCompositeCondition messageSentCondition = new ANDCompositeCondition();
		if(type.equals(ConditionType.EVENT))
		{			
			for(BPMNMessageFlow messageFlow : activity.getOutgoingMessageFlows())
			{
				InteractionNodeRequirements targetReq = new InteractionNodeRequirements(messageFlow.getTarget());
				messageSentCondition.addCondition(new WHENCondition(new MESSAGESENTCondition(messageFlow.getMessage().printSpec(),targetReq.getInternalActors().toString())));
			}
		}
			
		if(type.equals(ConditionType.STATE))
		{
			for(BPMNMessageFlow messageFlow : activity.getOutgoingMessageFlows())
			{
				InteractionNodeRequirements targetReq = new InteractionNodeRequirements(messageFlow.getTarget());
				messageSentCondition.addCondition(new MESSAGESENTCondition(messageFlow.getMessage().printSpec(),targetReq.getInternalActors().toString()));
			}
		}
	
		return messageSentCondition;
	}
	
	protected ConditionalExpression produceSuccessorCondition(ConditionType type)
	{
		ANDCompositeCondition successorCondition = new ANDCompositeCondition();
		LinkedList<ConditionalExpression> conditionalSuccessorConditions = new LinkedList<ConditionalExpression>();
		for(BPMNSequenceFlow sequenceFlow : activity.getOutgoings())
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
		ORCompositeCondition allCombinationSuccessorCondition = new ORCompositeCondition();
		for(int i=1 ; i<=conditionalSuccessorConditions.size() ; i++)
			allCombinationSuccessorCondition.addAllCondition(generateCombination(conditionalSuccessorConditions,i));
		
		if(activity.getDefaultSequenceFlow() != null)
		{
			ANDCompositeCondition nonDefaultSequenceFlowCondition = new ANDCompositeCondition();
			for(BPMNSequenceFlow sequenceFlow : activity.getOutgoings())
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
			NodeRequirements defReq = factory(activity.getDefaultSequenceFlow().getTarget());
			nonDefaultSequenceFlowCondition.addCondition(defReq.produceForwardCondition(type, activity.getDefaultSequenceFlow()));	
			allCombinationSuccessorCondition.addCondition(nonDefaultSequenceFlowCondition);
		}
		
		successorCondition.addCondition(allCombinationSuccessorCondition);
		return successorCondition;
	}
	
	protected ConditionalExpression produceWaitingCondition(ConditionType type)
	{
		ANDCompositeCondition waitingCondition = new ANDCompositeCondition();
		waitingCondition.addCondition(produceDataInputCondition(type));
		waitingCondition.addCondition(produceMessageReceivedCondition(type));
		return waitingCondition;
	}
	
	protected ConditionalExpression produceStandardGeneratedCondition(ConditionType type)
	{		
		ANDCompositeCondition standardGeneratedCondition = new ANDCompositeCondition();
		standardGeneratedCondition.addCondition(produceDataOutputCondition(type));
		ConditionalExpression messageSentCondition = (ConditionalExpression) produceMessageSentCondition(type);
		standardGeneratedCondition.addCondition(messageSentCondition);

		if(standardGeneratedCondition.isEmpty())	{
			if(type.equals(ConditionType.EVENT))
				standardGeneratedCondition.addCondition(new WHENDoneCondition(activity.getName() ));
			else if(type.equals(ConditionType.STATE))
				standardGeneratedCondition.addCondition(new DonePredicate( activity.getName() ));
		}
		return standardGeneratedCondition;
	}
	
	protected ConditionalExpression produceGeneratedCondition(ConditionType type)
	{
		if(!activity.getSingleInterruptingEvents().isEmpty())
		{
			ORCompositeCondition generatedCondition = new ORCompositeCondition();
			ANDCompositeCondition standardCondition = new ANDCompositeCondition();
			standardCondition.addCondition(produceStandardGeneratedCondition(type));
			
			ANDCompositeCondition terminationCondition = null;
			ORCompositeCondition terminationTriggeringCondition = null;
			ANDCompositeCondition failureCondition = null;
			ANDCompositeCondition compensationCondition = null;
			
			for(BPMNEvent boundary : activity.getSingleInterruptingEvents().values())
			{
				for(BPMNEventDefinition boundaryEventDefinition : boundary.getEventDefinitions())
				{
					if(!boundaryEventDefinition.getEventDefinitionType().equals(BPMNEventDefinitionType.TIMER))
					{
						if(type.equals(ConditionType.STATE))
						{
							EventDefinitionRequirements boundaryEventDefReq = EventDefinitionRequirements.factory(boundaryEventDefinition);
							ConditionalExpression eventWaitingCondition = boundaryEventDefReq.produceEventDefinitionWaitingCondition(BPMNNodeType.BOUNDARY_EVENT, ConditionType.STATE);
							if(boundaryEventDefinition.getEventDefinitionType().equals(BPMNEventDefinitionType.ERROR))
							{
								if(failureCondition == null)
								{
									failureCondition = new ANDCompositeCondition();
									failureCondition.addCondition(new Predicate("failed(" + activity.getName() + ")"));
								}
								failureCondition.addCondition(eventWaitingCondition);
							}
							else if(boundaryEventDefinition.getEventDefinitionType().equals(BPMNEventDefinitionType.COMPENSATE))
							{
								if(compensationCondition == null)
								{
									compensationCondition = new ANDCompositeCondition();
									compensationCondition.addCondition(new Predicate("compensated(" + activity.getName() + ")"));
								}
								compensationCondition.addCondition(eventWaitingCondition);
							}
							else
							{
								if(terminationCondition == null)
								{
									terminationCondition = new ANDCompositeCondition();
									terminationTriggeringCondition = new ORCompositeCondition();
									terminationCondition.addCondition(new Predicate("terminated(" + activity.getName() + ")"));
									terminationCondition.addCondition(terminationTriggeringCondition);
								}
								terminationTriggeringCondition.addCondition(eventWaitingCondition);
							}
						}
						EventDefinitionRequirements boundaryEventDefReq = EventDefinitionRequirements.factory(boundaryEventDefinition);
						standardCondition.addCondition(new NOTCompositeCondition(boundaryEventDefReq.produceEventDefinitionWaitingCondition(BPMNNodeType.BOUNDARY_EVENT, type)));
					}
				}
			}
			generatedCondition.addCondition(standardCondition);
			if(failureCondition != null)
				generatedCondition.addCondition(failureCondition);
			if(compensationCondition != null)
				generatedCondition.addCondition(compensationCondition);
			if(terminationCondition != null)
				generatedCondition.addCondition(terminationCondition);
			return generatedCondition;
		}
		return produceStandardGeneratedCondition(type);
	}
	
	public ConditionalExpression produceBackwardCondition(ConditionType type, BPMNSequenceFlow callProvenience)
	{
		return produceGeneratedCondition(type);
	}

	public ConditionalExpression produceForwardCondition(ConditionType type,BPMNSequenceFlow callProvenience)
	{
		return produceWaitingCondition(type);
	}
	
	public ConditionalExpression produceInputEvent()
	{
		BPMNStandardLoopCharacteristic loop = activity.getLoopCharacteristic();
		if(loop!=null)
		{
			if(loop.getTestBefore().equals(true))
			{
				ANDCompositeCondition inputEvent = new ANDCompositeCondition();
				if(!loop.getLoopCondition().equals(""))
					inputEvent.addCondition(new WHENCondition(new Predicate(loop.getLoopCondition())));
				if(!loop.getLoopMaximum().equals(""))
					inputEvent.addCondition(new WHENCondition(new Predicate("loopCounter<" + loop.getLoopMaximum())));
				inputEvent.addCondition(producePredecessorCondition(ConditionType.EVENT));
				inputEvent.addCondition(produceWaitingCondition(ConditionType.EVENT));
				return inputEvent;
			}
		}
		ANDCompositeCondition inputEvent = new ANDCompositeCondition();
		inputEvent.addCondition(produceWaitingCondition(ConditionType.EVENT));
		inputEvent.addCondition(producePredecessorCondition(ConditionType.EVENT));
		if(inputEvent.isEmpty()) //&& activity.getIncomings().isEmpty()
			if(activity.getIsForCompensation().equals(false))
				inputEvent.addCondition(new WHENCondition(new Predicate("started(" + activity.getContainingElement().getName() + ")")));
			else
				inputEvent.addCondition(new WHENCondition(new Predicate("caught(compensation)")));
		return inputEvent;
	}
	
	public ActorList produceActorList()
	{
		return new ActorList(true);
	}
	
	public ConditionalExpression produceOutputState()
	{
		BPMNStandardLoopCharacteristic loop = activity.getLoopCharacteristic();
		if(loop!=null)
		{
			if(loop.getTestBefore().equals(true))
			{
				ORCompositeCondition outputState = new ORCompositeCondition();
				ORCompositeCondition exitloopConditon = new ORCompositeCondition();
				ANDCompositeCondition subCondition1 = new ANDCompositeCondition();
				ANDCompositeCondition subCondition2 = new ANDCompositeCondition();
				if(!loop.getLoopCondition().equals(""))
				{
					exitloopConditon.addCondition(new Predicate("FALSE(" + loop.getLoopCondition() + ")"));
					subCondition2.addCondition(new Predicate(loop.getLoopCondition()));
				}
				if(!loop.getLoopMaximum().equals(""))
				{
					exitloopConditon.addCondition(new Predicate("loopCounter>=" + loop.getLoopMaximum()));
					subCondition2.addCondition(new Predicate("loopCounter<" + loop.getLoopMaximum()));
				}
				subCondition1.addCondition(exitloopConditon);
				subCondition1.addCondition(produceGeneratedCondition(ConditionType.STATE));
				subCondition1.addCondition(produceSuccessorCondition(ConditionType.STATE));
				subCondition2.addCondition(produceSuccessorCondition(ConditionType.STATE));
				outputState.addCondition(subCondition1);
				outputState.addCondition(subCondition2);
				return outputState;
			}
			if(loop.getTestBefore().equals(false))
			{
				ANDCompositeCondition outputState = new ANDCompositeCondition();
				ORCompositeCondition exitloopConditon = new ORCompositeCondition();
				if(!loop.getLoopCondition().equals(""))
					exitloopConditon.addCondition(new Predicate("FALSE(" + loop.getLoopCondition() + ")"));
				if(!loop.getLoopMaximum().equals(""))
					exitloopConditon.addCondition(new Predicate("loopCounter>=" + loop.getLoopMaximum()));
				outputState.addCondition(exitloopConditon);
				outputState.addCondition(produceSuccessorCondition(ConditionType.STATE));
				outputState.addCondition(produceGeneratedCondition(ConditionType.STATE));
				return outputState;
			}
		}
		ANDCompositeCondition outputState = new ANDCompositeCondition();
		outputState.addCondition(produceGeneratedCondition(ConditionType.STATE));
		outputState.addCondition(produceSuccessorCondition(ConditionType.STATE));
		return outputState;
	}
}