package com.food.chain.server.fcserver.validator.producttag;

import com.food.chain.server.fcserver.constant.FCConst;
import com.food.chain.server.fcserver.wsdto.CreateProductTagWsDTO;

import java.util.Map;

public class ProductTagEmptyFieldsValidator {

    public void validateProductTagEmptyFields(CreateProductTagWsDTO productTag, Map<String, String> errors) {
        if (productTag.getLongitude() == null)
            putEmptyFieldError(FCConst.PRODUCT_TAG_LONGITUTE, errors);
        if (productTag.getLatitude() == null)
            putEmptyFieldError(FCConst.PRODUCT_TAG_LATITUDE, errors);
        if (productTag.getProductTagProducer() == null)
            putEmptyFieldError(FCConst.PRODUCT_TAG_PRODUCER, errors);
        if (productTag.getProductTagActions() == null || productTag.getProductTagActions().isEmpty())
            putEmptyFieldError(FCConst.PRODUCT_TAG_ACTIONS, errors);
        // first check if is null, the second check is to ensure that there is at least one previous pt hash
        // as part of the requirements, pt can be generated based on multiple previous pts
        if (productTag.getPreviousProductTagHashes() == null || productTag.getPreviousProductTagHashes().size() < 1)
            putEmptyFieldError(FCConst.PREVIOUS_PRODUCT_TAG_HASH, errors);
    }

    private void putEmptyFieldError(String fieldName, Map<String, String> errors) {
        errors.put(fieldName, "can't be empty");
    }

}
