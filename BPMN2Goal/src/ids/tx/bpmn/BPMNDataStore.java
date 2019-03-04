package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNDataStore extends BPMNItemElement
{
	private static Integer counter = 0;
	private Integer capacity;
	private Boolean isUnlimited = true;
	
	public BPMNDataStore(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		if(name.equals(""))
		{
			name = "DataStore" + counter.toString();
			warningWriter.println("a BPMN dataStore should specify a name -> Assigned:" + name, this);
			counter = counter + 1;
		}
		else
			name = name.replaceFirst(name.substring(0, 1),name.substring(0, 1).toUpperCase());
		
		if(getAttributeValue(node,"isUnlimited").equals(false))
			isUnlimited = false;
		
		String capacityString = getAttributeValue(node,"capacity");
		if(!capacityString.equals(""))
			capacity = Integer.parseInt(capacityString);
	}
	
	public Boolean getIsUnlimited()
	{
		return isUnlimited;
	}
	
	public Integer getCapacity()
	{
		return capacity;
	}
	
	public String printSpec()
	{
		if(!this.getDataState().equals(""))
			return this.getDataState() + "(" + this.getName() + ")";
		return "available(" + this.getName() + ")";
	}
}