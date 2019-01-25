package treemembers

import documentelements.Page

import scala.meta._

final class TypedName(param: Term.Param)(implicit val fileName: String, val page: Page) extends TreeMember {

  lazy val position: Position = param.decltpe.get.pos

  lazy val name: String = param.name.value

  lazy val hasDefault: Boolean = param.default.isDefined

  lazy val tsType: String = param.decltpe match {
    case Some(t) =>
      TypeParameter.scalaToTSTypeMapper(t)
    case None => "any"
  }

  def asString: String = s"$name${if (hasDefault) "?" else ""}: $tsType"

}
