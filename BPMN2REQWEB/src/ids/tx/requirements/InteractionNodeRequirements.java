package ids.tx.requirements;

import ids.tx.bpmn.*;
import ids.tx.conditions.ActorList;

public class InteractionNodeRequirements
{
	BPMNInteractionNode interactionNode;
	
	public InteractionNodeRequirements(BPMNInteractionNode interactionNode)
	{
		this.interactionNode = interactionNode;
	}
	
	public ActorList getInternalActors()
	{
		ActorList actor = new ActorList(false);
		for(BPMNParticipant participant : interactionNode.getInternalParticipants())
			actor.addActor(participant.getName());
		return actor;
	}
}