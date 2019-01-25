package treemembers

import documentelements.Page
import tester.Main.FileName

import scala.meta._
import scala.meta.internal.semanticdb.{Range, SymbolOccurrence, TextDocument}

trait TreeMember {

  val fileName: String

  val page: Page

  val position: Position

  def positionToRange: Range = Range(position.startLine, position.startColumn, position.endLine, position.endColumn)

  def typeFromSemanticDB(occurrenceMap: Map[(FileName, Option[Range]), (SymbolOccurrence, TextDocument)]): String =
    occurrenceMap((fileName, Some(positionToRange)))._1.symbol

}

object TreeMember {

  def apply(node: Tree)(implicit fileName: String, page: Page): TreeMember = node match {
    case node: Defn.Class =>
      new Class(node, fileName, page)
    case node: Defn.Def =>
      new Method(node, fileName, page)
    case node: Defn.Val =>
      new Val(node, fileName, page)
    case node: Defn.Var =>
      new Var(node, fileName, page)
    case Mod.Annot(init) =>
      Annotation(init, fileName, page)
    case _ => new Other(node, fileName, page)
  }

  def parseTree(tree: Tree)(implicit fileName: String, page: Page): List[TreeMember] =
    tree.collect({ case node => apply(node)(fileName, page) })

  trait Dummy extends TreeMember

}
