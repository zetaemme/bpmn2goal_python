package ids.tx.bpmn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.w3c.dom.Node;

public class BPMNNode extends BPMNElement
{
	private LinkedList<BPMNSequenceFlow> incomings = null;
	private LinkedList<String> incomingIds;
	private LinkedList<BPMNSequenceFlow> outgoings = null;
	private LinkedList<String> outgoingIds;
	protected LinkedList<BPMNData> dataInputs;
	protected LinkedList<BPMNData> dataOutputs;
	protected LinkedList<BPMNInputSet> inputSets;
	protected LinkedList<BPMNOutputSet> outputSets;
	protected LinkedList<BPMNDataAssociation> dataInputAssociations;
	protected LinkedList<BPMNDataAssociation> dataOutputAssociations;
	protected HashMap<BPMNData,LinkedList<BPMNItemElement>> resolvedInputAssociations;
	protected HashMap<BPMNData,LinkedList<BPMNItemElement>> resolvedOutputAssociations;
	protected BPMNNodeType nodeType;
	
	public BPMNNode(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		name = name.toLowerCase();
		
		incomingIds = new LinkedList<String>();
		outgoingIds = new LinkedList<String>();
		
		dataInputs = new LinkedList<BPMNData>();
		dataOutputs = new LinkedList<BPMNData>();
		inputSets = new LinkedList<BPMNInputSet>();
		outputSets = new LinkedList<BPMNOutputSet>();
		dataInputAssociations = new LinkedList<BPMNDataAssociation>();
		dataOutputAssociations = new LinkedList<BPMNDataAssociation>();
		resolvedInputAssociations = new HashMap<BPMNData,LinkedList<BPMNItemElement>>();
		resolvedOutputAssociations = new HashMap<BPMNData,LinkedList<BPMNItemElement>>();
		
		for(Node incoming : getChildsByName(node,"incoming"))
			incomingIds.add(incoming.getTextContent());
		
		for(Node outgoing : getChildsByName(node,"outgoing"))
			outgoingIds.add(outgoing.getTextContent());
	}
	
	public BPMNNode(BPMNElement ambient, String name)
	{
		super(ambient,name);
		incomings = new LinkedList<BPMNSequenceFlow>();
		outgoings = new LinkedList<BPMNSequenceFlow>();
		dataInputs = new LinkedList<BPMNData>();
		dataOutputs = new LinkedList<BPMNData>();
		inputSets = new LinkedList<BPMNInputSet>();
		outputSets = new LinkedList<BPMNOutputSet>();
		dataInputAssociations = new LinkedList<BPMNDataAssociation>();
		dataOutputAssociations = new LinkedList<BPMNDataAssociation>();
		resolvedInputAssociations = new HashMap<BPMNData,LinkedList<BPMNItemElement>>();
		resolvedOutputAssociations = new HashMap<BPMNData,LinkedList<BPMNItemElement>>();		
	}
	
	private void parseIncomings()
	{
		if(incomings == null)
		{
			incomings = new LinkedList<BPMNSequenceFlow>();
			for(String incomingId : incomingIds)
			{
				BPMNSequenceFlow sequenceFlow = resolveSequenceFlowReference(incomingId);
				if(sequenceFlow!=null)
					incomings.add(sequenceFlow);
			}
		}
	}

	private void parseOutgoings()
	{
		if(outgoings == null)
		{
			outgoings = new LinkedList<BPMNSequenceFlow>();
			for(String outgoingId : outgoingIds)
			{
				BPMNSequenceFlow sequenceFlow = resolveSequenceFlowReference(outgoingId);
				if(sequenceFlow!=null)
					outgoings.add(sequenceFlow);
			}
		}
	}

	public LinkedList<BPMNSequenceFlow> getIncomings()
	{
		parseIncomings();
		return incomings;
	}
	
	public LinkedList<BPMNSequenceFlow> getOutgoings()
	{
		parseOutgoings();
		return outgoings;
	}
	
	public LinkedList<BPMNData> getDataInputs()
	{
		return dataInputs;
	}

	public LinkedList<BPMNData> getDataOutputs()
	{
		return dataOutputs;
	}

	public LinkedList<BPMNInputSet> getInputSets()
	{
		return inputSets;
	}

	public LinkedList<BPMNOutputSet> getOutputSets()
	{
		return outputSets;
	}

	public LinkedList<BPMNDataAssociation> getDataInputAssociations()
	{
		return dataInputAssociations;
	}

	public LinkedList<BPMNDataAssociation> getDataOutputAssociations()
	{
		return dataOutputAssociations;
	}

	public HashMap<BPMNData,LinkedList<BPMNItemElement>> getResolvedInputAssociations() 
	{
		return resolvedInputAssociations;
	}

	public HashMap<BPMNData,LinkedList<BPMNItemElement>> getResolvedOutputAssociations() 
	{
		return resolvedOutputAssociations;
	}

	public BPMNNodeType getNodeType()
	{
		return nodeType;
	}
	
	public Boolean isInteractionNode()
	{
		if(nodeType.equals(BPMNNodeType.TASK) || nodeType.equals(BPMNNodeType.SERVICE_TASK) || nodeType.equals(BPMNNodeType.RECEIVE_TASK)
				|| nodeType.equals(BPMNNodeType.SUBPROCESS) || nodeType.equals(BPMNNodeType.HUMAN_INTERACTION_TASK) || nodeType.equals(BPMNNodeType.SCRIPT_TASK)
				|| nodeType.equals(BPMNNodeType.SEND_TASK) || nodeType.equals(BPMNNodeType.BUSINESS_RULE_TASK) || nodeType.equals(BPMNNodeType.START_EVENT)
				|| nodeType.equals(BPMNNodeType.END_EVENT) || nodeType.equals(BPMNNodeType.INTERMEDIATE_CATCH_EVENT) || nodeType.equals(BPMNNodeType.INTERMEDIATE_THROW_EVENT)
				|| nodeType.equals(BPMNNodeType.CALL_ACTIVITY))
			return true;
		return false;
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

	/**
	 * produce the list of all BPMNItemElement that are associated with the specified dataInput
	 * @param dataInput
	 */
	protected LinkedList<BPMNItemElement> resolveDataInputAssociation(BPMNData dataInput)
	{
		LinkedList<BPMNItemElement> associatedObjects = new LinkedList<BPMNItemElement>();
		for(BPMNDataAssociation dataInputAssociation : dataInputAssociations)
		{
			BPMNItemElement dataReferenced = dataInputAssociation.getTarget();
			if(dataReferenced!=null && dataReferenced.equals(dataInput))
				for(BPMNItemElement sourceObject : dataInputAssociation.getSource())
					associatedObjects.add(sourceObject);
		}
		return associatedObjects;
	}

	/**
	 * produce the list of all BPMNItemElement that are associated with the specified dataOutput
	 * @param dataOutput
	 */
	protected LinkedList<BPMNItemElement> resolveDataOutputAssociation(BPMNData dataOutput)
	{
		LinkedList<BPMNItemElement> associatedObjects = new LinkedList<BPMNItemElement>();
		for(BPMNDataAssociation dataOutputAssociation : dataOutputAssociations)
			for(BPMNItemElement dataSource : dataOutputAssociation.getSource())
				if(dataSource.equals(dataOutput))
					if(dataOutputAssociation.getTarget()!=null)
						associatedObjects.add(dataOutputAssociation.getTarget());
		return associatedObjects;
	}

	public HashMap<BPMNOutputSet,LinkedList<BPMNItemElement>> collectItemInOutputSets()
	{
		HashMap<BPMNOutputSet,LinkedList<BPMNItemElement>> itemsInOutputSets = new  HashMap<BPMNOutputSet,LinkedList<BPMNItemElement>>();
		for(BPMNOutputSet outputSet : outputSets)
		{
			LinkedList<BPMNItemElement> itemInOutputSet = new LinkedList<BPMNItemElement>();
			for(BPMNData dataOutput : outputSet.getDataOutputs())
				itemInOutputSet.addAll(resolvedOutputAssociations.get(dataOutput));
			itemsInOutputSets.put(outputSet,itemInOutputSet);
		}
		return itemsInOutputSets;
	}

	private void produceReachableNodeList(LinkedList<BPMNNode> reachableNodes,BPMNNode source,HashSet<BPMNNode> visited)
	{
		visited.add(source);
		BPMNNodeType nodeType = source.getNodeType();
		if(nodeType.equals(BPMNNodeType.TASK) || nodeType.equals(BPMNNodeType.SERVICE_TASK) || nodeType.equals(BPMNNodeType.RECEIVE_TASK)
		|| nodeType.equals(BPMNNodeType.SUBPROCESS) || nodeType.equals(BPMNNodeType.HUMAN_INTERACTION_TASK) || nodeType.equals(BPMNNodeType.SCRIPT_TASK)
		|| nodeType.equals(BPMNNodeType.SEND_TASK) || nodeType.equals(BPMNNodeType.BUSINESS_RULE_TASK) || nodeType.equals(BPMNNodeType.CALL_ACTIVITY)
		|| nodeType.equals(BPMNNodeType.INTERMEDIATE_THROW_EVENT) || nodeType.equals(BPMNNodeType.END_EVENT))
			reachableNodes.add(source);
		else
		{
			for(BPMNSequenceFlow sequenceFlow : source.getOutgoings())
				if(!visited.contains(sequenceFlow.getTarget()))
					produceReachableNodeList(reachableNodes,sequenceFlow.getTarget(),visited);
		}
	}

	public LinkedList<BPMNNode> collectReachableActiveNodes(BPMNSequenceFlow callProvenience)
	{
		HashSet<BPMNNode> visited = new HashSet<BPMNNode>();
		LinkedList<BPMNNode> reachableNodes = new LinkedList<BPMNNode>();
		for(BPMNSequenceFlow outgoing : getOutgoings())
			if(!outgoing.equals(callProvenience))
				produceReachableNodeList(reachableNodes,outgoing.getTarget(),visited);
		return reachableNodes;
	}

	private void DFSVisit(LinkedList<BPMNNode> connectedNode,BPMNNode source,HashSet<BPMNNode> visited)
	{
		visited.add(source);
		BPMNNodeType nodeType = source.getNodeType();
		if(nodeType.equals(BPMNNodeType.TASK) || nodeType.equals(BPMNNodeType.SERVICE_TASK) || nodeType.equals(BPMNNodeType.RECEIVE_TASK)
		|| nodeType.equals(BPMNNodeType.SUBPROCESS) || nodeType.equals(BPMNNodeType.HUMAN_INTERACTION_TASK) || nodeType.equals(BPMNNodeType.SCRIPT_TASK)
		|| nodeType.equals(BPMNNodeType.SEND_TASK) || nodeType.equals(BPMNNodeType.BUSINESS_RULE_TASK) || nodeType.equals(BPMNNodeType.CALL_ACTIVITY)
		|| nodeType.equals(BPMNNodeType.INTERMEDIATE_THROW_EVENT) || nodeType.equals(BPMNNodeType.END_EVENT))
			connectedNode.add(source);
		for(BPMNSequenceFlow sequenceFlow : source.getOutgoings())
			if(!visited.contains(sequenceFlow.getTarget()))
				DFSVisit(connectedNode,sequenceFlow.getTarget(),visited);
	}

	public Boolean connected(BPMNNode nodeTarget)
	{
		HashSet<BPMNNode> visited = new HashSet<BPMNNode>();
		LinkedList<BPMNNode> connectedNode = new LinkedList<BPMNNode>();
		for(BPMNSequenceFlow outgoing : getOutgoings())
			DFSVisit(connectedNode,outgoing.getTarget(),visited);
		return (connectedNode.contains(nodeTarget));
	}
}