package ids.tx.bpmn;

import java.util.LinkedList;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ids.tx.utils.WarningWriter;

public class BPMNElement
{
	public static String NAMESPACE;
	public static BPMNParticipant externGenericParticipant;
	public static WarningWriter warningWriter;
	protected BPMNElement containingElement = null;
	protected String id = "";
	protected String name = "";
	
	public BPMNElement(BPMNElement ambient,Node node)
	{
		containingElement = ambient;
		id = getAttributeValue(node,"id");
		name = getAttributeValue(node,"name");
		name = name.replaceAll("[ \n]+","_");
	}
	
	public BPMNElement(BPMNElement ambient,String name)
	{
		containingElement = ambient;
		this.name = name;
		id = String.valueOf(this.hashCode());
	}
	
	public BPMNElement getContainingElement()
	{
		return containingElement;
	}
	
	public void setContainingElement(BPMNElement containingElement)
	{
		this.containingElement = containingElement;
	}
	
	public String getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	protected BPMNElement searchForElement(String elementId)
	{
		return null;
	}
	
	public BPMNElement resolveElementReference(String elementId)
	{
		BPMNElement element = searchForElement(elementId);
		if(element == null)
			if(containingElement!=null)
				return containingElement.resolveElementReference(elementId);
		return element;		
	}
	
	
	protected BPMNItemDefinition searchForItemDefinition(String itemId)
	{
		return null;
	}
	
	public BPMNItemDefinition resolveItemDefinitionReference(String itemId)
	{
		BPMNItemDefinition item = searchForItemDefinition(itemId);
		if(item == null)
			if(containingElement!=null)
				return containingElement.resolveItemDefinitionReference(itemId);
		return item;
	}
	
	
	protected BPMNItemElement searchForData(String dataId)
	{
		return null;
	}
	
	public BPMNItemElement resolveDataReference(String dataId)
	{
		BPMNItemElement data = searchForData(dataId);
		if(data == null)
			if(containingElement!=null)
				return containingElement.resolveDataReference(dataId);
		return data;
	}
	
	
	protected BPMNResource searchForResource(String resourceId)
	{
		return null;
	}
	
	public BPMNResource resolveResourceReference(String resourceId)
	{
		BPMNResource resource = searchForResource(resourceId);
		if(resource == null)
			if(containingElement!=null)
				return containingElement.resolveResourceReference(resourceId);
		return resource;
	}
	
	
	protected BPMNCallableElement searchForCallableElement(String callableId)
	{
		return null;
	}
	
	public BPMNCallableElement resolveCallableElementReference(String callableId)
	{
		BPMNCallableElement callableElement = searchForCallableElement(callableId);
		if(callableElement == null)
			if(containingElement!=null)
				return containingElement.resolveCallableElementReference(callableId);
		return callableElement;
	}
	
	
	protected BPMNInteractionNode searchForInteractionNode(String interactionNodeId)
	{
		return null;
	}
	
	public BPMNInteractionNode resolveInteractionNodeReference(String interactionNodeId)
	{
		BPMNInteractionNode interactionNode = searchForInteractionNode(interactionNodeId);
		if(interactionNode == null)
			if(containingElement!=null)
				return containingElement.resolveInteractionNodeReference(interactionNodeId);
		return interactionNode;
	}
	
	
	protected BPMNParticipant searchForParticipant(String participantId)
	{
		return null;
	}
	
	public BPMNParticipant resolveParticipantReference(String participantId)
	{
		BPMNParticipant participant = searchForParticipant(participantId);
		if(participant == null)
			if(containingElement!=null)
				return containingElement.resolveParticipantReference(participantId);
		return participant;
	}
	
	
	protected BPMNActivity searchForActivity(String activityId)
	{
		return null;
	}
	
	public BPMNActivity resolveActivityReference(String activityId)
	{
		BPMNActivity activity = searchForActivity(activityId);
		if(activity == null)
			if(containingElement!=null)
				return containingElement.resolveActivityReference(activityId);
		return activity;
	}
	

	protected BPMNNode searchForNode(String nodeId)
	{
		return null;
	}
	
	public BPMNNode resolveNodeReference(String nodeId)
	{
		BPMNNode node = searchForNode(nodeId);
		if(node == null)
			if(containingElement!=null)
				return containingElement.resolveNodeReference(nodeId);
		return node;
	}
	
	
	protected BPMNSequenceFlow searchForSequenceFlow(String sequenceFlowId)
	{
		return null;
	}
	
	public BPMNSequenceFlow resolveSequenceFlowReference(String sequenceFlowId)
	{
		BPMNSequenceFlow sequenceFlow = searchForSequenceFlow(sequenceFlowId);
		if(sequenceFlow == null)
			if(containingElement!=null)
				return containingElement.resolveSequenceFlowReference(sequenceFlowId);
		return sequenceFlow;
	}
	
	
	protected BPMNMessage searchForMessage(String messageId)
	{
		return null;
	}
	
	public BPMNMessage resolveMessageReference(String messageId)
	{
		BPMNMessage message = searchForMessage(messageId);
		if(message == null)
			if(containingElement!=null)
				return containingElement.resolveMessageReference(messageId);
		return message;
	}
	
	
	protected BPMNError searchForError(String errorId)
	{
		return null;
	}
	
	public BPMNError resolveErrorReference(String errorId)
	{
		BPMNError error = searchForError(errorId);
		if(error == null)
			if(containingElement!=null)
				return containingElement.resolveErrorReference(errorId);
		return error;
	}
	
	
	protected BPMNEscalation searchForEscalation(String errorId)
	{
		return null;
	}
	
	public BPMNEscalation resolveEscalationReference(String escalationId)
	{
		BPMNEscalation escalation = searchForEscalation(escalationId);
		if(escalation == null)
			if(containingElement!=null)
				return containingElement.resolveEscalationReference(escalationId);
		return escalation;
	}
	
	
	protected BPMNSignal searchForSignal(String signalId)
	{
		return null;
	}
	
	public BPMNSignal resolveSignalReference(String signalId)
	{
		BPMNSignal signal = searchForSignal(signalId);
		if(signal == null)
			if(containingElement!=null)
				return containingElement.resolveSignalReference(signalId);
		return signal;
	}
	
	
	protected BPMNEventDefinition searchForEventDefinition(String eventDefinitionId)
	{
		return null;
	}
	
	public BPMNEventDefinition resolveEventDefinitionReference(String eventDefinitionId)
	{
		BPMNEventDefinition eventDefinition = searchForEventDefinition(eventDefinitionId);
		if(eventDefinition == null)
			if(containingElement!=null)
				return containingElement.resolveEventDefinitionReference(eventDefinitionId);
		return eventDefinition;
	}
	
	
	protected BPMNProcess searchForProcess(String processId)
	{
		return null;
	}
	
	public BPMNProcess resolveProcessReference(String processId)
	{
		BPMNProcess process = searchForProcess(processId);
		if(process == null)
			if(containingElement!=null)
				return containingElement.resolveProcessReference(processId);
		return process;
	}
	
	protected void createExternParticipant()
	{
		if(externGenericParticipant == null)
		{
			externGenericParticipant = new BPMNParticipant((BPMNElement)this,"extern");
		}
	}
	
	public static String getAttributeValue(Node node,String attributeName)
	{
		NamedNodeMap nodemap = node.getAttributes();
		if(nodemap!=null)
		{
			Node attribute = nodemap.getNamedItem(attributeName);
			if (attribute!=null) 
				return attribute.getNodeValue();
		}
		return "";
	}
	
	public static LinkedList<Node> getChildsByName(Node node,String tagName)
	{
		NodeList childs = node.getChildNodes();
		LinkedList<Node> foundChilds = new LinkedList<Node>();
		for(int i=0 ; i<childs.getLength() ; i++)
		{
			Node child = childs.item(i);
			if(child.getNodeName().equals(NAMESPACE + tagName))
				foundChilds.add(child);
		}
		return foundChilds;
	}
	
}