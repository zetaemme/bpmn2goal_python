package ids.tx.bpmn;

import java.util.HashSet;
import java.util.LinkedList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BPMNEvent extends BPMNNode implements BPMNInteractionNode
{
	protected LinkedList<BPMNEventDefinition> eventDefinitions;
	protected LinkedList<BPMNMessageFlow> incomingMessageFlows;
	protected LinkedList<BPMNMessageFlow> outgoingMessageFlows;
	protected Boolean parallelMultiple = false;
	
	public BPMNEvent(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		eventDefinitions = new LinkedList<BPMNEventDefinition>();
		incomingMessageFlows = new LinkedList<BPMNMessageFlow>();
		outgoingMessageFlows = new LinkedList<BPMNMessageFlow>();
		
		if(getAttributeValue(node,"parallelMultiple").equals("true"))
			parallelMultiple = true;
		
		for(Node eventDefinitionNode : getChildsByName(node,"eventDefinitionRef"))
		{
			String eventDefinitionId = eventDefinitionNode.getTextContent();
			BPMNEventDefinition eventDefinition = resolveEventDefinitionReference(eventDefinitionId);
			if(eventDefinition!=null)
				eventDefinitions.add(eventDefinition);
		}
		
		NodeList childNodes = node.getChildNodes();
		for(int i=0 ; i<childNodes.getLength() ; i++)
		{
			Node eventDefinitionNode = childNodes.item(i);
			String eventDefinitionName = eventDefinitionNode.getNodeName(); 
			if(eventDefinitionName.equals(NAMESPACE.concat("linkEventDefinition")))
				eventDefinitions.add(new BPMNLinkEventDefinition((BPMNElement)this,eventDefinitionNode));
			else if(eventDefinitionName.equals(NAMESPACE.concat("messageEventDefinition")))
				eventDefinitions.add(new BPMNMessageEventDefinition((BPMNElement)this,eventDefinitionNode));
			else if(eventDefinitionName.equals(NAMESPACE.concat("signalEventDefinition")))
				eventDefinitions.add(new BPMNSignalEventDefinition((BPMNElement)this,eventDefinitionNode));
			else if(eventDefinitionName.equals(NAMESPACE.concat("terminateEventDefinition")))
				eventDefinitions.add(new BPMNTerminateEventDefinition((BPMNElement)this,eventDefinitionNode));
			else if(eventDefinitionName.equals(NAMESPACE.concat("timerEventDefinition")))
				eventDefinitions.add(new BPMNTimerEventDefinition((BPMNElement)this,eventDefinitionNode));
			else if(eventDefinitionName.equals(NAMESPACE.concat("cancelEventDefinition")))
				eventDefinitions.add(new BPMNCancelEventDefinition((BPMNElement)this,eventDefinitionNode));
			else if(eventDefinitionName.equals(NAMESPACE.concat("compensateEventDefinition")))
				eventDefinitions.add(new BPMNCompensateEventDefinition((BPMNElement)this,eventDefinitionNode));
			else if(eventDefinitionName.equals(NAMESPACE.concat("conditionalEventDefinition")))
				eventDefinitions.add(new BPMNConditionalEventDefinition((BPMNElement)this,eventDefinitionNode));
			else if(eventDefinitionName.equals(NAMESPACE.concat("errorEventDefinition")))
				eventDefinitions.add(new BPMNErrorEventDefinition((BPMNElement)this,eventDefinitionNode));
			else if(eventDefinitionName.equals(NAMESPACE.concat("escalationEventDefinition")))
				eventDefinitions.add(new BPMNEscalationEventDefinition((BPMNElement)this,eventDefinitionNode));
		}
		
		for(Node dataInputNode : getChildsByName(node,"dataInput"))
			dataInputs.add(new BPMNData((BPMNElement)this,dataInputNode));
		
		for(Node dataOutputNode : getChildsByName(node,"dataOutput"))
			dataOutputs.add(new BPMNData((BPMNElement)this,dataOutputNode));
		
		for(Node inputSetNode : getChildsByName(node,"inputSet"))
			inputSets.add(new BPMNInputSet((BPMNElement)this,inputSetNode));
		
		for(Node outputSetNode : getChildsByName(node,"outputSet"))
			outputSets.add(new BPMNOutputSet((BPMNElement)this,outputSetNode));
		
		for(Node dataInputAssociationNode : getChildsByName(node,"dataInputAssociation"))
			dataInputAssociations.add(new BPMNDataAssociation((BPMNElement)this,dataInputAssociationNode));
		
		for(Node dataOutputAssociationNode : getChildsByName(node,"dataOutputAssociation"))
			dataOutputAssociations.add(new BPMNDataAssociation((BPMNElement)this,dataOutputAssociationNode));
		
		for(BPMNData dataInput : dataInputs)
		{
			resolvedInputAssociations.put(dataInput, resolveDataInputAssociation(dataInput));
			if(dataInput.getName().equals("") && resolvedInputAssociations.get(dataInput).isEmpty())
				dataInput.fillName();
		}
		
		for(BPMNData dataOutput : dataOutputs)
		{
			resolvedOutputAssociations.put(dataOutput, resolveDataOutputAssociation(dataOutput));
			if(dataOutput.getName().equals("") && resolvedOutputAssociations.get(dataOutput).isEmpty())
				dataOutput.fillName();
		}
	}

	public BPMNEvent(BPMNElement ambient, String name)
	{
		super(ambient,name);
		
		eventDefinitions = new LinkedList<BPMNEventDefinition>();
		incomingMessageFlows = new LinkedList<BPMNMessageFlow>();
		outgoingMessageFlows = new LinkedList<BPMNMessageFlow>();
	}
	
	public Boolean getParallelMultiple()
	{
		return parallelMultiple;
	}

	public LinkedList<BPMNEventDefinition> getEventDefinitions()
	{
		return eventDefinitions;
	}

	public LinkedList<BPMNMessageFlow> getIncomingMessageFlows()
	{
		return incomingMessageFlows;
	}
	
	public LinkedList<BPMNMessageFlow> getOutgoingMessageFlows()
	{
		return outgoingMessageFlows;
	}
	
	public LinkedList<BPMNData> getDataInputs()
	{
		return dataInputs;
	}
	
	public LinkedList<BPMNData> getDataOutputs()
	{
		return dataOutputs;
	}
	
	public LinkedList<BPMNInputSet> getInputSets()
	{
		return inputSets;
	}
	
	public LinkedList<BPMNOutputSet> getOutputSets()
	{
		return outputSets;
	}
	
	public LinkedList<BPMNDataAssociation> getDataInputAssociations()
	{
		return dataInputAssociations;
	}
	
	public LinkedList<BPMNDataAssociation> getDataOutputAssociations()
	{
		return dataOutputAssociations;
	}
	
	public BPMNInteractionNodeType getInteractionNodeType()
	{
		return BPMNInteractionNodeType.EVENT;
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

	public BPMNMessage getMessage()
	{
		for(BPMNEventDefinition eventDefinition : eventDefinitions)
			if(eventDefinition.getEventDefinitionType().equals(BPMNEventDefinitionType.MESSAGE))
				return ((BPMNMessageEventDefinition) eventDefinition).getMessage();
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
	
	public void checkInputMessages()
	{
		for(BPMNEventDefinition eventDefinition : eventDefinitions)
			if(eventDefinition.getEventDefinitionType().equals(BPMNEventDefinitionType.MESSAGE))
				((BPMNMessageEventDefinition) eventDefinition).checkInputMessages();
	}
	
	public void checkOutputMessages()
	{
		for(BPMNEventDefinition eventDefinition : eventDefinitions)
			if(eventDefinition.getEventDefinitionType().equals(BPMNEventDefinitionType.MESSAGE))
				((BPMNMessageEventDefinition) eventDefinition).checkOutputMessages();
	}
	
	public static Boolean containsEventOfType(BPMNEvent event,BPMNEventDefinitionType type)
	{
		for(BPMNEventDefinition eventDefinition : event.getEventDefinitions())
			if(eventDefinition.getEventDefinitionType().equals(type))
				return true;
		return false;
	}
}