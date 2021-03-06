package treemembers

import documentelements.Page

import scala.meta._

final class Var(val defn: Defn.Var, val fileName: String, val page: Page) extends ValOrVar {

  val mods: List[Mod] = defn.mods
  val pats: List[Pat] = defn.pats
  val decltpe: Option[Type] = defn.decltpe
  val rhs: Option[Term] = defn.rhs

}
