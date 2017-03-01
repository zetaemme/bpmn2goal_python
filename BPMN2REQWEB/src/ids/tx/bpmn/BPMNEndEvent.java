package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNEndEvent extends BPMNEvent
{
	public BPMNEndEvent(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		nodeType = BPMNNodeType.END_EVENT;
	}
}