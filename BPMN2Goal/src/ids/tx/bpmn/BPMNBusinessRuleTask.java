package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNBusinessRuleTask extends BPMNTask
{
private String implementation = "##unspecified";
	
	public BPMNBusinessRuleTask(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		nodeType = BPMNNodeType.BUSINESS_RULE_TASK;

		implementation = getAttributeValue(node,"implementation");
	}
	
	public String getImplementation()
	{
		return implementation;
	}
}