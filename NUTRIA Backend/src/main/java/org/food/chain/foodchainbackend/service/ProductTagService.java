package org.food.chain.foodchainbackend.service;

import org.food.chain.foodchainbackend.constant.FCConst;
import org.food.chain.foodchainbackend.constant.OperationType;
import org.food.chain.foodchainbackend.converter.modeltodto.ProductTagModelToProductTagWsDTO;
import org.food.chain.foodchainbackend.domain.Producer;
import org.food.chain.foodchainbackend.domain.ProductTag;
import org.food.chain.foodchainbackend.ethereum.ETHClient;
import org.food.chain.foodchainbackend.exception.ResourceNotFoundException;
import org.food.chain.foodchainbackend.repository.ProductTagRepository;
import org.food.chain.foodchainbackend.response.ResponseBodyGenerator;
import org.food.chain.foodchainbackend.util.HashingUtil;
import org.food.chain.foodchainbackend.validator.producttag.ProductTagValidator;
import org.food.chain.foodchainbackend.wsdto.ProductTagWsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductTagService {

    private final ProductTagRepository productTagRepository;
    private final ProducerService producerService;
    private final ProductTagModelToProductTagWsDTO modelToProductTagWsDTOConverter;
    private final ProductTagValidator productTagValidator;
    private final ETHClient ethClient;

    public ProductTagService(ProductTagRepository productTagRepository,
                             ProducerService producerService,
                             ProductTagModelToProductTagWsDTO modelToProductTagWsDTOConverter, ETHClient ethClient) {
        this.productTagRepository = productTagRepository;
        this.producerService = producerService;
        this.modelToProductTagWsDTOConverter = modelToProductTagWsDTOConverter;
        this.ethClient = ethClient;
        this.productTagValidator = new ProductTagValidator();
    }

    public Set<ProductTag> getAllProductTags() {
        Set<ProductTag> productTags = new HashSet<>();
        productTagRepository.findAll().forEach(productTags::add);
        return productTags;
    }

    public ProductTag getProductTagById(Long id) {
        return productTagRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(FCConst.PPRODUCT_TAG, FCConst.PRODUCT_TAG_ID, id));
    }

    public Set<ProductTagWsDTO> getProductTagsByHash(String hash) {

        Optional<ProductTag> productTagOptional = productTagRepository.findByProductTagHash(hash);
        Set<ProductTagWsDTO> productTagWsDTOS = new HashSet<>();

        if (productTagOptional.isPresent()) {
            ProductTag productTag = productTagOptional.get();

            do {
                productTagWsDTOS.add(modelToProductTagWsDTOConverter.convert(productTag));
                productTag = productTag.getPreviousProductTag();
            }
            while (!productTag.getProductTagHash().equals(FCConst.GENESIS_PRODUCT_TAG_HASH_VALUE));

            // todo sort result if needed
            return productTagWsDTOS;
        }
        return productTagWsDTOS;
    }

    public ResponseEntity<?> addCheckPoint(ProductTag productTag) {
        Map<String, String> errors = productTagValidator.validateProductTag(productTag, productTagRepository, ethClient);
        if (errors.isEmpty()) {
            Long productTagId = generateId();
            productTag.setProductTagId(productTagId);
            productTag.setDate(new Date());
            productTag.setProductTagHash(
                    HashingUtil.getProductTagHash(
                            productTagId,
                            productTag.getDate(),
                            productTag.getLongitude(),
                            productTag.getLatitude(),
                            productTag.getPreviousProductTagHash()));
            ProductTagWsDTO savedProductTagWsDTO = modelToProductTagWsDTOConverter.convert(productTagRepository.save(productTag));

            try {
                // just for testing since the client hasn't implemented an ethereum c8lient to send the data
                ethClient.getFoodChainContract().addProductTagHash(savedProductTagWsDTO.getProductTagHash()).sendAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new ResponseEntity<>(savedProductTagWsDTO, HttpStatus.CREATED);
        } else {
            return ResponseBodyGenerator.generateErrorResponseBody(FCConst.PPRODUCT_TAG, Long.MIN_VALUE,
                    OperationType.CREATE, HttpStatus.BAD_REQUEST, errors);
        }
    }


    public Set<ProductTagWsDTO> getProductTagsForProducer(Long producerId) {
        Producer producer = producerService.getProducer(producerId);
        Set<ProductTagWsDTO> productTags = new HashSet<>();
        productTagRepository.findAllByProductTagProducer(producer)
                .forEach(productTag -> productTags.add(modelToProductTagWsDTOConverter.convert(productTag)));
        return productTags;
    }

    private Long generateId() {
        return productTagRepository.getMaxId() + 1L;
    }
}
