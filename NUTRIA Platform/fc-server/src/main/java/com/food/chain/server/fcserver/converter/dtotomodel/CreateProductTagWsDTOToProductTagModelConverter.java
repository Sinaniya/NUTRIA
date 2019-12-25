package com.food.chain.server.fcserver.converter.dtotomodel;

import com.food.chain.server.fcserver.domain.ProductTag;
import com.food.chain.server.fcserver.repository.ProductTagRepository;
import com.food.chain.server.fcserver.util.HashingUtil;
import com.food.chain.server.fcserver.wsdto.CreateProductTagWsDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class CreateProductTagWsDTOToProductTagModelConverter implements Converter<CreateProductTagWsDTO, ProductTag> {

    private final ProductTagRepository productTagRepository;

    public CreateProductTagWsDTOToProductTagModelConverter(ProductTagRepository productTagRepository) {
        this.productTagRepository = productTagRepository;
    }

    @Override
    public ProductTag convert(CreateProductTagWsDTO source) {
        if (source == null) {
            return null;
        }

        Set<ProductTag> previousProductTags = new HashSet<>();
        source.getPreviousProductTagHashes().forEach(previousProductTagHash -> {
            previousProductTags.add(productTagRepository.findByProductTagHash(previousProductTagHash).get());
        });

        final LocalDateTime dateTime = LocalDateTime.now().plusHours(2);
        return ProductTag.builder()
                .date(dateTime)
                .longitude(source.getLongitude())
                .latitude(source.getLatitude())
                .previousProductTags(previousProductTags)
                .productTagProducer(source.getProductTagProducer())
                .productTagActions(source.getProductTagActions())
                .productTagHash(HashingUtil.getProductTagHash(dateTime,
                        source.getLongitude(),
                        source.getLatitude(),
                        source.getPreviousProductTagHashes()))
                .build();
    }
}

