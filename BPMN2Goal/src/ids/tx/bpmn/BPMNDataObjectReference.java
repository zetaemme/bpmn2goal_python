package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNDataObjectReference extends BPMNItemElement
{
	private BPMNData dataObject;
	
	public BPMNDataObjectReference(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		String dataObjectId = getAttributeValue(node,"dataObjectRef");
		if(!dataObjectId.equals(""))
			dataObject = (BPMNData)resolveDataReference(dataObjectId);
		
		if(dataObject == null)
			warningWriter.println("a data object reference should specify a valid data object",this);
	}
	
	public BPMNData getDataObject()
	{
		return dataObject;
	}
	
	public String printSpec()
	{
		if(!this.getDataState().equals(""))
			return this.getDataState() + "(" + dataObject.getName() + ")";
		return "available(" + dataObject.getName() + ")";
	}
}