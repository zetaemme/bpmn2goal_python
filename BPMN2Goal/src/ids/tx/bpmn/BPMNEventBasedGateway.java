package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNEventBasedGateway extends BPMNNode
{
	BPMNEventBasedGatewayType type = BPMNEventBasedGatewayType.EXCLUSIVE;
	Boolean instantiate = false;
	
	public BPMNEventBasedGateway(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		nodeType = BPMNNodeType.EVENT_BASED_GATEWAY;
		
		if(getAttributeValue(node,"eventGatewayType").equals("parallel"))
			type = BPMNEventBasedGatewayType.PARALLEL;
		
		if(getAttributeValue(node,"instantiate").equals("true"))
			instantiate = true;
	}

	public BPMNEventBasedGatewayType getEventBasedGatewayType()
	{
		return type;
	}
	
	public Boolean getInstantiate()
	{
		return instantiate;
	}
}