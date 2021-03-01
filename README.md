# BM&F API

O objetivo desse projeto é buscar o preço de uma determinada cultura na BM&F em uma data específica.

# Dependências

* Maven
* JDK 8
* Docker

# Arquitetura

O projeto foi feito utilizando a versão 8 do Java, com o framework Spring. Utilizei o Docker para facilitar os testes.

## Execução

É necssário ter o Docker instalado na máquina para poder rodar a aplicação localmente.

Subir aplicação e BD: `docker-compose up`
Rodar os testes: `mvn test`


## Testando a API

Após subir a aplicação e BD no Docker, o teste deverá seguir a URL em exemplo abaixo:

`http://localhost:8080/v1/crops/{CÓDIGO-CULTURA}/price?date={DATA}`
O retorno será no formatado JSON.

Alguns exemplos de pesquisas: 

http://localhost:8080/v1/crops/COTTON/price?date=2021-02-26
```json
{
  "data": {
    "price": 511.26
  }
}
```

http://localhost:8080/v1/crops/PORK/price?date=2021-02-26
```json
{
  "data": {
    "price": 11.68
  }
}
```

http://localhost:8080/v1/crops/SOYBEAN/price?date=2021-02-26
```json
{
  "data": {
    "price": 162.26
  }
}
```

Obs.: Finais de semana e feriados nacionais e na cidade de SP a B3 (Bolsa brasileira) não funciona, portanto não é possível obter resultado nesses dias.