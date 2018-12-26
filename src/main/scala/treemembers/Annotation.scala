package treemembers

import scala.meta._

trait Annotation extends TreeMember {

  val implemented: Boolean = true

}

object Annotation {

  def apply(init: Init): Annotation = init match {
    case Init(tpe, _, argss) =>
      tpe.asInstanceOf[Type.Name].value match {
        case "JSExportTopLevel" => annotations.JSExportTopLevel(argss.flatten.head match {
          case Lit.String(value) => value
        })
        case "JSExportAll" => annotations.JSExportAll
        case "JSExport" =>
          annotations.JSExport(argss.flatten.headOption.map({
            case Lit.String(value) => value
          }))
        case _ =>
          new Annotation {
            override val implemented: Boolean = false
          }
      }
  }

}
