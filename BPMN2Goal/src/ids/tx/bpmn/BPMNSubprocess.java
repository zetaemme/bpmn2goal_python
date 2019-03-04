package ids.tx.bpmn;


import ids.tx.requirements.RequirementsBuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class BPMNSubprocess extends BPMNActivity implements BPMNFlowElementContainer
{
	private static Integer counter = 0;
	private Boolean triggeredByEvent = false;
	private HashMap<BPMNEventDefinitionType,BPMNSubprocess> interruptingEventSubProcesses;
	private LinkedList<BPMNNode> flowNodes;
	private LinkedList<BPMNSequenceFlow> sequenceFlows;
	private LinkedList<BPMNData> dataObjects;
	private LinkedList<BPMNDataObjectReference> dataObjectReferences;
	private LinkedList<BPMNDataStoreReference> dataStoreReferences;
	private LinkedList<BPMNLaneSet> laneSets;
	private LinkedList<String> laneActors;
	private HashSet<BPMNParticipant> collaboratingParticipants;
	
	public BPMNSubprocess(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		interruptingEventSubProcesses = new HashMap<BPMNEventDefinitionType,BPMNSubprocess>();
		dataObjects = new LinkedList<BPMNData>();
		dataObjectReferences = new LinkedList<BPMNDataObjectReference>();
		dataStoreReferences = new LinkedList<BPMNDataStoreReference>();
		sequenceFlows = new LinkedList<BPMNSequenceFlow>();
		flowNodes = new LinkedList<BPMNNode>();
		laneSets = new LinkedList<BPMNLaneSet>();
		laneActors = new LinkedList<String>();
		collaboratingParticipants = new HashSet<BPMNParticipant>();
		
		nodeType = BPMNNodeType.SUBPROCESS;
		
		if(name.equals(""))
		{
			name = "subprocess" + counter.toString();
			warningWriter.println("a BPMN subprocess should specify a name -> Assigned:" + name, this);
			counter = counter + 1;
		}
		
		if(getAttributeValue(node,"triggeredByEvent").equals("true"))
			triggeredByEvent=true;
		
		for(Node dataObjectNode : getChildsByName(node,"dataObject"))
		{	BPMNData dataObject = new BPMNData((BPMNElement)this,dataObjectNode);
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
		
		for(Node ioSpecificationNode : getChildsByName(node,"ioSpecification"))
		{
			for(Node dataInputNode : getChildsByName(ioSpecificationNode,"dataInput"))
				dataInputs.add(new BPMNData((BPMNElement)this,dataInputNode));
	
			for(Node dataOutputNode : getChildsByName(ioSpecificationNode,"dataOutput"))
				dataOutputs.add(new BPMNData((BPMNElement)this,dataOutputNode));
			
			for(Node inputSetNode : getChildsByName(ioSpecificationNode,"inputSet"))
				inputSets.add(new BPMNInputSet((BPMNElement)this,inputSetNode));
			
			for(Node outputSetNode : getChildsByName(ioSpecificationNode,"outputSet"))
				outputSets.add(new BPMNOutputSet((BPMNElement)this,outputSetNode));
		}
		
		for(Node dataInputAssociationNode : getChildsByName(node,"dataInputAssociation"))
			dataInputAssociations.add(new BPMNDataAssociation((BPMNElement)this,dataInputAssociationNode));
	
		for(Node dataOutputAssociationNode : getChildsByName(node,"dataOutputAssociation"))
			dataOutputAssociations.add(new BPMNDataAssociation((BPMNElement)this,dataOutputAssociationNode));
		
		for(BPMNData dataInput : dataInputs)
			resolvedInputAssociations.put(dataInput, resolveDataInputAssociation(dataInput));
		
		for(BPMNData dataOutput : dataOutputs)
			resolvedOutputAssociations.put(dataOutput, resolveDataOutputAssociation(dataOutput));		
		
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
			else if(tagName.equals(NAMESPACE.concat("scriptTask")))
				flowNodes.add(new BPMNScriptTask((BPMNElement)this,childNode));
			else if(tagName.equals(NAMESPACE.concat("businessRuleTask")))
				flowNodes.add(new BPMNBusinessRuleTask((BPMNElement)this,childNode));
			else if(tagName.equals(NAMESPACE.concat("sendTask")))
				flowNodes.add(new BPMNSendTask((BPMNElement)this,childNode));
			else if(tagName.equals(NAMESPACE.concat("receiveTask")))
				flowNodes.add(new BPMNReceiveTask((BPMNElement)this,childNode));
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
		
		if(triggeredByEvent.equals(true))
			for(BPMNNode flowNode : flowNodes)
				if(flowNode.getNodeType().equals(BPMNNodeType.START_EVENT))
					if(((BPMNStartEvent)flowNode).getIsInterrupting().equals(true))
						((BPMNSubprocess)this.getContainingElement()).addSubprocessInterruptingEvent(this,(BPMNEvent)flowNode);

		for(BPMNNode flowNode : flowNodes)
			if(flowNode.getNodeType().equals(BPMNNodeType.HUMAN_INTERACTION_TASK))
				((BPMNHumanInteractionTask)flowNode).checkActors();
	}
	
	public LinkedList<BPMNNode> getFlowNodes()
	{
		return flowNodes;
	}

	public LinkedList<BPMNSequenceFlow> getSequenceFlows() 
	{
		return sequenceFlows;
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

	public Boolean getTriggeredByEvent()
	{
		return triggeredByEvent;
	}
	
	public HashMap<BPMNEventDefinitionType,BPMNSubprocess> getInterruptingEventSubProcesses()
	{
		return interruptingEventSubProcesses;
	}
	
	public LinkedList<BPMNLaneSet> getLaneSets() 
	{
		return laneSets;
	}

	public BPMNFlowElementContainerType getFlowElementContainerType()
	{
		return BPMNFlowElementContainerType.SUBPROCESS;
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
	
	protected BPMNInteractionNode searchForInteractionNode(String interactionNodeId)
	{
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
	
	protected BPMNSequenceFlow searchForSequenceFlow(String sequenceFlowId)
	{
		for(BPMNSequenceFlow sequenceFlow : sequenceFlows)
			if(sequenceFlow.getId().equals(sequenceFlowId))
				return sequenceFlow;
		return null;
	}
	
	protected BPMNActivity searchForActivity(String activityId)
	{
		for(BPMNNode flowNode : flowNodes)
			if(flowNode.getId().equals(activityId))
				return (BPMNActivity)flowNode;
		return null;
	}
	
	protected BPMNNode searchForNode(String nodeId)
	{
		for(BPMNNode flowNode : flowNodes)
			if(flowNode.getId().equals(nodeId))
				return flowNode;
		return null;
	}
	
	protected BPMNItemElement searchForData(String dataId)
	{
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
	
	public void addResource(BPMNResource resource)
	{
		resources.add(resource);
	}
	
	public void addLaneActor(String laneName)
	{
		laneActors.add(laneName);
	}

	public Collection<String> getLaneActors() 
	{
		return laneActors;
	}

	public void addInterruptingEvent(InterruptingEventAdder adder, BPMNEvent boundary)
	{
		adder.addEvent(this,boundary);
	}
	
	public void addSubprocessInterruptingEvent(BPMNSubprocess eventSubprocess, BPMNEvent interruptingEvent)
	{
		BPMNEventDefinitionType type = interruptingEvent.getEventDefinitions().getFirst().getEventDefinitionType();
		if(!interruptingEventSubProcesses.containsKey(type))
		{
			interruptingEventSubProcesses.put(type,eventSubprocess);
			BPMNBoundaryEvent boundaryInterruptingEvent = new BPMNBoundaryEvent(containingElement,interruptingEvent.getEventDefinitions().getFirst(),true,(BPMNActivity)this);
			singleInterruptingEvents.put(type,boundaryInterruptingEvent);
		}
		else
			BPMNElement.warningWriter.println("Does there exist a BPMN interrupting event subprocess of the same type yet",this);
	}
	
	public HashSet<BPMNParticipant> collectCollaboratingParticipants()
	{
		HashSet<BPMNParticipant> parts = new HashSet<BPMNParticipant>();
		for(BPMNNode flowNode : flowNodes)
			if(flowNode.isInteractionNode())
				parts.addAll(((BPMNInteractionNode)flowNode).collectCollaboratingParticipants());
		return parts;
	}
	
	public void addCollaboratingParticipant(BPMNParticipant participant)
	{
		this.collaboratingParticipants.add(participant);
	}
	
	public HashSet<BPMNParticipant> getCollaboratingParticipants()
	{
		return collaboratingParticipants;
	}
	
	public HashSet<BPMNResource> collectGeneralActors()
	{
		HashSet<BPMNResource> actors = new HashSet<BPMNResource>();
		actors.addAll(resources);
		for(BPMNNode flowNode : flowNodes)
			if(flowNode.getNodeType().equals(BPMNNodeType.HUMAN_INTERACTION_TASK))
				actors.addAll(((BPMNHumanInteractionTask) flowNode).collectGeneralActors());
			else if(flowNode.getNodeType().equals(BPMNNodeType.SUBPROCESS))
				actors.addAll(((BPMNSubprocess) flowNode).collectGeneralActors());
		return actors;
	}


	public void acceptVisitor(String locationString, RequirementsBuilder requirementsBuilder)
	{
		requirementsBuilder.analyzeSubprocess(locationString,this);
	}
	
}