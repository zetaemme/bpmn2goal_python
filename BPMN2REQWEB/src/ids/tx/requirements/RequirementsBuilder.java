package ids.tx.requirements;

import java.util.LinkedList;

import ids.tx.utils.GoalDBInserter;
import ids.tx.utils.GoalPrinter;

import ids.tx.bpmn.*;
import ids.tx.conditions.ActorList;
import ids.tx.conditions.ConditionalExpression;
import ids.tx.conditions.Goal;
import ids.tx.conditions.IndividualGoal;
import ids.tx.conditions.SocialGoal;
import ids.tx.conditions.WHEREClause;

public class RequirementsBuilder 
{
	private static Integer counter = 0;
	private GoalDBInserter dbInserter;
	private GoalPrinter printer;
	private BPMNProcess currentProcess;
	private LinkedList<Goal> goalList;
	
	public RequirementsBuilder(BPMNWorkflow workflow,GoalDBInserter dbInserter, GoalPrinter printer) 
	{
		goalList = new LinkedList<Goal>();
		this.dbInserter = dbInserter;
		this.printer = printer;
		workflow.acceptVisitor(this);
	}

	public void analyzeWorkflow(BPMNWorkflow workflow) 
	{
		for(BPMNProcess process : workflow.getProcesses())
			process.acceptVisitor(this);
	}

	public void analyzeProcess(BPMNProcess process) 
	{
		currentProcess = process;
		counter = 0;
		String locationString = process.getName();
		produceSocialGoal(locationString,process);
		produceAgentGoals(locationString,process);
	}
	
	public void analyzeSubprocess(String locationString, BPMNSubprocess subprocess) 
	{
		String newLocationString = locationString + "_" + subprocess.getName();
		produceSocialGoal(newLocationString,subprocess);
		produceAgentGoals(newLocationString,subprocess);
	}

	private SocialRequirements socialFactory(BPMNFlowElementContainer container)
	{
		if(container.getFlowElementContainerType().equals(BPMNFlowElementContainerType.PROCESS))
			return new ProcessRequirements((BPMNProcess)container);
		if(container.getFlowElementContainerType().equals(BPMNFlowElementContainerType.SUBPROCESS))
			return new SubprocessRequirements((BPMNSubprocess)container);
		return null;
		
	}
	
	private void produceSocialGoal(String locationString,BPMNFlowElementContainer container) 
	{
		SocialRequirements requirements = socialFactory(container);
		String socialGoalName = locationString;
		ConditionalExpression inputEvent = requirements.produceInputEvent();
		ActorList actorList = requirements.produceActorList();
		ConditionalExpression outputState = requirements.produceOutputState();
		WHEREClause itemDescription = requirements.produceItemDescriptions();
		Goal socialGoal = new SocialGoal(socialGoalName,inputEvent,actorList,outputState,itemDescription);
		
		goalList.add(socialGoal);
		printer.goalPrint(socialGoal);
		dbInserter.goalDBInserting(currentProcess,socialGoal);
		
	}
	
	private void produceAgentGoals(String locationString,BPMNFlowElementContainer container) 
	{
		for(BPMNNode node : container.getFlowNodes())
		{
			BPMNNodeType typeNode = node.getNodeType();			
			if(typeNode.equals(BPMNNodeType.TASK) || typeNode.equals(BPMNNodeType.SERVICE_TASK) 
			|| typeNode.equals(BPMNNodeType.SCRIPT_TASK)
			|| typeNode.equals(BPMNNodeType.BUSINESS_RULE_TASK) || typeNode.equals(BPMNNodeType.SEND_TASK)
			|| typeNode.equals(BPMNNodeType.RECEIVE_TASK) || typeNode.equals(BPMNNodeType.CALL_ACTIVITY)
			|| typeNode.equals(BPMNNodeType.INTERMEDIATE_THROW_EVENT) || (typeNode.equals(BPMNNodeType.END_EVENT) && !((BPMNEvent)node).getEventDefinitions().isEmpty()))
				produceAgentGoal(locationString,node);
			else if(typeNode.equals(BPMNNodeType.HUMAN_INTERACTION_TASK))
				produceAgentGoal(locationString,(BPMNHumanInteractionTask)node);
			else if(typeNode.equals(BPMNNodeType.SUBPROCESS))
				((BPMNSubprocess)node).acceptVisitor(locationString,this);
		}
	}

	private void produceAgentGoal(String locationString, BPMNNode node) 
	{
		Requirements requirements = NodeRequirements.factory(node);
		//ANTONELLA
		node.getName();
	//	System.out.println("ANTONELLA NODE GET NAME-->"+node.getName());
		String agentGoalName = node.getName();
		
		//String agentGoalName = locationString + ".g" + counter.toString();
		counter = counter + 1;
		ConditionalExpression inputEvent = requirements.produceInputEvent();
		ActorList actorList = requirements.produceActorList();
		ConditionalExpression outputState = requirements.produceOutputState();
		Goal agentGoal = new IndividualGoal(agentGoalName,inputEvent,actorList,outputState);
		
		goalList.add(agentGoal);
		printer.goalPrint(agentGoal);
		dbInserter.goalDBInserting(currentProcess,agentGoal);
	}
	
	private void produceAgentGoal(String locationString, BPMNHumanInteractionTask humanTask)
	{
		HumanInteractionTaskRequirements requirements = new HumanInteractionTaskRequirements(humanTask);
		ConditionalExpression inputEvent = requirements.produceInputEvent();
		
		ConditionalExpression outputState = requirements.produceOutputState();
		while(requirements.hasNextActor())
		{
			String agentGoalName = humanTask.getName();
//			String agentGoalName = locationString + ".g" + counter.toString();
			counter = counter + 1;
			ActorList actorList = requirements.produceActorList();
			Goal agentGoal = new IndividualGoal(agentGoalName,inputEvent,actorList,outputState);
			goalList.add(agentGoal);
			printer.goalPrint(agentGoal);
			dbInserter.goalDBInserting(currentProcess,agentGoal);
		}
	}
	
	public LinkedList<Goal> getGoalList()
	{
		return goalList;
	}
	
}
