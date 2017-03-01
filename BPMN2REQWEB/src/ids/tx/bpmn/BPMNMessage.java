package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNMessage extends BPMNItemElement 
{	
	private static Integer counter = 0;
	
	public BPMNMessage(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		String referencedItemId = getAttributeValue(node,"itemRef");
		if(!referencedItemId.equals(""))
			itemRef = resolveItemDefinitionReference(referencedItemId);
		
		if(itemRef == null)	
			warningWriter.println("a BPMN message should refer a valid ItemDefinition", (BPMNElement)this);
		
		if(name.equals(""))
		{
			name = "message" + counter.toString();
			warningWriter.println("a BPMN Message should specify a name -> Assigned:" + name, this);
			counter = counter + 1;
		}
	}
	
	public BPMNMessage(BPMNElement ambient)
	{
		super(ambient,"message" + counter.toString());
		counter = counter + 1;
	}
	
	public String printSpec()
	{
		return this.getName();
	}
}