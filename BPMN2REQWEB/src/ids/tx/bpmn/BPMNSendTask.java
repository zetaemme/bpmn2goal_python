package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNSendTask extends BPMNTask
{
	private BPMNMessage sentMessage;
	
	public BPMNSendTask(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		nodeType = BPMNNodeType.SEND_TASK;
		
		String messageId = getAttributeValue(node,"messageRef");
		if(!messageId.equals(""))
			sentMessage = resolveMessageReference(messageId);
	}

	public BPMNMessage getSentMessage()
	{
		return sentMessage;
	}
	
	public BPMNMessage getMessage()
	{
		return this.getSentMessage();
	}
	
	public void checkMessages()
	{
		if(getOutgoingMessageFlows().isEmpty())
		{
			if(sentMessage == null)
			{
				sentMessage = new BPMNMessage(this);
				warningWriter.println("a BPMN send task with non outgoing message flow should specify a message -> Assigned: " + sentMessage.getName(), this);
			}
			createExternParticipant();
			BPMNMessageFlow messageFlow = new BPMNMessageFlow(this,this,externGenericParticipant,sentMessage);
			addOutgoingMessageFlow(messageFlow);
			warningWriter.println("a generic external participant is specificed like destination of a message sent by a BPMN send task with no outgoing message flow", this);
		}
	}
}