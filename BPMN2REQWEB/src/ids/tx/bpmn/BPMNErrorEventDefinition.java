package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNErrorEventDefinition extends BPMNEventDefinition
{
	BPMNError error = null;
	
	public BPMNErrorEventDefinition(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		String errorId = getAttributeValue(node,"errorRef");
		if(!errorId.equals(""))
			error = resolveErrorReference(errorId);
		
		if(error==null)
		{
			error = new BPMNError(this);
			warningWriter.println("a BPMN error event should specify a defined error -> Assigned:" + error.getName(),(BPMNElement)this);
		}
		
		eventDefinitionType = BPMNEventDefinitionType.ERROR;
	}
	
	public BPMNError getError()
	{
		return error;
	}
}