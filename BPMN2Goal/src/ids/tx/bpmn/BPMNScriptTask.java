package ids.tx.bpmn;

import org.w3c.dom.Node;

public class BPMNScriptTask extends BPMNTask
{
	private static Integer counter = 0;
	private String script = "";
	private String scriptLanguage = "";
	
	public BPMNScriptTask(BPMNElement ambient, Node node)
	{
		super(ambient,node);
		
		nodeType = BPMNNodeType.SCRIPT_TASK;
		
		if(name.equals(""))
		{
			name = "task" + counter.toString();
			warningWriter.println("a BPMN task should specify a name -> Assigned:" + name, this);
			counter = counter + 1;
		}
		
		script = getAttributeValue(node,"script");
		scriptLanguage = getAttributeValue(node,"scriptLanguage");
	}
	
	public String getScript()
	{
		return script;
	}
	
	public String getScriptLanguage()
	{
		return scriptLanguage;
	}
}