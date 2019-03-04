package ids.tx.requirements;

import ids.tx.bpmn.*;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;
import ids.tx.conditions.MESSAGERECEIVEDCondition;
import ids.tx.conditions.ORCompositeCondition;
import ids.tx.conditions.WHENCondition;

public class ReceiveTaskRequirements extends TaskRequirements
{
	BPMNReceiveTask receiveTask;
	
	public ReceiveTaskRequirements(BPMNReceiveTask receiveTask)
	{
		super(receiveTask);
		this.receiveTask = receiveTask;
	}
	
	protected ConditionalExpression produceMessageReceivedCondition(ConditionType type)
	{
		ORCompositeCondition messageReceivedCondition = new ORCompositeCondition();
		if(type.equals(ConditionType.EVENT))
		{
			for(BPMNMessageFlow messageFlow : receiveTask.getIncomingMessageFlows())
			{
					if(messageFlow.getMessage() == null)
					{
						InteractionNodeRequirements sourceReq = new InteractionNodeRequirements(messageFlow.getSource());
						messageReceivedCondition.addCondition(new WHENCondition(new MESSAGERECEIVEDCondition(receiveTask.getMessage().printSpec(),sourceReq.getInternalActors().toString())));
					}
					else
					{
						InteractionNodeRequirements sourceReq = new InteractionNodeRequirements(messageFlow.getSource());
						messageReceivedCondition.addCondition(new WHENCondition(new MESSAGERECEIVEDCondition(messageFlow.getMessage().printSpec(),sourceReq.getInternalActors().toString())));
					}
			}
		}
		
		return messageReceivedCondition;
	}
}