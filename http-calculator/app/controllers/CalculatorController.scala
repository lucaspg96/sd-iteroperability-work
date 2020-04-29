package controllers

import javax.inject._
import models.expression.Expression
import play.api.libs.json.{JsNumber, JsValue}
import play.api.mvc._


@Singleton
class CalculatorController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  // Função para processar requisição
  def calc(): Action[JsValue] = Action(parse.json) { implicit request: Request[JsValue] =>
    // Recupero a expressão, em JSON, no corpo da requisição e transformo em um Objeto de Expressão
    val expr = request.body.as[Expression]
    // Respondo a requisição com 200 e o valor da expressão
    Ok(JsNumber(expr.value))
  }
  
}
