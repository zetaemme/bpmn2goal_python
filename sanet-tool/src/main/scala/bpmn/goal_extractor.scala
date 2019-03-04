package bpmn

import java.io.{FileInputStream}

object goal_extractor extends App {

  val file = "process/procurement_example.bpmn"

  val is = new FileInputStream(file)
  val report = bpmnLoader.fullFromInputStream(is)

  println(report)

}
