package ids.tx.bpmn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import org.w3c.dom.Node;

public abstract class BPMNActivity extends BPMNNode implements BPMNInteractionNode
{
	protected Boolean isForCompensation = false;
	protected BPMNSequenceFlow defaultSequenceFlow;
	protected String defaultSequenceFlowId = "";
	protected LinkedList<BPMNResource> resources;
	protected BPMNStandardLoopCharacteristic loopCharacteristic = null;
	protected LinkedList<BPMNMessageFlow> incomingMessageFlows;
	protected LinkedList<BPMNMessageFlow> outgoingMessageFlows;
	protected HashMap<BPMNEventDefinitionType,BPMNEvent> singleInterruptingEvents;
	
	public BPMNActivity(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		if(getAttributeValue(node,"isForCompensation").equals("true"))
			isForCompensation = true;
		
		defaultSequenceFlowId = getAttributeValue(node,"default");
		
		resources = new LinkedList<BPMNResource>();
		singleInterruptingEvents = new HashMap<BPMNEventDefinitionType,BPMNEvent>();
		incomingMessageFlows = new LinkedList<BPMNMessageFlow>();
		outgoingMessageFlows = new LinkedList<BPMNMessageFlow>();
		
		for(Node resourceRoleNode : getChildsByName(node,"resourceRole"))
		{
			for(Node resourceNode : getChildsByName(resourceRoleNode,"resourceRef"))
			{
				String resourceId = resourceNode.getTextContent();
				BPMNResource resource = resolveResourceReference(resourceId);
				if(resource!=null)
					resources.add(resource);
			}
		}
		for(Node performerNode : getChildsByName(node,"performer"))
		{
			for(Node resourceNode : getChildsByName(performerNode,"resourceRef"))
			{
				String resourceId = resourceNode.getTextContent();
				BPMNResource resource = resolveResourceReference(resourceId);
				if(resource!=null)
					resources.add(resolveResourceReference(resourceId));
			}
		}
		for(Node humanPerformerNode : getChildsByName(node,"humanPerformer"))
		{
			for(Node resourceNode : getChildsByName(humanPerformerNode,"resourceRef"))
			{
				String resourceId = resourceNode.getTextContent();
				BPMNResource resource = resolveResourceReference(resourceId);
				if(resource!=null)
					resources.add(resolveResourceReference(resourceId));
			}
		}
		for(Node potentialOwnerNode : getChildsByName(node,"potentialOwner"))
		{
			for(Node resourceNode : getChildsByName(potentialOwnerNode,"resourceRef"))
			{
				String resourceId = resourceNode.getTextContent();
				BPMNResource resource = resolveResourceReference(resourceId);
				if(resource!=null)
					resources.add(resolveResourceReference(resourceId));
			}
		}

		for(Node standardLoopCharacteristicNode : getChildsByName(node,"standardLoopCharacteristics"))
			loopCharacteristic = new BPMNStandardLoopCharacteristic((BPMNElement)this, standardLoopCharacteristicNode);
	}
	
	public Boolean getIsForCompensation()
	{
		return isForCompensation;
	}

	public BPMNStandardLoopCharacteristic getLoopCharacteristic()
	{
		return loopCharacteristic;
	}
	
	public BPMNSequenceFlow getDefaultSequenceFlow()
	{
		if(defaultSequenceFlow == null)
			defaultSequenceFlow = resolveSequenceFlowReference(defaultSequenceFlowId);
		return defaultSequenceFlow;
	}

	public LinkedList<BPMNResource> getResources()
	{
		return resources;
	}

	public HashMap<BPMNEventDefinitionType,BPMNEvent> getSingleInterruptingEvents()
	{
		return singleInterruptingEvents;
	}

	public LinkedList<BPMNMessageFlow> getIncomingMessageFlows()
	{
		return incomingMessageFlows;
	}
	
	public LinkedList<BPMNMessageFlow> getOutgoingMessageFlows()
	{
		return outgoingMessageFlows;
	}
	
	public HashSet<BPMNParticipant> collectCollaboratingParticipants()
	{
		HashSet<BPMNParticipant> collaboratings = new HashSet<BPMNParticipant>();
		for(BPMNMessageFlow messageFlow : this.getIncomingMessageFlows())
			collaboratings.addAll(messageFlow.getSource().getInternalParticipants());
		for(BPMNMessageFlow messageFlow : this.getOutgoingMessageFlows())
			collaboratings.addAll(messageFlow.getTarget().getInternalParticipants());
		return collaboratings;
	}
	
	public BPMNInteractionNodeType getInteractionNodeType()
	{
		return BPMNInteractionNodeType.ACTIVITY;
	}
	
	public BPMNMessage getMessage()
	{
		return null;
	}
	
	public LinkedList<BPMNParticipant> getInternalParticipants()
	{
		return ((BPMNFlowElementContainer)containingElement).getInternalParticipants();
	}
	
	public void addIncomingMessageFlow(BPMNMessageFlow messageFlow)
	{
		incomingMessageFlows.add(messageFlow);
	}
	
	public void addOutgoingMessageFlow(BPMNMessageFlow messageFlow)
	{
		outgoingMessageFlows.add(messageFlow);
	}
	
	protected BPMNResource searchForResource(String resourceId)
	{
		for(BPMNResource resource : resources)
			if(resource.getId().equals(resourceId))
				return resource;
		return null;
	}

	public abstract void addInterruptingEvent(InterruptingEventAdder adder,BPMNEvent boundary);
	
}