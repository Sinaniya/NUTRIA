package org.food.chain.foodchainbackend.ethereum;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class ETHPropertiesBean {

    @Value("${foodchain.contract.address}")
    String contractAddress;

    @Value("${foodchain.account.privatekey}")
    String accountPrivateKey;

    @Value("${foodchain.testnetwork.rinkebyClient}")
    String rinkebyClient;

}
