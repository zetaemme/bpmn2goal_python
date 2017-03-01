package ids.tx.requirements;

import ids.tx.bpmn.*;
import ids.tx.conditions.ANDCompositeCondition;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;
import ids.tx.conditions.NOTCompositeCondition;

public class BoundaryEventRequirements extends EventRequirements
{
	private BPMNBoundaryEvent boundaryEvent;
	
	public BoundaryEventRequirements(BPMNBoundaryEvent boundaryEvent)
	{
		super(boundaryEvent);
		this.boundaryEvent = boundaryEvent;		
	}
	
	public ConditionalExpression produceBackwardCondition(ConditionType type,BPMNSequenceFlow callProvenience)
	{
		ANDCompositeCondition backwardCondition = new ANDCompositeCondition();
		backwardCondition.addCondition(super.produceGeneratedCondition(type));
		if(boundaryEvent.getCancelActivity() == true)
			if(!BPMNEvent.containsEventOfType(boundaryEvent,BPMNEventDefinitionType.TIMER))
			{
				NodeRequirements req = factory(boundaryEvent.getActivityAttachedTo());
				backwardCondition.addCondition(new NOTCompositeCondition(req.produceBackwardCondition(type,null)));
			}
		return backwardCondition;
	}
}
	