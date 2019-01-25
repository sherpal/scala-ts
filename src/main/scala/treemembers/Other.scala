package treemembers

import documentelements.Page

import scala.meta._

final class Other(val node: Tree, val fileName: String, val page: Page) extends TreeMember.Dummy {

  val position: Position = node.pos

}
