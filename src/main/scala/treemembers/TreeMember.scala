package treemembers

import scala.meta._

trait TreeMember

object TreeMember {

  def apply(node: Tree): TreeMember = node match {
    case node: Defn.Class =>
      new Class(node)
    case node: Defn.Def =>
      new Method(node)
    case node: Defn.Val =>
      new Val(node)
    case node: Defn.Var =>
      new Var(node)
    case Mod.Annot(init) =>
      Annotation(init)
    case Lit.String(value) =>
      new StringLiteral(value)
    case _ => new Other(node)
  }

  def parseTree(tree: Tree): List[TreeMember] = tree.collect({ case node => apply(node) })

  trait Dummy extends TreeMember

}
