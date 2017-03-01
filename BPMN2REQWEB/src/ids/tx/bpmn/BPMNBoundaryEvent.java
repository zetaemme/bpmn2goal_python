package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNBoundaryEvent extends BPMNEvent
{
	private static Integer counter = 0;
	private Boolean cancelActivity = true;
	private BPMNActivity activityAttachedTo;
	
	public BPMNBoundaryEvent(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		nodeType = BPMNNodeType.BOUNDARY_EVENT;
		
		if(getAttributeValue(node,"cancelActivity").equals("false"))
			cancelActivity = false;
		
		String activityId = getAttributeValue(node,"attachedToRef");
		activityAttachedTo = resolveActivityReference(activityId);
		
		if(activityAttachedTo == null)
		{
			warningWriter.println("a BPMN boundary event should specify the activity at which it is attached", this);
			if(containsEventOfType(this, BPMNEventDefinitionType.MESSAGE))
			{
				warningWriter.println("a general extern participant is specified like source of a message received by a BPMN message boundary event whit no specified attached activity ", this);
				createExternParticipant();
			}
		}
		
		if(cancelActivity == true)
			if(activityAttachedTo!=null)
				activityAttachedTo.addInterruptingEvent(new InterruptingEventAdder(),(BPMNEvent)this);
	}
	
	public BPMNBoundaryEvent(BPMNElement ambient, BPMNEventDefinition eventDefinition, Boolean cancelActivity, BPMNActivity activityAttachedTo)
	{
		super(ambient,"boundary" + counter.toString());
		counter = counter + 1;
		this.nodeType = BPMNNodeType.BOUNDARY_EVENT;
		this.getEventDefinitions().add(eventDefinition);
		eventDefinition.setContainingElement((BPMNElement)this);
		this.cancelActivity = cancelActivity;
		this.activityAttachedTo = activityAttachedTo;
	}
	
	public Boolean getCancelActivity()
	{
		return cancelActivity;
	}
	
	public BPMNActivity getActivityAttachedTo()
	{
		return activityAttachedTo;
	}
	
	public void setCancelActivity(Boolean cancelActivity)
	{
		this.cancelActivity = cancelActivity;
	}
	
	public void setActivityAttachedTo(BPMNActivity activityAttachedTo)
	{
		this.activityAttachedTo = activityAttachedTo;
	}
}