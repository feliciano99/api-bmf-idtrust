# BM&F Crops Price API

Este projeto tem como finalidade buscar o preço de uma cultura na BM&F em uma data específica.

**ATENÇÃO:** dado que para calcular por Kg o valor de cada cultura, eu precisaria entrar em cada cultura no site 
da Quandl e pegar o peso da saca, eu preferi não fazer isso, e retornei apenas o valor da cultura calculado pela API da
Quandl

## Dependências

- Maven
- JDK 8
- Docker
- Docker Compose

## Arquitetura

O projeto foi feito em Java na versão 8, utilizando Spring como framework. Foram criados testes unitários e testes de
integração para assegurar a confiabilidade no que está sendo desenvolvido. Além disso criei um `Dockerfile` e um
`docker-compose.yml` para subir a aplicação sem a necessidade do `JDK` ou do `Maven`. Mas atenção, eu não automatizei
a chamada aos testes dentro do Docker, durante a construção da imagem docker os testes NÃO SÃO executados. Para executar
os testes é necessário ter o `Maven` e o `JDK` instalados.

Para tratar o versionamento da camada de persistência, adicionei o FlyWay que utiliza o pattern de `migrations` que são
aplicadas uma vez durante o start da aplicação. Assim, utilizei o banco `HSQLDB` como repositório de testes, então todos
os testes de integração rodam utilizando ele, e dentro do `docker-compose.yml` configurei um banco `PostgreSQL` para
rodar em modo de produção.

## Execução

- `docker-compose up`: sobe a aplicação dentro do Docker em modo de produção
- `mvn test`: roda todos os testes da aplicação

## Acessando a aplicação

Depois de executar a aplicação, você poderá testar a API chamando
`http://localhost:8080/v1/crops/{CÓDIGO-DA-CULTURA}/price?date={DATA-DESEJADA}`

Perceba que os parâmetros `{CÓDIGO-DA-CULTURA}` e `{DATA-DESEJADA}` devem ser substituídos pelos filtros desejados.
Assim sendo temos o exemplo: http://localhost:8080/v1/crops/SOYBEAN/price?date=2021-02-17 que busca os dados da cultura
de Soja na data 17/02/2021 (perceba que o formato da data na URL deve ser sempre YYYY-MM-DD). Aqui está o retorno dessa
chamada:

```json
{
  "data": {
    "price": 157.86
  }
}
```

## Exemplos de Retorno da API

- http://localhost:8080/v1/crops/SOYBEAN/price?date=2021-02-17
  ```json
  {
    "data": {
      "price": 157.86
    }
  }
  ```

- http://localhost:8080/v1/crops/CORN/price?date=2021-02-17
  ```json
  {
    "data": {
      "price": 83.21
    }
  }
  ```
  
- http://localhost:8080/v1/crops/CORN/price?date=2021-02-13 (sábado, domingo ou feriado a b3 não abre)
  ```json
  {
    "metadata": {
      "status": "BAD_GATEWAY",
      "message": "Erro ao tentar desserializar a resposta da API Quandl"
    }
  }
  ```
