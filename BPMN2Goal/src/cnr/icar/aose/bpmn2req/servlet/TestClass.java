package cnr.icar.aose.bpmn2req.servlet;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import ids.tx.bpmn.BPMNWorkflow;
import ids.tx.conditions.Goal;
import ids.tx.requirements.RequirementsBuilder;
import ids.tx.utils.GoalDBInserter;
import ids.tx.utils.GoalPrinter;
import ids.tx.utils.WarningWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class TestClass {
	private LinkedList<Goal> resultingGoals;
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		LinkedList<Goal> resultingGoals;
		//System.out.println("bpmnFile-->"+bpmnFile.toString());
		File bpmnFile = new File("/Users/antonellacavaleri/Downloads/diagramTest.xml");
		if(bpmnFile.exists()){
 		Document doc = dBuilder.parse(bpmnFile);
 		doc.getDocumentElement().normalize();
 		Element root = doc.getDocumentElement();
 		WarningWriter warningWriter = new WarningWriter(false,"",bpmnFile.getName());
 		BPMNWorkflow workflow = new BPMNWorkflow(root,warningWriter);
 		GoalDBInserter dbInserter = new GoalDBInserter(false);
 		GoalPrinter printer = new GoalPrinter(false,System.out);
 		RequirementsBuilder requirementsBuilder = new RequirementsBuilder(workflow,dbInserter,printer);
 		resultingGoals = requirementsBuilder.getGoalList();
 		String goalString="";
 		for (int i = 0; i <resultingGoals.size(); i++) {
 			System.out.println("Goals-->"+i+": "+resultingGoals.get(i).toString());
 			goalString+= resultingGoals.get(i).toString()+"\n";
		}
		}
	}

}
