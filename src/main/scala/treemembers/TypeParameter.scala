package treemembers

import scala.meta._

final class TypeParameter(tpe: Type) {

  def toTS: String = TypeParameter.scalaToTSTypeMapper(tpe)

}

object TypeParameter {

  // todo
  private val _scalaToTSTypeMapper: Map[String, String] = Map(
    "Boolean" -> "boolean",
    "Int" -> "number",
    "Double" -> "number",
    "Float" -> "boolean",
    "String" -> "string",
    "Unit" -> "void"
  )

  def scalaToTSTypeMapper(scalaType: Type): String = scalaType match {
    case Type.Name(value) => _scalaToTSTypeMapper.getOrElse(value, value)
    case Type.Function(params, res) =>
      params.zipWithIndex.map({
        case (t, j) => s"arg$j: ${scalaToTSTypeMapper(t)}"
      }).mkString("(", ", ", ")") + " => " + scalaToTSTypeMapper(res)
    case Type.Param(_, name, _, _, _, _) =>
      name.value
  }

}