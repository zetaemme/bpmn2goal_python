package cnr.icar.aose.bpmn2req.servlet;

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


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

//TODO Realizzare la servlet che quando invocata riceve un file contenente la definizione bpmn di un workflow e ritorna un file con l 
// definizione in goalSpec ottenuta invocando il main della classe BPMN2Requirements
/**
 * Servlet implementation class Bpmn2GoalSpecService
 */
@WebServlet("/Bpmn2GoalSpecService")
public class Bpmn2GoalSpecService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LinkedList<Goal> resultingGoals;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Bpmn2GoalSpecService() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter httpout = response.getWriter();
		String idFileBPMN=request.getParameter("idFile");
		System.out.println("idFileBPMN-->"+idFileBPMN);
		System.out.println("CALL BPMN2GOALSPEC SERVICE");
		
		//effettua una query nel database in modo da poter recuperare la descrizione BPMN del processo
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String dbMusaAdress=MusaProperties.getDbMusaAdress();
			String dbPort=MusaProperties.getDbPort();
			String userDB=MusaProperties.getUserDB();
			String passDB=MusaProperties.getPassDB();
		    String dbName=MusaProperties.getDbName();
		   
		  Connection dbconn=DriverManager.getConnection("jdbc:mysql://"+dbMusaAdress+":"+dbPort+"/"+dbName,userDB,passDB);
		  Statement statement = dbconn.createStatement();
		 
		  String query="SELECT * FROM adw_workflow WHERE id= "+Integer.parseInt(idFileBPMN)+"";
		  System.out.println("query-->"+query);
		  ResultSet rs = statement.executeQuery(query);
		  
		  String fileSaved="";
		  while (rs.next()) 
			{
				
			  fileSaved = rs.getString("bpmn_file");
			
			
			 
			}
		  System.out.println("fileSaved-->"+fileSaved.toString());
		  InputStream is = new ByteArrayInputStream(fileSaved.getBytes());
		
		   BufferedReader in = new BufferedReader(new InputStreamReader(is ));
		
		   File bpmnFile= new File("my.txt");
		   //ai fini della demo OCCP per la macchina virtuale imposto questo percorso
			      //       File bpmnFile= new File("/home/oocp/.my.txt");
			             FileWriter fr = new FileWriter(bpmnFile);
			             BufferedWriter br  = new BufferedWriter(fr);
			
			             String line;
			
			             while ((line = in.readLine()) != null) {
			            	 br.write(line);
			            	 System.out.println("WRITE FILE");
			             }
			 
			             in.close();
			             br.close();
			             
			            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			     		DocumentBuilder dBuilder;
						
							dBuilder = dbFactory.newDocumentBuilder();
							 System.out.println("bpmnFile-->"+bpmnFile.toString());
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
//			     		  httpout.println(resultingGoals.toString());
//			     		  System.out.println("resulstong goals-->"+resultingGoals.toString());
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		URL url = new URL(request.getParameter("urlFile"));
//		
//		 System.out.println("CALL BPMN2GOALSPEC SERVICE  URL--->>"+url);
//	
//		             // read text returned by server
//		
//		             BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
//		
//		             File bpmnFile= new File("my.txt");
//		             FileWriter fr = new FileWriter(bpmnFile);
//		             BufferedWriter br  = new BufferedWriter(fr);
//		
//		             String line;
//		
//		             while ((line = in.readLine()) != null) {
//		            	 br.write(line);
//		             }
//		 
//		             in.close();
//		             br.close();
//		             
//		            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//		     		DocumentBuilder dBuilder;
//					try {
//						dBuilder = dbFactory.newDocumentBuilder();
//					
//		     		Document doc = dBuilder.parse(bpmnFile);
//		     		doc.getDocumentElement().normalize();
//		     		Element root = doc.getDocumentElement();
//		     		WarningWriter warningWriter = new WarningWriter(false,"",bpmnFile.getName());
//		     		BPMNWorkflow workflow = new BPMNWorkflow(root,warningWriter);
//		     		GoalDBInserter dbInserter = new GoalDBInserter(false);
//		     		GoalPrinter printer = new GoalPrinter(true,System.out);
//		     		RequirementsBuilder requirementsBuilder = new RequirementsBuilder(workflow,dbInserter,printer);
//		     		resultingGoals = requirementsBuilder.getGoalList();
//		     		String goalString="";
//		     		for (int i = 0; i <resultingGoals.size(); i++) {
//		     			System.out.println("Goals-->"+i+": "+resultingGoals.get(i).toString());
//		     			goalString+= resultingGoals.get(i).toString()+"\n";
//					}
//		     		httpout.println(goalString);
//		     		//System.out.println("resultingGoals-->"+goalString);
////		     		  httpout.println(resultingGoals.toString());
////		     		  System.out.println("resulstong goals-->"+resultingGoals.toString());
//					} catch (ParserConfigurationException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (SAXException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}


	}
	public LinkedList<Goal> getGoals()
	{
		return resultingGoals;
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
