# Exemplo de Calculadora Socket usando YAML

Este exemplo foi feito usando o Akka Stream.

## Servidor

Para executar o servidor, basta executar o objeto ```socket-calculator/src/main/scala/Server.scala```
utilizando o comando:

```./sbt "socketCalculator/runMain Server"```

## Cliente

Para executar o cliente, basta executar o objeto ```socket-calculator/src/main/scala/Client.scala```
utilizando o comando:

```./sbt "socketCalculator/runMain Client"```

Nele deverá ser escrito o YAML da expressão e, ao executar, a mensagem será enviada 
via socket TCP para o servidor e, quando o mesmo responder, sua resposta será apresentada.