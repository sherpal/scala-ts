package treemembers

import scala.meta._

trait Annotation extends TreeMember {

  val implemented: Boolean = true

}

object Annotation {

  def apply(init: Init, fileName: String): Annotation = init match {
    case Init(tpe, _, argss) =>
      tpe.asInstanceOf[Type.Name].value match {
        case "JSExportTopLevel" => annotations.JSExportTopLevel(argss.flatten.head match {
          case Lit.String(value) => value
        }, tpe.pos, fileName)
        case "JSExportAll" => annotations.JSExportAll(tpe.pos, fileName)
        case "JSExport" =>
          annotations.JSExport(argss.flatten.headOption.map({
            case Lit.String(value) => value
          }), tpe.pos, fileName)
        case _ =>
          val f = fileName
          new Annotation {
            override val implemented: Boolean = false

            val position: Position = tpe.pos

            val fileName: String = f
          }
      }
  }

}
