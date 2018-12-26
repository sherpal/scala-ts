package treemembers

import scala.meta._

final class TypedName(param: Term.Param) {

  lazy val name: String = param.name.value

  lazy val hasDefault: Boolean = param.default.isDefined

  lazy val tsType: String = param.decltpe match {
    case Some(t) =>
      TypeParameter.scalaToTSTypeMapper(t)
    case None => "any"
  }

  def asString: String = s"$name${if (hasDefault) "?" else ""}: $tsType"

}
