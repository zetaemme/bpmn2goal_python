package ids.tx.bpmn;

import java.util.*;
import org.w3c.dom.*;

public class BPMNOutputSet extends BPMNElement
{
	private LinkedList<BPMNData> dataOutputs;
	private LinkedList<BPMNData> optionalOutputs;
	
	public BPMNOutputSet(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		dataOutputs = new LinkedList<BPMNData>();
		optionalOutputs = new LinkedList<BPMNData>();
		
		for(Node dataOutputNode : getChildsByName(node,"dataOutputRefs"))
		{
			String dataOutputId = dataOutputNode.getTextContent();
			BPMNData dataOutput = (BPMNData)resolveDataReference(dataOutputId);
			if(dataOutput!=null)
				dataOutputs.add(dataOutput); 
		}
		
		for(Node optionalOutputNode : getChildsByName(node,"optionalOutputRefs"))
		{
			String optionalOutputId = optionalOutputNode.getTextContent();
			BPMNData optionalDataOutput = (BPMNData)resolveDataReference(optionalOutputId);
			optionalOutputs.add(optionalDataOutput); 
		}
	}
	
	public LinkedList<BPMNData> getDataOutputs()
	{
		return dataOutputs;
	}
	
	public LinkedList<BPMNData> getOptionalOutputs()
	{
		return optionalOutputs;
	}
}