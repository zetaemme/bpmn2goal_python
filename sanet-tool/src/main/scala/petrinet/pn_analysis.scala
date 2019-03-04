package petrinet

class pn_analysis(petrinet: Petrinet) {

  var markersmap = List[String](petrinet.dynamic.markers_string)

  def reachability(node : PetrinetDynamic) : Unit = {
    val pn = Petrinet(petrinet.structure,node)
    val trx = pn.get_fireable_transitions

    for (t <- trx) {
      val clone_node = node.clone()
      val clone_pn = Petrinet(petrinet.structure,clone_node)
      clone_pn.fire(t)

      val markers = clone_pn.dynamic.markers_string
      if (! markersmap.contains(markers)) {
        markersmap = markers :: markersmap
        reachability(clone_node)
      }
    }
  }



/*
  class ReachabilityTree(structure: PetrinetStructure, node : PetrinetDynamic) {
    var subnodes = Array[ReachabilityTree]()
    //val markers : Array[Boolean] = initial.markers

    init()

    def init() : Unit = {
      val pn = Petrinet(structure,node)
      val trx = pn.get_fireable_transitions

      for (t <- trx) {
        val clone_node = node.clone()
        val clone_pn = Petrinet(structure,clone_node)
        clone_pn.fire(t)

        val markers = clone_pn.dynamic.markers.toString
        if (! markersmap.contains(markers)) {
          markersmap = markers :: markersmap
          val subnode = new ReachabilityTree(structure,clone_node)


        }
      }

    }



  }
*/

}
