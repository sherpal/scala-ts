package tester

import java.nio.file.{Files, Paths}

import scala.meta.internal.semanticdb
import treemembers.TreeMember

import scala.meta._
import scala.collection.JavaConverters._
import scala.meta.internal.semanticdb.{SymbolOccurrence, TextDocument, Range}

object Main {

  final class NoArgumentException(msg: String) extends Exception(msg)

  type FileName = String

  def main(args: Array[String]): Unit = {

    if (args.isEmpty) {
      throw new NoArgumentException("No folder provided.")
    }

//    val path = Paths.get(args.head)
//    val bytes = java.nio.file.Files.readAllBytes(path)
//    val text = new String(bytes, "UTF-8")
//    val input = Input.VirtualFile(path.toString, text)
//    val exampleTree = input.parse[Source].get
//
////    exampleTree.collect({
////      case node => node.productPrefix -> node
////    })
////      .foreach(println)
//
//    val treeMembers = TreeMember.parseTree(exampleTree)
//
//    val classes = treeMembers.flatMap({
//      case c: treemembers.Class => Some(c)
//      case _ => None
//    })
//
//    classes.map(_.toTSDef).foreach(println)

    val projectRoot: String = args.head

    val semanticdbRoot = Paths.get(args.tail.head)
      .resolve("META-INF").resolve("semanticdb")
    println(semanticdbRoot)
    val semanticdbFiles = Files
      .walk(semanticdbRoot)
      .iterator()
      .asScala
      .filter(_.getFileName.toString.endsWith(".semanticdb"))
      .toList

    val documents = semanticdbFiles.flatMap(semanticdbFile => semanticdb.TextDocuments
      .parseFrom(Files.readAllBytes(semanticdbFile))
      .documents
    )

    val occurrencesMap: Map[(FileName, Option[Range]), (SymbolOccurrence, TextDocument)] =
      (for {
        document <- documents
        occurrences <- document.occurrences
      } yield {
        (projectRoot + document.uri, occurrences.range) -> (occurrences, document)
      })
        .toMap

    occurrencesMap.toList
      .groupBy(_._1._1)
      .foreach({
        case (fileName, info) =>
          println("----------------------------------------")
          println(fileName)
          println(info.map(_._2._1).map(_.toProtoString).mkString("\n\n"))
      })

    val allClasses = documents.map(_.uri).map(projectRoot + _).flatMap(fileName => {
      implicit val implicitFileName: String = fileName

      val path = Paths.get(fileName)
      val bytes = java.nio.file.Files.readAllBytes(path)
      val text = new String(bytes, "UTF-8")
      val input = Input.VirtualFile(path.toString, text)
      val exampleTree = input.parse[Source].get


      //    exampleTree.collect({
      //      case node => node.productPrefix -> node
      //    })
      //      .foreach(println)

      val treeMembers = TreeMember.parseTree(exampleTree)

      treeMembers.flatMap({
        case c: treemembers.Class => Some(c)
        case _ => None
      })
    })

    val declarationFileContent = allClasses.groupBy(_.fileName).map({
      case (fileName, classes) =>
        println(fileName)
        println(classes.map(_.name.value))
        (fileName, classes.map(_.toTSDef).mkString("\n\n"))
    })
      .filter(_._2.nonEmpty)
      .map({
      case (fileName, classes) =>
        s"""
           |/** $fileName */
           |$classes
         """.stripMargin
    }).mkString("\n\n")

    val declarationFile = Paths.get("output/declarationFile.d.ts")

    Files.write(declarationFile, declarationFileContent.getBytes())


  }

}
