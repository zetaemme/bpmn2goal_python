package ids.tx.bpmn;

import java.util.HashSet;
import java.util.LinkedList;

import ids.tx.requirements.RequirementsBuilder;
import ids.tx.utils.WarningWriter;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class BPMNWorkflow extends BPMNElement
{ 
	private LinkedList<BPMNError> errors;
	private LinkedList<BPMNEscalation> escalations;
	private LinkedList<BPMNMessage> messages;
	private LinkedList<BPMNMessageFlow> messageFlows;
	private LinkedList<BPMNSignal> signals;
	private LinkedList<BPMNResource> resources;
	private LinkedList<BPMNParticipant> participants;
	private LinkedList<BPMNPartner> partners;
	private LinkedList<BPMNEventDefinition> eventDefinitions;
	private LinkedList<BPMNItemDefinition> itemDefinitions;
	private LinkedList<BPMNDataStore> dataStores;
	private LinkedList<BPMNProcess> processes;
	private LinkedList<BPMNGlobalTask> globalTasks;
	
	public BPMNWorkflow(Node node, WarningWriter warningWriter)
	{
		super(null,node);
		
		BPMNElement.warningWriter = warningWriter;
		
		NamedNodeMap attributes = node.getAttributes(); 
		for(int i=0 ; i<attributes.getLength() ; i++)
		{
			Node attribute = attributes.item(i);
			if(attribute.getNodeValue().equals("http://www.omg.org/spec/BPMN/20100524/MODEL"))
				NAMESPACE = attribute.getNodeName().substring("xmlns".length());
		}
		if(!NAMESPACE.equals(""))
			NAMESPACE = NAMESPACE.substring(1) + ":";
		
		itemDefinitions = new LinkedList<BPMNItemDefinition>();
		dataStores = new LinkedList<BPMNDataStore>();
		errors = new LinkedList<BPMNError>();
		escalations = new LinkedList<BPMNEscalation>();
		resources = new LinkedList<BPMNResource>();
		messages = new LinkedList<BPMNMessage>();
		signals = new LinkedList<BPMNSignal>();
		eventDefinitions = new LinkedList<BPMNEventDefinition>();
		processes = new LinkedList<BPMNProcess>();
		globalTasks = new LinkedList<BPMNGlobalTask>();
		participants = new LinkedList<BPMNParticipant>();
		messageFlows = new LinkedList<BPMNMessageFlow>();
		partners = new LinkedList<BPMNPartner>();
		
		for(Node itemDefinitionNode : getChildsByName(node,"itemDefinition"))
		{
			BPMNItemDefinition itemDefinition = new BPMNItemDefinition((BPMNElement)this,itemDefinitionNode);
			itemDefinitions.add(itemDefinition);
		}
		
		for(Node dataStoreNode : getChildsByName(node,"dataStore"))
		{
			BPMNDataStore dataStore = new BPMNDataStore((BPMNElement)this,dataStoreNode);
			dataStores.add(dataStore);
		}
		
		for(Node errorNode : getChildsByName(node,"error"))
		{
			BPMNError error = new BPMNError((BPMNElement)this,errorNode);
			errors.add(error);
		}
		
		for(Node escalationNode : getChildsByName(node,"escalation"))
		{
			BPMNEscalation escalation = new BPMNEscalation((BPMNElement)this,escalationNode);
			escalations.add(escalation);
		}
		
		for(Node resourceNode : getChildsByName(node,"resource"))
		{
			BPMNResource resource = new BPMNResource((BPMNElement)this,resourceNode);
			resources.add(resource);
		}
		
		for(Node messageNode : getChildsByName(node,"message"))
		{
			BPMNMessage message = new BPMNMessage((BPMNElement)this,messageNode);
			messages.add(message);
		}
		
		for(Node signalNode : getChildsByName(node,"signal"))
		{
			BPMNSignal signal = new BPMNSignal((BPMNElement)this,signalNode);
			signals.add(signal);
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

		
		for(Node processNode : getChildsByName(node,"process"))
			processes.add(new BPMNProcess((BPMNElement)this,processNode));
		
		for(Node globalTaskNode : getChildsByName(node,"globalTask"))
			globalTasks.add(new BPMNGlobalTask((BPMNElement)this,globalTaskNode));
		
		for(Node collaborationNode : getChildsByName(node,"collaboration"))
		{
			for(Node participantNode : getChildsByName(collaborationNode,"participant"))
			{
				BPMNParticipant participant = new BPMNParticipant((BPMNElement)this,participantNode);
				participants.add(participant);
			}
			
			for(Node messageFlowNode : getChildsByName(collaborationNode,"messageFlow"))
			{
				BPMNMessageFlow messageFlow = new BPMNMessageFlow((BPMNElement)this,messageFlowNode);
				messageFlows.add(messageFlow);
			}
		}
		
		for(Node partnerRoleNode : getChildsByName(node,"partnerRole"))
		{
			BPMNPartner partnerRole = new BPMNPartner((BPMNElement)this,partnerRoleNode);
			partners.add(partnerRole);
		}
		for(Node partnerEntityNode : getChildsByName(node,"partnerEntity"))
		{
			BPMNPartner partnerEntity = new BPMNPartner((BPMNElement)this,partnerEntityNode);
			partners.add(partnerEntity);
		}
		
		for(BPMNProcess process : processes)
			process.checkActors();
		
		for(BPMNProcess process : processes)
		{
			HashSet<BPMNResource> collectedResources = process.collectGeneralActors();
			collectedResources.removeAll(process.getResources());
			for(BPMNResource resource : collectedResources)
				process.addResource(resource);
		}
		
		for(BPMNProcess process : processes)
			process.checkMessages();
		
		for(BPMNProcess process : processes)
		{
			for(BPMNParticipant participant : process.collectCollaboratingParticipants())
				process.addCollaboratingParticipant(participant);
			
			if(process.hasActiveCollaboratingParticipant().equals(true) && process.getParticipants().isEmpty())
			{
				BPMNParticipant newParticipant = new BPMNParticipant(this,process);
				process.addParticipant(newParticipant);
				warningWriter.println("a BPMN process that interacts with other process should have a participant -> Assigned: " + newParticipant.getName(), process);
			}
		}
	}
	
	
	public LinkedList<BPMNError> getErrors()
	{
		return errors;
	}
	
	public LinkedList<BPMNEscalation> getEscalations()
	{
		return escalations;
	}
	
	public LinkedList<BPMNMessage> getMessages()
	{
		return messages;
	}
	
	public LinkedList<BPMNSignal> getSignals()
	{
		return signals;
	}
	
	public LinkedList<BPMNItemDefinition> getItemDefinitions()
	{
		return itemDefinitions;
	}
	
	public LinkedList<BPMNEventDefinition> getEventDefinitions()
	{
		return eventDefinitions;
	}
	
	public LinkedList<BPMNProcess> getProcesses()
	{
		return processes;
	}

	public LinkedList<BPMNParticipant> getParticipants()
	{
		return participants;
	}
	
	public LinkedList<BPMNPartner> getPartners()
	{
		return partners;
	}

	public LinkedList<BPMNMessageFlow> getMessageFlows()
	{
		return messageFlows;
	}
	
	public LinkedList<BPMNResource> getResources()
	{
		return resources;
	}
	
	protected BPMNItemDefinition searchForItemDefinition(String itemId)
	{
		for(BPMNItemDefinition itemDefinition : itemDefinitions)
			if(itemDefinition.getId().equals(itemId))
				return itemDefinition;
		return null;
	}
	
	protected BPMNMessage searchForMessage(String messageId)
	{
		for(BPMNMessage message : messages)
			if(message.getId().equals(messageId))
				return message;
		return null;
	}
	
	protected BPMNResource searchForResource(String resourceId)
	{
		for(BPMNResource resource : resources)
			if(resource.getId().equals(resourceId))
				return resource;
		return null;
	}
	
	protected BPMNError searchForError(String errorId)
	{
		for(BPMNError error : errors)
			if(error.getId().equals(errorId))
				return error;
		return null;
	}
	
	protected BPMNEscalation searchForEscalation(String escalationId)
	{
		for(BPMNEscalation escalation : escalations)
			if(escalation.getId().equals(escalationId))
				return escalation;
		return null;
	}
	
	protected BPMNSignal searchForSignal(String signalId)
	{
		for(BPMNSignal signal : signals)
			if(signal.getId().equals(signalId))
				return signal;
		return null;
	}
	
	protected BPMNParticipant searchForParticipant(String participantId)
	{
		for(BPMNParticipant participant : participants)
			if(participant.getId().equals(participantId))
				return participant;
		return null;
	}
	
	protected BPMNEventDefinition searchForEventDefinition(String eventDefinitionId)
	{
		for(BPMNEventDefinition eventDefinition : eventDefinitions)
			if(eventDefinition.getId().equals(eventDefinitionId))
				return eventDefinition;
		return null;
	}
	
	protected BPMNProcess searchForProcess(String processId)
	{
		for(BPMNProcess process : processes)
			if(process.getId().equals(processId))
				return process;
		return null;
	}
	
	protected BPMNInteractionNode searchForInteractionNode(String interactionNodeId)
	{
		BPMNInteractionNode interactionNode = searchForParticipant(interactionNodeId);
		if(interactionNode==null)
			for(BPMNProcess process : processes)
			{
				interactionNode = process.searchForInteractionNode(interactionNodeId);
				if(interactionNode!=null)
					return interactionNode;
			}
		return interactionNode;
	}
	
	protected BPMNItemElement searchForData(String dataId)
	{
		for(BPMNDataStore data : dataStores)
			if(data.getId().equals(dataId))
				return data;
		return null;
	}
	
	public void acceptVisitor(RequirementsBuilder requirementsBuilder)
	{
		requirementsBuilder.analyzeWorkflow(this);
	}
}