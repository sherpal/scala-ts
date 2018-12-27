package tester

import treemembers.TreeMember

import scala.meta._

object Main {

  final class NoArgumentException(msg: String) extends Exception(msg)

  def main(args: Array[String]): Unit = {

    if (args.isEmpty) {
      throw new NoArgumentException("No file provided.")
    }

    val path = java.nio.file.Paths.get(args.head)
    val bytes = java.nio.file.Files.readAllBytes(path)
    val text = new String(bytes, "UTF-8")
    val input = Input.VirtualFile(path.toString, text)
    val exampleTree = input.parse[Source].get

//    exampleTree.collect({
//      case node => node.productPrefix -> node
//    })
//      .foreach(println)

    val treeMembers = TreeMember.parseTree(exampleTree)

    val classes = treeMembers.flatMap({
      case c: treemembers.Class => Some(c)
      case _ => None
    })

    classes.map(_.toTSDef).foreach(println)


  }

}
