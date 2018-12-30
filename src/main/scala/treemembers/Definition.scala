package treemembers

import scala.meta._

trait Definition extends TreeMember {

  val defn: Defn

  lazy val position: Position = defn.pos

}
