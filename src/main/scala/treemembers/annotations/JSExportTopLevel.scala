package treemembers.annotations

import treemembers.Annotation

import scala.meta.inputs.Position

final case class JSExportTopLevel(
                                   className: String,
                                   position: Position,
                                   fileName: String
                                 ) extends Annotation
