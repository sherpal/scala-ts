package treemembers

import treemembers.annotations.{JSExportTopLevel, JSExportAll}

import scala.meta._

final class Class(
                  val mods: List[Mod],
                  val name: Name,
                  val tparams: List[Type.Param],
                  val ctor: Ctor.Primary,
                  val template: Template
                 ) extends WithAnnotations {

  lazy val superClasses: List[Init] = template.inits

  def extendsFrom: String = superClasses.map(init => TypeParameter.scalaToTSTypeMapper(init.tpe)) match {
    case Nil => ""
    case list => s"extends " + list.mkString(", ")
  }

  lazy val constructorParams: List[List[TypedName]] = ctor.paramss.map(_.map(new TypedName(_)))

  def jsConstructorParams: String = constructorParams.map(_.map(_.asString).mkString("(", ", ", ")")).mkString

  lazy val isJSTopLevelExported: Boolean = annotations.exists(_.isInstanceOf[JSExportTopLevel])

  lazy val jsExportAll: Boolean = annotations.contains(JSExportAll)

  lazy val jsClassName: String = annotations.find(_.isInstanceOf[JSExportTopLevel])
    .get.asInstanceOf[JSExportTopLevel].className

  lazy val methods: List[Method] = template.children.map(TreeMember.apply)
    .flatMap({
      case method: Method => Some(method)
      case _ => None
    })

  lazy val jsExportedMethods: List[Method] =
    if (jsExportAll) methods
    else methods.filter(_.jsExported)

  lazy val vals: List[Val] = template.children.map(TreeMember.apply)
    .flatMap({
      case v: Val => Some(v)
      case _ => None
    })

  lazy val jsExportedVals: List[Val] =
    if (jsExportAll) vals
    else vals.filter(_.jsExported)

  def classOrInterface: String = if (isJSTopLevelExported) "class" else "interface"

  def toTSDef: String =
    s"""
       |$classOrInterface $jsClassName $extendsFrom {
       |
       |${if (isJSTopLevelExported) "\tconstructor" + jsConstructorParams else ""}
       |
       |${jsExportedVals.map(_.toTSDef).map("\t" + _).mkString("\n")}
       |
       |${jsExportedMethods.map(_.toTSDef).map("\t" + _).mkString("\n")}
       |
       |}
   """.stripMargin

}
