package org.food.chain.foodchainbackend.converter.modeltodto;

import org.food.chain.foodchainbackend.domain.Action;
import org.food.chain.foodchainbackend.domain.ProductTag;
import org.food.chain.foodchainbackend.wsdto.ProducerWsDTO;
import org.food.chain.foodchainbackend.wsdto.ProductTagWsDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ProductTagModelToProductTagWsDTO implements Converter<ProductTag, ProductTagWsDTO> {


    @Override
    public ProductTagWsDTO convert(ProductTag source) {
        if (source == null) {
            return null;
        }
        final ProductTagWsDTO productTagWsDTO = new ProductTagWsDTO();
        productTagWsDTO.setProductTagId(source.getProductTagId());
        productTagWsDTO.setDate(source.getDate());
        productTagWsDTO.setLongitude(source.getLongitude());
        productTagWsDTO.setLatitude(source.getLatitude());
        productTagWsDTO.setProductTagHash(source.getProductTagHash());
        productTagWsDTO.setPreviousProductTagHash(source.getPreviousProductTagHash());

        ProducerWsDTO producerWsDTO = new ProducerWsDTO();
        producerWsDTO.setProducerId(source.getProductTagProducer().getProducerId());
        producerWsDTO.setLicenceNumber(source.getProductTagProducer().getLicenceNumber());
        producerWsDTO.setProducerName(source.getProductTagProducer().getProducerName());
        producerWsDTO.setUrl(source.getProductTagProducer().getUrl());


        productTagWsDTO.setProductTagProducer(producerWsDTO);
        if (!source.getProductTagActions().isEmpty()) {
            Set<Action> actions = new HashSet<>();
            source.getProductTagActions().forEach(action -> actions.add(action));
            productTagWsDTO.setProductTagActions(actions);
        }
        return productTagWsDTO;
    }
}
