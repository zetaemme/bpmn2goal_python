package ids.tx.bpmn;


import ids.tx.requirements.RequirementsBuilder;

import java.util.HashSet;
import java.util.LinkedList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BPMNProcess extends BPMNCallableElement implements BPMNFlowElementContainer
{
	private static Integer counter = 0;
	private LinkedList<BPMNNode> flowNodes;
	private LinkedList<BPMNSequenceFlow> sequenceFlows;
	private LinkedList<BPMNResource> resources;
	private LinkedList<BPMNData> dataObjects;
	private LinkedList<BPMNDataObjectReference> dataObjectReferences;
	private LinkedList<BPMNDataStoreReference> dataStoreReferences;
	private LinkedList<BPMNLaneSet> laneSets;
	private LinkedList<BPMNParticipant> participants;
	private HashSet<BPMNParticipant> collaboratingParticipants;
	
	public BPMNProcess(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		if(name.equals(""))
		{
			name = "process" + counter.toString();
			warningWriter.println("a BPMN process should specify a name -> Assigned:" + name, this);
			counter = counter + 1;
		}
		
		resources = new LinkedList<BPMNResource>();
		dataObjects = new LinkedList<BPMNData>();
		dataObjectReferences = new LinkedList<BPMNDataObjectReference>();
		dataStoreReferences = new LinkedList<BPMNDataStoreReference>();
		flowNodes = new LinkedList<BPMNNode>();
		participants = new LinkedList<BPMNParticipant>();
		collaboratingParticipants = new HashSet<BPMNParticipant>();
		sequenceFlows = new LinkedList<BPMNSequenceFlow>();
		laneSets = new LinkedList<BPMNLaneSet>();
		
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
		
		for(Node dataObjectNode : getChildsByName(node,"dataObject"))
		{
			BPMNData dataObject = new BPMNData((BPMNElement)this,dataObjectNode);
			dataObjects.add(dataObject);
			if(dataObject.getName().equals(""))
				dataObject.fillName();
			if(dataObject.getItemRef() == null)	
				warningWriter.println("a BPMN item and data should refer a valid ItemDefinition", (BPMNElement)dataObject);

		}
		
		for(Node dataObjectRefNode : getChildsByName(node,"dataObjectReference"))
			dataObjectReferences.add(new BPMNDataObjectReference((BPMNElement)this,dataObjectRefNode));
		
		for(Node dataStoreRefNode : getChildsByName(node,"dataStoreReference"))
			dataStoreReferences.add(new BPMNDataStoreReference((BPMNElement)this,dataStoreRefNode));
		
		NodeList childNodes = node.getChildNodes();
		for(int i=0 ; i<childNodes.getLength() ; i++)
		{
			Node childNode = childNodes.item(i);
			String tagName = childNode.getNodeName();
			if(tagName.equals(NAMESPACE.concat("intermediateCatchEvent")))
				flowNodes.add(new BPMNIntermediateCatchEvent((BPMNElement)this,childNode));
			else if(tagName.equals(NAMESPACE.concat("intermediateThrowEvent")))
				flowNodes.add(new BPMNIntermediateThrowEvent((BPMNElement)this,childNode));
			else if(tagName.equals(NAMESPACE.concat("startEvent")))
				flowNodes.add(new BPMNStartEvent((BPMNElement)this,childNode));
			else if(tagName.equals(NAMESPACE.concat("endEvent")))
				flowNodes.add(new BPMNEndEvent((BPMNElement)this,childNode));
			else if(tagName.equals(NAMESPACE.concat("boundaryEvent")))
				flowNodes.add(new BPMNBoundaryEvent((BPMNElement)this,childNode));
			else if(tagName.equals(NAMESPACE.concat("task")) || tagName.equals(NAMESPACE.concat("serviceTask")))
				flowNodes.add(new BPMNTask((BPMNElement)this,childNode));
			else if(tagName.equals(NAMESPACE.concat("sendTask")))
				flowNodes.add(new BPMNSendTask((BPMNElement)this,childNode));
			else if(tagName.equals(NAMESPACE.concat("receiveTask")))
				flowNodes.add(new BPMNReceiveTask((BPMNElement)this,childNode));
			else if(tagName.equals(NAMESPACE.concat("scriptTask")))
				flowNodes.add(new BPMNScriptTask((BPMNElement)this,childNode));
			else if(tagName.equals(NAMESPACE.concat("businessRuleTask")))
				flowNodes.add(new BPMNBusinessRuleTask((BPMNElement)this,childNode));
			else if(tagName.equals(NAMESPACE.concat("userTask")) || tagName.equals(NAMESPACE.concat("manualTask")))
				flowNodes.add(new BPMNHumanInteractionTask((BPMNElement)this,childNode));
			else if(tagName.equals(NAMESPACE.concat("callActivity")))
				flowNodes.add(new BPMNCallActivity((BPMNElement)this,childNode));
			else if(tagName.equals(NAMESPACE.concat("subProcess")))
				flowNodes.add(new BPMNSubprocess((BPMNElement)this,childNode));
			else if(tagName.equals(NAMESPACE.concat("inclusiveGateway")))
				flowNodes.add(new BPMNInclusiveGateway((BPMNElement)this,childNode));
			else if(tagName.equals(NAMESPACE.concat("exclusiveGateway")))
				flowNodes.add(new BPMNExclusiveGateway((BPMNElement)this,childNode));
			else if(tagName.equals(NAMESPACE.concat("parallelGateway")))
				flowNodes.add(new BPMNParallelGateway((BPMNElement)this,childNode));
			else if(tagName.equals(NAMESPACE.concat("complexGateway")))
				flowNodes.add(new BPMNComplexGateway((BPMNElement)this,childNode));
			else if(tagName.equals(NAMESPACE.concat("eventBasedGateway")))
				flowNodes.add(new BPMNEventBasedGateway((BPMNElement)this,childNode));
		}
		
		for(Node sequenceFlowNode : getChildsByName(node,"sequenceFlow"))
			sequenceFlows.add(new BPMNSequenceFlow((BPMNElement)this,sequenceFlowNode));
		
		for(Node laneSetNode : getChildsByName(node,"laneSet"))
			laneSets.add(new BPMNLaneSet((BPMNElement)this,laneSetNode));
		
		for(BPMNNode flowNode : flowNodes)
			if(flowNode.getNodeType().equals(BPMNNodeType.SUBPROCESS))
					((BPMNSubprocess)flowNode).checkActors();
		
		for(BPMNNode flowNode : flowNodes)
			if(flowNode.getNodeType().equals(BPMNNodeType.SUBPROCESS))
					((BPMNSubprocess)flowNode).checkMessages();
		
		for(BPMNNode flowNode : flowNodes)
			if(flowNode.getNodeType().equals(BPMNNodeType.SUBPROCESS))
			{
				HashSet<BPMNResource> collectedResources = ((BPMNSubprocess)flowNode).collectGeneralActors();
				collectedResources.removeAll(((BPMNSubprocess)flowNode).getResources());
				for(BPMNResource resource : collectedResources)
					((BPMNSubprocess)flowNode).addResource(resource);
			}
	}
	
	public LinkedList<BPMNNode> getFlowNodes()
	{
		return flowNodes;
	}

	public LinkedList<BPMNSequenceFlow> getSequenceFlows()
	{
		return sequenceFlows;
	}

	public LinkedList<BPMNResource> getResources()
	{
		return resources;
	}

	public LinkedList<BPMNData> getDataObjects()
	{
		return dataObjects;
	}

	public LinkedList<BPMNDataObjectReference> getDataObjectReferences()
	{
		return dataObjectReferences;
	}

	public LinkedList<BPMNDataStoreReference> getDataStoreReferences()
	{
		return dataStoreReferences;
	}

	public LinkedList<BPMNLaneSet> getLaneSets()
	{
		return laneSets;
	}

	public LinkedList<BPMNParticipant> getParticipants()
	{
		return participants;
	}
	
	public BPMNFlowElementContainerType getFlowElementContainerType()
	{
		return BPMNFlowElementContainerType.PROCESS;
	}
	
	public HashSet<BPMNResource> collectGeneralActors()
	{
		HashSet<BPMNResource> actors = new HashSet<BPMNResource>();
		for(BPMNNode flowNode : flowNodes)
			if(flowNode.getNodeType().equals(BPMNNodeType.HUMAN_INTERACTION_TASK))
				actors.addAll(((BPMNHumanInteractionTask) flowNode).collectGeneralActors());
			else if(flowNode.getNodeType().equals(BPMNNodeType.SUBPROCESS))
				actors.addAll(((BPMNSubprocess) flowNode).collectGeneralActors());
		return actors;
	}
	
	public LinkedList<BPMNParticipant> getInternalParticipants()
	{
		return getParticipants();
	}

	public HashSet<BPMNParticipant> collectCollaboratingParticipants()
	{
		HashSet<BPMNParticipant> parts = new HashSet<BPMNParticipant>();
		for(BPMNNode flowNode : flowNodes)
			if(flowNode.isInteractionNode())
			{
				HashSet<BPMNParticipant> subParts = ((BPMNInteractionNode)flowNode).collectCollaboratingParticipants();
				if(flowNode.getNodeType().equals(BPMNNodeType.SUBPROCESS))
					for(BPMNParticipant subPart : subParts)
						((BPMNSubprocess)flowNode).addCollaboratingParticipant(subPart);
				parts.addAll(subParts);
			}
		return parts;
	}

	public Boolean hasActiveCollaboratingParticipant()
	{
		for(BPMNParticipant participant : getCollaboratingParticipants())
			if(participant.getProcess()!=null)
				return true;
		return false;
	}
	
	public void addCollaboratingParticipant(BPMNParticipant participant)
	{
		this.collaboratingParticipants.add(participant);
	}

	public HashSet<BPMNParticipant> getCollaboratingParticipants()
	{
		return collaboratingParticipants;
	}
	
	public void addParticipant(BPMNParticipant participant)
	{
		participants.add(participant);
	}
	
	public void addResource(BPMNResource resource)
	{
		resources.add(resource);
	}
	
	public void checkMessages()
	{
		for(BPMNNode flowNode : flowNodes)
		{
			if(flowNode.getNodeType().equals(BPMNNodeType.SEND_TASK))
				((BPMNSendTask)flowNode).checkMessages();
			else if(flowNode.getNodeType().equals(BPMNNodeType.RECEIVE_TASK))
				((BPMNReceiveTask)flowNode).checkMessages();
			else if(flowNode.getNodeType().equals(BPMNNodeType.INTERMEDIATE_THROW_EVENT)
					|| flowNode.getNodeType().equals(BPMNNodeType.END_EVENT))
				((BPMNEvent)flowNode).checkOutputMessages();
			else if(flowNode.getNodeType().equals(BPMNNodeType.INTERMEDIATE_CATCH_EVENT)
					|| flowNode.getNodeType().equals(BPMNNodeType.START_EVENT)
					|| flowNode.getNodeType().equals(BPMNNodeType.BOUNDARY_EVENT))
				((BPMNEvent)flowNode).checkInputMessages();
		}
	}
	
	public void checkActors()
	{
		for(BPMNNode flowNode : flowNodes)
			if(flowNode.getNodeType().equals(BPMNNodeType.HUMAN_INTERACTION_TASK))
				((BPMNHumanInteractionTask)flowNode).checkActors();
	}
	
	protected BPMNItemElement searchForData(String dataId)
	{
		BPMNItemElement item = super.searchForData(dataId);
		if(item!=null)
			return item;
		for(BPMNData data : dataObjects)
			if(data.getId().equals(dataId))
				return data;
		for(BPMNDataObjectReference dataObjectRef : dataObjectReferences)
			if(dataObjectRef.getId().equals(dataId))
				return dataObjectRef;
		for(BPMNDataStoreReference dataStoreRef : dataStoreReferences)
			if(dataStoreRef.getId().equals(dataId))
				return dataStoreRef;
		return null;
	}
	
	protected BPMNSequenceFlow searchForSequenceFlow(String sequenceFlowId)
	{
		for(BPMNSequenceFlow sequenceFlow : sequenceFlows)
			if(sequenceFlow.getId().equals(sequenceFlowId))
				return sequenceFlow;
		return null;
	}
	
	protected BPMNNode searchForNode(String nodeId)
	{
		for(BPMNNode flowNode : flowNodes)
			if(flowNode.getId().equals(nodeId))
				return flowNode;
		return null;
	}
	
	protected BPMNResource searchForResource(String resourceId)
	{
		for(BPMNResource resource : resources)
			if(resource.getId().equals(resourceId))
				return resource;
		return null;
	}
	
	protected BPMNActivity searchForActivity(String activityId)
	{
		for(BPMNNode flowNode : flowNodes)
			if(flowNode.getId().equals(activityId))
				return (BPMNActivity)flowNode;
		return null;
	}
	
	protected BPMNInteractionNode searchForInteractionNode(String interactionNodeId)
	{
		for(BPMNParticipant participant : participants)
			if(participant.getId().equals(interactionNodeId))
				return (BPMNInteractionNode) participant;
		for(BPMNNode flowNode : flowNodes)
		{
			if(flowNode.getId().equals(interactionNodeId))
				return (BPMNInteractionNode)flowNode;
			if(flowNode.getNodeType().equals(BPMNNodeType.SUBPROCESS))
			{
				BPMNInteractionNode interactionNode = ((BPMNSubprocess)flowNode).searchForInteractionNode(interactionNodeId);
				if(interactionNode!=null)
					return interactionNode;
			}
		}
		return null;
	}
	
	public void acceptVisitor(RequirementsBuilder requirementsBuilder) 
	{
		requirementsBuilder.analyzeProcess(this);
	}
	
}