package treemembers

import tester.Main.FileName

import scala.meta._
import scala.meta.internal.semanticdb.{Range, SymbolOccurrence, TextDocument}

trait TreeMember {

  val fileName: String

  val position: Position

  def positionToRange: Range = Range(position.startLine, position.startColumn, position.endLine, position.endColumn)

  def typeFromSemanticDB(
                          pos: Position,
                          occurrenceMap: Map[(FileName, Option[Range]), (SymbolOccurrence, TextDocument)]
                        ): String = {
    occurrenceMap((fileName, Some(positionToRange)))._1.symbol
  }

}

object TreeMember {

  def apply(node: Tree)(implicit fileName: String): TreeMember = node match {
    case node: Defn.Class =>
      new Class(node, fileName)
    case node: Defn.Def =>
      new Method(node, fileName)
    case node: Defn.Val =>
      new Val(node, fileName)
    case node: Defn.Var =>
      new Var(node, fileName)
    case Mod.Annot(init) =>
      Annotation(init, fileName)
    case _ => new Other(node, fileName)
  }

  def parseTree(tree: Tree)(implicit fileName: String): List[TreeMember] =
    tree.collect({ case node => apply(node)(fileName) })

  trait Dummy extends TreeMember

}
