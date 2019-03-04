package cnr.icar.aose.bpmn2req.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import bpmn.bpmnLoader;

/**
 * Servlet implementation class GetGoalSpecFromBPMNServlet
 */
@WebServlet("/GoalsFromBPMN")
public class GetGoalSpecFromBPMNServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetGoalSpecFromBPMNServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Date current = new Date();
		System.out.println("received GET from " + request.getRemoteAddr() + " ("+current.toString()+")");

		// Set response content type
	      response.setContentType("text/html");

	      // Actual logic goes here.
	      PrintWriter out = response.getWriter();
	      out.println("<h1>This servlet is working.</h1><p>Please use an http POST request, with a bpmnDiagramm parameter containing all the XMI of the Business Process</p>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Date current = new Date();
		System.out.println("received POST from " + request.getRemoteAddr() + " ("+current.toString()+")");
		long start = System.currentTimeMillis();
		
		String data = request.getParameter("bpmnDiagramm");
		InputStream is = new ByteArrayInputStream(data.getBytes());
		String goalString = bpmnLoader.goalsFromInputStream(is);

		String callback = request.getParameter("callback");
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		response.setHeader("Cache-control", "no-cache, no-store");
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "-1");

		JSONObject obj = new JSONObject();
		obj.put("goals", goalString);
		if (callback != null) {
			out.println(callback + "(" + obj.toString() + ");");
		} else {
			out.println(obj.toString());
		}

		out.close();
		long end = System.currentTimeMillis();
		System.out.println("replied in "+(end-start)+" ms");
	}

}
