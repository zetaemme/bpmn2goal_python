package ids.tx.requirements;

import ids.tx.bpmn.*;
import ids.tx.conditions.AFTERSINCECompositeCondition;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;
import ids.tx.conditions.EVERYCondition;
import ids.tx.conditions.ONCondition;

public class TimerEventDefinitionRequirements extends EventDefinitionRequirements
{
	BPMNTimerEventDefinition timerEventDefinition;
	
	public TimerEventDefinitionRequirements(BPMNTimerEventDefinition timerEventDefinition)
	{
		this.timerEventDefinition = timerEventDefinition;
	}

	public ConditionalExpression produceEventDefinitionWaitingCondition(BPMNNodeType nodeType,ConditionType condType)
	{
		if(condType.equals(ConditionType.EVENT))
		{
			if(nodeType.equals(BPMNNodeType.INTERMEDIATE_CATCH_EVENT) || nodeType.equals(BPMNNodeType.START_EVENT))
			{
				if(!timerEventDefinition.getTimeDate().equals(""))
					return new ONCondition(timerEventDefinition.getTimeDate());
				
				if(!timerEventDefinition.getTimeCycle().equals(""))
					return new EVERYCondition(timerEventDefinition.getTimeCycle());
			}
		}
		
		if(condType.equals(ConditionType.STATE))
			if(nodeType.equals(BPMNNodeType.BOUNDARY_EVENT))
			{
				if(!timerEventDefinition.getTimeDate().equals(""))
					return new ONCondition(timerEventDefinition.getTimeDate());
				
				if(!timerEventDefinition.getTimeCycle().equals(""))
					return new EVERYCondition(timerEventDefinition.getTimeCycle());
			}
		
		return null;
	}
	
	public ConditionalExpression produceEventDefinitionGeneratedCondition(BPMNNodeType nodeType,ConditionType condType)
	{
		if(condType.equals(ConditionType.EVENT))
		{
			if(nodeType.equals(BPMNNodeType.INTERMEDIATE_CATCH_EVENT))
			{
				if(!timerEventDefinition.getTimeDate().equals(""))
					return new ONCondition(timerEventDefinition.getTimeDate());
				
				if(!timerEventDefinition.getTimeDuration().equals(""))
				{
					NodeRequirements req = NodeRequirements.factory((BPMNEvent)timerEventDefinition.getContainingElement());
					return new AFTERSINCECompositeCondition(timerEventDefinition.getTimeDuration(),req.producePredecessorCondition(condType));
				}
				
				if(!timerEventDefinition.getTimeCycle().equals(""))
					return new EVERYCondition(timerEventDefinition.getTimeCycle());
				
			}
			
			if(nodeType.equals(BPMNNodeType.START_EVENT))
			{
				if(!timerEventDefinition.getTimeDate().equals(""))
					return new ONCondition(timerEventDefinition.getTimeDate());
				
				if(!timerEventDefinition.getTimeCycle().equals(""))
					return new EVERYCondition(timerEventDefinition.getTimeCycle());	
			}
			
			if(nodeType.equals(BPMNNodeType.BOUNDARY_EVENT))
			{
				if(!timerEventDefinition.getTimeDate().equals(""))
					return new ONCondition(timerEventDefinition.getTimeDate());
				
				if(!timerEventDefinition.getTimeDuration().equals(""))
				{
					NodeRequirements req = NodeRequirements.factory(((BPMNBoundaryEvent)timerEventDefinition.getContainingElement()).getActivityAttachedTo());
					return new AFTERSINCECompositeCondition(timerEventDefinition.getTimeDuration(),req.produceInputEvent());
				}
				
				if(!timerEventDefinition.getTimeCycle().equals(""))
					return new EVERYCondition(timerEventDefinition.getTimeCycle());
			}
		}
		
		return null;
	}
}