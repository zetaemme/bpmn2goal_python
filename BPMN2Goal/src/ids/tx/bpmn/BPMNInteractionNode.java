package ids.tx.bpmn;

import java.util.HashSet;
import java.util.LinkedList;

public interface BPMNInteractionNode
{
	public LinkedList<BPMNParticipant> getInternalParticipants();
	public HashSet<BPMNParticipant> collectCollaboratingParticipants();
	public BPMNInteractionNodeType getInteractionNodeType();
	public BPMNMessage getMessage();
	public void addIncomingMessageFlow(BPMNMessageFlow messageFlow);
	public void addOutgoingMessageFlow(BPMNMessageFlow messageFlow);
	public LinkedList<BPMNMessageFlow> getIncomingMessageFlows();
	public LinkedList<BPMNMessageFlow> getOutgoingMessageFlows();
}