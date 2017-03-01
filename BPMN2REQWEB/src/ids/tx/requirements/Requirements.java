package ids.tx.requirements;

import ids.tx.conditions.ActorList;
import ids.tx.conditions.ConditionalExpression;

public interface Requirements
{
	public ConditionalExpression produceInputEvent();
	public ActorList produceActorList();
	public ConditionalExpression produceOutputState();
}