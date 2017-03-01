package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNMessageFlow extends BPMNElement
{
	private BPMNInteractionNode source;
	private BPMNInteractionNode target;
	private BPMNMessage message = null;
	
	public BPMNMessageFlow(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		String sourceId = getAttributeValue(node,"sourceRef");
		source = resolveInteractionNodeReference(sourceId);
		
		String targetId = getAttributeValue(node,"targetRef");
		target = resolveInteractionNodeReference(targetId);
		
		String messageId = getAttributeValue(node,"messageRef");
		if(!messageId.equals(""))
			message = resolveMessageReference(messageId);
		
		if(source!=null)
			source.addOutgoingMessageFlow(this);
		else
		{
			createExternParticipant();
			source = externGenericParticipant;
			warningWriter.println("a message flow should refer a valid source interactionNode",(BPMNElement)this);
		}
			
		if(target!=null)
			target.addIncomingMessageFlow(this);
		else
		{
			createExternParticipant();
			target = externGenericParticipant;
			warningWriter.println("a message flow should refer a valid target interactionNode",(BPMNElement)this);
		}
		
		if(message==null)
		{
			if((source.getMessage() == null) && (target.getMessage() == null))
			{
				message = new BPMNMessage(this);
				warningWriter.println("a message flow whose extremes do not refer a message should specify a message -> Assigned:" + message.getName(),(BPMNElement)this);
			}
			else if((source.getMessage() != null) && (target.getMessage() == null))
			{
				message = source.getMessage();
				warningWriter.println("a message flow that does not refer a message and whose target does not specify a message but whose source does, will carry the source message",(BPMNElement)this);
			}
			else if((source.getMessage() == null) && (target.getMessage() != null))
			{
				message = target.getMessage();
				warningWriter.println("a message flow that does not refer a message and whose source does not specify a message but whose target does, will carry the target message",(BPMNElement)this);
			}
		}

	}
	
	public BPMNMessageFlow(BPMNElement ambient,BPMNInteractionNode source,BPMNInteractionNode target,BPMNMessage message)
	{
		super(ambient,"");
		this.source = source;
		this.target = target;
		this.message = message;
	}
	
	
	public BPMNInteractionNode getSource()
	{
		return source;
	}
	
	public BPMNInteractionNode getTarget()
	{
		return target;
	}
	
	public BPMNMessage getMessage()
	{
		return message;
	}
	
	public void setMessage(BPMNMessage message)
	{
		this.message = message;
	}
}