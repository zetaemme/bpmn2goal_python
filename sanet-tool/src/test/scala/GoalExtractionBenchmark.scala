import bpmn.{Task, Workflow, WorkflowState, bpmnLoader}
import org.scalameter.api._

import org.scalameter.picklers.Implicits._

object GoalExtractionBenchmark extends Bench.LocalTime {
  val files: Array[String] = Array("process/analysis_of_complexity/example_bpmn_1.bpmn",
    "process/analysis_of_complexity/example_bpmn_2.bpmn",
    "process/analysis_of_complexity/example_bpmn_3.bpmn",
    "process/analysis_of_complexity/example_bpmn_4.bpmn",
    "process/analysis_of_complexity/example_bpmn_5.bpmn",
    "process/analysis_of_complexity/example_bpmn_6.bpmn")

  val sizes: Gen[Int] = Gen.range("size")(300000, 1500000, 300000)
  val file_gen = Gen.enumeration[String]("files")(
    files(0),files(1),files(2),files(3),files(4),files(5)
  )

  files.foreach( file => {
    val wf = bpmnLoader.fromFile(file).get
    println("Complexity("+file+"): "+wf.complexity)

  })

  performance of "GoalExtraction" in {
    measure method "extract_goal" in {
      using(file_gen) in {
        f =>
          val wfopt = bpmnLoader.fromFile(f)
          val wfstate = new WorkflowState(wfopt.get)
          extract_goal(wfopt.get, wfstate)
      }

    }
  }

  private def extract_goal(wf: Workflow, wfstate: WorkflowState) = {
    for (i <- wf.items if (i.isInstanceOf[Task])) {
      val task = i.asInstanceOf[Task]

      val pre = wfstate.goal_trigger_condition(i)
      val post = wfstate.goal_final_state(i)

    }
  }

}
