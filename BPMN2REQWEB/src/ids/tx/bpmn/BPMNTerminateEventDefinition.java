package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNTerminateEventDefinition extends BPMNEventDefinition
{
	public BPMNTerminateEventDefinition(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		eventDefinitionType = BPMNEventDefinitionType.TERMINATE;
	}
}