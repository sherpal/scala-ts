package treemembers

import documentelements.Page
import treemembers.annotations.JSExport

import scala.meta._
import scala.meta.internal.semanticdb.SymbolInformation

final class Method(val defn: Defn.Def, val fileName: String, val page: Page)
  extends Definition with WithAnnotations with WithModifierKW {

  implicit val implicitFileName: String = fileName
  implicit val implicitPage: Page = page

  val mods: List[Mod] = defn.mods
  val name: Term.Name = defn.name
  val tparams: List[Type.Param] = defn.tparams
  val paramss: List[List[Term.Param]] = defn.paramss
  val decltype: Option[Type] = defn.decltpe
  val body: Term = defn.body

  lazy val symbol: SymbolInformation = page.symbolsMap((fileName, name.toString))

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

  lazy val signature = symbol.signature

  // todo: curried methods
  def toTSDef: String =
    if (emptyParens) s"readonly $jsName: ${returnType.toTS};"
    else s"$jsName$jsTypeParameters$jsParameters: ${returnType.toTS};"

}
