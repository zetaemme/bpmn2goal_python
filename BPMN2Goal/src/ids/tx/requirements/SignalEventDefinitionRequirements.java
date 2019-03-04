package ids.tx.requirements;

import ids.tx.bpmn.*;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;
import ids.tx.conditions.Predicate;
import ids.tx.conditions.WHENCondition;

public class SignalEventDefinitionRequirements extends EventDefinitionRequirements
{
	BPMNSignalEventDefinition signalEventDefinition;
	
	public SignalEventDefinitionRequirements(BPMNSignalEventDefinition signalEventDefinition)
	{
		this.signalEventDefinition = signalEventDefinition;
	}

	public ConditionalExpression produceEventDefinitionWaitingCondition(BPMNNodeType nodeType,ConditionType condType)
	{
		if(condType.equals(ConditionType.EVENT))
			if(nodeType.equals(BPMNNodeType.INTERMEDIATE_CATCH_EVENT) || nodeType.equals(BPMNNodeType.BOUNDARY_EVENT) || nodeType.equals(BPMNNodeType.START_EVENT))
				return new WHENCondition(new Predicate("caught(" + signalEventDefinition.getSignal().printSpec() + ")"));
		
		if(condType.equals(ConditionType.STATE))
			if(nodeType.equals(BPMNNodeType.BOUNDARY_EVENT))
				return new Predicate("caught(" + signalEventDefinition.getSignal().printSpec() + ")");
		
		return null;
	}
	
	public ConditionalExpression produceEventDefinitionGeneratedCondition(BPMNNodeType nodeType,ConditionType condType)
	{
		if(condType.equals(ConditionType.EVENT))
		{
			if(nodeType.equals(BPMNNodeType.INTERMEDIATE_CATCH_EVENT) || nodeType.equals(BPMNNodeType.BOUNDARY_EVENT) || nodeType.equals(BPMNNodeType.START_EVENT))
				return new WHENCondition(new Predicate("caught(" + signalEventDefinition.getSignal().printSpec() + ")"));
			
			if(nodeType.equals(BPMNNodeType.INTERMEDIATE_THROW_EVENT) || nodeType.equals(BPMNNodeType.END_EVENT))
				return new WHENCondition(new Predicate("thrown(" + signalEventDefinition.getSignal().printSpec() + ")"));	
		}
		
		if(condType.equals(ConditionType.STATE))
		{
			if(nodeType.equals(BPMNNodeType.INTERMEDIATE_CATCH_EVENT) || nodeType.equals(BPMNNodeType.BOUNDARY_EVENT) || nodeType.equals(BPMNNodeType.START_EVENT))
					return new Predicate("caught(" + signalEventDefinition.getSignal().printSpec() + ")");
			
			if(nodeType.equals(BPMNNodeType.INTERMEDIATE_THROW_EVENT) || nodeType.equals(BPMNNodeType.END_EVENT))
					return new Predicate("thrown(" + signalEventDefinition.getSignal().printSpec() + ")");
		}
		
		return null;
	}
}