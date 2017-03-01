package ids.tx.bpmn;

import java.util.LinkedList;

import org.w3c.dom.Node;

public class BPMNLaneSet extends BPMNElement
{
private LinkedList<BPMNLane> lanes;
	
	public BPMNLaneSet(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		lanes = new LinkedList<BPMNLane>();
		for(Node laneNode : getChildsByName(node,"lane"))
			lanes.add(new BPMNLane((BPMNElement)this,laneNode));
	}
	
	public LinkedList<BPMNLane> getLanes()
	{
		return lanes;
	}
}