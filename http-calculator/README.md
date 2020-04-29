# Exemplo de Calculadora HTTP usando Json

Este exemplo foi feito usando Play Framework, sendo construido sobre seu projeto base de Hello World.

## Servidor

Para executar o servidor, basta rodar o seguinte comando:

```./sbt httpCalculator/run```

Em seguida, o `sbt` fará o download das dependencias, compilar o código e subir o servidor na porta `9000`

Por ser uma aplicação em Play, a primeira requsição será mais demorada, uma vez que seus controladores são *lazy*

## Cliente
O cliente que consome o servidor é um `Object` executável localizado em
```http-calculator/app/clients/CalculatorClient.scala```.  O comando é:

```./sbt "httpCalculator/runMain clients.CalculatorClient"```

Nele deverá ser escrito o Json da expressão e, ao executar, a requisição será disparada para o servidor e,
quando o mesmo responder, sua resposta será apresentada.