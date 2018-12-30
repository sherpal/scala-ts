package treemembers.annotations

import treemembers.Annotation

import scala.meta.inputs.Position

final case class JSExportAll(position: Position, fileName: String) extends Annotation
