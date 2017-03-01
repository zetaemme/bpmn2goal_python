package cnr.icar.aose.bpmn2req.servlet;

import ids.tx.bpmn.BPMNWorkflow;
import ids.tx.conditions.Goal;
import ids.tx.requirements.RequirementsBuilder;
import ids.tx.utils.GoalDBInserter;
import ids.tx.utils.GoalPrinter;
import ids.tx.utils.WarningWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Servlet implementation class GetGoalSpecFromBPMNServlet
 */
@WebServlet("/GetGoalSpecFromBPMNServlet")
public class GetGoalSpecFromBPMNServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LinkedList<Goal> resultingGoals;  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetGoalSpecFromBPMNServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("CAL DO GET GetGoalSpecFromBPMNServlet");
		 String callback = request.getParameter("callback");
		 PrintWriter out = response.getWriter();
	        response.setContentType("text/html");
	        response.setHeader("Cache-control", "no-cache, no-store");
	        response.setHeader("Pragma", "no-cache");
	        response.setHeader("Expires", "-1");
	        
		String data=request.getParameter("bpmnDiagramm");
		System.out.println("data-->"+data);
		  InputStream is = new ByteArrayInputStream(data.getBytes());
			
		   BufferedReader in = new BufferedReader(new InputStreamReader(is ));
		
		 //  File bpmnFile= new File("my.txt");
		   	File bpmnFile = java.io.File.createTempFile("diagram",".tmp");    
		   //	bpmnFile.deleteOnExit();
		  
			             FileWriter fr = new FileWriter(bpmnFile);
			             BufferedWriter br  = new BufferedWriter(fr);
			
			             String line;
			
			             while ((line = in.readLine()) != null) {
			            	 br.write(line);
			            	 System.out.println("WRITE FILE");
			             }
			 
			             in.close();
			             br.close();
			             
			          
					try {
						DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
						
						//System.out.println("bpmnFile-->"+bpmnFile.toString());
						
			     		Document doc = dBuilder.parse(bpmnFile);
			     		doc.getDocumentElement().normalize();
			     		Element root = doc.getDocumentElement();
			     		WarningWriter warningWriter = new WarningWriter(false,"",bpmnFile.getName());
			     		BPMNWorkflow workflow = new BPMNWorkflow(root,warningWriter);
			     		GoalDBInserter dbInserter = new GoalDBInserter(false);
			     		GoalPrinter printer = new GoalPrinter(true,System.out);
			     		RequirementsBuilder requirementsBuilder = new RequirementsBuilder(workflow,dbInserter,printer);
			     		resultingGoals = requirementsBuilder.getGoalList();
			     		String goalString="";
			     		for (int i = 0; i <resultingGoals.size(); i++) {
			     			System.out.println("Goals-->"+i+": "+resultingGoals.get(i).toString());
			     			goalString+= resultingGoals.get(i).toString()+"\n";
						}
			     		//creo un file json che contiene i goals
			     		JSONObject obj = new JSONObject();
			     		
			     		obj.put("goals", goalString);
			     	
			     		 if(callback != null) {
			                 out.println(callback + "(" + obj.toString() + ");");
			             }
			             else {
			                 out.println(obj.toString());
			             }
			             
			             out.close();
			             bpmnFile.delete();
			     		//httpout.println("{diagram:'test'}");
							} catch (ParserConfigurationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SAXException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("CAL DO POST GetGoalSpecFromBPMNServlet");
		 String callback = request.getParameter("callback");
		 PrintWriter out = response.getWriter();
	        response.setContentType("text/html");
	        response.setHeader("Cache-control", "no-cache, no-store");
	        response.addHeader("Access-Control-Allow-Origin", "*");
	        response.setHeader("Pragma", "no-cache");
	        response.setHeader("Expires", "-1");
	        
		String data=request.getParameter("bpmnDiagramm");
		System.out.println("data-->"+data);
		  InputStream is = new ByteArrayInputStream(data.getBytes());
			
		   BufferedReader in = new BufferedReader(new InputStreamReader(is ));
		
		 //  File bpmnFile= new File("my.txt");
		   	File bpmnFile = java.io.File.createTempFile("diagram",".tmp");    
		   //	bpmnFile.deleteOnExit();
		  
			             FileWriter fr = new FileWriter(bpmnFile);
			             BufferedWriter br  = new BufferedWriter(fr);
			
			             String line;
			
			             while ((line = in.readLine()) != null) {
			            	 br.write(line);
			            	 System.out.println("WRITE FILE");
			             }
			 
			             in.close();
			             br.close();
			             
			          
					try {
						DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
						
						//System.out.println("bpmnFile-->"+bpmnFile.toString());
						
			     		Document doc = dBuilder.parse(bpmnFile);
			     		doc.getDocumentElement().normalize();
			     		Element root = doc.getDocumentElement();
			     		WarningWriter warningWriter = new WarningWriter(false,"",bpmnFile.getName());
			     		BPMNWorkflow workflow = new BPMNWorkflow(root,warningWriter);
			     		GoalDBInserter dbInserter = new GoalDBInserter(false);
			     		GoalPrinter printer = new GoalPrinter(true,System.out);
			     		RequirementsBuilder requirementsBuilder = new RequirementsBuilder(workflow,dbInserter,printer);
			     		resultingGoals = requirementsBuilder.getGoalList();
			     		String goalString="";
			     		for (int i = 0; i <resultingGoals.size(); i++) {
			     			System.out.println("Goals-->"+i+": "+resultingGoals.get(i).toString());
			     			goalString+= resultingGoals.get(i).toString()+"\n";
						}
			     		//creo un file json che contiene i goals
			     		JSONObject obj = new JSONObject();
			     		
			     		obj.put("goals", goalString);
			     	
			     		 if(callback != null) {
			                 out.println(callback + "(" + obj.toString() + ");");
			             }
			             else {
			                 out.println(obj.toString());
			             }
			             
			             out.close();
			             bpmnFile.delete();
			     		//httpout.println("{diagram:'test'}");
							} catch (ParserConfigurationException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SAXException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	}

}
