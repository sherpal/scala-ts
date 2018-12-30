package treemembers

import treemembers.annotations.JSExport

import scala.meta._

trait WithAnnotations extends TreeMember {

  val mods: List[Mod]

  private implicit lazy val implicitFileName: String = fileName

  lazy val annotations: List[Annotation] = mods.filter(_.isInstanceOf[Mod.Annot])
    .map(TreeMember.apply).map(_.asInstanceOf[Annotation])

  lazy val jsExported: Boolean = annotations.exists(_.isInstanceOf[JSExport])

}
