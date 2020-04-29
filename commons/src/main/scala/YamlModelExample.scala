import io.circe.{Json, ParsingFailure, yaml}
import io.circe.syntax._
import io.circe.yaml.parser
import models.expression.Expression

object YamlModelExample extends App {
  val yamlString =
    """
      |sum:
      |    operator1: 1.0
      |    operator2:
      |        multiplication:
      |            operator1:
      |                subtraction:
      |                    operator1: 5.0
      |                    operator2: 3.0
      |            operator2:
      |                division:
      |                    operator1: 10.0
      |                    operator2: 2.0
      |""".stripMargin

  val json: Either[ParsingFailure, Json] = parser.parse(yamlString)

  val expr = Expression.fromYaml(yamlString)
  println(s"$expr = ${expr.value}")
  println(expr.toYaml)
}
