package ids.tx.bpmn;

import java.util.LinkedList;

import org.w3c.dom.Node;

public class BPMNDataAssociation extends BPMNElement
{
	private LinkedList<BPMNItemElement> source;
	private BPMNItemElement target;
	
	public BPMNDataAssociation(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		source = new LinkedList<BPMNItemElement>();
		for(Node sourceItemNode : getChildsByName(node,"sourceRef"))
		{
			String sourceItemId = sourceItemNode.getTextContent();
			BPMNItemElement sourceItem = resolveDataReference(sourceItemId);
			if(sourceItem!=null)
				source.add(sourceItem);
		}
		
		for(Node targetItemNode : getChildsByName(node,"targetRef"))
		{
			String targetItemId = targetItemNode.getTextContent();
			BPMNItemElement targetItem = resolveDataReference(targetItemId);
			target = targetItem;
		}
	}
	
	public LinkedList<BPMNItemElement> getSource()
	{
		return source;
	}
	
	public BPMNItemElement getTarget()
	{
		return target;
	}
}