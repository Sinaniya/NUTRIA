[![CircleCI](https://circleci.com/gh/itdany/food-chain-backend.svg?style=svg)](https://circleci.com/gh/itdany/food-chain-backend)


# FoodChain Backend App 

Application for tracking Agricultural Products from the Producers to the Consumers

## Technologies
- Java
- Spring Boot
- Ethereum, Web3j
- MySQL

## How to make it work
- Ethereum client required 
```
sudo apt-get install software-properties-common
sudo add-apt-repository -y ppa:ethereum/ethereum
sudo apt-get update
sudo apt-get install ethereum
```

- Create an account

```
https://github.com/ethereum/go-ethereum/wiki/Managing-your-accounts
```

- Since we use the Rinkeby test network for the testing, free ether for testing can be obtained here: 

```
https://www.rinkeby.io/#faucet
```

- Provide the account private key into the `application.properties` file

```
foodchain.account.privatekey= your account private key
```

- The smart contract is already deployed on the address `0x668003AAa66E9f195e5626A3cBba543Ffb94fE20`
, make sure that there the fallowing property is into the `application.properties` file
```
foodchain.contract.address=0x668003AAa66E9f195e5626A3cBba543Ffb94fE20
```

- Install the MySQL Database, create new user and the database with the name of your choice,
provide your db info into the `application.properties` as an example below
```
spring.datasource.url=jdbc:mysql://localhost:3306/NEW_DB_NAME?useSSL=false
spring.datasource.username=USERNAME
spring.datasource.password=PASSWORD
```

- Download the Ethereum client as a docker image: `docker pull ethereum/client-go` and start the client:
```
docker run -it -p 8545:8545 -p 30303:30303 ethereum/client-go --rinkeby --syncmode "light" --rpc
```


- The db and eth clients have to be running before the server, JPA will create or update the tables if they are already there, 
it's just important to have the database created. The smart contract will be loaded on startup.

- Finally run the server by executing the commands `mvn package` under the source project folder and running the created jar file
`java -jar jar_file_name`.

## Swagger API 
Available under the URL: `http://localhost:8080/swagger-ui.html`