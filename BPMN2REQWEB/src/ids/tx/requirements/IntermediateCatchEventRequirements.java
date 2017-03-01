package ids.tx.requirements;

import ids.tx.bpmn.*;
import ids.tx.conditions.AFTERCompositeCondition;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;

public class IntermediateCatchEventRequirements extends EventRequirements
{	
	BPMNIntermediateCatchEvent catchEvent;
	
	public IntermediateCatchEventRequirements(BPMNIntermediateCatchEvent catchEvent)
	{
		super(catchEvent);
		this.catchEvent = catchEvent;
	}
	
	public ConditionalExpression produceBackwardCondition(ConditionType type,BPMNSequenceFlow callProvenience)
	{
		if(type.equals(ConditionType.EVENT))
			if(!catchEvent.getEventDefinitions().isEmpty())
				if(BPMNEvent.containsEventOfType(catchEvent, BPMNEventDefinitionType.TIMER).equals(false)&&BPMNEvent.containsEventOfType(catchEvent, BPMNEventDefinitionType.LINK).equals(false))
					return new AFTERCompositeCondition(produceGeneratedCondition(type),producePredecessorCondition(type));
		return super.produceBackwardCondition(type, callProvenience);
	}
}