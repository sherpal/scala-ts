package treemembers

import treemembers.annotations.JSExport

import scala.meta._

trait ValOrVar extends Definition with WithAnnotations with WithModifierKW {

  val pats: List[Pat]
  val decltpe: Option[Type]

  lazy val scalaName: String = pats.head match {
    case Pat.Var(name) => name.value
  }

  lazy val jsName: String = annotations.find(_.isInstanceOf[JSExport]) match {
    case Some(annotation: JSExport) => // this will necessarily be a JSExport if found
      annotation.name.getOrElse(scalaName)
    case _ => // This will be None, but the compiler will complain if we put "None"
      scalaName
  }

  lazy val jsType: String = decltpe match {
    case Some(tpe) => TypeParameter.scalaToTSTypeMapper(tpe)
    case None => "any"
  }

  lazy val isVal: Boolean = this.isInstanceOf[Val]

  def toTSDef: String =
    s"${if (isVal) "readonly " else ""}$jsName: $jsType;"


}
