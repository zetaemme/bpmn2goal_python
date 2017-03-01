package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNStandardLoopCharacteristic extends BPMNElement
{
	Boolean testBefore = false;
	String loopMaximum = "";
	String loopCondition = "";
	
	public BPMNStandardLoopCharacteristic(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		if(getAttributeValue(node,"testBefore").equals("true"))
			testBefore = true;
			
		String loopMaximumValue = getAttributeValue(node,"loopMaximum");
		if(!loopMaximumValue.equals("ERROR_NO_LOOPMAXIMUM"))
			loopMaximum = loopMaximumValue;
		
		for(Node loopConditionNode : getChildsByName(node,"loopCondition"))
			loopCondition = loopConditionNode.getTextContent();
	}
	
	public Boolean getTestBefore()
	{
		return testBefore;
	}
	
	public String getLoopMaximum()
	{
		return loopMaximum;
	}
	
	public String getLoopCondition()
	{
		return loopCondition;
	}
}