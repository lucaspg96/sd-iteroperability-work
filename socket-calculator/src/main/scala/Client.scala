import akka.actor.ActorSystem
import akka.stream.scaladsl.{Source, Tcp}
import akka.util.ByteString

object Client extends App {

  // Criando o sistema de atores para o Akka executar
  implicit val system: ActorSystem = ActorSystem()
  import system._

  // Crio um "fluxo" de dados para representar a passagem da mensagem para o servidor
  // TCP e a resposta do mesmo
  val socketFlow = Tcp().outgoingConnection("localhost", 9001)

  // Defino uma expressão feita em YAML
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

  // Crio uma fonte de dados em Stream com apenas um elemento:
  // a expressão em YAML
  val src = Source.single(yamlString)

  src
    // Transformo a expressão em ByteString, para ser enviada
    .map(ByteString.apply)
    // Mando para o servidor e recupero a resposta
    .via(socketFlow)
    // Recupero a string da resposta (que vem em ByteString)
    .map(_.utf8String)
    // Apresento o valor retornado
    .runForeach(println)
    // Finalizo o sistema do Akka para encerrar o programa
    .map(_ => system.terminate().map(_ => System.exit(0)))
}
