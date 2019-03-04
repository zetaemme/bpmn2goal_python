package petrinet

import scala.collection.mutable.ArrayBuffer
import scala.xml.{Elem, Node, NodeSeq, XML}

object pnmlLoader {

  def fromFile(filename : String) : Option[Petrinet] = {

    val node = XML.loadFile(filename)
    if (node.label == "pnml") {
      val places = parse_places(node \\ "place")
      val transs = parse_transitions(node \\ "transition")
      val arcs = parse_arcs(places, transs, node \\ "arc")

      val structure = PetrinetStructure(places,transs,arcs)

      val tokens = parse_initial_marking(node \\ "place")
      val dynamic = PetrinetDynamic(tokens)

      Some(Petrinet(structure,dynamic))
    } else  {

      None
    }
  }

  def parse(elem: Node) : Unit = {
    for (c <- elem.child if c.isInstanceOf[Elem]) {
      println(c.label)
      parse(c)
    }

  }

  def parse_initial_marking(nodes: NodeSeq): scala.collection.mutable.Map[String,Int] = {
    var map = scala.collection.mutable.Map[String,Int]()

    for (p <- nodes) {
      val id = p \ "@id"
      val marking : Int = (p \ "initialMarking" \ "value").text.toInt

      map += (id.text -> marking)
    }


    map
  }


  def parse_places(nodes: NodeSeq): Array[Place] = {
    var array = ArrayBuffer[Place]()

    for (p <- nodes) {
      val id = p \ "@id"
      val place = Place(id.text,1)
      array += place
    }


    array.toArray
  }

  def parse_transitions(nodes: NodeSeq): Array[Transition] = {
    var array = ArrayBuffer[Transition]()

    for (t <- nodes) {
      val id = t \ "@id"
      val trans = Transition(id.text)
      array += trans
    }


    array.toArray
  }

  def parse_arcs(places: Array[Place], transs: Array[Transition], nodes: NodeSeq) : Array[Arc] = {
    var array = ArrayBuffer[Arc]()

    for (a <- nodes) {
      //val id = a \ "@id"
      val source = (a \ "@source").text
      val dest = (a \ "@target").text

      if (places.find(_.id == source).isDefined) {
        val p = places.find(_.id == source).get
        val t = transs.find(_.label == dest).get
        array += PlaceToTransition(p,t)

      } else if (transs.find(_.label == source).isDefined){
        val p = places.find(_.id == dest).get
        val t = transs.find(_.label == source).get
        array += TransitionToPlace(t,p)
      }
    }


    array.toArray

  }


}
