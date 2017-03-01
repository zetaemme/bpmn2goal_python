package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNMessageEventDefinition extends BPMNEventDefinition
{
	private BPMNMessage message = null;
	
	public BPMNMessageEventDefinition(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		String messageId = getAttributeValue(node,"messageRef");
		if(!messageId.equals(""))
			message = resolveMessageReference(messageId);
		
		eventDefinitionType = BPMNEventDefinitionType.MESSAGE;
	}
	
	public BPMNMessage getMessage()
	{
		return message;
	}
	
	public void checkInputMessages()
	{
		if(((BPMNEvent)getContainingElement()).getIncomingMessageFlows().isEmpty())
		{
			if(message == null)
			{
				message = new BPMNMessage(this);
				warningWriter.println("a BPMN message catch event with no incoming message flow should specify a message -> Assigned: " + message.getName(), getContainingElement());
			}
			createExternParticipant();
			BPMNMessageFlow messageFlow = new BPMNMessageFlow(this.getContainingElement(),externGenericParticipant,(BPMNEvent)this.getContainingElement(),message);
			((BPMNEvent)getContainingElement()).addIncomingMessageFlow(messageFlow);
			warningWriter.println("a generic external participant is specificed like source of a message received by a BPMN message catch event with no incoming message flow", getContainingElement());		
		}
	}
	
	public void checkOutputMessages()
	{
		if(((BPMNEvent)getContainingElement()).getOutgoingMessageFlows().isEmpty())
		{
			if(message == null)
			{
				message = new BPMNMessage(this);
				warningWriter.println("a BPMN message throw event with no incoming message flow should specify a message -> Assigned: " + message.getName(), getContainingElement());
			}
			createExternParticipant();
			BPMNMessageFlow messageFlow = new BPMNMessageFlow(this.getContainingElement(),(BPMNEvent)this.getContainingElement(),externGenericParticipant,message);
			((BPMNEvent)getContainingElement()).addOutgoingMessageFlow(messageFlow);
			warningWriter.println("a generic external participant is specificed like destination of a message sent by a BPMN message throw event with no incoming message flow", getContainingElement());		
		}
	} 
}