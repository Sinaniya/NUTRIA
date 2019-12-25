package com.food.chain.server.fcserver.wsdto;


import com.food.chain.server.fcserver.domain.Action;
import com.food.chain.server.fcserver.domain.Certificate;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnProducerWsDTO {

    private Long producerId;

    private String producerName;

    private String licenceNumber;

    private String ethereumAccount;

    private String url;

    private Set<Certificate> producerCertificates;

    private Set<Action> producerActions;


}
