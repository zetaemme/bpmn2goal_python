package ids.tx.bpmn;

import java.util.LinkedList;

import org.w3c.dom.Node;

public class BPMNCallableElement extends BPMNElement
{
	private LinkedList<BPMNData> dataInputs;
	private LinkedList<BPMNData> dataOutputs;
	private LinkedList<BPMNInputSet> inputSets;
	private LinkedList<BPMNOutputSet> outputSets;
	
	public BPMNCallableElement(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		name = name.toLowerCase();
		
		dataInputs = new LinkedList<BPMNData>();
		dataOutputs = new LinkedList<BPMNData>();
		inputSets = new LinkedList<BPMNInputSet>();
		outputSets = new LinkedList<BPMNOutputSet>();
		
		for(Node ioSpecificationNode : getChildsByName(node,"ioSpecification"))
		{
			
			for(Node dataInputNode : getChildsByName(ioSpecificationNode,"dataInput"))
				dataInputs.add(new BPMNData((BPMNElement)this,dataInputNode));
		
			for(Node dataOutputNode : getChildsByName(ioSpecificationNode,"dataOutput"))
				dataOutputs.add(new BPMNData((BPMNElement)this,dataOutputNode));
			
			for(Node inputSetNode : getChildsByName(ioSpecificationNode,"inputSet"))
				inputSets.add(new BPMNInputSet((BPMNElement)this,inputSetNode));
			
			for(Node outputSetNode : getChildsByName(ioSpecificationNode,"outputSet"))
				outputSets.add(new BPMNOutputSet((BPMNElement)this,outputSetNode));
		}
	}
	
	protected BPMNItemElement searchForData(String dataId)
	{
		for(BPMNData data : dataInputs)
			if(data.getId().equals(dataId))
				return data;
		for(BPMNData data : dataOutputs)
			if(data.getId().equals(dataId))
				return data;
		return null;
	}
}