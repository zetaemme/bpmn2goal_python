package ids.tx.requirements;

import ids.tx.conditions.WHEREClause;

public interface SocialRequirements extends Requirements
{
	public WHEREClause produceItemDescriptions();
}