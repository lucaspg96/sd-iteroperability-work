import akka.Done
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Sink, Tcp}
import akka.util.ByteString
import models.expression.Expression

object Server extends App{

  // Criando o sistema de atores para o Akka executar
  implicit val system: ActorSystem = ActorSystem()

  val port = 9001
  // Inicio um servidor TCP
  val connections = Tcp().bind("localhost", port)

  // Defino o "fluxo" de processos para serem executados para cada mensagem que
  // for enviada para o servidor
  val processingFlow = Flow[ByteString]
    // Recupero a string enviada (que vem em ByteString)
    .map(_.utf8String)
    // Faço o parser do YAML para a expressão
    .map(Expression.fromYaml)
    // Obtenho o valor da expressão
    .map(_.value)
    // Transformo o valor para String
    .map(_.toString)
    // Apresento no log do servidor o resultado da expressão
    .map{v =>
      println(v)
      v
    }
    // Transformo a resposta para ByteString, para ser enviada para o cliente
    .map(ByteString.apply)

  println(s"Iniciando servidor socket na porta $port")
  connections.runForeach{connection =>
    println("Nova conexão recebida!")
    // Atrelo o "fluxo" de processamento de mensagens criado
    // para cada nova conexão iniciada
    connection.handleWith(processingFlow)
  }

}
