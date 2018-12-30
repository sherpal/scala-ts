package treemembers

import scala.meta._

final class Other(val node: Tree, val fileName: String) extends TreeMember.Dummy {

  val position: Position = node.pos

}
