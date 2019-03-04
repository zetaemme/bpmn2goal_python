package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNResource extends BPMNElement 
{	
	private static Integer counter = 0;
	
	public BPMNResource(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		if(name.equals(""))
		{
			name = "resource" + counter.toString();
			warningWriter.println("a BPMN Resource should specify a name -> Assigned:" + name, this);
		}
		else
			name = name.toLowerCase();
	}
	
	public BPMNResource(BPMNElement ambient)
	{
		super(ambient,"performer" + counter.toString());
		counter = counter + 1;
	}
}