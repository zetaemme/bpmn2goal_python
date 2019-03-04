package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNTask extends BPMNActivity
{
	private static Integer counter = 0;
	public BPMNTask(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		nodeType = BPMNNodeType.TASK;
		
		if(name.equals(""))
		{
			name = "task" + counter.toString();
			warningWriter.println("a BPMN task should specify a name -> Assigned:" + name, this);
			counter = counter + 1;
		}
		
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
		
		for(Node dataInputAssociationNode : getChildsByName(node,"dataInputAssociation"))
			dataInputAssociations.add(new BPMNDataAssociation((BPMNElement)this,dataInputAssociationNode));
	
		for(Node dataOutputAssociationNode : getChildsByName(node,"dataOutputAssociation"))
			dataOutputAssociations.add(new BPMNDataAssociation((BPMNElement)this,dataOutputAssociationNode));
		
		for(BPMNData dataInput : dataInputs)
		{
			resolvedInputAssociations.put(dataInput, resolveDataInputAssociation(dataInput));
			if(dataInput.getName().equals("") && resolvedInputAssociations.get(dataInput).isEmpty())
				dataInput.fillName();
		}
		
		for(BPMNData dataOutput : dataOutputs)
		{
			resolvedOutputAssociations.put(dataOutput, resolveDataOutputAssociation(dataOutput));
			if(dataOutput.getName().equals("") && resolvedOutputAssociations.get(dataOutput).isEmpty())
				dataOutput.fillName();
		}
		
	}
	
	public void addInterruptingEvent(InterruptingEventAdder adder, BPMNEvent boundary)
	{
		adder.addEvent(this,boundary);
	}
}