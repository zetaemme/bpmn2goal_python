package ids.tx.requirements;

import ids.tx.bpmn.*;
import ids.tx.conditions.ANDCompositeCondition;
import ids.tx.conditions.ActorList;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;
import ids.tx.conditions.ItemDescription;
import ids.tx.conditions.ORCompositeCondition;
import ids.tx.conditions.WHEREClause;

public class SubprocessRequirements extends ActivityRequirements implements SocialRequirements
{	
	BPMNSubprocess subprocess;
	
	public SubprocessRequirements(BPMNSubprocess subprocess)
	{
		super(subprocess);
		this.subprocess = subprocess;
	}
	
	private ConditionalExpression produceStartingCondition(ConditionType type)
	{
		ORCompositeCondition startFowardCondition = new ORCompositeCondition();
		for(BPMNNode flowNode : subprocess.getFlowNodes())
			if(flowNode.getIncomings().isEmpty() && !flowNode.getNodeType().equals(BPMNNodeType.SUBPROCESS) && !flowNode.getNodeType().equals(BPMNNodeType.BOUNDARY_EVENT) && !flowNode.getNodeType().equals(BPMNNodeType.INTERMEDIATE_CATCH_EVENT))
			{
				NodeRequirements req = factory(flowNode);
				startFowardCondition.addCondition(req.produceForwardCondition(type, null));
			}
		return startFowardCondition;
	}
	
	private ConditionalExpression produceEndingCondition(ConditionType type)
	{
		ORCompositeCondition endBackwardCondition = new ORCompositeCondition();
		for(BPMNNode flowNode : subprocess.getFlowNodes())
			if(flowNode.getOutgoings().isEmpty() && !flowNode.getNodeType().equals(BPMNNodeType.SUBPROCESS) && !flowNode.getNodeType().equals(BPMNNodeType.BOUNDARY_EVENT))
			{
				NodeRequirements req = factory(flowNode);
				endBackwardCondition.addCondition(req.produceBackwardCondition(type, null));
			}
		return endBackwardCondition;
	}
	
	protected ConditionalExpression produceWaitingCondition(ConditionType type)
	{
		ANDCompositeCondition waitingCondition = new ANDCompositeCondition();
		waitingCondition.addCondition(super.produceWaitingCondition(type));
		waitingCondition.addCondition(produceStartingCondition(type));
		return waitingCondition;
	}
	
	protected ConditionalExpression produceStandardGeneratedCondition(ConditionType type)
	{
		ANDCompositeCondition standardCondition = new ANDCompositeCondition();
		standardCondition.addCondition(super.produceStandardGeneratedCondition(type));
		standardCondition.addCondition(produceEndingCondition(type));
		return standardCondition;
	}
	
	public ActorList produceActorList()
	{
		ActorList actors = new ActorList(true);
		
		for(BPMNResource resource : subprocess.getResources())
			actors.addActor(resource.getName());
		
		if(actors.isEmpty())
			actors.addAllActors(subprocess.getLaneActors());
		
		if(actors.isEmpty())
			for(BPMNParticipant participant : subprocess.getInternalParticipants())
				actors.addActor(participant.getName());
		
		for(BPMNParticipant collaboratingParts : subprocess.getCollaboratingParticipants())
			actors.addActor(collaboratingParts.getName());
		
		return actors;
	}
	
	public WHEREClause produceItemDescriptions()
	{
		WHEREClause itemDescriptions = new WHEREClause();
		for(BPMNData dataObject : subprocess.getDataObjects())
			itemDescriptions.addItemDescription(new ItemDescription(dataObject));
		return itemDescriptions;
	}
}