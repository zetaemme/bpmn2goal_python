package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNStartEvent extends BPMNEvent
{
	private Boolean isInterrupting = false;
	
	public BPMNStartEvent(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		nodeType = BPMNNodeType.START_EVENT;
		
		if(getAttributeValue(node,"isInterrupting").equals("true"))
				isInterrupting = true;
	}
	
	public Boolean getIsInterrupting()
	{
		return isInterrupting;
	}
}