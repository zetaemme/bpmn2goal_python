package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNSignalEventDefinition extends BPMNEventDefinition
{
	private BPMNSignal signal;
	
	public BPMNSignalEventDefinition(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		String signalId = getAttributeValue(node,"signalRef");
		if(!signalId.equals(""))
			signal = resolveSignalReference(signalId);
		
		if(signal==null)
		{
			signal = new BPMNSignal(this);
			warningWriter.println("a BPMN signal event should specify a defined signal -> Assigned:" + signal.getName(),(BPMNElement)this);
		}
		
		eventDefinitionType = BPMNEventDefinitionType.SIGNAL;
	}
	
	public BPMNSignal getSignal()
	{
		return signal;
	}
}