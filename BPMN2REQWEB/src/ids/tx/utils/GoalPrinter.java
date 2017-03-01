package ids.tx.utils;

import ids.tx.conditions.Goal;

import java.io.PrintStream;

public class GoalPrinter
{
	private Boolean active;
	private PrintStream printStream;
	
	public GoalPrinter(Boolean goalPrinting,PrintStream printStream)
	{
		active = goalPrinting;
		if(active.equals(true))
			this.printStream = printStream;
	}
	
	public void goalPrint(Goal goal)
	{
		if(active.equals(true))
			printStream.println(goal.toString());
	}
}