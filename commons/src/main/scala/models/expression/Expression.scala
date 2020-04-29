package models.expression

import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, ParsingFailure, yaml, Json => CJson}
import play.api.libs.json.{Format, JsError, JsNumber, JsObject, JsResult, JsSuccess, JsValue, Json}

sealed trait Expression {
  def value: Double

  def toYaml: String = {
    yaml.Printer().pretty(Expression.encoder(this))
  }
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

  implicit val decoder: Decoder[Expression] = (c: HCursor) => {
    c.keys.map(_.head) match {
      case Some(k) =>
        val innerObject = c.get[HCursor](k).getOrElse(throw new Error("Falha ao realizar parser"))
        val op1: Expression = innerObject
          .get[Expression]("operator1").getOrElse(throw new Error("Falha ao realizar parser"))
        val op2 = innerObject
          .get[Expression]("operator2").getOrElse(throw new Error("Falha ao realizar parser"))
        k match {
          case "sum" =>
            Right(Sum(op1, op2))
          case "subtraction" =>
            Right(Subtraction(op1, op2))
          case "multiplication" =>
            Right(Multiplication(op1, op2))
          case "division" =>
            Right(Division(op1, op2))
        }
      case None =>
        val constant = c.as[Double].getOrElse(throw new ParsingFailure("Falha ao processar documento. É esperada uma expressão", null))
        Right(Constant(constant))

    }
  }

  implicit val encoder: Encoder[Expression] = {
    case s: Sum => Sum.encoder(s)
    case s: Subtraction => Subtraction.encoder(s)
    case s: Multiplication => Multiplication.encoder(s)
    case s: Division => Division.encoder(s)
    case s: Constant => Constant.encoder(s)
  }

  def fromYaml(value: String): Expression = {
    yaml.parser.parse(value)
      .getOrElse(throw new Error("Erro ao fazer o parser da YAML "))
      .as[Expression]
      .getOrElse(throw new Error("Erro ao fazer o parser da Expressão "))
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

  implicit val encoder: Encoder[Constant] = (a: Constant) => {
    CJson.fromDouble(a.value).get
  }
}

case class Sum(operator1: Expression, operator2: Expression) extends Expression {
  override def value: Double = operator1.value + operator2.value

  override def toString: String = s"($operator1 + $operator2)"
}

object Sum {
  implicit val format: Format[Sum] = Json.format

  implicit val encoder: Encoder[Sum] = (a: Sum) => {
    val operators = CJson.obj(
      "operator1" -> Expression.encoder(a.operator1),
      "operator2" -> Expression.encoder(a.operator2)
    )

    CJson.obj("sum" -> operators)
  }
}

case class Subtraction(operator1: Expression, operator2: Expression) extends Expression {
  override def value: Double = operator1.value - operator2.value

  override def toString: String = s"($operator1 - $operator2)"
}

object Subtraction {
  implicit val format: Format[Subtraction] = Json.format

  implicit val encoder: Encoder[Subtraction] = (a: Subtraction) => {
    val operators = CJson.obj(
      "operator1" -> Expression.encoder(a.operator1),
      "operator2" -> Expression.encoder(a.operator2)
    )

    CJson.obj("subtraction" -> operators)
  }
}

case class Multiplication(operator1: Expression, operator2: Expression) extends Expression {
  override def value: Double = operator1.value * operator2.value

  override def toString: String = s"($operator1 * $operator2)"
}

object Multiplication {
  implicit val format: Format[Multiplication] = Json.format

  implicit val encoder: Encoder[Multiplication] = (a: Multiplication) => {
    val operators = CJson.obj(
      "operator1" -> Expression.encoder(a.operator1),
      "operator2" -> Expression.encoder(a.operator2)
    )

    CJson.obj("multiplication" -> operators)
  }
}

case class Division(operator1: Expression, operator2: Expression) extends Expression {
  override def value: Double = operator1.value / operator2.value

  override def toString: String = s"($operator1 / $operator2)"
}

object Division {
  implicit val format: Format[Division] = Json.format

  implicit val encoder: Encoder[Division] = (a: Division) => {
    val operators = CJson.obj(
      "operator1" -> Expression.encoder(a.operator1),
      "operator2" -> Expression.encoder(a.operator2)
    )

    CJson.obj("division" -> operators)
  }
}
