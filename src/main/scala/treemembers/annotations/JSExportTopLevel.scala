package treemembers.annotations

import documentelements.Page
import treemembers.Annotation

import scala.meta.inputs.Position

final case class JSExportTopLevel(
                                   className: String,
                                   position: Position,
                                   fileName: String,
                                   page: Page
                                 ) extends Annotation
