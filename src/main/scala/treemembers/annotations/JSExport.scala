package treemembers.annotations

import documentelements.Page
import treemembers.Annotation

import scala.meta.inputs.Position

final case class JSExport(
                           name: Option[String],
                           position: Position,
                           fileName: String,
                           page: Page
                         ) extends Annotation {

}
