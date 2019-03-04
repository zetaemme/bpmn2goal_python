package ids.tx.requirements;

import ids.tx.bpmn.*;
import ids.tx.conditions.ANDCompositeCondition;
import ids.tx.conditions.ActorList;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;
import ids.tx.conditions.DonePredicate;
import ids.tx.conditions.NOTCompositeCondition;
import ids.tx.conditions.ORCompositeCondition;
import ids.tx.conditions.Predicate;
import ids.tx.conditions.WHENCondition;
import ids.tx.conditions.WHENDoneCondition;

import java.util.LinkedList;

public abstract class NodeRequirements implements Requirements
{
	private BPMNNode node;

	public NodeRequirements(BPMNNode node)
	{
		this.node = node;
	}	
	
	protected ConditionalExpression produceDataInputCondition(ConditionType type)
	{			
		ORCompositeCondition dataWaitingCondition = new ORCompositeCondition();
		if(type.equals(ConditionType.EVENT))
		{
			if(node.getInputSets().size() == 1)
			{
				LinkedList<BPMNItemElement> sourcesItems = new LinkedList<BPMNItemElement>();
				for(BPMNSequenceFlow sequenceFlow : node.getIncomings())
				{
					BPMNNode sourceNode = sequenceFlow.getSource();
					if(sourceNode!=null)
					{
						LinkedList<BPMNOutputSet> sourceOutputSets = sourceNode.getOutputSets(); 
						if(sourceOutputSets.size() == 1)
							sourcesItems.addAll((sourceNode.collectItemInOutputSets()).get(sourceOutputSets.getFirst()));
					}	
				}
				
				ANDCompositeCondition inputSetWaitingCondition = new ANDCompositeCondition();
				BPMNInputSet inputSet = node.getInputSets().getFirst();
				for(BPMNData dataInput : inputSet.getDataInputs())
				{
					ANDCompositeCondition sourceObjectCondition = new ANDCompositeCondition();
					LinkedList<BPMNItemElement> sourceObjects = node.getResolvedInputAssociations().get(dataInput);
					sourceObjects.removeAll(sourcesItems);
					if(!sourceObjects.isEmpty())
						for(BPMNItemElement sourceObject : sourceObjects)
							sourceObjectCondition.addCondition(new WHENCondition(new Predicate(sourceObject.printSpec())));
					inputSetWaitingCondition.addCondition(sourceObjectCondition);
				}
				dataWaitingCondition.addCondition(inputSetWaitingCondition);
				
			}
			else{
			for(BPMNInputSet inputSet : node.getInputSets())
			{
				ANDCompositeCondition inputSetWaitingCondition = new ANDCompositeCondition();
				for(BPMNData dataInput : inputSet.getDataInputs())
				{
					ANDCompositeCondition sourceObjectCondition = new ANDCompositeCondition();
					LinkedList<BPMNItemElement> sourceObjects = node.getResolvedInputAssociations().get(dataInput);
					System.out.println("sourceObjects-->"+sourceObjects);
					if(!sourceObjects.isEmpty())
						for(BPMNItemElement sourceObject : sourceObjects)
							sourceObjectCondition.addCondition(new WHENCondition(new Predicate(sourceObject.printSpec())));
					else
						sourceObjectCondition.addCondition(new WHENCondition(new Predicate(dataInput.printSpec())));
					inputSetWaitingCondition.addCondition(sourceObjectCondition);
				}
				dataWaitingCondition.addCondition(inputSetWaitingCondition);
			}
			}
		}
		return dataWaitingCondition;
	}
	
	protected ConditionalExpression produceDataOutputCondition(ConditionType type)
	{
		ANDCompositeCondition dataGeneratedCondition = new ANDCompositeCondition();
		for(BPMNOutputSet outputSet : node.getOutputSets())
		{
			ANDCompositeCondition outputSetGeneratedCondition = new ANDCompositeCondition();
			for(BPMNData dataOutput : outputSet.getDataOutputs())
			{
				ANDCompositeCondition sourceObjectCondition = new ANDCompositeCondition();
				LinkedList<BPMNItemElement> targetObjects = node.getResolvedOutputAssociations().get(dataOutput);
				if(!targetObjects.isEmpty())
					for(BPMNItemElement targetObject : targetObjects)
					{
						if(type.equals(ConditionType.EVENT))
							sourceObjectCondition.addCondition(new WHENCondition(new Predicate(targetObject.printSpec())));
						else if(type.equals(ConditionType.STATE))
							sourceObjectCondition.addCondition(new Predicate(targetObject.printSpec()));
					
					}
				else
				{
					if(type.equals(ConditionType.EVENT))
						sourceObjectCondition.addCondition(new WHENCondition(new Predicate(dataOutput.printSpec())));
					else if(type.equals(ConditionType.STATE))
						sourceObjectCondition.addCondition(new Predicate(dataOutput.printSpec()));
				}
				outputSetGeneratedCondition.addCondition(sourceObjectCondition);
			}			
			dataGeneratedCondition.addCondition(outputSetGeneratedCondition);
		}
		return dataGeneratedCondition;
	}
	
	protected ConditionalExpression producePredecessorCondition(ConditionType type)
	{
		ORCompositeCondition predecessorCondition = new ORCompositeCondition();
		for(BPMNSequenceFlow sequenceFlow : node.getIncomings())
		{
			Predicate sequenceFlowPredicate = sequenceFlow.getCondition();
			if(sequenceFlowPredicate!=null)
			{
				ANDCompositeCondition conditionTerm = new ANDCompositeCondition();
				if(type.equals(ConditionType.EVENT))
					conditionTerm.addCondition(new WHENCondition(sequenceFlowPredicate));
				else if(type.equals(ConditionType.STATE))
					conditionTerm.addCondition(sequenceFlowPredicate);
				
				NodeRequirements sourceReq = factory(sequenceFlow.getSource());
				conditionTerm.addCondition(sourceReq.produceBackwardCondition(type, sequenceFlow));
				predecessorCondition.addCondition(conditionTerm);
			}
			else
			{
				NodeRequirements sourceReq = factory(sequenceFlow.getSource());
				predecessorCondition.addCondition(sourceReq.produceBackwardCondition(type, sequenceFlow));
			}
		}
		return predecessorCondition;
	}
	
	protected ConditionalExpression produceSuccessorCondition(ConditionType type)
	{
		ANDCompositeCondition successorCondition = new ANDCompositeCondition();
		LinkedList<ConditionalExpression> conditionalSuccessorConditions = new LinkedList<ConditionalExpression>();
		for(BPMNSequenceFlow sequenceFlow : node.getOutgoings())
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
		for(int i=0 ; i<conditionalSuccessorConditions.size() ; i++)
			allCombinationSuccessorCondition.addAllCondition(generateCombination(conditionalSuccessorConditions,i));
		successorCondition.addCondition(allCombinationSuccessorCondition);
		return successorCondition;
	}
	
	protected ConditionalExpression produceSuccessorUndoneTaskCondition(ConditionType type, BPMNSequenceFlow callProvenience)
	{
		ANDCompositeCondition undoneCondition = new ANDCompositeCondition();
		for(BPMNNode connectedNode : node.collectReachableActiveNodes(callProvenience))
		{
			Boolean cycleFound = false;
			for(BPMNSequenceFlow incoming : node.getIncomings())
			{
				BPMNNode sourceNode = incoming.getSource();
				if(connectedNode.connected(sourceNode))
					cycleFound = true;
			}
			if(cycleFound.equals(false))
			{
				if(connectedNode.getNodeType().equals(BPMNNodeType.INTERMEDIATE_THROW_EVENT) || connectedNode.getNodeType().equals(BPMNNodeType.END_EVENT))
				{
					NodeRequirements req = factory(connectedNode);
					undoneCondition.addCondition(new NOTCompositeCondition(req.produceGeneratedCondition(type)));
				}
				else
				{
					if(type.equals(ConditionType.EVENT))
						undoneCondition.addCondition(new NOTCompositeCondition(new WHENDoneCondition( connectedNode.getName() )));
					else if(type.equals(ConditionType.STATE))
						undoneCondition.addCondition(new NOTCompositeCondition(new DonePredicate(connectedNode.getName() )));
				}
			}
		}
		return undoneCondition.optimize();
	}
	
	protected abstract ConditionalExpression produceWaitingCondition(ConditionType type);
	protected abstract ConditionalExpression produceGeneratedCondition(ConditionType type);
	public abstract ConditionalExpression produceForwardCondition(ConditionType type,BPMNSequenceFlow callProvenience);
	public abstract ConditionalExpression produceBackwardCondition(ConditionType type,BPMNSequenceFlow callProvenience);
	
	public ConditionalExpression produceInputEvent()
	{
		ANDCompositeCondition inputEvent = new ANDCompositeCondition();
		inputEvent.addCondition(produceWaitingCondition(ConditionType.EVENT));
		inputEvent.addCondition(producePredecessorCondition(ConditionType.EVENT));
		return inputEvent;
	}
	
	public ActorList produceActorList()
	{
		return new ActorList(true);
	}
	
	public ConditionalExpression produceOutputState()
	{
		ANDCompositeCondition outputState = new ANDCompositeCondition();
		outputState.addCondition(produceGeneratedCondition(ConditionType.STATE));
		outputState.addCondition(produceSuccessorCondition(ConditionType.STATE));
		return outputState;
	}
	
	public static NodeRequirements factory(BPMNNode node)
	{
		BPMNNodeType typeNode = node.getNodeType();
		if(typeNode.equals(BPMNNodeType.TASK) || typeNode.equals(BPMNNodeType.SERVICE_TASK) || typeNode.equals(BPMNNodeType.SCRIPT_TASK)
				|| typeNode.equals(BPMNNodeType.BUSINESS_RULE_TASK))
			return new TaskRequirements((BPMNTask)node);
		else if(typeNode.equals(BPMNNodeType.HUMAN_INTERACTION_TASK))
			return new HumanInteractionTaskRequirements((BPMNHumanInteractionTask)node);
		else if(typeNode.equals(BPMNNodeType.SEND_TASK))
			return new SendTaskRequirements((BPMNSendTask)node);
		else if(typeNode.equals(BPMNNodeType.RECEIVE_TASK))
			return new ReceiveTaskRequirements((BPMNReceiveTask)node);
		else if(typeNode.equals(BPMNNodeType.CALL_ACTIVITY))
			return new CallActivityRequirements((BPMNCallActivity)node);
		else if(typeNode.equals(BPMNNodeType.SUBPROCESS))
			return new SubprocessRequirements((BPMNSubprocess)node);
		else if(typeNode.equals(BPMNNodeType.INTERMEDIATE_THROW_EVENT))
			return new IntermediateThrowEventRequirements((BPMNIntermediateThrowEvent)node);
		else if(typeNode.equals(BPMNNodeType.INTERMEDIATE_CATCH_EVENT))
			return new IntermediateCatchEventRequirements((BPMNIntermediateCatchEvent)node);
		else if(typeNode.equals(BPMNNodeType.BOUNDARY_EVENT))
			return new BoundaryEventRequirements((BPMNBoundaryEvent)node);
		else if(typeNode.equals(BPMNNodeType.END_EVENT))
			return new EndEventRequirements((BPMNEndEvent)node);
		else if(typeNode.equals(BPMNNodeType.START_EVENT))
			return new StartEventRequirements((BPMNStartEvent)node);
		else if(typeNode.equals(BPMNNodeType.EXCLUSIVE_GATEWAY))
			return new ExclusiveGatewayRequirements((BPMNExclusiveGateway)node);
		else if(typeNode.equals(BPMNNodeType.INCLUSIVE_GATEWAY))
			return new InclusiveGatewayRequirements((BPMNInclusiveGateway)node);
		else if(typeNode.equals(BPMNNodeType.PARALLEL_GATEWAY))
			return new ParallelGatewayRequirements((BPMNParallelGateway)node);
		else if(typeNode.equals(BPMNNodeType.COMPLEX_GATEWAY))
			return new ComplexGatewayRequirements((BPMNComplexGateway)node);
		else if(typeNode.equals(BPMNNodeType.EVENT_BASED_GATEWAY))
			return new EventBasedGatewayRequirements((BPMNEventBasedGateway)node);
		return null;
	}
	
	public static LinkedList<ConditionalExpression> generateCombination(LinkedList<ConditionalExpression> conditions,int subSetCardinality)
	{
		if(subSetCardinality<=conditions.size())
		{
			if(subSetCardinality==1)
				return conditions;
			if(subSetCardinality==conditions.size())
			{
				ANDCompositeCondition ausiliaryCondition = new ANDCompositeCondition(conditions);
				LinkedList<ConditionalExpression> resultingCondition = new LinkedList<ConditionalExpression>();
				resultingCondition.add(ausiliaryCondition);
				return resultingCondition;
			}
			LinkedList<ConditionalExpression> ausiliaryList,withLast,withoutLast;
			ausiliaryList = new LinkedList<ConditionalExpression>(conditions);
			ConditionalExpression Last = ausiliaryList.removeLast();
			withoutLast = generateCombination(ausiliaryList,subSetCardinality-1);
			withLast = generateCombination(ausiliaryList,subSetCardinality);
			congiungeCondition(withoutLast,Last);
			withoutLast.addAll(withLast);
			return withoutLast;
		}
		return null;
	}
	
	public static void congiungeCondition(LinkedList<ConditionalExpression> oldConditionList, ConditionalExpression addedCondition)
	{
		for(int i=0 ; i<oldConditionList.size() ; i++)
		{
			ANDCompositeCondition substitutingTerm = new ANDCompositeCondition();
			substitutingTerm.addCondition(oldConditionList.get(i));
			substitutingTerm.addCondition(addedCondition);
			oldConditionList.set(i, substitutingTerm);
		}
	}
	
	public BPMNNode getNode()
	{
		return node;
	}
}