package ids.tx.bpmn;

import java.util.LinkedList;

import org.w3c.dom.Node;

public class BPMNGlobalTask extends BPMNCallableElement
{
	private LinkedList<BPMNResource> resources;
	
	public BPMNGlobalTask(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		resources = new LinkedList<BPMNResource>();
		for(Node resourceRoleNode : getChildsByName(node,"resourceRole"))
			for(Node resourceNode : getChildsByName(resourceRoleNode,"resourceRef"))
			{
				String resourceId = resourceNode.getTextContent();
				BPMNResource resource = resolveResourceReference(resourceId);
				if(resource!=null)
					resources.add(resolveResourceReference(resourceId));
			}
		for(Node performerNode : getChildsByName(node,"performer"))
			for(Node resourceNode : getChildsByName(performerNode,"resourceRef"))
			{
				String resourceId = resourceNode.getTextContent();
				BPMNResource resource = resolveResourceReference(resourceId);
				if(resource!=null)
					resources.add(resolveResourceReference(resourceId));
			}
		for(Node humanPerformerNode : getChildsByName(node,"humanPerformer"))
			for(Node resourceNode : getChildsByName(humanPerformerNode,"resourceRef"))
			{
				String resourceId = resourceNode.getTextContent();
				BPMNResource resource = resolveResourceReference(resourceId);
				if(resource!=null)
					resources.add(resolveResourceReference(resourceId));
			}
		for(Node potentialOwnerNode : getChildsByName(node,"potentialOwner"))
			for(Node resourceNode : getChildsByName(potentialOwnerNode,"resourceRef"))
			{
				String resourceId = resourceNode.getTextContent();
				BPMNResource resource = resolveResourceReference(resourceId);
				if(resource!=null)
					resources.add(resolveResourceReference(resourceId));
			}
	}
	
	public LinkedList<BPMNResource> getResources()
	{
		return resources;
	}
}