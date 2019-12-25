https://docs.web3j.io/command_line.html

1. compile contract:
    solc ./ethereum/contract/FoodChain.sol --bin --abi --optimize -o ./ethereum/contract/compiled/

2. generate java class out of the compiled contract:
    ./ethereum/web3j-3.5.0/bin/web3j solidity generate ./ethereum/contract/compiled/FoodChain.bin ./ethereum/contract/compiled/FoodChain.abi -o ./src/main/java/ -p org.food.chain.foodchainbackend.ethereum

