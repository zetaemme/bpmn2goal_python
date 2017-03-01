package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNEscalationEventDefinition extends BPMNEventDefinition
{
	BPMNEscalation escalation;
	
	public BPMNEscalationEventDefinition(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		String escalationId = getAttributeValue(node,"escalationRef");
		if(!escalationId.equals(""))
			escalation = resolveEscalationReference(escalationId);
		
		if(escalation==null)
		{
			escalation = new BPMNEscalation(this);
			warningWriter.println("a BPMN escalation event should specify a defined escalation -> Assigned:" + escalation.getName(),(BPMNElement)this);
		}
		
		eventDefinitionType = BPMNEventDefinitionType.ESCALATION;
	}
	
	public BPMNEscalation getEscalation()
	{
		return escalation;
	}
}