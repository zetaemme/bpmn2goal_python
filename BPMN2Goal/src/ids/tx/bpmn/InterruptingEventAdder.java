package ids.tx.bpmn;

public class InterruptingEventAdder
{
	public void addEvent(BPMNTask task, BPMNEvent boundary)
	{
		BPMNEventDefinitionType type = boundary.getEventDefinitions().getFirst().getEventDefinitionType();
		task.getSingleInterruptingEvents().put(type,boundary);
	}
	
	public void addEvent(BPMNSubprocess subprocess, BPMNEvent interruptingEvent)
	{
		BPMNEventDefinitionType type = interruptingEvent.getEventDefinitions().getFirst().getEventDefinitionType();
		if(!subprocess.getSingleInterruptingEvents().containsKey(type))
			subprocess.getSingleInterruptingEvents().put(type,interruptingEvent);
		else
		{
			if(subprocess.getInterruptingEventSubProcesses().containsKey(type))
				subprocess.getInterruptingEventSubProcesses().get(type).addInterruptingEvent(new InterruptingEventAdder(),interruptingEvent);
			else
				BPMNElement.warningWriter.println("Does there exist a BPMN interrupting events of the same type attached to the subprocess yet, the last inserted will be ignored",(BPMNElement)subprocess);
		}
	}
}