package ids.tx.requirements;

import java.util.Iterator;

import ids.tx.bpmn.*;
import ids.tx.conditions.ActorList;

public class HumanInteractionTaskRequirements extends TaskRequirements
{	
	private BPMNHumanInteractionTask humanTask;
	private Iterator<BPMNResource> actorIterator;
	private Boolean continuing = true;
	
	public HumanInteractionTaskRequirements(BPMNHumanInteractionTask humanTask)
	{
		super(humanTask);
		this.humanTask = humanTask;
		this.actorIterator = humanTask.getResources().iterator();
	}
	
	public ActorList produceActorList()
	{
		if(humanTask.hasMultipleActors())
			return produceOneOfMultipleActors();
		else
			return produceFillingActors();
	}
	
	private ActorList produceOneOfMultipleActors()
	{
		ActorList actors = new ActorList(false);
		actors.addActor(actorIterator.next().getName());
		return actors;
	}
	
	private ActorList produceFillingActors()
	{
		ActorList actors = new ActorList(false);
		actors.addAllActors(humanTask.getLaneActors());
		if(actors.isEmpty())
			for(BPMNParticipant participant : humanTask.getInternalParticipants())
				actors.addActor(participant.getName());
		continuing = false;
		return actors;
	}
	
	public Boolean hasNextActor()
	{
		if(humanTask.hasMultipleActors())
			return actorIterator.hasNext();
		return continuing;
	}
}