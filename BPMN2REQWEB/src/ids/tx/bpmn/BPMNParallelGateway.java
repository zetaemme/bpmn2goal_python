package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNParallelGateway extends BPMNNode
{
	public BPMNParallelGateway(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		nodeType = BPMNNodeType.PARALLEL_GATEWAY;
	}
}