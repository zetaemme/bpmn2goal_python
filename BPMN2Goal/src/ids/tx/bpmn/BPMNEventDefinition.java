package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNEventDefinition extends BPMNElement
{
	protected BPMNEventDefinitionType eventDefinitionType;
	
	public BPMNEventDefinition(BPMNElement ambient, Node node)
	{
		super(ambient,node);
	}
	
	public BPMNEventDefinitionType getEventDefinitionType()
	{
		return eventDefinitionType;
	}
}