package treemembers

import treemembers.annotations.{JSExportTopLevel, JSExportAll}

import scala.meta._

final class Class(val defn: Defn.Class, val fileName: String) extends Definition with WithAnnotations {

  implicit val implicitFileName: String = fileName

  val mods: List[Mod] = defn.mods
  val name: Name = defn.name
  val tparams: List[Type.Param] = defn.tparams
  val ctor: Ctor.Primary = defn.ctor
  val template: Template = defn.templ

  lazy val superClasses: List[Init] = template.inits

  def extendsFrom: String = superClasses.map(init => TypeParameter.scalaToTSTypeMapper(init.tpe)) match {
    case Nil => ""
    case list => s"extends " + list.mkString(", ")
  }

  lazy val typeParameters: List[String] = tparams.map(_.name.value)

  def jsTypeParameters: String =
    if (typeParameters.isEmpty) ""
    else typeParameters.mkString("<", ", ", ">")

  lazy val constructorParams: List[List[TypedName]] = ctor.paramss.map(_.map(new TypedName(_)))

  def jsConstructorParams: String = constructorParams.map(_.map(_.asString).mkString("(", ", ", ")")).mkString

  lazy val isJSTopLevelExported: Boolean = annotations.exists(_.isInstanceOf[JSExportTopLevel])

  lazy val jsExportAll: Boolean = annotations.exists(_.isInstanceOf[JSExportAll])

  lazy val jsClassName: String = annotations.find(_.isInstanceOf[JSExportTopLevel]) match {
    case Some(jsExportTopLevel: JSExportTopLevel) => jsExportTopLevel.className
    case _ => name.value
  }

  lazy val methods: List[Method] = template.children.map(TreeMember.apply)
    .flatMap({
      case method: Method => Some(method)
      case _ => None
    })

  lazy val jsExportedMethods: List[Method] = thoseExported(methods)

  lazy val vals: List[Val] = template.children.map(TreeMember.apply)
    .flatMap({
      case v: Val => Some(v)
      case _ => None
    })

  lazy val jsExportedVals: List[Val] = thoseExported(vals)

  lazy val vars: List[Var] = template.children.map(TreeMember.apply)
    .flatMap({
      case v: Var => Some(v)
      case _ => None
    })

  lazy val jsExportedVars: List[Var] = thoseExported(vars)

  def classOrInterface: String = if (isJSTopLevelExported) "class" else "interface"

  def toTSDef: String =
    s"""
       |declare $classOrInterface $jsClassName$jsTypeParameters $extendsFrom {
       |
       |${if (isJSTopLevelExported) "\tconstructor" + jsConstructorParams else ""}
       |
       |${jsExportedVals.map(_.toTSDef).map("\t" + _).mkString("\n")}
       |
       |${jsExportedVars.map(_.toTSDef).map("\t" + _).mkString("\n")}
       |
       |${jsExportedMethods.map(_.toTSDef).map("\t" + _).mkString("\n")}
       |
       |}
   """.stripMargin.split("\n").filter(_.nonEmpty).mkString("\n")

  private def thoseExported[T <: WithAnnotations with WithModifierKW](list: List[T]): List[T] =
    list.filter(_.isPublic).filter(jsExportAll | _.jsExported)

}
