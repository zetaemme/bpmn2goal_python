package ids.tx.conditions;

import ids.tx.bpmn.BPMNItemDefinition;
import ids.tx.bpmn.BPMNItemElement;

public class ItemDescription
{
	private String dataSpecification = "";
	private String itemDefinition = "UNSPECIFIED";
	
	public ItemDescription(BPMNItemElement data)
	{
		this.dataSpecification = data.getName();
		BPMNItemDefinition itemReferenced = data.getItemRef();
		if(itemReferenced!=null)
			if(!itemReferenced.getStructureRef().equals(""))
				itemDefinition = itemReferenced.getStructureRef();
	}
	
	public String toString()
	{
		//antonella
		return 	dataSpecification + " TYPEOF " + itemDefinition;
//		return 	dataSpecification + " IS " + itemDefinition;
	}
}