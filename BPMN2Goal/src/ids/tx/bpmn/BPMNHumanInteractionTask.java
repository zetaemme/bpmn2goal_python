package ids.tx.bpmn;

import java.util.HashSet;
import java.util.LinkedList;

import org.w3c.dom.Node;

public class BPMNHumanInteractionTask extends BPMNTask
{
private LinkedList<String> laneActors;
	
	public BPMNHumanInteractionTask(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		nodeType = BPMNNodeType.HUMAN_INTERACTION_TASK;
		
		laneActors = new LinkedList<String>();
	}
	
	public LinkedList<String> getLaneActors()
	{
		return laneActors;
	}
	
	public void addLaneActor(String laneName)
	{
		laneActors.add(laneName);
	}
	
	public HashSet<BPMNResource> collectGeneralActors()
	{
		return new HashSet<BPMNResource>(resources);
	}
	
	public Boolean hasMultipleActors()
	{
		return (!resources.isEmpty());
	}
	
	public void checkActors()
	{
		if(resources.isEmpty() && laneActors.isEmpty() && this.getInternalParticipants().isEmpty())
		{
			BPMNResource newResource = new BPMNResource(this);
			resources.add(newResource);
			warningWriter.println("a BPMN manual or user task should have a performer", this);
		}
	}
}