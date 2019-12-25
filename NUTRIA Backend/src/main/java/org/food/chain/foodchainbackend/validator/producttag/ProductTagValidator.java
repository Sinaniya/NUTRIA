package org.food.chain.foodchainbackend.validator.producttag;

import org.food.chain.foodchainbackend.constant.FCConst;
import org.food.chain.foodchainbackend.domain.ProductTag;
import org.food.chain.foodchainbackend.ethereum.ETHClient;
import org.food.chain.foodchainbackend.repository.ProductTagRepository;

import java.util.HashMap;
import java.util.Map;

public class ProductTagValidator {


    private final ProductTagEmptyFieldsValidator productTagEmptyFieldsValidator;
    private final PreviousProductTagValidator previousProductTagValidator;

    public ProductTagValidator() {
        this.productTagEmptyFieldsValidator = new ProductTagEmptyFieldsValidator();
        this.previousProductTagValidator = new PreviousProductTagValidator();
    }


    public Map<String, String> validateProductTag(ProductTag productTag, ProductTagRepository productTagRepository, ETHClient ethClient) {
        Map<String, String> errors = new HashMap<>();
        productTagEmptyFieldsValidator.validateProductTagEmptyFields(productTag, errors);

        if (!errors.keySet().contains(FCConst.PREVIOUS_PRODUCT_TAG_HASH)) {
            if (!productTag.getPreviousProductTagHash().equals(FCConst.GENESIS_PRODUCT_TAG_HASH_VALUE)) {
                previousProductTagValidator.validatePreviousProductTag(productTag, ethClient, productTagRepository, errors);
            } else {
                productTag.setPreviousProductTag(productTagRepository.findByProductTagHash(FCConst.GENESIS_PRODUCT_TAG_HASH_VALUE).get());
            }
        }
        return errors;
    }

}
