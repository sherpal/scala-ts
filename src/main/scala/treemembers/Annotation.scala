package treemembers

import documentelements.Page

import scala.meta._

trait Annotation extends TreeMember {

  val implemented: Boolean = true

}

object Annotation {

  def apply(init: Init, fileName: String, page: Page): Annotation = init match {
    case Init(tpe, _, argss) =>
      tpe.asInstanceOf[Type.Name].value match {
        case "JSExportTopLevel" => annotations.JSExportTopLevel(argss.flatten.head match {
          case Lit.String(value) => value
        }, tpe.pos, fileName, page)
        case "JSExportAll" => annotations.JSExportAll(tpe.pos, fileName, page)
        case "JSExport" =>
          annotations.JSExport(argss.flatten.headOption.map({
            case Lit.String(value) => value
          }), tpe.pos, fileName, page)
        case _ =>
          val f = fileName
          val p = page
          new Annotation {
            override val implemented: Boolean = false

            val position: Position = tpe.pos

            val fileName: String = f

            val page: Page = p
          }
      }
  }

}
