package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNTimerEventDefinition extends BPMNEventDefinition
{
	private String timeDate = "";
	private String timeDuration = "";
	private String timeCycle = "";
	
	public BPMNTimerEventDefinition(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		for(Node timeDateNode : getChildsByName(node,"timeDate"))
			timeDate = timeDateNode.getTextContent();
		
		for(Node timeDurationNode : getChildsByName(node,"timeDuration"))
			timeDuration = timeDurationNode.getTextContent();
		
		for(Node timeCycleNode : getChildsByName(node,"timeCycle"))
			timeCycle = timeCycleNode.getTextContent();
		
		if(timeDate.equals("") && timeDuration.equals("") && timeCycle.equals(""))
			warningWriter.println("a BPMN timer event should have a valid time condition (timeDate,timeDuration or timeCycle)", (BPMNElement)this);
		
		eventDefinitionType = BPMNEventDefinitionType.TIMER;
	}

	public String getTimeDate()
	{
		return timeDate;
	}
	
	public String getTimeDuration()
	{
		return timeDuration;
	}
	
	public String getTimeCycle()
	{
		return timeCycle;
	}
}