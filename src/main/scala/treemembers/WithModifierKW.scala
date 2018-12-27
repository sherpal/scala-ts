package treemembers

import scala.meta._

trait WithModifierKW {

  val mods: List[Mod]

  lazy val isPrivate: Boolean = mods.exists(_.isInstanceOf[Mod.Private])

  lazy val isProtected: Boolean = mods.exists(_.isInstanceOf[Mod.Protected])

  lazy val isPublic: Boolean = !(isPrivate | isProtected)

  lazy val isLazy: Boolean = mods.exists(_.isInstanceOf[Mod.Lazy])

}
