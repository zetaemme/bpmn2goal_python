package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNComplexGateway extends BPMNNode
{	
	private String activationCondition = "";
	private BPMNSequenceFlow defaultSequenceFlow = null;
	private String defaultSequenceFlowId = "";
	
	public BPMNComplexGateway(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		nodeType = BPMNNodeType.COMPLEX_GATEWAY;
		
		for(Node activationConditionNode : getChildsByName(node,"activationCondition"))
			activationCondition = activationConditionNode.getTextContent();
		
		if(activationCondition.equals(""))
			warningWriter.println("a BPMN complex gateway should have an activation condition",(BPMNElement)this);
		
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
	
	public String getActivationCondition()
	{
		return activationCondition;
	}
}