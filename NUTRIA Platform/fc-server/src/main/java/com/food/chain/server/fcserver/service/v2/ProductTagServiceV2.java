package com.food.chain.server.fcserver.service.v2;

import com.food.chain.server.fcserver.constant.FCConst;
import com.food.chain.server.fcserver.constant.OperationType;
import com.food.chain.server.fcserver.converter.dtotomodel.CreateProductTagWsDTOToProductTagModelConverter;
import com.food.chain.server.fcserver.converter.modeltodto.ProductTagModelToProductTagWsDTO;
import com.food.chain.server.fcserver.converter.modeltodto.v2.ProductTagModelToProductTagWsDTOV2;
import com.food.chain.server.fcserver.domain.Producer;
import com.food.chain.server.fcserver.domain.ProductTag;
import com.food.chain.server.fcserver.ethereum.ETHClient;
import com.food.chain.server.fcserver.repository.ProductTagRepository;
import com.food.chain.server.fcserver.response.ResponseBodyGenerator;
import com.food.chain.server.fcserver.service.ProducerService;
import com.food.chain.server.fcserver.validator.producttag.ProductTagValidator;
import com.food.chain.server.fcserver.wsdto.CreateProductTagWsDTO;
import com.food.chain.server.fcserver.wsdto.ProductTagWsDTO;
import com.food.chain.server.fcserver.wsdto.v2.ProductTagChainWsDTOV2;
import com.food.chain.server.fcserver.wsdto.v2.ProductTagWsDTOV2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductTagServiceV2 {

    private final ProductTagRepository productTagRepository;
    private final ProductTagModelToProductTagWsDTOV2 modelToProductTagWsDTOV2;
    private final ProductTagValidator productTagValidator;
    private final CreateProductTagWsDTOToProductTagModelConverter createProductTagWsDTOToProductTagModelConverter;
    private final ETHClient ethClient;
    private final ProductTagModelToProductTagWsDTO modelToProductTagWsDTOConverter;
    private final ProducerService producerService;

    public ProductTagServiceV2(ProductTagRepository productTagRepository,
                               ProductTagModelToProductTagWsDTOV2 modelToProductTagWsDTOV2,
                               CreateProductTagWsDTOToProductTagModelConverter createProductTagWsDTOToProductTagModelConverter,
                               ETHClient ethClient,
                               ProductTagModelToProductTagWsDTO modelToProductTagWsDTOConverter,
                               ProducerService producerService) {
        this.productTagRepository = productTagRepository;
        this.modelToProductTagWsDTOV2 = modelToProductTagWsDTOV2;
        this.createProductTagWsDTOToProductTagModelConverter = createProductTagWsDTOToProductTagModelConverter;
        this.ethClient = ethClient;
        this.modelToProductTagWsDTOConverter = modelToProductTagWsDTOConverter;
        this.producerService = producerService;
        this.productTagValidator = new ProductTagValidator();
    }


    public Set<ProductTagWsDTOV2> getProductTagsByHashV2(String hash) {
        Set<ProductTagWsDTOV2> ptWsDTOV2List = new HashSet<>();
        Optional<ProductTag> productTagOptional = productTagRepository.findByProductTagHash(hash);

        if (productTagOptional.isPresent()) {
            ProductTag productTag = productTagOptional.get();
            convertPT(productTag, ptWsDTOV2List);
        }
        return ptWsDTOV2List;
    }

    private void convertPT(ProductTag productTag, Set<ProductTagWsDTOV2> ptWsDTOV2List) {
        if (!productTag.getProductTagHash().equals(FCConst.GENESIS_PRODUCT_TAG_HASH_VALUE) &&
                !containsPT(ptWsDTOV2List, productTag.getProductTagHash())) {
            ptWsDTOV2List.add(modelToProductTagWsDTOV2.convert(productTag));
        }
        if (productTag.getPreviousProductTags().size() > 0) {
            productTag.getPreviousProductTags().forEach(previousProductTag ->
                    convertPT(previousProductTag, ptWsDTOV2List));
        }
    }

    private boolean containsPT(final Set<ProductTagWsDTOV2> ptWsDTOV2List, final String hash) {
        return ptWsDTOV2List.stream().filter(pt -> pt.getProductTagHash().equals(hash)).findFirst().isPresent();
    }

    public ResponseEntity<?> addProductTag(CreateProductTagWsDTO productTag) {

        Map<String, String> errors = productTagValidator.validateProductTag(productTag, productTagRepository, ethClient);

        if (errors.size() == 0) {

            ProductTag productTagModel = createProductTagWsDTOToProductTagModelConverter.convert(productTag);

            ProductTagWsDTO savedProductTagWsDTO = modelToProductTagWsDTOConverter.convert(productTagRepository.save(productTagModel));

            ProductTagChainWsDTOV2 productTagChainWsDTOV2 =
                    ProductTagChainWsDTOV2.builder()
                            .hash(savedProductTagWsDTO.getProductTagHash())
                            .ptChain(getProductTagsByHashV2(savedProductTagWsDTO.getProductTagHash()))
                            .build();

            try {
                ethClient.getFoodChainContract().addProductTagHash(savedProductTagWsDTO.getProductTagHash()).sendAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return new ResponseEntity<>(productTagChainWsDTOV2, HttpStatus.CREATED);
        } else {
            return ResponseBodyGenerator.generateErrorResponseBody(FCConst.PPRODUCT_TAG, Long.MIN_VALUE,
                    OperationType.CREATE, HttpStatus.BAD_REQUEST, errors);
        }

    }

    public Set<ProductTagChainWsDTOV2> getProductTagsForProducer(Long producerId) {
        Producer producer = producerService.getProducerModelById(producerId);
        Set<ProductTagChainWsDTOV2> productTags = new HashSet<>();
        productTagRepository.findAllByProductTagProducer(producer)
                .forEach(productTag -> {
                    ProductTagWsDTO productTagWsDTO = modelToProductTagWsDTOConverter.convert(productTag);
                    ProductTagChainWsDTOV2 productTagChainWsDTOV2 =
                            ProductTagChainWsDTOV2.builder()
                                    .hash(productTagWsDTO.getProductTagHash())
                                    .ptChain(getProductTagsByHashV2(productTagWsDTO.getProductTagHash()))
                                    .build();
                    productTags.add(productTagChainWsDTOV2);
                });
        return productTags;
    }

}
