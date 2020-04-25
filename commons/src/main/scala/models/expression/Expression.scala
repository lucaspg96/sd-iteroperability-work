package models.expression

import play.api.libs.json.{Format, JsError, JsNumber, JsObject, JsResult, JsSuccess, JsValue, Json}

sealed trait Expression {
  def value: Double
}

object Expression {
  implicit val format: Format[Expression] = new Format[Expression] {
    override def writes(o: Expression): JsValue = o match {
      case s: Sum => Sum.format.writes(s)
      case s: Subtraction => Subtraction.format.writes(s)
      case m: Multiplication => Multiplication.format.writes(m)
      case d: Division => Division.format.writes(d)
      case c: Constant => Constant.format.writes(c)
    }

    override def reads(json: JsValue): JsResult[Expression] = json match {
      case obj: JsObject =>
        (obj \ "operation").as[String] match {
          case "+" => Sum.format.reads(obj)
          case "-" => Subtraction.format.reads(obj)
          case "*" => Multiplication.format.reads(obj)
          case "/" => Division.format.reads(obj)
        }

      case n: JsNumber => Constant.format.reads(n)
    }
  }

}

case class Constant(value: Double) extends Expression {
  override def toString: String = value.toString
}

object Constant {
  implicit val format: Format[Constant] = new Format[Constant] {
    override def writes(o: Constant): JsValue = JsNumber(o.value)

    override def reads(json: JsValue): JsResult[Constant] = json match {
      case n: JsNumber => JsSuccess(Constant(n.as[Double]))
      case _ => JsError("Constant value expects a number")
    }
  }
}

case class Sum(operator1: Expression, operator2: Expression) extends Expression {
  override def value: Double = operator1.value + operator2.value

  override def toString: String = s"($operator1 + $operator2)"
}

object Sum {
  implicit val format: Format[Sum] = Json.format
}

case class Subtraction(operator1: Expression, operator2: Expression) extends Expression {
  override def value: Double = operator1.value - operator2.value

  override def toString: String = s"($operator1 - $operator2)"
}

object Subtraction {
  implicit val format: Format[Subtraction] = Json.format
}

case class Multiplication(operator1: Expression, operator2: Expression) extends Expression {
  override def value: Double = operator1.value * operator2.value

  override def toString: String = s"($operator1 * $operator2)"
}

object Multiplication {
  implicit val format: Format[Multiplication] = Json.format
}

case class Division(operator1: Expression, operator2: Expression) extends Expression {
  override def value: Double = operator1.value / operator2.value

  override def toString: String = s"($operator1 / $operator2)"
}

object Division {
  implicit val format: Format[Division] = Json.format
}
