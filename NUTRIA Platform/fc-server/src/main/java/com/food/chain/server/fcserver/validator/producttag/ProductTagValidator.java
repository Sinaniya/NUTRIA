package com.food.chain.server.fcserver.validator.producttag;

import com.food.chain.server.fcserver.constant.FCConst;
import com.food.chain.server.fcserver.ethereum.ETHClient;
import com.food.chain.server.fcserver.repository.ProductTagRepository;
import com.food.chain.server.fcserver.wsdto.CreateProductTagWsDTO;

import java.util.HashMap;
import java.util.Map;

public class ProductTagValidator {


    private final ProductTagEmptyFieldsValidator productTagEmptyFieldsValidator;
    private final PreviousProductTagsValidator previousProductTagsValidator;

    public ProductTagValidator() {
        this.productTagEmptyFieldsValidator = new ProductTagEmptyFieldsValidator();
        this.previousProductTagsValidator = new PreviousProductTagsValidator();
    }


    public Map<String, String> validateProductTag(CreateProductTagWsDTO productTag, ProductTagRepository productTagRepository, ETHClient ethClient) {
        Map<String, String> errors = new HashMap<>();
        productTagEmptyFieldsValidator.validateProductTagEmptyFields(productTag, errors);


        if (!errors.keySet().contains(FCConst.PREVIOUS_PRODUCT_TAG_HASH)) {
            // here we are sure that the size is at least 1, now we check if the size is exactly one and that the hash is
            // the genesis pt hash, if that is the case, we just return
            if (productTag.getPreviousProductTagHashes().size() == 1 &&
                    productTag.getPreviousProductTagHashes().contains(FCConst.GENESIS_PRODUCT_TAG_HASH_VALUE)) {
                return errors;
            } else {
                // means the size is one or more so we should validate each hash separately
                previousProductTagsValidator.validatePreviousProductTags(productTag, ethClient, productTagRepository, errors);
            }
        }
        return errors;
    }

}
