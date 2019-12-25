package org.food.chain.foodchainbackend.wsdto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProducerWsDTO {
    private Long producerId;
    private String producerName;
    private String licenceNumber;
    private String url;
}
