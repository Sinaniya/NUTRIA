package com.food.chain.server.fcserver.service;

import com.food.chain.server.fcserver.constant.FCConst;
import com.food.chain.server.fcserver.constant.OperationType;
import com.food.chain.server.fcserver.converter.dtotomodel.CreateProductTagWsDTOToProductTagModelConverter;
import com.food.chain.server.fcserver.converter.modeltodto.ProductTagModelToProductTagWsDTO;
import com.food.chain.server.fcserver.domain.Producer;
import com.food.chain.server.fcserver.domain.ProductTag;
import com.food.chain.server.fcserver.ethereum.ETHClient;
import com.food.chain.server.fcserver.exception.ResourceNotFoundException;
import com.food.chain.server.fcserver.repository.ProductTagRepository;
import com.food.chain.server.fcserver.response.ResponseBodyGenerator;
import com.food.chain.server.fcserver.validator.producttag.ProductTagValidator;
import com.food.chain.server.fcserver.wsdto.CreateProductTagWsDTO;
import com.food.chain.server.fcserver.wsdto.ProductTagWsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductTagService {

    private final ProductTagRepository productTagRepository;
    private final ProducerService producerService;
    private final ProductTagModelToProductTagWsDTO modelToProductTagWsDTOConverter;
    private final CreateProductTagWsDTOToProductTagModelConverter createProductTagWsDTOToProductTagModelConverter;
    private final ETHClient ethClient;
    private final ProductTagValidator productTagValidator;

    public ProductTagService(ProductTagRepository productTagRepository,
                             ProducerService producerService,
                             ProductTagModelToProductTagWsDTO modelToProductTagWsDTOConverter,
                             CreateProductTagWsDTOToProductTagModelConverter createProductTagWsDTOToProductTagModelConverter, ETHClient ethClient) {
        this.productTagRepository = productTagRepository;
        this.producerService = producerService;
        this.modelToProductTagWsDTOConverter = modelToProductTagWsDTOConverter;
        this.createProductTagWsDTOToProductTagModelConverter = createProductTagWsDTOToProductTagModelConverter;
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

    public ProductTagWsDTO getProductTagsByHash(String hash) {

        Optional<ProductTag> productTagOptional = productTagRepository.findByProductTagHash(hash);

        if (productTagOptional.isPresent()) {
            ProductTag productTag = productTagOptional.get();


            return modelToProductTagWsDTOConverter.convert(productTag);


//            if (productTag.getPreviousProductTagHash() != null) {
//                do {
//                    productTagWsDTOS.add(modelToProductTagWsDTOConverter.convert(productTag));
//                    productTag = productTag.getPreviousProductTag();
//                }
//                while (!productTag.getProductTagHash().equals(FCConst.GENESIS_PRODUCT_TAG_HASH_VALUE));
//            }

//            // todo sort result if needed
//            return productTagWsDTOS;
//        }
//        return productTagWsDTOS;
        }
        return new ProductTagWsDTO();
    }

    public ResponseEntity<?> addProductTag(CreateProductTagWsDTO productTag) {

        Map<String, String> errors = productTagValidator.validateProductTag(productTag, productTagRepository, ethClient);

        if (errors.size() == 0) {

            ProductTag productTagModel = createProductTagWsDTOToProductTagModelConverter.convert(productTag);


            ProductTagWsDTO savedProductTagWsDTO = modelToProductTagWsDTOConverter.convert(productTagRepository.save(productTagModel));

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
        Producer producer = producerService.getProducerModelById(producerId);
        Set<ProductTagWsDTO> productTags = new HashSet<>();
        productTagRepository.findAllByProductTagProducer(producer)
                .forEach(productTag -> productTags.add(modelToProductTagWsDTOConverter.convert(productTag)));
        return productTags;
    }
}
