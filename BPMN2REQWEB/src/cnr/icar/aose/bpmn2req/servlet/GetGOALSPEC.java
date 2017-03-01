package cnr.icar.aose.bpmn2req.servlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ids.tx.bpmn.BPMN2Requirements;
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
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Servlet implementation class GetGOALSPEC
 */
@WebServlet("/GetGOALSPEC")
public class GetGOALSPEC extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LinkedList<Goal> resultingGoals;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetGOALSPEC() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		URL url = new URL(request.getParameter("urlFile"));
//		
		 System.out.println("CALL BPMN2GOALSPEC SERVICE  URL--->>"+url);
			PrintWriter httpout = response.getWriter();
		             // read text returned by server
		
		             BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		             String path = request.getSession().getServletContext().getRealPath("/bpmnFile.txt");
		         	
		             File bpmnFile= new File(path);
		             FileWriter fr = new FileWriter(bpmnFile);
		             BufferedWriter br  = new BufferedWriter(fr);
		
		             String line;
		
		             while ((line = in.readLine()) != null) {
		            	 br.write(line);
		             }
		 
		             in.close();
		             br.close();
		             
		            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		     		DocumentBuilder dBuilder;
					try {
						dBuilder = dbFactory.newDocumentBuilder();
					
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
		     		httpout.println(goalString);
		     		//System.out.println("resultingGoals-->"+goalString);
//		     		  httpout.println(resultingGoals.toString());
//		     		  System.out.println("resulstong goals-->"+resultingGoals.toString());
					} catch (ParserConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	}

	/**
	 * @throws IOException 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	//	URL url = new URL(request.getParameter("urlFile"));
			System.out.println("CALLdoPost  GetGOALSPEC ");
		PrintWriter httpout = response.getWriter();
		             // read text returned by server
				      
					 BufferedReader in = request.getReader();
		
		             File bpmnFile= new File("my.txt");
		             FileWriter fr = new FileWriter(bpmnFile);
		             BufferedWriter br  = new BufferedWriter(fr);
		
		             String line;
		
		             while ((line = in.readLine()) != null) {
		            	 br.write(line);
		             }
		 
		             in.close();
		             br.close();
		             
		            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		     		DocumentBuilder dBuilder;
					try {
						dBuilder = dbFactory.newDocumentBuilder();
					
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
		     		httpout.println(goalString);
		     		//System.out.println("resultingGoals-->"+goalString);
//		     		  httpout.println(resultingGoals.toString());
//		     		  System.out.println("resulstong goals-->"+resultingGoals.toString());
					} catch (ParserConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	}

}
