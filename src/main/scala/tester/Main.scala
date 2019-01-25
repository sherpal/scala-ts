package tester

import java.nio.file.{Files, Paths}

import scala.meta.internal.semanticdb
import treemembers.TreeMember

import scala.meta._
import documentelements.Page

import scala.collection.JavaConverters._
import scala.meta.internal.semanticdb.{SignatureMessage, SymbolInformation}

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

    documents.map(document => (document.uri, document.symbols))
      .filter(_._1.contains("Fraction"))
      .foreach { case (fileName, symbols) =>
        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        println(fileName)
        println(
          symbols
          .map {
            symbol: SymbolInformation =>
              s"${symbol.displayName} ----> ${symbol.symbol}"
          }
          .mkString("\n\n")
        )
    }

    val symbolsMap = documents.flatMap(
      document => document.symbols.map(symbol => ((projectRoot + document.uri, symbol.displayName), symbol))
    ).toMap

    val symlinksToSymbols = symbolsMap.map({
      case ((fileName, _), symbol) => (fileName, symbol.symbol) -> symbol
    })

    val pages = documents.map(
      document => (projectRoot + document.uri, new Page(projectRoot + document.uri, document.symbols, symbolsMap))
    )
      .toMap

    val allClasses = documents.map(_.uri).map(projectRoot + _).flatMap(fileName => {
      implicit val implicitFileName: String = fileName
      implicit val page: Page = pages(fileName)

      val path = Paths.get(implicitFileName)
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

    allClasses.filter(_.name.toString == "Fraction").flatMap(_.methods).map(_.signature).map(_.asMessage.sealedValue)
      .foreach(println)

    val fractionMethods = allClasses.filter(_.name.toString == "Fraction").flatMap(_.methods)

    def argList(signature: SignatureMessage): Seq[String] =
      signature.getMethodSignature.parameterLists.map(_.toString)

    fractionMethods.map(_.name.toString).foreach(println)

//    println(documents.filter(_.uri.contains("Fraction")).map(_.toProtoString).mkString("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n"))
//
//    val occurrencesMap: Map[(FileName, Option[Range]), (SymbolOccurrence, TextDocument)] =
//      (for {
//        document <- documents
//        occurrences <- document.occurrences
//      } yield {
//        (projectRoot + document.uri, occurrences.range) -> (occurrences, document)
//      })
//        .toMap
//
//    occurrencesMap.toList
//      .groupBy(_._1._1)
//      .foreach({
//        case (fileName, info) =>
//          val path = Paths.get(
//            "output/" +
//              fileName.substring(fileName.indexOf("scala/") + 6).dropRight(6)
//                .replaceAll("/", "_")
//          )
//          Files.write(path, info.map(_._2._1).map(_.toProtoString).mkString("\n\n").getBytes())
//      })
//
//    val allClasses = documents.map(_.uri).map(projectRoot + _).flatMap(fileName => {
//      implicit val implicitFileName: String = fileName
//
//      val path = Paths.get(fileName)
//      val bytes = java.nio.file.Files.readAllBytes(path)
//      val text = new String(bytes, "UTF-8")
//      val input = Input.VirtualFile(path.toString, text)
//      val exampleTree = input.parse[Source].get
//
//
//      //    exampleTree.collect({
//      //      case node => node.productPrefix -> node
//      //    })
//      //      .foreach(println)
//
//      val treeMembers = TreeMember.parseTree(exampleTree)
//
//      treeMembers.flatMap({
//        case c: treemembers.Class => Some(c)
//        case _ => None
//      })
//    })
//
//    val declarationFileContent = allClasses.groupBy(_.fileName).map({
//      case (fileName, classes) =>
//        println(fileName)
//        println(classes.map(_.name.value))
//        (fileName, classes.map(_.toTSDef).mkString("\n\n"))
//    })
//      .filter(_._2.nonEmpty)
//      .map({
//      case (fileName, classes) =>
//        s"""
//           |/** $fileName */
//           |$classes
//         """.stripMargin
//    }).mkString("\n\n")
//
//    allClasses.flatMap(_.methods).flatMap(_.parameters.flatten)
//      .map(typedName => (typedName.name, typedName.typeFromSemanticDB(occurrencesMap)))
//      .foreach(println)
//
//    val declarationFile = Paths.get("output/declarationFile.d.ts")
//
//    Files.write(declarationFile, declarationFileContent.getBytes())
//

  }

}
