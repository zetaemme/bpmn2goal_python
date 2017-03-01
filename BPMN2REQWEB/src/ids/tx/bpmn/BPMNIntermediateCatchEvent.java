package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNIntermediateCatchEvent extends BPMNEvent
{
	public BPMNIntermediateCatchEvent(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		nodeType = BPMNNodeType.INTERMEDIATE_CATCH_EVENT;
	}
}