# microservice
Microservice made with Spring cloud, Spring config server, Eureka registry, Zuul and Docker compose

1) Clone to local machine
2) In the project folder execute:
  >gradlew copyAllDockerFiles   //to build the jars and move dockers files together.
3) Turn on the Docker Machine and MySQL service.
4) Execute the docker-compose.yml
  $docker-compose build 
  $docker-compose up -d
6) Execute:> docker ps   //there should be 6 containers running.

** Test the microservice **

7) In the browser whe open Eureka Server: http://192.168.99.100:8761/
  * It should be 3 application:
    -DB-SERVICE
    -MICRO-PROXY
    -STOCK-SERVICE



8) We add a record using Postman
post:
http://192.168.99.100:8099/api/db-service/rest/db/add
payload:
{
  "userName":"javier",
  "quotes":["GOOG", "AAPL", "ORCL", "AMZN"]
}

the resoult should be:
payload:
[
    "GOOG",
    "AAPL",
    "ORCL",
    "AMZN"
]


9) Get the records
get:
http://192.168.99.100:8099/api/stock-service/rest/stock/Javier

the resoult should be:
payload:
[
    {
        "symbol": "GOOG",
        "id": 2147483647,
        "exchange": "NASDAQ",
        "name": "Alphabet Inc",
        "price": 1032.5
    },
    {
        "symbol": "AAPL",
        "id": 22144,
        "exchange": "NASDAQ",
        "name": "Apple Inc.",
        "price": 171.1
    },
    {
        "symbol": "ORCL",
        "id": 419344,
        "exchange": "NYSE",
        "name": "Oracle Corporation",
        "price": 49.2
    },
    {
        "symbol": "AMZN",
        "id": 660463,
        "exchange": "NASDAQ",
        "name": "Amazon.com, Inc.",
        "price": 1137.29
    }
]


10) To turn off the services execute:
>docker-compose down




