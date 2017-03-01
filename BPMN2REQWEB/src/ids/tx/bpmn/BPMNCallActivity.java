package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNCallActivity extends BPMNTask
{
	private BPMNCallableElement calledElement = null;
	
	public BPMNCallActivity(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		nodeType = BPMNNodeType.CALL_ACTIVITY;
		
		String calledElementId = getAttributeValue(node,"calledElement");
		if(!calledElementId.equals(""))
			calledElement = resolveCallableElementReference(calledElementId);
	}
	
	public BPMNCallableElement getCalledElement()
	{
		return calledElement;
	}
}