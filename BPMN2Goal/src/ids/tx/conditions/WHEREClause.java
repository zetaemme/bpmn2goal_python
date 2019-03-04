package ids.tx.conditions;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class WHEREClause
{
	private LinkedList<ItemDescription> itemDescriptions;
	
	public WHEREClause()
	{
		itemDescriptions = new LinkedList<ItemDescription>();
	}
	
	public WHEREClause(Collection<ItemDescription> itemDescriptions)
	{
		this.itemDescriptions = new LinkedList<ItemDescription>(itemDescriptions);
	}
	
	public void addItemDescription(ItemDescription itemDescription)
	{
		itemDescriptions.add(itemDescription);
	}
	
	public void addAllItemDescriptions(Collection<ItemDescription> moreItemDescriptions)
	{
		itemDescriptions.addAll(moreItemDescriptions);
	}

	public String toString()
	{
		String whereClause = "";
		if(!itemDescriptions.isEmpty())
		{
			Iterator<ItemDescription> iterator = itemDescriptions.iterator();
			whereClause = "WHERE " + iterator.next().toString();
			while(iterator.hasNext())
				whereClause = whereClause + " AND " + iterator.next().toString();
		}
		return whereClause;
	}
}