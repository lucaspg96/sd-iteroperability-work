package clients

import akka.actor.{ActorSystem, ClassicActorSystemProvider}
import akka.stream.{ActorMaterializer, Materializer, SystemMaterializer}
import models.expression.Expression
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.ahc.StandaloneAhcWSClient
import play.api.libs.ws.{StandaloneWSClient, WSClient}
import play.shaded.ahc.org.asynchttpclient.DefaultAsyncHttpClient

object CalculatorClient extends App {

  implicit val system: ActorSystem = ActorSystem()
  implicit def matFromSystem(implicit provider: ClassicActorSystemProvider): Materializer =
    SystemMaterializer(system).materializer

  import system._

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

  val exprJson = Json.parse(exprString)
  val expr = exprJson.as[Expression]

  val client = new StandaloneAhcWSClient(new DefaultAsyncHttpClient())

  client.url("http://localhost:9000/calc")
    .post(exprJson)
    .map{response =>
      val answer = response.body[JsValue].as[Double]
      println(s"$expr = "+answer)
      system.terminate().map(_ => System.exit(0))
    }
    .recover{
      case t: Throwable =>
        t.printStackTrace()
        system.terminate().map(_ => System.exit(0))
    }

}
