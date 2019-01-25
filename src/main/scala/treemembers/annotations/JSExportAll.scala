package treemembers.annotations

import documentelements.Page
import treemembers.Annotation

import scala.meta.inputs.Position

final case class JSExportAll(position: Position, fileName: String, page: Page) extends Annotation
