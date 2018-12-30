package treemembers.annotations

import treemembers.Annotation

import scala.meta.inputs.Position

final case class JSExport(
                           name: Option[String],
                           position: Position,
                           fileName: String
                         ) extends Annotation {

}
