package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNCompensateEventDefinition extends BPMNEventDefinition
{
	BPMNNode compensateActivity;
	
	public BPMNCompensateEventDefinition(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		eventDefinitionType = BPMNEventDefinitionType.COMPENSATE;
		
		String activityId = getAttributeValue(node,"activityRef");
		if(!activityId.equals(""))
			compensateActivity = resolveNodeReference(activityId);
		
		if(compensateActivity == null)
			warningWriter.println("a BPMN compensation event should specify an activity to compensate", (BPMNElement) this);
	}
	
	public BPMNNode getCompensateActivity()
	{
		return compensateActivity;
	}
}