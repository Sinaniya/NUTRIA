package org.food.chain.foodchainbackend.validator.producer;

import com.google.common.base.Strings;
import org.food.chain.foodchainbackend.constant.FCConst;
import org.food.chain.foodchainbackend.domain.Producer;

import java.util.Map;

public class ProducerEmptyFieldsValidator {

    public void validateProducerEmptyFields(Producer producer, Map<String, String> errors) {
        if (Strings.isNullOrEmpty(producer.getProducerName()))
            putEmptyFieldError(FCConst.PRODUCER_NAME, errors);
        if (Strings.isNullOrEmpty(producer.getUsername()))
            putEmptyFieldError(FCConst.PRODUCER_USERNAME, errors);
        if (Strings.isNullOrEmpty(producer.getPassword()))
            putEmptyFieldError(FCConst.PRODUCER_PASSWORD, errors);
        if (Strings.isNullOrEmpty(producer.getLicenceNumber()))
            putEmptyFieldError(FCConst.PRODUCER_LICENCE_NUMBER, errors);
        if (Strings.isNullOrEmpty(producer.getEthereumAccount()))
            putEmptyFieldError(FCConst.PRODUCER_ETHEREUM_ACCOUNT, errors);
        if (Strings.isNullOrEmpty(producer.getUrl()))
            putEmptyFieldError(FCConst.PRODUCER_URL, errors);
    }

    private void putEmptyFieldError(String fieldName, Map<String, String> errors) {
        errors.put(fieldName, "can't be empty");
    }

}
