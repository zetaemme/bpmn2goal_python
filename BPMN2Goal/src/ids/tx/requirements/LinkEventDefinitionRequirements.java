package ids.tx.requirements;

import ids.tx.bpmn.*;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;
import ids.tx.conditions.ORCompositeCondition;
import ids.tx.conditions.Predicate;
import ids.tx.conditions.WHENCondition;

public class LinkEventDefinitionRequirements extends EventDefinitionRequirements
{
	BPMNLinkEventDefinition linkEventDefinition;
	
	public LinkEventDefinitionRequirements(BPMNLinkEventDefinition linkEventDefinition)
	{
		this.linkEventDefinition = linkEventDefinition;
	}

	public ConditionalExpression produceEventDefinitionWaitingCondition(BPMNNodeType nodeType,ConditionType condType)
	{
		if(condType.equals(ConditionType.EVENT))
		{
			if(nodeType.equals(BPMNNodeType.INTERMEDIATE_CATCH_EVENT) || nodeType.equals(BPMNNodeType.BOUNDARY_EVENT) || nodeType.equals(BPMNNodeType.START_EVENT))
			{
				ORCompositeCondition waitingCond = new ORCompositeCondition();
				for(BPMNEventDefinition source : linkEventDefinition.getSources())
					waitingCond.addCondition(new WHENCondition(new Predicate("caught(" + source.getName() + "link)")));
				return waitingCond;
			}
		}
		
		return null;
	}
	
	public ConditionalExpression produceEventDefinitionGeneratedCondition(BPMNNodeType nodeType,ConditionType condType)
	{
		if(condType.equals(ConditionType.EVENT))
		{
			if(nodeType.equals(BPMNNodeType.INTERMEDIATE_CATCH_EVENT) || nodeType.equals(BPMNNodeType.BOUNDARY_EVENT) || nodeType.equals(BPMNNodeType.START_EVENT))
			{
				ORCompositeCondition waitingCond = new ORCompositeCondition();
				for(BPMNEventDefinition source : linkEventDefinition.getSources())
					waitingCond.addCondition(new WHENCondition(new Predicate("caught(" + source.getName() + "link)")));
				return waitingCond;
			}
			
			if(nodeType.equals(BPMNNodeType.INTERMEDIATE_THROW_EVENT) || nodeType.equals(BPMNNodeType.END_EVENT))
				if(linkEventDefinition.getTarget()!=null)
					return new WHENCondition(new Predicate("thrown(" + linkEventDefinition.getTarget().getName() + "_link)"));
				else
					return new WHENCondition(new Predicate("thrown(link)"));
		}
		
		if(condType.equals(ConditionType.STATE))
		{
			if(nodeType.equals(BPMNNodeType.INTERMEDIATE_CATCH_EVENT) || nodeType.equals(BPMNNodeType.BOUNDARY_EVENT) || nodeType.equals(BPMNNodeType.START_EVENT))
			{
				ORCompositeCondition waitingCond = new ORCompositeCondition();
				for(BPMNEventDefinition source : linkEventDefinition.getSources())
					waitingCond.addCondition(new Predicate("caught(" + source.getName() + "_link)"));
				return waitingCond;
			}
			
			if(nodeType.equals(BPMNNodeType.INTERMEDIATE_THROW_EVENT) || nodeType.equals(BPMNNodeType.END_EVENT))
				if(linkEventDefinition.getTarget() != null)
					return new Predicate("thrown(" + linkEventDefinition.getTarget().getName() + "_link)");
				else
					return new Predicate("thrown(link)");
		}
		
		return null;
	}
}