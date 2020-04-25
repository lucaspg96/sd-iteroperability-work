package controllers

import javax.inject._
import models.expression.Expression
import play.api.libs.json.{JsNumber, JsValue}
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class CalculatorController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def calc(): Action[JsValue] = Action(parse.json) { implicit request: Request[JsValue] =>
    val expr = request.body.as[Expression]
    Ok(JsNumber(expr.value))
  }
  
}
