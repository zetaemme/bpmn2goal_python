package ids.tx.conditions;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class ANDCompositeCondition implements ConditionalExpression
{
	private LinkedList<ConditionalExpression> conditions;
	
	public ANDCompositeCondition()
	{
		conditions = new LinkedList<ConditionalExpression>();
	}
	
	public ANDCompositeCondition(Collection<ConditionalExpression> givenConditions)
	{
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
	
	public boolean requiresDone() {
		boolean result = true;
		
		Iterator it = conditions.iterator();
		while (result==true & it.hasNext() ) {
			ConditionalExpression c = (ConditionalExpression) it.next();
			if (c.requiresDone()==false) {
				result=false;
			}
		}
		
		return result;
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
						outputCondition+=" AND ";
					} else {
						first=false;
					}
					
					if (c instanceof ORCompositeCondition)
						outputCondition+="("+c.toString()+")";
					else
						outputCondition+=c.toString();
					
				//}
			}
		}
		return  outputCondition;		
	}

	public String toString(boolean dones) {
		String outputCondition = "";

		boolean first = true;
		
		Iterator it = conditions.iterator();
		while (it.hasNext()) {
			ConditionalExpression c = (ConditionalExpression) it.next();
			if (!c.toString(dones).equals("")) {
				//if (dones==true | !(c instanceof WHENDoneCondition) ) {
					
					if (first != true) {
						outputCondition+=" AND ";
					} else {
						first=false;
					}
					
					if (c instanceof ORCompositeCondition)
						outputCondition+="("+c.toString()+")";
					else
						outputCondition+=c.toString();
					
				//}
			}
		}
		
		
		
		return  outputCondition;		
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
			{
				
				//ANTONELLA delete WHEN string if exist example before the modify : WHEN available AND WHEN ready after the modify WHEN available AND ready
				outputCondition = firstCond + " AND " + secondCond.replaceAll("WHEN", "");
//				outputCondition = firstCond + " AND " + secondCond;
			}
			while(iterator.hasNext())
			{
				String internalCondition = iterator.next().toString();
				if(!internalCondition.equals(""))
					if(!outputCondition.equals(""))
					{
						//ANTONELLA delete WHEN string if exist example before the modify : WHEN available AND WHEN ready after the modify WHEN available AND ready
						outputCondition = outputCondition + " AND " + internalCondition.replaceAll("WHEN", "");
//						outputCondition = outputCondition + " AND " + internalCondition;
					}
					else
						outputCondition = outputCondition + internalCondition;
				else
					emptyFactorNumber = emptyFactorNumber + 1;
			}
			
			if(!outputCondition.equals("") && emptyFactorNumber != factorNumber - 1)
				//ANTONELLA: eliminate le parentesi di apertura e chiusura nella clausola WHEN
				return outputCondition ;
//				return "(" + outputCondition + ")";
		}
		return  outputCondition;
	}

	@Override
	public String internalString() {
		
		String outputCondition = "and"+conditions.size()+"([";

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
		}
		
		outputCondition+="])";
		
		return  outputCondition;		
	}
	
	public ConditionalExpression optimize() {
		ConditionalExpression output;

		int count_done = 0;
		int count_cond = 0;
		/* remove all empty conditions */
		ANDCompositeCondition alternate = new ANDCompositeCondition();
		//System.out.println("evaluating "+this.internalString());
		
		Iterator it = conditions.iterator();
		while (it.hasNext()) {
			ConditionalExpression item = (ConditionalExpression) it.next();
			ConditionalExpression opt = item.optimize();
			if (opt !=null & !opt.isEmpty() ) {
				alternate.addCondition(opt);
				
				if (opt instanceof DonePredicate || opt instanceof WHENDoneCondition) {
					count_done++;
				} else {
					count_cond++;
				}
			}
		}
		
		//System.out.println("first step "+alternate.internalString());
		//System.out.println("cond "+count_cond+" - done "+count_done);
		ANDCompositeCondition alternate2;
		
		if ( count_cond>0 && count_done>0 ) {
			alternate2 = new ANDCompositeCondition();
			it = alternate.conditions.iterator();
			while (it.hasNext()) {
				ConditionalExpression item = (ConditionalExpression) it.next();
				if (!(item instanceof DonePredicate || item instanceof WHENDoneCondition)) 
					alternate2.addCondition(item);
			}
		} else {
			alternate2 = alternate;
		}
		
		//System.out.println("second step "+alternate2.internalString());

		if (alternate2.isEmpty()) {
			output = new EmptyCondition();
		} else if (alternate2.isOneTerm()) {
			output = alternate2.getOneTerm();
		} else {
			output = alternate2;
		}
		
		//System.out.println("final step "+output.internalString());
		return output;
	}

	private ConditionalExpression getOneTerm() {
		return conditions.getFirst();
	}

	private boolean isOneTerm() {
		return (conditions.size()==1);
	}
	
}