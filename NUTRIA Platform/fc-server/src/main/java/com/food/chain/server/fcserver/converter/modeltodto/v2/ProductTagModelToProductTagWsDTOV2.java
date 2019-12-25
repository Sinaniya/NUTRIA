package com.food.chain.server.fcserver.converter.modeltodto.v2;

import com.food.chain.server.fcserver.converter.modeltodto.ReturnProducerWsDTOModelToWsDTOConverter;
import com.food.chain.server.fcserver.domain.ProductTag;
import com.food.chain.server.fcserver.wsdto.ProductTagWsDTO;
import com.food.chain.server.fcserver.wsdto.v2.ProductTagWsDTOV2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ProductTagModelToProductTagWsDTOV2 implements Converter<ProductTag, ProductTagWsDTOV2> {

    private final ReturnProducerWsDTOModelToWsDTOConverter producerWsDTOModelToWsDTOConverter;

    public ProductTagModelToProductTagWsDTOV2(ReturnProducerWsDTOModelToWsDTOConverter producerWsDTOModelToWsDTOConverter) {
        this.producerWsDTOModelToWsDTOConverter = producerWsDTOModelToWsDTOConverter;
    }

    @Override
    public ProductTagWsDTOV2 convert(ProductTag source) {
        if (source == null) {
            return null;
        }

        Set<String> previousProductTagHashes = new HashSet<>();

        if (source.getPreviousProductTags().size() > 0) {
            source.getPreviousProductTags().forEach(previousProductTag ->
                    previousProductTagHashes.add(previousProductTag.getProductTagHash()));
        }

        return ProductTagWsDTOV2.builder()
                .productTagId(source.getProductTagId())
                .dateTime(source.getDate())
                .longitude(source.getLongitude())
                .latitude(source.getLatitude())
                .productTagHash(source.getProductTagHash())
                .productTagActions(source.getProductTagActions())
                .previousProductTagHashes(previousProductTagHashes)
                .productTagProducer(producerWsDTOModelToWsDTOConverter.convert(source.getProductTagProducer()))
                .build();
    }
}
