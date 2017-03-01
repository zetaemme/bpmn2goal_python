package ids.tx.bpmn;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;



import ids.tx.conditions.Goal;
import ids.tx.requirements.RequirementsBuilder;
import ids.tx.utils.FileChooser;
import ids.tx.utils.GoalDBInserter;
import ids.tx.utils.GoalPrinter;
import ids.tx.utils.WarningWriter;

public class BPMN2Requirements
{
	private LinkedList<Goal> resultingGoals;
	
	public BPMN2Requirements(File bpmnFile, Boolean dbInserting, Boolean goalsPrinting, Boolean createWarnings, String warningFilePath) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(bpmnFile);
		doc.getDocumentElement().normalize();
		Element root = doc.getDocumentElement();
		WarningWriter warningWriter = new WarningWriter(createWarnings,warningFilePath,bpmnFile.getName());
		BPMNWorkflow workflow = new BPMNWorkflow(root,warningWriter);
		GoalDBInserter dbInserter = new GoalDBInserter(dbInserting);
		GoalPrinter printer = new GoalPrinter(goalsPrinting,System.out);
		RequirementsBuilder requirementsBuilder = new RequirementsBuilder(workflow,dbInserter,printer);
		resultingGoals = requirementsBuilder.getGoalList();
	}
	
	public LinkedList<Goal> getGoals()
	{
		return resultingGoals;
	}
	
	
	public static void main(String args[]) throws IOException, ParserConfigurationException, SAXException
	{
	// Settare in run configuration il valore desiderato (-db,-p,-w)
		Boolean dbInserting = false;
		Boolean goalsPrinting = false;
		Boolean createWarning = false;
		
		
		String file_to_convert = args[0];
		for(int i=0 ; i<args.length ; i++)
		{
			if(args[i].equals("-db"))
				dbInserting = true;
			if(args[i].equals("-p"))
				goalsPrinting = true;
			if(args[i].equals("-w"))
				createWarning = true;
		}

		File file = new File(file_to_convert);
		if (file.exists()) {
			System.out.println("Opening "+file_to_convert);
			
	        try 
	        {
				BPMN2Requirements extractor = new BPMN2Requirements(file,dbInserting,goalsPrinting,createWarning,"warning/");
			} 
	        catch (ParserConfigurationException e1) 
	        {
				e1.printStackTrace();
			}
	        catch (SAXException e1) 
	        {
				e1.printStackTrace();
			}
	        catch (IOException e1) 
	        {
				e1.printStackTrace();
			}
		} else {
			System.out.println("Cannot open "+file_to_convert);
			System.out.println("Usage: java ids.tx.bpmn.BPMN2Requirements <file> [-p] [-db] [-w]");
			
		}
	
	
	}
}