package org.food.chain.foodchainbackend.wsdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.food.chain.foodchainbackend.domain.Action;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ProductTagWsDTO {

    private Long productTagId;
    private Date date;
    private Double longitude;
    private Double latitude;
    private String productTagHash;
    private String previousProductTagHash;
    private ProducerWsDTO productTagProducer;
    private Set<Action> productTagActions = new HashSet<>();

}
