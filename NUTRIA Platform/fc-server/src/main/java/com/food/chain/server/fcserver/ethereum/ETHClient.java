package com.food.chain.server.fcserver.ethereum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
public class ETHClient {
    private final ETHPropertiesBean ethProperties;
    private FoodChain foodChainContract;

    public ETHClient(ETHPropertiesBean ethProperties) {
        this.ethProperties = ethProperties;
        Optional<FoodChain> initializedContract = initializeContract();
        if (initializedContract.isPresent()) {
            this.foodChainContract = initializedContract.get();
        } else {
            log.error("Error while initializing contract. ");
        }
    }

    // since the contract is already deployed, we just load it by providing its address and credentials
    private Optional<FoodChain> initializeContract() {
        Web3j web3j = Web3j.build(new HttpService(getEthProperties().getFoodchainWeb3jHttpServiceUrl()));
        try {
            log.info("Connected to Ethereum Client version: " + web3j.web3ClientVersion().send().getWeb3ClientVersion());
            log.info("Ethereum Client URL: " + getEthProperties().getFoodchainWeb3jHttpServiceUrl());
        } catch (IOException e) {
            log.error("Error while Connecting to Ethereum Client. " + e);
        }
        Credentials credentials = null;
        try {

            credentials = Credentials.create(ethProperties.getAccountPrivateKey());
//            credentials = WalletUtils.loadCredentials(ethProperties.walletPassword, getWalletFile());
            log.info("Credentials loaded");
        } catch (Exception e) {
            log.error("Error while Loading Credentials: " + e);
        }

        FoodChain foodChain = null;
        try {
//            foodChain = FoodChain.deploy(web3j, credentials, Contract.GAS_PRICE, Contract.GAS_LIMIT).send();
            foodChain = FoodChain.load(ethProperties.getContractAddress(),
                    web3j,
                    credentials,
                    Contract.GAS_PRICE,
                    Contract.GAS_LIMIT);
            log.info("Smart Contract loaded.");
        } catch (Exception e) {
            log.error("Error while Loading Contract. " + e);
        }

        return Optional.ofNullable(foodChain);
    }

    public ETHPropertiesBean getEthProperties() {
        return ethProperties;
    }

    public FoodChain getFoodChainContract() {
        return foodChainContract;
    }

    public void setFoodChainContract(FoodChain foodChainContract) {
        this.foodChainContract = foodChainContract;
    }
}
