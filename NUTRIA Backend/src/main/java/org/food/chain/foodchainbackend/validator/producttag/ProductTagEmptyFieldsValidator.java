package org.food.chain.foodchainbackend.validator.producttag;

import com.google.common.base.Strings;
import org.food.chain.foodchainbackend.constant.FCConst;
import org.food.chain.foodchainbackend.domain.ProductTag;

import java.util.Map;

public class ProductTagEmptyFieldsValidator {

    public void validateProductTagEmptyFields(ProductTag productTag, Map<String, String> errors) {
        if (productTag.getLongitude() == null)
            putEmptyFieldError(FCConst.PRODUCT_TAG_LONGITUTE, errors);
        if (productTag.getLatitude() == null)
            putEmptyFieldError(FCConst.PRODUCT_TAG_LATITUDE, errors);
        if (productTag.getProductTagProducer() == null)
            putEmptyFieldError(FCConst.PRODUCT_TAG_PRODUCER, errors);
        if (productTag.getProductTagActions() == null || productTag.getProductTagActions().isEmpty())
            putEmptyFieldError(FCConst.PRODUCT_TAG_ACTIONS, errors);
        if (Strings.isNullOrEmpty(productTag.getPreviousProductTagHash()))
            putEmptyFieldError(FCConst.PREVIOUS_PRODUCT_TAG_HASH, errors);
    }

    private void putEmptyFieldError(String fieldName, Map<String, String> errors) {
        errors.put(fieldName, "can't be empty");
    }

}
