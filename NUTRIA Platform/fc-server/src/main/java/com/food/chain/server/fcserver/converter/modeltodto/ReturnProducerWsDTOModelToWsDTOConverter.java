package com.food.chain.server.fcserver.converter.modeltodto;

import com.food.chain.server.fcserver.domain.Producer;
import com.food.chain.server.fcserver.wsdto.ReturnProducerWsDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ReturnProducerWsDTOModelToWsDTOConverter implements Converter<Producer, ReturnProducerWsDTO> {
    @Override
    public ReturnProducerWsDTO convert(Producer source) {

        if (source == null) {
            return null;
        }

        return ReturnProducerWsDTO.builder()
                .producerId(source.getProducerId())
                .producerName(source.getProducerName())
                .licenceNumber(source.getLicenceNumber())
                .ethereumAccount(source.getEthereumAccount())
                .url(source.getUrl())
                .producerCertificates(source.getProducerCertificates())
                .producerActions(source.getProducerActions())
                .build();
    }
}
