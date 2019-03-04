import bpmn._
import fol.TweetyFormula
import petrinet.{pn_analysis, pnmlLoader}
import junit.framework.TestCase


class test_xml extends TestCase {

  def testLoadPNML(): Unit = {
    val file = "/Users/luca/Desktop/SANET TOOL/process/email-voting/PROCESS_IBP.xml.pnml"
    val pn_option = pnmlLoader.fromFile(file)
    if (pn_option.isDefined) {
      val pn = pn_option.get

      var a = new pn_analysis(pn)
      a.reachability(pn.dynamic)

      for (m <- a.markersmap.sorted)
        println(m)


    }


  }

  def testLoadBPMN(): Unit = {
    extract_goals("/Users/luca/Desktop/processes4goals/email-voting.bpmn")
  }

  def extract_goals(file:String):Unit = {

    val wfopt = bpmnLoader.fromFile(file)

    if (wfopt.isDefined) {
      println("FILE: "+file)

      val wf = wfopt.get
      val wfstate = new WorkflowState(wf)

      println("workflow elements: "+wf.items.size)
      println("workflow flows: "+wf.flows.size)
      println("workflow complexity: "+wf.complexity)

      extract_goal(wf, wfstate,false,true)
    }
  }


  private def extract_goal(wf: Workflow, wfstate: WorkflowState,minimal:Boolean,verbose:Boolean=false) = {
    for (i <- wf.items if (i.isInstanceOf[Task])) {
      val task = i.asInstanceOf[Task]

      if (verbose) println("TASK: " + task.label.replace("\n", " "))

      if (!minimal) {
        val ws = wfstate.waited_state(i)
        if (verbose) println("WS: " + ws.prettyString())

        val gs = wfstate.generated_state(i)
        if (verbose) println("GS: " + gs.prettyString())

        val pre_inf = wfstate.predecessors_influence(i)
        if (verbose) println("PRE_INF: " + pre_inf.prettyString())

        val post_inf = wfstate.successors_influence(i)
        if (verbose) println("POST_INF: " + post_inf.prettyString())
      }
      val pre = wfstate.goal_trigger_condition(i)
      if (verbose) println("GOAL TC: " + pre.prettyString())

      val post = wfstate.goal_final_state(i)
      if (verbose) println("GOAL FS: " + post.prettyString())
      if (verbose) println()

    }
  }

  private def print_events(wf: Workflow, wfstate: WorkflowState) = {
    for (i <- wf.items if i.isInstanceOf[Event]) {
      val event = i.asInstanceOf[Event]
      println("EVENT: " + event.label.replace("\n", " "))

      val ws = wfstate.waited_state(i)
      println("WS: " + ws.prettyString())

      val gs = wfstate.generated_state(i)
      println("GS: " + gs.prettyString())

      println()
    }
  }
}
