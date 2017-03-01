package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNExclusiveGateway extends BPMNNode
{
	private BPMNSequenceFlow defaultSequenceFlow = null;
	private String defaultSequenceFlowId = "";
	
	public BPMNExclusiveGateway(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		nodeType = BPMNNodeType.EXCLUSIVE_GATEWAY;
		
		String defaultId = getAttributeValue(node,"default");
		if(!defaultId.equals(""))
			defaultSequenceFlowId = defaultId;
	}
	
	public BPMNSequenceFlow getDefaultSequenceFlow()
	{
		if(defaultSequenceFlow == null)
			defaultSequenceFlow = resolveSequenceFlowReference(defaultSequenceFlowId);
		return defaultSequenceFlow;
	}
}