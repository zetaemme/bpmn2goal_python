package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNError extends BPMNItemElement 
{
	private static Integer counter = 0;
	private String errorCode = "";
	
	public BPMNError(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		String referencedItemId = getAttributeValue(node,"structureRef");
		if(!referencedItemId.equals(""))
			itemRef = resolveItemDefinitionReference(referencedItemId);
		
		if(itemRef == null)	
			warningWriter.println("a BPMN error should refer a valid ItemDefinition", (BPMNElement)this);
		
		if(name.equals(""))
		{
			name = "error" + counter.toString();
			warningWriter.println("a BPMN Error should specify a name -> Assigned:" + name, this);
			counter = counter + 1;
		}
		else
			name = name.toLowerCase();
		
		errorCode = getAttributeValue(node,"errorCode");
	}
	
	public BPMNError(BPMNElement ambient)
	{
		super(ambient,"error" + counter.toString());
		counter = counter + 1;
		errorCode = "";
	}
	
	public String getErrorCode()
	{
		return errorCode;
	}
	
	public String printSpec()
	{
		if(!errorCode.equals(""))
			return this.getName() + "_" + errorCode;
		return this.getName();
	}
}