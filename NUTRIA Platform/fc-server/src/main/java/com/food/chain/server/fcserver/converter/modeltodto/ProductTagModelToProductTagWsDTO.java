package com.food.chain.server.fcserver.converter.modeltodto;

import com.food.chain.server.fcserver.domain.ProductTag;
import com.food.chain.server.fcserver.wsdto.ProductTagWsDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ProductTagModelToProductTagWsDTO implements Converter<ProductTag, ProductTagWsDTO> {

    private final ReturnProducerWsDTOModelToWsDTOConverter producerWsDTOModelToWsDTOConverter;

    public ProductTagModelToProductTagWsDTO(ReturnProducerWsDTOModelToWsDTOConverter producerWsDTOModelToWsDTOConverter) {
        this.producerWsDTOModelToWsDTOConverter = producerWsDTOModelToWsDTOConverter;
    }

    @Override
    public ProductTagWsDTO convert(ProductTag source) {
        if (source == null) {
            return null;
        }

        Set<ProductTagWsDTO> previousProductTags = new HashSet<>();

        if (source.getPreviousProductTags().size() > 0) {
            source.getPreviousProductTags().forEach(previousProductTag ->
                    previousProductTags.add(convert(previousProductTag)));
        }

        return ProductTagWsDTO.builder()
                .productTagId(source.getProductTagId())
                .dateTime(source.getDate())
                .longitude(source.getLongitude())
                .latitude(source.getLatitude())
                .productTagHash(source.getProductTagHash())
                .productTagActions(source.getProductTagActions())
                .previousProductTags(previousProductTags)
                .productTagProducer(producerWsDTOModelToWsDTOConverter.convert(source.getProductTagProducer()))
                .build();
    }
}
