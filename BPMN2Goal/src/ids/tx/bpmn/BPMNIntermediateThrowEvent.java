package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNIntermediateThrowEvent extends BPMNEvent
{
	public BPMNIntermediateThrowEvent(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		nodeType = BPMNNodeType.INTERMEDIATE_THROW_EVENT;
	}
}