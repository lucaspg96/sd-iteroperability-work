
import models.expression.Expression
import play.api.libs.json.Json

object JsonModelExample extends App{
  val exprString =
    """
      |{
      |    "operator1": 1,
      |    "operation": "+",
      |    "operator2": {
      |        "operator1": {
      |            "operator1": 5,
      |            "operation": "-",
      |            "operator2": 3
      |        },
      |        "operation": "*",
      |        "operator2": {
      |            "operator1": 10,
      |            "operation": "/",
      |            "operator2": 2
      |        }
      |    }
      |}
      |""".stripMargin

  val expr = Json.parse(exprString).as[Expression]

  println(expr)
  println(expr.value)
}
