package treemembers

import scala.meta._

final class Val(val defn: Defn.Val, val fileName: String) extends ValOrVar {

  val mods: List[Mod] = defn.mods
  val pats: List[Pat] = defn.pats
  val decltpe: Option[Type] = defn.decltpe
  val rhs: Term = defn.rhs

}
