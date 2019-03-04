package ids.tx.utils;

import ids.tx.bpmn.BPMNElement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

public class WarningWriter
{
	private static Boolean active = false;
	private File warnings;
	private FileWriter writer;
	private Integer counter = 0;
	
	public WarningWriter(Boolean createWarnings, String warningFilePath, String bpmnFileName) 
	{
		active = createWarnings;
		Timestamp timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis());
		String identifier = bpmnFileName.split("[.]")[0] + "_" + timestamp.toString().replaceAll("[-:]","").replaceAll(" ","_").substring(0, 15);
		if(active.equals(true))
		{

			try
			{
				warnings = new File(warningFilePath + identifier + "_warning.log");
				writer = new FileWriter(warnings);
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void close()
	{
		try 
		{
			writer.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void println(String string, BPMNElement element)
	{
		if(warnings!=null)
		{
			String line = "WARNING " + counter.toString() + " [" + element.getName() + "] : " + string + ";\n";
			try
			{
				writer.write(line, 0, line.length());
				counter = counter + 1;
				writer.flush();
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}