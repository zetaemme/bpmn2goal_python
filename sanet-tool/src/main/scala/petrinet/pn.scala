package petrinet

case class Place(id : String, maxTokens : Int = 1)
case class Transition(label:String)

abstract class Arc ()
case class PlaceToTransition (place : Place, transition: Transition) extends Arc
case class TransitionToPlace (transition: Transition,place : Place) extends Arc


case class PetrinetStructure(places: Array[Place], transitions: Array[Transition], arcs : Array[Arc])

case class PetrinetDynamic(tokens : scala.collection.mutable.Map[String,Int]) {

  def markers : Array[Boolean] = {

    val columns = tokens.keys.toArray.sorted
    var array=Array.fill(columns.length){false}

    var i = 0
    for (c <- columns) {
      array(i) = tokens(c)>0
      i += 1
    }

    array
  }

  def markers_string : String = {
    var string = ""
    val m = markers
    for (i <- m.indices)
      if (m(i)==true) string += "1" else string += "0"

    string
  }

  def token_in_place(name : String): Int = {
    if (tokens.contains(name))
      tokens(name)
    else
      0
  }

  override def clone(): PetrinetDynamic = {
    var newtokens = scala.collection.mutable.Map[String,Int]()
    for (t <- tokens.keys)
      //if (tokens(t)>0)
        newtokens = newtokens + (t -> tokens(t))

    PetrinetDynamic(newtokens)
  }

  override def toString: String = {
    var string=""
    for (s <- tokens.keys)
      if (tokens(s)>0)
        string += "("+s+")"

    string
  }
}

case class Petrinet(structure: PetrinetStructure, dynamic : PetrinetDynamic) {

  override def toString: String = dynamic.toString

  def get_fireable_transitions:List[Transition] = for (t<-all_transitions if can_fire(t)) yield t
  def all_transitions:List[Transition] = {
    var l = Set[Transition]()

    for (a <- structure.arcs) {
      a match {
        case ptt: PlaceToTransition =>
          l = l + ptt.transition
        case _ =>
      }
    }
    for (a <- structure.arcs) {
      a match {
        case ptt: TransitionToPlace =>
          l = l + ptt.transition
        case _ =>
      }
    }

    l.toList
  }

  def incoming_places(t:Transition): List[Place] = {
    var l = List[Place]()

    for (a <- structure.arcs if a.isInstanceOf[PlaceToTransition]) {
      val ptt = a.asInstanceOf[PlaceToTransition]
      if (ptt.transition==t)
        l = ptt.place :: l
    }

    l
  }
  def outgoing_places(t:Transition): List[Place] = {
    var l = List[Place]()

    for (a <- structure.arcs if a.isInstanceOf[TransitionToPlace]) {
      val ptt = a.asInstanceOf[TransitionToPlace]
      if (ptt.transition==t)
        l = ptt.place :: l
    }

    l
  }

  def can_fire(t : Transition) : Boolean = all_incoming_places_have_tokens(t) & all_outgoing_places_can_receive_tokens(t)
  def all_incoming_places_have_tokens(t: Transition): Boolean = {
    var b = true
    for (p <- incoming_places(t))
      if (dynamic.token_in_place(p.id)==0 )
        b=false
    b
  }
  def all_outgoing_places_can_receive_tokens(t: Transition): Boolean = {
    var b = true
    for (p <- outgoing_places(t))
      if (dynamic.token_in_place(p.id)>=p.maxTokens )
        b=false
    b
  }

  def fire(t : Transition) : Unit = {
    consume_tokens_in_incoming_places(t)
    produce_tokens_in_outgoing_places(t)
  }

  def consume_tokens_in_incoming_places(t : Transition) : Unit= {
    for (p <- incoming_places(t))
      dynamic.tokens(p.id) = dynamic.token_in_place(p.id)-1
  }

  def produce_tokens_in_outgoing_places(t : Transition): Unit = {
    for (p <- outgoing_places(t))
      dynamic.tokens(p.id) = dynamic.token_in_place(p.id) + 1
  }

  def get_places_with_tokens : List[Place] = {
    var arr = List[Place]()
    for (p <- structure.places)
      if (dynamic.token_in_place(p.id)>0)
        arr = p :: arr
    arr
  }

}