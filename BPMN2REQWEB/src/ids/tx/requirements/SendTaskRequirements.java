package ids.tx.requirements;

import ids.tx.bpmn.*;
import ids.tx.conditions.ANDCompositeCondition;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;
import ids.tx.conditions.MESSAGESENTCondition;
import ids.tx.conditions.WHENCondition;

public class SendTaskRequirements extends TaskRequirements
{
	BPMNSendTask sendTask;
	
	public SendTaskRequirements(BPMNSendTask sendTask)
	{
		super(sendTask);
		this.sendTask = sendTask;
	}
	
	protected ConditionalExpression produceMessageSentCondition(ConditionType type)
	{
		ANDCompositeCondition messageSentCondition = new ANDCompositeCondition();
		if(type.equals(ConditionType.EVENT))
		{
			for(BPMNMessageFlow messageFlow : sendTask.getOutgoingMessageFlows())
			{
				if(messageFlow.getMessage()==null)
				{
					InteractionNodeRequirements targetReq = new InteractionNodeRequirements(messageFlow.getTarget());
					messageSentCondition.addCondition(new WHENCondition(new MESSAGESENTCondition(sendTask.getMessage().printSpec(),targetReq.getInternalActors().toString())));
				}
				else
				{
					InteractionNodeRequirements targetReq = new InteractionNodeRequirements(messageFlow.getTarget());
					messageSentCondition.addCondition(new WHENCondition(new MESSAGESENTCondition(messageFlow.getMessage().printSpec(),targetReq.getInternalActors().toString())));
				}
			}
		}
			
		if(type.equals(ConditionType.STATE))
		{
			for(BPMNMessageFlow messageFlow : sendTask.getOutgoingMessageFlows())
			{
				if(messageFlow.getMessage()==null)
				{
					InteractionNodeRequirements targetReq = new InteractionNodeRequirements(messageFlow.getTarget());
					messageSentCondition.addCondition(new MESSAGESENTCondition(sendTask.getMessage().printSpec(),targetReq.getInternalActors().toString()));
				}
				else
				{
					InteractionNodeRequirements targetReq = new InteractionNodeRequirements(messageFlow.getTarget());
					messageSentCondition.addCondition(new MESSAGESENTCondition(messageFlow.getMessage().printSpec(),targetReq.getInternalActors().toString()));
				}
			}
		}
	
		return messageSentCondition;
	}
}