package treemembers

import treemembers.annotations.JSExport

import scala.meta._

final class Method(
                    val mods: List[Mod],
                    val name: Term.Name,
                    val tparams: List[Type.Param],
                    val paramss: List[List[Term.Param]],
                    val decltype: Option[Type],
                    val body: Term
                  ) extends WithAnnotations {

  lazy val scalaName: String = name.value

  lazy val typeParameters: List[TypeParameter] = tparams.map(_.name.value).map(Type.Name(_))
    .map(new TypeParameter(_))

  def jsTypeParameters: String =
    if (typeParameters.isEmpty) "" else typeParameters.map(_.toTS).mkString("<", ", ", ">")

  lazy val jsName: String = annotations.find(_.isInstanceOf[JSExport]) match {
    case Some(annotation: JSExport) => // this will necessarily be a JSExport if found
      annotation.name.getOrElse(scalaName)
    case _ => // This will be None, but the compiler will complain if we put "None"
      scalaName
  }

  lazy val parameters: List[List[TypedName]] = paramss.map(_.map(new TypedName(_)))

  def jsParameters: String = parameters.map(_.map(_.asString).mkString("(", ", ", ")")).mkString

  lazy val emptyParens: Boolean = parameters.isEmpty

  lazy val returnType: TypeParameter = new TypeParameter(decltype.get)

  // todo: curried methods
  def toTSDef: String =
    if (emptyParens) s"readonly $jsName: ${returnType.toTS};"
    else s"$jsName$jsTypeParameters$jsParameters: ${returnType.toTS};"

}
