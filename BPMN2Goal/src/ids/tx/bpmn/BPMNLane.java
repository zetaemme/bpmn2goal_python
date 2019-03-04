package ids.tx.bpmn;

import java.util.LinkedList;

import org.w3c.dom.Node;

public class BPMNLane extends BPMNElement
{
	private static Integer counter = 0;
	private BPMNLaneSet childLaneSet = null;
	private LinkedList<BPMNNode> flowNodes;
	
	public BPMNLane(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		if(name.equals(""))
		{
			name = "lane" + counter.toString();
			warningWriter.println("a BPMN lane should specify a name -> Assigned:" + name, this);
			counter = counter + 1;
		}
		else
			name = name.toLowerCase();
		
		for(Node childLaneSetNode : getChildsByName(node,"childLaneSet"))
			childLaneSet = new BPMNLaneSet((BPMNElement)this,childLaneSetNode);
		
		flowNodes = new LinkedList<BPMNNode>();
		for(Node flowNodeRefNode : getChildsByName(node,"flowNodeRef"))
		{
			String flowNodeId = flowNodeRefNode.getTextContent();
			if(!flowNodeId.equals(""))
			{
				BPMNNode flowNode = resolveNodeReference(flowNodeId);
				if(flowNode!=null)
				{
					flowNodes.add(flowNode);
					BPMNNodeType type = flowNode.getNodeType();
					if(type.equals(BPMNNodeType.HUMAN_INTERACTION_TASK))
						((BPMNHumanInteractionTask)flowNode).addLaneActor(this.getName());
					else if(type.equals(BPMNNodeType.SUBPROCESS))
						((BPMNSubprocess)flowNode).addLaneActor(this.getName());
				}
			}
		}
	}
	
	public BPMNLaneSet getChildLaneSet()
	{
		return childLaneSet;
	}
	
	public LinkedList<BPMNNode> getFlowNodes()
	{
		return flowNodes;
	}
}