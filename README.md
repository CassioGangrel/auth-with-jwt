# Project auth-with-jwt

Este projeto usa quakus, se tem interese em conhecer mais sobre
este framework acesse o site: https://quarkus.io/

## Rodando a aplicação em modo desenvolvedor

Você pode rodar no modo desenvolvedor com o seguinte comando:
```
./mvnw quarkus:dev
```

## Empacotando e rodando a aplicação

A aplicação pode ser empacotada usando `./mvnw package`.
Isto produs o arquivo `auth-with-jwt-1.0.0-SNAPSHOT-runner.jar`na pasta `/target` .
Esteja ciente de que não é um _über-jar_, pois as dependências são copiadas para o diretório `target/lib`.

A aplicação agora é executada usando `java -jar target/auth-with-jwt-1.0.0-SNAPSHOT-runner.jar`.

## Criando um executavel nativo.

É possivel criar um executavel nativo usando o comando: `./mvnw package -Pnative`.

Ou, se você nao tem a GraalVM instalada, você pode rodar o build para nativo em um using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

Então pode executar usando o comando: `./target/auth-with-jwt-1.0.0-SNAPSHOT-runner`

Para mais informações, favor consultar https://quarkus.io/guides/building-native-image.

## Criando as chaves no linux

1 - openssl req -newkey rsa:2048 -new -nodes -keyout privatekey.pem -out csr.pem

2 - openssl rsa -in privatekey.pem -pubout > publickey.pem
