package ids.tx.bpmn;

import java.util.LinkedList;

import org.w3c.dom.Node;

public class BPMNPartner extends BPMNElement
{
	private static Integer counter = 0;
	private LinkedList<BPMNParticipant> participants;
	
	public BPMNPartner(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		if(name.equals(""))
		{
			name = "partner" + counter.toString();
			warningWriter.println("a BPMN partner should specify a name -> Assigned:" + this.getName(),(BPMNElement)this);
		}
		
		participants = new LinkedList<BPMNParticipant>();
		for(Node participantNode : getChildsByName(node,"participantRef"))
		{
			String participantId = participantNode.getTextContent();
			BPMNParticipant participant = resolveParticipantReference(participantId);
			if(participant!=null)
			{
				participants.add(participant);
				participant.addPartner(this);
			}
		}	
	}
	
	public LinkedList<BPMNParticipant> getParticipants()
	{
		return participants;
	}
}