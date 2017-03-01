package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNCancelEventDefinition extends BPMNEventDefinition
{
	public BPMNCancelEventDefinition(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		eventDefinitionType = BPMNEventDefinitionType.CANCEL;
	}	
}