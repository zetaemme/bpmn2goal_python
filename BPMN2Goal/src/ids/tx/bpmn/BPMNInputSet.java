package ids.tx.bpmn;

import java.util.*;
import org.w3c.dom.*;

public class BPMNInputSet extends BPMNElement
{
	private LinkedList<BPMNData> dataInputs;
	private LinkedList<BPMNData> optionalInputs;
	
	public BPMNInputSet(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		dataInputs = new LinkedList<BPMNData>();
		optionalInputs = new LinkedList<BPMNData>();
		
		for(Node dataInputNode : getChildsByName(node,"dataInputRefs"))
		{
			String dataInputId = dataInputNode.getTextContent();
			BPMNData dataInput = (BPMNData)resolveDataReference(dataInputId);
			if(dataInput!=null)
				dataInputs.add(dataInput); 
		}
		
		for(Node optionalInputNode : getChildsByName(node,"optionalInputRefs"))
		{
			String optionalInputId = optionalInputNode.getTextContent();
			BPMNData optionalInput = (BPMNData)resolveDataReference(optionalInputId);
			if(optionalInput!=null)
				optionalInputs.add(optionalInput); 
		}
		
	}
	
	public LinkedList<BPMNData> getDataInputs()
	{
		return dataInputs;
	}
	
	public LinkedList<BPMNData> getOptionalInputs()
	{
		return optionalInputs;
	}
}