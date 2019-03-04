package ids.tx.requirements;

import ids.tx.bpmn.*;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;

public abstract class EventDefinitionRequirements
{
	
	public abstract ConditionalExpression produceEventDefinitionWaitingCondition(BPMNNodeType nodeType,ConditionType type);
	public abstract ConditionalExpression produceEventDefinitionGeneratedCondition(BPMNNodeType nodeType,ConditionType type);
	
	public static EventDefinitionRequirements factory(BPMNEventDefinition eventDefinition)
	{
		if(eventDefinition.getEventDefinitionType().equals(BPMNEventDefinitionType.CANCEL))
			return new CancelEventDefinitionRequirements();
		else if(eventDefinition.getEventDefinitionType().equals(BPMNEventDefinitionType.COMPENSATE))
			return new CompensateEventDefinitionRequirements((BPMNCompensateEventDefinition)eventDefinition);
		else if(eventDefinition.getEventDefinitionType().equals(BPMNEventDefinitionType.CONDITIONAL))
			return new ConditionalEventDefinitionRequirements((BPMNConditionalEventDefinition)eventDefinition);
		else if(eventDefinition.getEventDefinitionType().equals(BPMNEventDefinitionType.ERROR))
			return new ErrorEventDefinitionRequirements((BPMNErrorEventDefinition)eventDefinition);
		else if(eventDefinition.getEventDefinitionType().equals(BPMNEventDefinitionType.ESCALATION))
			return new EscalationEventDefinitionRequirements((BPMNEscalationEventDefinition)eventDefinition);
		else if(eventDefinition.getEventDefinitionType().equals(BPMNEventDefinitionType.LINK))
			return new LinkEventDefinitionRequirements((BPMNLinkEventDefinition)eventDefinition);
		else if(eventDefinition.getEventDefinitionType().equals(BPMNEventDefinitionType.MESSAGE))
			return new MessageEventDefinitionRequirements((BPMNMessageEventDefinition)eventDefinition);
		else if(eventDefinition.getEventDefinitionType().equals(BPMNEventDefinitionType.SIGNAL))
			return new SignalEventDefinitionRequirements((BPMNSignalEventDefinition)eventDefinition);
		else if(eventDefinition.getEventDefinitionType().equals(BPMNEventDefinitionType.TERMINATE))
			return new TerminateEventDefinitionRequirements();
		else if(eventDefinition.getEventDefinitionType().equals(BPMNEventDefinitionType.TIMER))
			return new TimerEventDefinitionRequirements((BPMNTimerEventDefinition)eventDefinition);
		return null;
	}
}