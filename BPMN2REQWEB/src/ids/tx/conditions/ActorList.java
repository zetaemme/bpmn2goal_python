package ids.tx.conditions;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * ActorList provides a simple structured type to collect actors name for a specific BPMNActivity.
 * ActorList constructor allow you to instantiate an empty ActorList or an ActorList containing names
 * in a specified collection (it must have super-type Collection<String>). In both cases a Boolean 
 * parameter systemParticipation is needed that indicate the involvement of the whole system in the 
 * related BPMNActivity.     
 */

public class ActorList
{
	private LinkedList<String> actors;
	Boolean systemParticipation = false;
	
	public ActorList(Boolean systemParticipation)
	{
		actors = new LinkedList<String>();
		this.systemParticipation = systemParticipation;
	}
	
	public ActorList(Collection<String> actors,Boolean systemParticipation)
	{
		this.actors = new LinkedList<String>(actors);
		this.systemParticipation = systemParticipation;
	}
	
	public void addActor(String actor)
	{
		if(actor!=null)
			actors.add(actor);
	}
	
	public void addAllActors(Collection<String> moreActors)
	{
		if(!moreActors.isEmpty())
			actors.addAll(moreActors);
	}
	
	public LinkedList<String> getActors()
	{
		return actors;
	}
	
	public Boolean getSystemParticipation()
	{
		return systemParticipation;
	}
	
	public Boolean isEmpty()
	{
		return actors.isEmpty();
	}
	
	public String toString()
	{
		if(!actors.isEmpty())
		{
			Iterator<String> iterator = actors.iterator();
			String actorList = "THE " + iterator.next() + " ROLE";
			while(iterator.hasNext())
				actorList = actorList + " AND THE " + iterator.next() + " ROLE";
			if(systemParticipation.equals(true))
				actorList = actorList + " AND THE SYSTEM";
			return actorList;
		}
		if(systemParticipation.equals(true))		
			return "THE SYSTEM";
		return "";
	}
}