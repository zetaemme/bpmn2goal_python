package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNSignal extends BPMNItemElement 
{	
	private static Integer counter = 0;
	
	public BPMNSignal(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		String referencedItemId = getAttributeValue(node,"structureRef");
		if(!referencedItemId.equals(""))
			itemRef = resolveItemDefinitionReference(referencedItemId);
		
		if(itemRef == null)	
			warningWriter.println("a BPMN signal should refer a valid ItemDefinition", (BPMNElement)this);
		
		if(name.equals(""))
		{
			name = "signal" + counter.toString();
			warningWriter.println("a BPMN Signal should specify a name -> Assigned:" + name, this);
			counter = counter + 1;
		}
		else
			name = name.toLowerCase();
		
	}
	
	public BPMNSignal(BPMNElement ambient)
	{
		super(ambient,"signal" + counter.toString());
		counter = counter + 1;
	}
	
	public String printSpec()
	{
		return this.getName();
	}
}