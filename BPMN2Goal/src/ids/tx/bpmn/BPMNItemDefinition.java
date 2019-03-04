package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNItemDefinition extends BPMNElement
{
	private String structureRef = "";
	private BPMNItemKind itemKind = BPMNItemKind.INFORMATION;
	private Boolean isCollection = false;
	
	public BPMNItemDefinition(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		if(getAttributeValue(node,"itemKind").equals("physical"))
			itemKind = BPMNItemKind.PHYSICAL;
		
		if(getAttributeValue(node,"isCollection").equals("true"))
			isCollection = true;
		
		structureRef = getAttributeValue(node,"structureRef");
		if(structureRef.equals(""))
			warningWriter.println("a BPMN item definition should define a concrete data structure",(BPMNElement) this);
	}
	
	public String getStructureRef()
	{
		return structureRef;
	}
	
	public BPMNItemKind getItemKind()
	{
		return itemKind;
	}
	
	public Boolean getIsCollection()
	{
		return isCollection;
	}
}
