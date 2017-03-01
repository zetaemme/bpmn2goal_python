package ids.tx.requirements;

import ids.tx.bpmn.*;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;
import ids.tx.conditions.Predicate;
import ids.tx.conditions.WHENCondition;

public class ConditionalEventDefinitionRequirements extends EventDefinitionRequirements
{
	BPMNConditionalEventDefinition conditionalEventDefinition;
	
	public ConditionalEventDefinitionRequirements(BPMNConditionalEventDefinition conditionalEventDefinition)
	{
		this.conditionalEventDefinition = conditionalEventDefinition;
	}
	
	public ConditionalExpression produceEventDefinitionWaitingCondition(BPMNNodeType nodeType,ConditionType condType)
	{
		if(condType.equals(ConditionType.EVENT))
			return new WHENCondition(new Predicate(conditionalEventDefinition.getCondition()));
		
		if(condType.equals(ConditionType.STATE))
			if(nodeType.equals(BPMNNodeType.BOUNDARY_EVENT))
				return new Predicate(conditionalEventDefinition.getCondition());		
		
		return null;
	}
	
	public ConditionalExpression produceEventDefinitionGeneratedCondition(BPMNNodeType nodeType,ConditionType condType)
	{
		if(condType.equals(ConditionType.EVENT))
			return new WHENCondition(new Predicate(conditionalEventDefinition.getCondition()));
		
		if(condType.equals(ConditionType.STATE))
			return new Predicate(conditionalEventDefinition.getCondition());
		
		return null;
	}

}