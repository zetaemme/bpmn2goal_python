package ids.tx.bpmn;

import java.util.HashSet;
import java.util.LinkedList;

public interface BPMNFlowElementContainer
{
	public LinkedList<BPMNNode> getFlowNodes();
	public LinkedList<BPMNSequenceFlow> getSequenceFlows();
	public LinkedList<BPMNData> getDataObjects();
	public LinkedList<BPMNDataObjectReference> getDataObjectReferences();
	public LinkedList<BPMNDataStoreReference> getDataStoreReferences();
	public LinkedList<BPMNLaneSet> getLaneSets();
	public BPMNFlowElementContainerType getFlowElementContainerType();
	public LinkedList<BPMNParticipant> getInternalParticipants();
	public HashSet<BPMNParticipant> getCollaboratingParticipants();
}