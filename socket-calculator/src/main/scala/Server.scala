import akka.Done
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Sink, Tcp}
import akka.util.ByteString
import models.expression.Expression

object Server extends App{

  implicit val system: ActorSystem = ActorSystem()
  import system._

  val port = 9001
  val connections = Tcp().bind("localhost", port)

  val processingFlow = Flow[ByteString]
    .map(_.utf8String)
    .map(Expression.fromYaml)
    .map(_.value)
    .map(_.toString)
    .map{v =>
      println(v)
      v
    }
    .map(ByteString.apply)

  println(s"Iniciando servidor socket na porta $port")
  connections.runForeach{connection =>
    println("Nova conexão recebida!")
    connection.handleWith(processingFlow)
  }

}
