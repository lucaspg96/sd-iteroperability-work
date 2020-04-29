import akka.actor.ActorSystem
import akka.stream.scaladsl.{Source, Tcp}
import akka.util.ByteString

object Client extends App {

  implicit val system: ActorSystem = ActorSystem()

  import system._

  val socketFlow = Tcp().outgoingConnection("localhost", 9001)

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

  val src = Source.single(yamlString)

  src
    .map(ByteString.apply)
    .via(socketFlow)
    .map(_.utf8String)
    .runForeach(println)
    .map(_ => system.terminate().map(_ => System.exit(0)))
}
