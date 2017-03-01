package ids.tx.requirements;

import ids.tx.bpmn.*;
import ids.tx.conditions.ActorList;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;
import ids.tx.conditions.ItemDescription;
import ids.tx.conditions.ORCompositeCondition;
import ids.tx.conditions.Predicate;
import ids.tx.conditions.WHENCondition;
import ids.tx.conditions.WHEREClause;


public class ProcessRequirements implements SocialRequirements
{
BPMNProcess process;
	
	public ProcessRequirements(BPMNProcess process)
	{
		this.process = process;
	}
	
	private ConditionalExpression produceStartingCondition(ConditionType type)
	{
		ORCompositeCondition startingCondition = new ORCompositeCondition(); 
		for(BPMNNode flowNode : process.getFlowNodes())
			if(flowNode.getIncomings().isEmpty() && !flowNode.getNodeType().equals(BPMNNodeType.BOUNDARY_EVENT))
			{
				NodeRequirements startReq = NodeRequirements.factory(flowNode);
				startingCondition.addCondition(startReq.produceForwardCondition(type, null));
			}
		if(startingCondition.isEmpty())
			startingCondition.addCondition(new WHENCondition(new Predicate("started(" + process.getName() + ")")));
		return startingCondition;
	}
	
	private ConditionalExpression produceEndingCondition(ConditionType type)
	{
		ORCompositeCondition endingCondition = new ORCompositeCondition(); 
		for(BPMNNode flowNode : process.getFlowNodes())
			if(flowNode.getOutgoings().isEmpty() && !flowNode.getNodeType().equals(BPMNNodeType.BOUNDARY_EVENT))
			{
				NodeRequirements endReq = NodeRequirements.factory(flowNode);
				endingCondition.addCondition(endReq.produceBackwardCondition(type, null));
			}
		return endingCondition;
	}
	
	public ConditionalExpression produceInputEvent()
	{
		return produceStartingCondition(ConditionType.EVENT);
	}
	
	public ActorList produceActorList()
	{
		ActorList actors = new ActorList(true);
		
		for(BPMNResource resource : process.getResources())
			actors.addActor(resource.getName());
		
		for(BPMNParticipant participant : process.getParticipants())
			actors.addActor(participant.getName());
		
		for(BPMNParticipant collaboratingParts : process.getCollaboratingParticipants())
			actors.addActor(collaboratingParts.getName());
		
		return actors;
	}
	
	public ConditionalExpression produceOutputState()
	{
		return produceEndingCondition(ConditionType.STATE);
	}
	
	public WHEREClause produceItemDescriptions()
	{
		WHEREClause itemDescriptions = new WHEREClause();
		for(BPMNData dataObject : process.getDataObjects())
			itemDescriptions.addItemDescription(new ItemDescription(dataObject));
		return itemDescriptions;
	}
}