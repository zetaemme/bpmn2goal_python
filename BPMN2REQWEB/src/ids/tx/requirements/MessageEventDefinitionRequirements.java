package ids.tx.requirements;

import java.util.LinkedList;

import ids.tx.bpmn.*;
import ids.tx.conditions.ANDCompositeCondition;
import ids.tx.conditions.ConditionType;
import ids.tx.conditions.ConditionalExpression;
import ids.tx.conditions.MESSAGERECEIVEDCondition;
import ids.tx.conditions.MESSAGESENTCondition;
import ids.tx.conditions.ORCompositeCondition;
import ids.tx.conditions.WHENCondition;

public class MessageEventDefinitionRequirements extends EventDefinitionRequirements
{
BPMNMessageEventDefinition messageEventDefinition;
	
	public MessageEventDefinitionRequirements(BPMNMessageEventDefinition messageEventDefinition)
	{
		this.messageEventDefinition = messageEventDefinition;
	}
	
	public ConditionalExpression produceEventDefinitionWaitingCondition(BPMNNodeType nodeType,ConditionType condType)
	{
		if(condType.equals(ConditionType.EVENT))
		{
			if(nodeType.equals(BPMNNodeType.INTERMEDIATE_CATCH_EVENT) || nodeType.equals(BPMNNodeType.BOUNDARY_EVENT) || nodeType.equals(BPMNNodeType.START_EVENT))
			{
				ORCompositeCondition messageReceivedCondition = new ORCompositeCondition();
				LinkedList<BPMNMessageFlow> incomingMessageFlows = ((BPMNInteractionNode)messageEventDefinition.getContainingElement()).getIncomingMessageFlows();
				for(BPMNMessageFlow messageFlow : incomingMessageFlows)
					if(messageFlow.getMessage()==null)
					{
						InteractionNodeRequirements sourceReq = new InteractionNodeRequirements(messageFlow.getSource());
						messageReceivedCondition.addCondition(new WHENCondition(new MESSAGERECEIVEDCondition(messageEventDefinition.getMessage().printSpec(),sourceReq.getInternalActors().toString())));
					}
					else
					{
						InteractionNodeRequirements sourceReq = new InteractionNodeRequirements(messageFlow.getSource());
						messageReceivedCondition.addCondition(new WHENCondition(new MESSAGERECEIVEDCondition(messageFlow.getMessage().printSpec(),sourceReq.getInternalActors().toString())));
					}
				return messageReceivedCondition;
			}
		}
		
		if(condType.equals(ConditionType.STATE))
			if(nodeType.equals(BPMNNodeType.BOUNDARY_EVENT))
			{				
				BPMNActivity activity = ((BPMNBoundaryEvent)messageEventDefinition.getContainingElement()).getActivityAttachedTo();
				if(activity!=null)
				{
					NodeRequirements activityReq = NodeRequirements.factory(activity);
					return new MESSAGERECEIVEDCondition(messageEventDefinition.getMessage().printSpec(),activityReq.produceActorList().toString());
				}
				else
				{
					InteractionNodeRequirements externReq = new InteractionNodeRequirements(BPMNElement.externGenericParticipant);
					return new MESSAGERECEIVEDCondition(messageEventDefinition.getMessage().printSpec(),externReq.getInternalActors().toString());
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
				ORCompositeCondition messageReceivedCondition = new ORCompositeCondition();
				LinkedList<BPMNMessageFlow> incomingMessageFlows = ((BPMNInteractionNode)messageEventDefinition.getContainingElement()).getIncomingMessageFlows();
				for(BPMNMessageFlow messageFlow : incomingMessageFlows)
					if(messageFlow.getMessage()==null)
					{
						InteractionNodeRequirements sourceReq = new InteractionNodeRequirements(messageFlow.getSource());
						messageReceivedCondition.addCondition(new WHENCondition(new MESSAGERECEIVEDCondition(messageEventDefinition.getMessage().printSpec(),sourceReq.getInternalActors().toString())));
					}
					else
					{
						InteractionNodeRequirements sourceReq = new InteractionNodeRequirements(messageFlow.getSource());
						messageReceivedCondition.addCondition(new WHENCondition(new MESSAGERECEIVEDCondition(messageFlow.getMessage().printSpec(),sourceReq.getInternalActors().toString())));
					}
				
				return messageReceivedCondition;
			}
			
			if(nodeType.equals(BPMNNodeType.INTERMEDIATE_THROW_EVENT) || nodeType.equals(BPMNNodeType.END_EVENT))
			{
				ANDCompositeCondition messageSentCondition = new ANDCompositeCondition();
				LinkedList<BPMNMessageFlow> outgoingMessageFlows = ((BPMNInteractionNode)messageEventDefinition.getContainingElement()).getOutgoingMessageFlows();
				for(BPMNMessageFlow messageFlow : outgoingMessageFlows)
				{
					if(messageFlow.getMessage()==null)
					{
						InteractionNodeRequirements targetReq = new InteractionNodeRequirements(messageFlow.getTarget());
						messageSentCondition.addCondition(new WHENCondition(new MESSAGESENTCondition(messageEventDefinition.getMessage().printSpec(),targetReq.getInternalActors().toString())));
					}
					else
					{
						InteractionNodeRequirements targetReq = new InteractionNodeRequirements(messageFlow.getTarget());
						messageSentCondition.addCondition(new WHENCondition(new MESSAGESENTCondition(messageFlow.getMessage().printSpec(),targetReq.getInternalActors().toString())));
					}
				}
			}
		}
		
		if(condType.equals(ConditionType.STATE))
		{
			if(nodeType.equals(BPMNNodeType.INTERMEDIATE_CATCH_EVENT) || nodeType.equals(BPMNNodeType.BOUNDARY_EVENT) || nodeType.equals(BPMNNodeType.START_EVENT))
			{
				ORCompositeCondition messageReceivedCondition = new ORCompositeCondition();
				LinkedList<BPMNMessageFlow> incomingMessageFlows = ((BPMNInteractionNode)messageEventDefinition.getContainingElement()).getIncomingMessageFlows();
				for(BPMNMessageFlow messageFlow : incomingMessageFlows)
					if(messageFlow.getMessage()==null)
					{
						InteractionNodeRequirements sourceReq = new InteractionNodeRequirements(messageFlow.getSource());
						messageReceivedCondition.addCondition(new MESSAGERECEIVEDCondition(messageEventDefinition.getMessage().printSpec(),sourceReq.getInternalActors().toString()));
					}
					else
					{
						InteractionNodeRequirements sourceReq = new InteractionNodeRequirements(messageFlow.getSource());
						messageReceivedCondition.addCondition(new MESSAGERECEIVEDCondition(messageFlow.getMessage().printSpec(),sourceReq.getInternalActors().toString()));
					}

				return messageReceivedCondition;
			}
			
			if(nodeType.equals(BPMNNodeType.INTERMEDIATE_THROW_EVENT) || nodeType.equals(BPMNNodeType.END_EVENT))
			{
				ANDCompositeCondition messageSentCondition = new ANDCompositeCondition();
				LinkedList<BPMNMessageFlow> outgoingMessageFlows = ((BPMNInteractionNode)messageEventDefinition.getContainingElement()).getOutgoingMessageFlows();
				for(BPMNMessageFlow messageFlow : outgoingMessageFlows)
				{
					if(messageFlow.getMessage()==null)
					{
						InteractionNodeRequirements targetReq = new InteractionNodeRequirements(messageFlow.getTarget());
						messageSentCondition.addCondition(new MESSAGESENTCondition(messageEventDefinition.getMessage().printSpec(),targetReq.getInternalActors().toString()));
					}
					else
					{
						InteractionNodeRequirements targetReq = new InteractionNodeRequirements(messageFlow.getTarget());
						messageSentCondition.addCondition(new MESSAGESENTCondition(messageFlow.getMessage().printSpec(),targetReq.getInternalActors().toString()));
					}
				}

				return messageSentCondition;
			}
		}
		
		return null;
	}
	
}