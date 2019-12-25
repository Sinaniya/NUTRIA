package com.food.chain.server.fcserver.ethereum;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Log
@Configuration
public class ETHPropertiesBean {

    @Value("${foodchain.contract.address}")
    private String contractAddress;

    @Value("${foodchain.account.privatekey}")
    private String accountPrivateKey;

    @Value("${foodchain.web3j.httpservice.url}")
    private String foodchainWeb3jHttpServiceUrl;

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getAccountPrivateKey() {
        return accountPrivateKey;
    }

    public void setAccountPrivateKey(String accountPrivateKey) {
        this.accountPrivateKey = accountPrivateKey;
    }

    public String getFoodchainWeb3jHttpServiceUrl() {
        return foodchainWeb3jHttpServiceUrl;
    }

    public void setFoodchainWeb3jHttpServiceUrl(String foodchainWeb3jHttpServiceUrl) {
        this.foodchainWeb3jHttpServiceUrl = foodchainWeb3jHttpServiceUrl;
    }
}
