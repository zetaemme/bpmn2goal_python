package ids.tx.requirements;

import ids.tx.bpmn.*;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;
import ids.tx.conditions.Predicate;
import ids.tx.conditions.WHENCondition;

public class CompensateEventDefinitionRequirements extends EventDefinitionRequirements
{
BPMNCompensateEventDefinition compensateEventDefinition;
	
	public CompensateEventDefinitionRequirements(BPMNCompensateEventDefinition compensateEventDefinition)
	{
		this.compensateEventDefinition = compensateEventDefinition;
	}
	
	public ConditionalExpression produceEventDefinitionWaitingCondition(BPMNNodeType nodeType,ConditionType condType)
	{
		if(condType.equals(ConditionType.EVENT))
		{
			if(nodeType.equals(BPMNNodeType.INTERMEDIATE_CATCH_EVENT) || nodeType.equals(BPMNNodeType.BOUNDARY_EVENT) || nodeType.equals(BPMNNodeType.START_EVENT))
				if(compensateEventDefinition.getCompensateActivity()!=null)
					return new WHENCondition(new Predicate("caught(" + compensateEventDefinition.getCompensateActivity().getName() + "compensation)"));
				else
					return new WHENCondition(new Predicate("caught(compensation)"));
		}
		
		if(condType.equals(ConditionType.STATE))
			if(nodeType.equals(BPMNNodeType.BOUNDARY_EVENT))
				if(compensateEventDefinition.getCompensateActivity()!=null)
					return new Predicate("caught(" + compensateEventDefinition.getCompensateActivity().getName() + "compensation)");
				else
					return new Predicate("caught(compensation)");		
		
		return null;
	}
	
	public ConditionalExpression produceEventDefinitionGeneratedCondition(BPMNNodeType nodeType,ConditionType condType)
	{
		if(condType.equals(ConditionType.EVENT))
		{
			if(nodeType.equals(BPMNNodeType.INTERMEDIATE_CATCH_EVENT) || nodeType.equals(BPMNNodeType.BOUNDARY_EVENT) || nodeType.equals(BPMNNodeType.START_EVENT))
				if(compensateEventDefinition.getCompensateActivity()!=null)
					return new WHENCondition(new Predicate("caught(" + compensateEventDefinition.getCompensateActivity().getName() + "compensation)"));
				else
					return new WHENCondition(new Predicate("caught(compensation)"));
			
			if(nodeType.equals(BPMNNodeType.INTERMEDIATE_THROW_EVENT) || nodeType.equals(BPMNNodeType.END_EVENT))
				if(compensateEventDefinition.getCompensateActivity()!=null)
					return new WHENCondition(new Predicate("thrown(" + compensateEventDefinition.getCompensateActivity().getName() + "compensation)"));
				else
					return new WHENCondition(new Predicate("thrown(compensation)"));
		}
		
		if(condType.equals(ConditionType.STATE))
		{
			if(nodeType.equals(BPMNNodeType.INTERMEDIATE_CATCH_EVENT) || nodeType.equals(BPMNNodeType.BOUNDARY_EVENT) || nodeType.equals(BPMNNodeType.START_EVENT))
				if(compensateEventDefinition.getCompensateActivity()!=null)
					return new Predicate("caught(" + compensateEventDefinition.getCompensateActivity().getName() + "compensation)");
				else
					return new Predicate("caught(compensation)");
			
			if(nodeType.equals(BPMNNodeType.INTERMEDIATE_THROW_EVENT) || nodeType.equals(BPMNNodeType.END_EVENT))
				if(compensateEventDefinition.getCompensateActivity()!=null)
					return new Predicate("thrown(" + compensateEventDefinition.getCompensateActivity().getName() + "compensation)");
				else
					return new Predicate("thrown(compensation)");
		}
		
		return null;
	}

}