package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNReceiveTask extends BPMNTask
{
	private BPMNMessage receivedMessage;
	
	public BPMNReceiveTask(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		nodeType = BPMNNodeType.RECEIVE_TASK;
		
		String messageId = getAttributeValue(node,"messageRef");
		if(!messageId.equals(""))
			receivedMessage = resolveMessageReference(messageId);
	}

	public BPMNMessage getReceivedMessage()
	{
		return receivedMessage;
	}
	
	public BPMNMessage getMessage()
	{
		return this.getReceivedMessage();
	}
	
	public void checkMessages()
	{
		if(getIncomingMessageFlows().isEmpty())
		{
			if(receivedMessage == null)
			{
				receivedMessage = new BPMNMessage(this);
				warningWriter.println("a BPMN receive task with non incoming message flow should specify a message -> Assigned: " + receivedMessage.getName(), this);
			}
			createExternParticipant();
			BPMNMessageFlow messageFlow = new BPMNMessageFlow(this,externGenericParticipant,this,receivedMessage);
			addIncomingMessageFlow(messageFlow);
			warningWriter.println("a generic external participant is specificed like source of a message received by a BPMN receive task with non incoming message flow", this);		
		}
	}
}