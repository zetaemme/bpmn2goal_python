package ids.tx.utils;

import java.sql.SQLException;

import ids.database.GoalTable;
import ids.model.GoalEntity;
import ids.tx.bpmn.BPMNProcess;
import ids.tx.conditions.Goal;

public class GoalDBInserter
{
	private Boolean active;
	private GoalTable goalTable;
	
	public GoalDBInserter(Boolean dbInserting)
	{
		active = dbInserting;
		if(active.equals(true))
			goalTable = new GoalTable();
	}
	
	public void goalDBInserting(BPMNProcess process,Goal goal)
	{
		if(active.equals(true))
		{
			GoalEntity goalEntity = new GoalEntity();
			goalEntity.setPack(process.getName());
			goalEntity.setName(goal.getName());
			goalEntity.setGoalDescription(goal.toString());
			try 
			{
				goalTable.insertElement(goalEntity);
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	}
}