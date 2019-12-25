package com.food.chain.server.fcserver.wsdto.v2;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductTagChainWsDTOV2 {
    String hash;
    Set<ProductTagWsDTOV2> ptChain = new HashSet<>();
}
