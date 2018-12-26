package treemembers

import scala.meta._

trait TreeMember

object TreeMember {

  def apply(node: Tree): TreeMember = node match {
    case Defn.Class(mods, name, tparams, ctor, tmpl) =>
      new Class(mods, name, tparams, ctor, tmpl)
    case Defn.Def(mods, name, tparams, paramss, decltpe, body) =>
      new Method(mods, name, tparams, paramss, decltpe, body)
    case Defn.Val(mods, pats, decltpe, rhs) =>
      new Val(mods, pats, decltpe, rhs)
    case Mod.Annot(init) =>
      Annotation(init)
    case Lit.String(value) =>
      new StringLiteral(value)
    case _ => new Other(node)
  }

  def parseTree(tree: Tree): List[TreeMember] = tree.collect({ case node => apply(node) })

  trait Dummy extends TreeMember

}
