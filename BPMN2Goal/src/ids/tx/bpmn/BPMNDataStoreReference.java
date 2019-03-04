package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNDataStoreReference extends BPMNItemElement
{
	private BPMNDataStore dataStore;
	
	public BPMNDataStoreReference(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		String dataStoreId = getAttributeValue(node,"dataStoreRef");
		if(!dataStoreId.equals(""))
			dataStore = (BPMNDataStore)resolveDataReference(dataStoreId);
		
		if(dataStore == null)
			warningWriter.println("a data store reference should specify a valid data store",this);
	}
	
	public BPMNDataStore getDataStore()
	{
		return dataStore;
	}
	
	public String printSpec()
	{
		if(!this.getDataState().equals(""))
			return this.getDataState() + "(" + dataStore.getName() + ")";
		return "available(" + dataStore.getName() + ")";
	}
}