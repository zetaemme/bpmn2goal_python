package ids.tx.bpmn;

import org.w3c.dom.*;

public abstract class BPMNItemElement extends BPMNElement
{ 
	protected BPMNItemDefinition itemRef = null;
	private String dataState = "";
	
	public BPMNItemElement(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		String referencedItemId = getAttributeValue(node,"itemSubjectRef");
		if(!referencedItemId.equals(""))
			itemRef = resolveItemDefinitionReference(referencedItemId);
		
		for(Node dataStateNode : getChildsByName(node,"dataState"))
			dataState = getAttributeValue(dataStateNode,"name").toLowerCase().replaceAll("[ \n]+","_");
	}
	
	public BPMNItemElement(BPMNElement ambient, String name)
	{
		super(ambient,name);
	}
	
	public BPMNItemDefinition getItemRef()
	{
		return itemRef;
	}
	
	public String getDataState()
	{
		return dataState;
	}
	
	public abstract String printSpec();
}