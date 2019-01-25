package documentelements

import scala.meta.internal.semanticdb.{Annotation, SymbolInformation}

final class Page(
                  val fileName: String,
                  val symbols: Seq[SymbolInformation],
                  val symbolsMap: Map[(String, String), SymbolInformation]
                ) {

  lazy val classes: Map[String, SymbolInformation] =
    symbols.filter(_.kind.isClass).map(cls => (cls.displayName, cls)).toMap

  lazy val classesAnnotations: Map[String, Seq[Annotation]] =
    classes.values.map(cls => (cls.displayName, cls.annotations)).toMap

}
