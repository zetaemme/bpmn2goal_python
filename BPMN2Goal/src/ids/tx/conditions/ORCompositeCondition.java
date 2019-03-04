package ids.tx.conditions;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class ORCompositeCondition implements ConditionalExpression
{
	private LinkedList<ConditionalExpression> conditions;
	
	public ORCompositeCondition()
	{
		conditions = new LinkedList<ConditionalExpression>();
	}
	
	public ORCompositeCondition(Collection<ConditionalExpression> givenConditions)
	{
		while(givenConditions.contains(null))
			givenConditions.remove(null);
		conditions = new LinkedList<ConditionalExpression>(givenConditions);
	}
	
	public void addCondition(ConditionalExpression condition)
	{
		if(condition!=null)
			conditions.add(condition);
	}
	
	public void addAllCondition(Collection<ConditionalExpression> moreConditions)
	{
		while(moreConditions.contains(null))
			moreConditions.remove(null);
		if(!moreConditions.isEmpty())
			conditions.addAll(moreConditions);
	}
	
	public Boolean isEmpty()
	{
		if(conditions.isEmpty())
			return true;
		else
		{
			Boolean empty = true;
			for(int i=0 ; i<conditions.size() && empty.equals(true); i++)
				empty = empty && conditions.get(i).isEmpty();
			return empty;
		}
	}
	
	public String toStringOLD()
	{
		String outputCondition = "";
		int factorNumber = conditions.size();
		if(factorNumber == 0)
			return "";
		else if(factorNumber == 1)
			outputCondition = conditions.getFirst().toString();
		else if(factorNumber > 1)
		{
			Integer emptyFactorNumber = 0;
			Iterator<ConditionalExpression> iterator= conditions.iterator();
			
			String firstCond, secondCond;
			firstCond = iterator.next().toString();
			secondCond = iterator.next().toString();
			
			if(firstCond.equals("") || secondCond.equals(""))
				if(!firstCond.equals("") || !secondCond.equals(""))
				{
					outputCondition = firstCond + secondCond;
					emptyFactorNumber = emptyFactorNumber + 1;
				}
				else
					emptyFactorNumber = emptyFactorNumber + 2;
			else
				outputCondition = firstCond + " OR " + secondCond;
			
			while(iterator.hasNext())
			{
				String internalCondition = iterator.next().toString();
				if(!internalCondition.equals(""))
					if(!outputCondition.equals(""))
						outputCondition = outputCondition + " OR " + internalCondition;
					else
						outputCondition = outputCondition + internalCondition;
				else
					emptyFactorNumber = emptyFactorNumber + 1;
			}
			
			if(!outputCondition.equals("") && emptyFactorNumber != factorNumber - 1)
				return "(" + outputCondition + ")";
		}
		return  outputCondition;
	}

	public String toString() {
		String outputCondition = "";
		boolean dones = requiresDone();

		boolean first = true;
		
		Iterator it = conditions.iterator();
		while (it.hasNext()) {
			ConditionalExpression c = (ConditionalExpression) it.next();
			if (!c.toString(dones).equals("")) {
				//if (dones==true | !(c instanceof WHENDoneCondition) ) {
					
					if (first != true) {
						outputCondition+=" OR ";
					} else {
						first=false;
					}
					
					if (c instanceof ANDCompositeCondition)
						outputCondition+="("+c.toString()+")";
					else
						outputCondition+=c.toString();
					
				//}
			}
		}
		return  outputCondition;		
	}

	@Override
	public String toString(boolean dones) {
		String outputCondition = "";

		boolean first = true;
		
		Iterator it = conditions.iterator();
		while (it.hasNext()) {
			ConditionalExpression c = (ConditionalExpression) it.next();
			if (!c.toString(dones).equals("")) {
				//if (dones==true | !(c instanceof WHENDoneCondition) ) {
					
					if (first != true) {
						outputCondition+=" OR ";
					} else {
						first=false;
					}
					
					if (c instanceof ANDCompositeCondition)
						outputCondition+="("+c.toString()+")";
					else
						outputCondition+=c.toString();
					
				//}
			}
		}
		return  outputCondition;		
	}

	@Override
	public boolean requiresDone() {
		boolean result = true;
		return result;
	}

	@Override
	public String internalString() {
		String outputCondition = "or"+conditions.size()+"([";

		boolean first = true;
		
		Iterator it = conditions.iterator();
		while (it.hasNext()) {
			ConditionalExpression c = (ConditionalExpression) it.next();
			if (first != true) {
				outputCondition+=" , ";
			} else {
				first=false;
			}
			
			outputCondition+=c.internalString();
			if (c.internalString().trim().isEmpty())
				outputCondition+="class="+c.getClass().getName();
		}
		
		outputCondition+="])";
		
		return  outputCondition;		
	}

	public ConditionalExpression optimize() {
		ConditionalExpression output;
		
		/* remove all empty conditions */
		ORCompositeCondition alternate = new ORCompositeCondition();
		Iterator it = conditions.iterator();
		while (it.hasNext()) {
			ConditionalExpression item = (ConditionalExpression) it.next();
			ConditionalExpression opt = item.optimize();
			if (opt !=null & !opt.isEmpty() ) {
				alternate.addCondition(opt);
			}
		}
		
		if (alternate.isEmpty()) {
			output = new EmptyCondition();
		} else if (alternate.isOneTerm()) {
			output = alternate.getOneTerm();
		} else {
			output = alternate;
		}
		
		return output;
	}

	private ConditionalExpression getOneTerm() {
		return conditions.getFirst();
	}

	private boolean isOneTerm() {
		return (conditions.size()==1);
	}
}