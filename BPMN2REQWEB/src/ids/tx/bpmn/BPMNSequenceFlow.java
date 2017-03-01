package ids.tx.bpmn;

import ids.tx.conditions.Predicate;

import org.w3c.dom.Node;

public class BPMNSequenceFlow extends BPMNElement
{
	private BPMNNode source;
	private BPMNNode target;
	private Predicate condition = null;
	
	public BPMNSequenceFlow(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		for(Node conditionExpressionNode : getChildsByName(node,"conditionExpression"))
			condition = new Predicate(conditionExpressionNode.getTextContent());
		
		String sourceNodeId = getAttributeValue(node,"sourceRef");
		if(!sourceNodeId.equals(""))
			source = resolveNodeReference(sourceNodeId);
		
		String targetNodeId = getAttributeValue(node,"targetRef");
		if(!targetNodeId.equals(""))
			target = resolveNodeReference(targetNodeId);
	}
	
	public BPMNNode getSource()
	{
		return source;
	}
	
	public BPMNNode getTarget()
	{
		return target;
	}
	
	public Predicate getCondition()
	{
		return condition;
	}
}