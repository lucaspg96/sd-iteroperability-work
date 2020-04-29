package clients

import akka.actor.{ActorSystem, ClassicActorSystemProvider}
import akka.stream.{ActorMaterializer, Materializer, SystemMaterializer}
import models.expression.Expression
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.ahc.StandaloneAhcWSClient
import play.api.libs.ws.{StandaloneWSClient, WSClient}
import play.shaded.ahc.org.asynchttpclient.DefaultAsyncHttpClient

object CalculatorClient extends App {
  // Criando o sistema de atores para o Akka executar e um materializador
  implicit val system: ActorSystem = ActorSystem()
  implicit def matFromSystem(implicit provider: ClassicActorSystemProvider): Materializer =
    SystemMaterializer(system).materializer

  import system._

  // Expressão, em Json, a ser enviada para o servidor
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

  // Faço o parser da expressão, apra validá-la
  val exprJson = Json.parse(exprString)
  val expr = exprJson.as[Expression]

  // Construo uma requisição HTTP
  val client = new StandaloneAhcWSClient(new DefaultAsyncHttpClient())
  client.url("http://localhost:9000/calc")
    // Faço um POST cujo corpo é o JSON da expressão
    .post(exprJson)
    // Processo a resposta
    .map{response =>
      // Recupero o resultado
      val answer = response.body[JsValue].as[Double]
      // Apresento o resultado
      println(s"$expr = "+answer)
      // Encerro o sistema de atores e o código encerra
      system.terminate().map{_ =>
        scala.sys.exit()}
    }
    // Caso dê algum erro na requisição
    .recover{
      case t: Throwable =>
        // Apresento o stackTrace do erro
        t.printStackTrace()
        // Encerro o sistema de atores e o código encerra
        system.terminate().map(_ => System.exit(0))
    }

}
