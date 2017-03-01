package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNData extends BPMNItemElement
{
	private static Integer counter = 0;
	private Boolean isCollection = false;
	
	public BPMNData(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		if(!name.equals(""))
			name = name.replaceFirst(name.substring(0, 1),name.substring(0, 1).toUpperCase());
		if(this.getItemRef()!=null)
			isCollection = this.getItemRef().getIsCollection();
		else if(getAttributeValue(node,"isCollection").equals("true"))
			isCollection = true;
	}
	
	public Boolean getIsCollection()
	{
		return isCollection;
	}
	
	public void fillName()
	{
		name = "Data" + counter.toString();
		warningWriter.println("a BPMN item and data should specify a name -> Assigned:" + this.getName(), this);
		counter = counter + 1;
	}
	
	public String printSpec()
	{
		if(!this.getDataState().equals(""))
			return this.getDataState() + "(" + this.getName() + ")";
		return "available(" + this.getName() + ")";
	}
}