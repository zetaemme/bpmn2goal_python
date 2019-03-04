package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNEscalation extends BPMNItemElement 
{
	private static Integer counter = 0;
	private String escalationCode = "";
	
	public BPMNEscalation(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		String referencedItemId = getAttributeValue(node,"structureRef");
		if(!referencedItemId.equals(""))
			itemRef = resolveItemDefinitionReference(referencedItemId);
		
		if(itemRef == null)	
			warningWriter.println("a BPMN escalation should refer a valid ItemDefinition", (BPMNElement)this);
		
		if(name.equals(""))
		{
			name = "escalation" + counter.toString();
			warningWriter.println("a BPMN Escalation should specify a name -> Assigned:" + name,this);
			counter = counter + 1;
		}
		else
			name = name.toLowerCase();
		
		escalationCode = getAttributeValue(node,"escalationCode");
	}
	
	public BPMNEscalation(BPMNElement ambient)
	{
		super(ambient,"escalation" + counter.toString());
		counter = counter + 1;
		escalationCode = "";
	}
	
	public String getEscalationCode()
	{
		return escalationCode;
	}
	
	public String printSpec()
	{
		if(!escalationCode.equals(""))
			return this.getName() + "_" + escalationCode;
		return this.getName();
	}
}