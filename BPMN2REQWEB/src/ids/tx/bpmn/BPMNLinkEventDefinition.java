package ids.tx.bpmn;


import java.util.LinkedList;
import org.w3c.dom.Node;

public class BPMNLinkEventDefinition extends BPMNEventDefinition
{
	LinkedList<BPMNEventDefinition> sources;
	BPMNEventDefinition target;
	
	public BPMNLinkEventDefinition(BPMNElement ambient,Node node)
	{
		super(ambient,node);
		
		sources = new LinkedList<BPMNEventDefinition>();
		for(Node sourceNode : getChildsByName(node,"source"))
		{
			String sourceLinkEventDefinitionId = sourceNode.getTextContent();
			BPMNEventDefinition sourceLinkEvent = resolveEventDefinitionReference(sourceLinkEventDefinitionId);
			if(sourceLinkEvent!=null)
				sources.add(sourceLinkEvent);
		}
		
		for(Node targetNode : getChildsByName(node,"target"))
		{
			String targetLinkEventDefinitionId = targetNode.getTextContent();
			target = resolveEventDefinitionReference(targetLinkEventDefinitionId);
		}
		
		eventDefinitionType = BPMNEventDefinitionType.LINK;
		
	}
	
	public LinkedList<BPMNEventDefinition> getSources()
	{
		return sources;
	}
	
	public BPMNEventDefinition getTarget()
	{
		return target;
	}
}