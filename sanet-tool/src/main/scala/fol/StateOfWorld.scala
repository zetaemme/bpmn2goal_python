package fol
import scala.collection.mutable.ArrayBuffer

case class StateOfWorld private(statements : ArrayBuffer[GroundPredicate]) {
  
  override def toString: String = {
    var a_string: String = ""

    a_string += "["
    for (i <- statements.indices) {
      a_string += statements(i).toString
      if (i<statements.length-1)
      a_string += ","
        
    }
    a_string += "]"
    
    a_string
  }
  
  def stat_clone : Array[GroundPredicate] = statements.clone().toArray

}

object StateOfWorld {
  def create(statements : GroundPredicate*) : StateOfWorld = {

    val sorted : ArrayBuffer[GroundPredicate] = statements.to[ArrayBuffer].sortWith(_.toString < _.toString)
    StateOfWorld(sorted)
  }
  def create(statements : Array[GroundPredicate]) : StateOfWorld = {
    val sorted = statements.to[ArrayBuffer].sortWith(_.toString < _.toString)
    StateOfWorld(sorted)
  }
  
  def extend(w : StateOfWorld, new_statements : GroundPredicate*) : StateOfWorld = {
    val a = w.stat_clone.to[ArrayBuffer]
    for (x <- new_statements if !a.contains(x))
       a.append(x)
       
    StateOfWorld.create(a.toArray) 
  }

}