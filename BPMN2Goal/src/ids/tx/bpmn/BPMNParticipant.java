package ids.tx.bpmn;

import java.util.HashSet;
import java.util.LinkedList;

import org.w3c.dom.Node;

public class BPMNParticipant extends BPMNElement implements BPMNInteractionNode
{
	private static Integer counter = 0;
	private BPMNProcess process = null;
	private LinkedList<BPMNPartner> partners;
	private LinkedList<BPMNMessageFlow> incomingMessageFlows;
	private LinkedList<BPMNMessageFlow> outgoingMessageFlows;
	
	public BPMNParticipant(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		partners = new LinkedList<BPMNPartner>();
		incomingMessageFlows = new LinkedList<BPMNMessageFlow>();
		outgoingMessageFlows = new LinkedList<BPMNMessageFlow>();
		
		if(name.equals(""))
		{
			name = "participant" + counter.toString();
			warningWriter.println("a BPMN Participant should specify a name -> Assigned:" + name, this);
			counter = counter + 1;
		}
		else
			name = name.toLowerCase();
		
		String processId = getAttributeValue(node,"processRef");
		if(!processId.equals(""))
			process = resolveProcessReference(processId);
		
		if(process!=null)
			process.addParticipant(this);
	}
	
	public BPMNParticipant(BPMNElement ambient,BPMNProcess process)
	{
		super(ambient,"participant" + counter.toString());
		counter = counter + 1;
		this.process = process;
		partners = new LinkedList<BPMNPartner>();
		incomingMessageFlows = new LinkedList<BPMNMessageFlow>();
		outgoingMessageFlows = new LinkedList<BPMNMessageFlow>();
	}
	
	public BPMNParticipant(BPMNElement ambient,String name)
	{
		super(ambient,name);
		partners = new LinkedList<BPMNPartner>();
		incomingMessageFlows = new LinkedList<BPMNMessageFlow>();
		outgoingMessageFlows = new LinkedList<BPMNMessageFlow>();
	}
	
	public void addIncomingMessageFlow(BPMNMessageFlow messageFlow)
	{
		incomingMessageFlows.add(messageFlow);
	}
	
	public void addOutgoingMessageFlow(BPMNMessageFlow messageFlow)
	{
		outgoingMessageFlows.add(messageFlow);
	}
	
	public BPMNInteractionNodeType getInteractionNodeType()
	{
		return BPMNInteractionNodeType.PARTICIPANT;
	}
	
	public BPMNProcess getProcess()
	{
		return process;
	}
	
	public LinkedList<BPMNMessageFlow> getIncomingMessageFlows()
	{
		return incomingMessageFlows;
	}
	
	public LinkedList<BPMNMessageFlow> getOutgoingMessageFlows()
	{
		return outgoingMessageFlows;
	}
	
	public LinkedList<BPMNPartner> getPartners()
	{
		return partners;
	}
	
	public BPMNMessage getMessage()
	{
		return null;
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
	
	public LinkedList<BPMNParticipant> getInternalParticipants()
	{
		LinkedList<BPMNParticipant> parts = new LinkedList<BPMNParticipant>();
		parts.add(this);
		return parts;
	}
	
	public void addPartner(BPMNPartner partner)
	{
		partners.add(partner);
	}
}