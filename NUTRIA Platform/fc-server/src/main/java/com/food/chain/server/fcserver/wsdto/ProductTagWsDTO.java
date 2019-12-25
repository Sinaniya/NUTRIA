package com.food.chain.server.fcserver.wsdto;

import com.food.chain.server.fcserver.domain.Action;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductTagWsDTO {

    private Long productTagId;
    private LocalDateTime dateTime;
    private Double longitude;
    private Double latitude;
    private String productTagHash;
    private ReturnProducerWsDTO productTagProducer;
    private Set<Action> productTagActions = new HashSet<>();
    private Set<ProductTagWsDTO> previousProductTags;

}
