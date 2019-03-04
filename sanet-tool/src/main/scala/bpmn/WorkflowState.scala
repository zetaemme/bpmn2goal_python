package bpmn

import fol._

import scala.collection.mutable.ArrayBuffer

class WorkflowState(wf : Workflow) {

  def waited_state(item : Item) : folFormula = {
    item match {
      case  t : Task => task_waited_state(t)
      case e : Event => event_waited_state(e)
      case g : Gateway => gateway_waited_state(g)
    }
  }

  def generated_state(item : Item) : folFormula = {
    item match {
      case  t : Task => task_generated_state(t)
      case e : Event => event_generated_state(e)
      case g : Gateway => gateway_generated_state(g)
    }
  }

  def successors_influence(item : Item) : folFormula = {
    var disj = ArrayBuffer[folFormula]()
    val out_seq = outgoing_seq(item)
    for (f <- out_seq)
      if (f.condition.isDefined)
        disj += conjunction_or_truth(inverse_propagation(f),f.condition.get)
      else
        disj += inverse_propagation(f)

    x_disjunction_or_truth(disj)
  }

  def predecessors_influence(item : Item) : folFormula = {
    var disj = ArrayBuffer[folFormula]()
    val in_seq = incoming_seq(item)
    for (f <- in_seq)
      if (f.condition.isDefined)
        disj += conjunction_or_truth(direct_propagation(f),f.condition.get)
      else
        disj += direct_propagation(f)

    x_disjunction_or_truth(disj)
  }

  def goal_trigger_condition(item : Item) : folFormula = {
    conjunction_or_truth(waited_state(item),predecessors_influence(item))
  }

  def goal_final_state(item : Item) : folFormula = {
    conjunction_or_truth(generated_state(item),successors_influence(item))
  }

  def direct_propagation(f: SequenceFlow): folFormula = {
    val pred = f.start
    pred match {
      case  t : Task => generated_state(t)
      case e : Event => generated_state(e)
      case g : Gateway =>
        if (g.gwtype=="exclusive") {
          var disj = ArrayBuffer[folFormula]()
          val in_seq = incoming_seq(g)
          for (f <- in_seq)
            if (f.condition.isDefined)
              disj += conjunction_or_truth(direct_propagation(f),f.condition.get)
            else
              disj += direct_propagation(f)

          x_disjunction_or_truth(disj)

        } else if (g.gwtype=="inclusive") {
          var conj = ArrayBuffer[folFormula]()
          val in_seq = incoming_seq(g)
          for (f <- in_seq)
            if (f.condition.isDefined)
              conj += conjunction_or_truth(direct_propagation(f),f.condition.get)
            else
              conj += direct_propagation(f)

          disjunction_or_truth(conj)

        } else if (g.gwtype=="parallel") {
          var conj = ArrayBuffer[folFormula]()
          val in_seq = incoming_seq(g)
          for (f <- in_seq)
            if (f.condition.isDefined)
              conj += conjunction_or_truth(direct_propagation(f),f.condition.get)
            else
              conj += direct_propagation(f)

          conjunction_or_truth(conj)

        } else {
          Truth()
        }
      case _ => Truth()
    }

  }

  def inverse_propagation(f: SequenceFlow): folFormula = {
    val pred = f.end
    pred match {
      case  t : Task => waited_state(t)
      case e : Event => waited_state(e)
      case g : Gateway =>
        if (g.gwtype=="exclusive") {
          var disj = ArrayBuffer[folFormula]()
          val out_seq = outgoing_seq(g)
          for (f <- out_seq)
            if (f.condition.isDefined)
              disj += conjunction_or_truth(inverse_propagation(f),f.condition.get)
            else
              disj += inverse_propagation(f)

          x_disjunction_or_truth(disj)

        } else if (g.gwtype=="inclusive") {
          var disj = ArrayBuffer[folFormula]()
          val out_seq = outgoing_seq(g)
          for (f <- out_seq)
            if (f.condition.isDefined)
              disj += conjunction_or_truth(direct_propagation(f),f.condition.get)
            else
              disj += inverse_propagation(f)

          disjunction_or_truth(disj)

        } else if (g.gwtype=="parallel") {
          var conj = ArrayBuffer[folFormula]()
          val out_seq = outgoing_seq(g)
          for (f <- out_seq)
            if (f.condition.isDefined)
              conj += conjunction_or_truth(inverse_propagation(f),f.condition.get)
            else
              conj += inverse_propagation(f)

          conjunction_or_truth(conj)

        } else {
          Truth()
        }
      case _ => Truth()
    }

  }


  private def task_waited_state(t: Item): folFormula = {
    var conj = ArrayBuffer[folFormula]()

    val data_in = expected_data(t)
    val mess_in = expected_messages(t)

    for (d<-data_in) {
      val state = if (d.state.isDefined) label(d.state.get) else "available"
      val p = GroundPredicate(state,AtomTerm(label(d.objecttype.name)))
      conj += GroundLiteral(p)
    }

    for (m<-mess_in) {
      val state = "received"
      val p = GroundPredicate(state,AtomTerm(label(m.label)))
      conj += GroundLiteral(p)
    }

    conjunction_or_truth(conj)
  }

  private def event_waited_state(t: Item): folFormula = Truth()

  private def gateway_waited_state(g: Gateway): folFormula = Truth()


  private def task_generated_state(t: Item): folFormula = {
    var normal_termination_terms = ArrayBuffer[folFormula]()

    val data_out = produced_data(t)
    val mess_out = produced_messages(t)

    for (d<-data_out) {
      val state = if (d.state.isDefined) label(d.state.get) else "available"
      val p = GroundPredicate(state,AtomTerm(label(d.objecttype.name)))
      normal_termination_terms += GroundLiteral(p)
    }

    for (m<-mess_out) {
      val state = "sent"
      val p = GroundPredicate(state,AtomTerm(label(m.label)))
      normal_termination_terms += GroundLiteral(p)
    }

    if (normal_termination_terms.isEmpty && t.isInstanceOf[Task]) {
      val task = t.asInstanceOf[Task]
      normal_termination_terms += GroundLiteral(GroundPredicate("done",AtomTerm(label(task.label))))
    }

    var normal_termination = conjunction_or_truth(normal_termination_terms)


    /* check for boundary conditions */
    var boundary_termination_terms = ArrayBuffer[folFormula](normal_termination)
    val boundaries = attached_bondary_conditions(t)
    for (bf <- boundaries) {
      val boundary_event = bf.boundary
      boundary_termination_terms += event_generated_state(boundary_event)
    }
    x_disjunction_or_truth(boundary_termination_terms)

  }

  private def attached_bondary_conditions(t: Item) : List[BoundaryFlow] = {
    var att : List[BoundaryFlow] = List()

    for (f <- wf.flows if f.isInstanceOf[BoundaryFlow]) {
      val bf = f.asInstanceOf[BoundaryFlow]
      if (bf.source==t)
        att = bf :: att
    }

    att
  }


  private def event_generated_state(t: Item): folFormula = {
    var conj = ArrayBuffer[folFormula]()

    if (t.isInstanceOf[Event]) {
      val event = t.asInstanceOf[Event]

      event.definition match {
        case m: MessageEventDefinition =>
          val state = "received"
          if (!m.mess.label.isEmpty) {
            val p = GroundPredicate(state, AtomTerm(label(m.mess.label)))
            conj += GroundLiteral(p)
          } else {
            val p = GroundPredicate(state, AtomTerm(label(event.label)))
            conj += GroundLiteral(p)
          }

        case s: SignalEventDefinition =>
          val state = "catched"
          val p = GroundPredicate(state, AtomTerm(label(s.signal.label)))
          conj += GroundLiteral(p)

        case x: TimerEventDefinition =>
          x.timertype match {
            case "duration" =>
              if (event.eventtype=="boundary") {
                val task = search_task_via_boundary(t)
                val pre_inf = predecessors_influence(task)
                val p = GroundPredicate("after", AtomTerm(x.timecondition), AtomTerm(pre_inf.prettyString()))
                conj += GroundLiteral(p)
              } else {
                val pre_inf = predecessors_influence(t)
                val p = GroundPredicate("after", AtomTerm(x.timecondition), AtomTerm(pre_inf.prettyString()))
                conj += GroundLiteral(p)
              }
            case "timedate" =>
              val p = GroundPredicate("at", AtomTerm(x.timecondition))
              conj += GroundLiteral(p)
            case "repetition" =>
              val p = GroundPredicate("every", AtomTerm(x.timecondition))
              conj += GroundLiteral(p)
            case _ =>
          }
      }
    }

    conjunction_or_truth(conj)
  }

  private def search_task_via_boundary(t: Item) : Task = {
    var task : Task = null
    for (f <- wf.flows if f.isInstanceOf[BoundaryFlow]) {
      val bf = f.asInstanceOf[BoundaryFlow]
      if (bf.boundary==t)
        task = bf.source
    }
    task
  }



  private def gateway_generated_state(g: Gateway): folFormula = Truth()


  private def incoming_seq(item : Item) : List[SequenceFlow] = {
    var l = List[SequenceFlow]()

    for (f <- wf.flows if f.isInstanceOf[SequenceFlow]) {
      val flow = f.asInstanceOf[SequenceFlow]
      if (flow.end == item)
        l = flow :: l
    }

    l
  }

  private def outgoing_seq(item : Item) : List[SequenceFlow] = {
    var l = List[SequenceFlow]()

    for (f <- wf.flows if f.isInstanceOf[SequenceFlow]) {
      val flow = f.asInstanceOf[SequenceFlow]
      if (flow.start == item)
        l = flow :: l
    }

    l
  }

  private def expected_data(t: Item) : List[DataObjectRef] = {
    var l = List[DataObjectRef]()

    for (f <- wf.flows if f.isInstanceOf[DataInputFlow]) {
      val in_flow = f.asInstanceOf[DataInputFlow]
      if (in_flow.target == t)
        l = in_flow.data :: l
    }

    l
  }

  private def expected_messages(t: Item) : List[Message] = {
    var l = Set[Message]()

    /*for (f <- wf.flows if f.isInstanceOf[InMessageFlow]) {
      val in_flow = f.asInstanceOf[InMessageFlow]
      if (in_flow.receiver == t)
        l += in_flow.mess
    }*/

    if (t.isInstanceOf[Task]) {
      val task = t.asInstanceOf[Task]
      if (task.tasktype=="receive" && task.message_opt.isDefined) {
        val mess = task.message_opt.get
        l += mess
      }
    }

    l.toList
  }

  private def produced_data(t: Item) : List[DataObjectRef] = {
    var l = List[DataObjectRef]()

    for (f <- wf.flows if f.isInstanceOf[DataOutputFlow]) {
      val out_flow = f.asInstanceOf[DataOutputFlow]
      if (out_flow.source == t)
        l = out_flow.data :: l
    }

    l
  }

  private def produced_messages(t: Item) : List[Message] = {
    var l = Set[Message]()

    if (t.isInstanceOf[Task]) {
      val task = t.asInstanceOf[Task]
      if (task.tasktype=="send" && task.message_opt.isDefined) {
        val mess = task.message_opt.get
        l += mess
      }
    }

    l.toList
  }

  private def label(s : String) : String = s.replace("\n","_").replace(" ","_").toLowerCase

  private def disjunction_or_truth(term1:folFormula,term2:folFormula) : folFormula = {
    disjunction_or_truth(ArrayBuffer(term1,term2))
  }

  private def disjunction_or_truth(array : ArrayBuffer[folFormula]) : folFormula = {
    var array_no_true : scala.collection.mutable.Set[folFormula] = scala.collection.mutable.Set()

    for (a<-array if a != Truth())
      array_no_true += a

    if (array_no_true.isEmpty) {
      Truth()
    }else if (array_no_true.size==1) {
      array_no_true.head
    }else {
      Disjunction(array_no_true.to[ArrayBuffer])
    }
  }

  private def x_disjunction_or_truth(term1:folFormula,term2:folFormula) : folFormula = {
    x_disjunction_or_truth(ArrayBuffer(term1,term2))
  }

  private def x_disjunction_or_truth(array : ArrayBuffer[folFormula]) : folFormula = {
    var array_no_true : scala.collection.mutable.Set[folFormula] = scala.collection.mutable.Set()

    for (a<-array if a != Truth())
      array_no_true += a

    if (array_no_true.isEmpty) {
      Truth()
    }else if (array_no_true.size==1) {
      array_no_true.head
    }else {
      XDisjunction(array_no_true.to[ArrayBuffer])
    }
  }


  private def conjunction_or_truth(term1:folFormula,term2:folFormula) : folFormula = {
    conjunction_or_truth(ArrayBuffer(term1,term2))
  }

  private def conjunction_or_truth(array : ArrayBuffer[folFormula]) : folFormula = {
    var array_no_true : scala.collection.mutable.Set[folFormula] = scala.collection.mutable.Set()

    for (a<-array if a != Truth())
      array_no_true += a

    if (array_no_true.isEmpty) {
      Truth()
    } else if (array_no_true.size==1) {
      array_no_true.head
    }else{
      Conjunction(array_no_true.to[ArrayBuffer])
    }
  }
}
