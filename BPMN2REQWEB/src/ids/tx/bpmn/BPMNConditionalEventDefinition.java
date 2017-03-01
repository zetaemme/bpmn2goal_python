package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNConditionalEventDefinition extends BPMNEventDefinition
{
	String condition = "";
	
	public BPMNConditionalEventDefinition(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		for(Node conditionNode : getChildsByName(node,"condition"))
			condition = conditionNode.getTextContent();
		
		if(condition.equals(""))
			warningWriter.println("a BPMN condition event should specify a non empty condition",(BPMNElement)this);
		
		eventDefinitionType = BPMNEventDefinitionType.CONDITIONAL;
	}

	public String getCondition()
	{
		return condition;
	}
}