package com.food.chain.server.fcserver.wsdto;

import com.food.chain.server.fcserver.domain.Action;
import com.food.chain.server.fcserver.domain.Producer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CreateProductTagWsDTO {

    private Double longitude;
    private Double latitude;
    private Producer productTagProducer;
    private Set<Action> productTagActions = new HashSet<>();
    private Set<String> previousProductTagHashes = new HashSet<>();

}
