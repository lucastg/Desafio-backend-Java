## Pre-requisitos para executar aplicação
- [Install docker](https://docs.docker.com/engine/install/)
- [Install docker-compose](https://docs.docker.com/compose/install/)

## IDE
- [IntelliJ](https://www.jetbrains.com/idea/download/#section=linux)

## Iniciar a aplicação pelo docker
### Acesse a pasta do docker
```sh
cd docker
```
### Rode o comando abaixo para buildar e subir a aplicação
```sh
 docker-compose up --build
```

### Rode o comando abaixo para subir a aplicação e o banco
```sh
 docker-compose up
```

## Hospedagem (Render)
### Clique no link abaixo para acessar o swagger da aplicação
- [Swagger](https://desafio-back-end.onrender.com/swagger-ui/index.html)

- Foi utilizado como banco de dados o Postgre, que tambem esta hospedade em nuvem.

*Obs.: A aplicação fica inativa após 15 minutos sem uso, então a primeira requisição levara um tempo consideravel caso a aplicação esteja inativa.*

