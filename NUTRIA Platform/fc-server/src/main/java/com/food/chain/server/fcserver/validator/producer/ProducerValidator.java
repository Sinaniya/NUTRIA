package com.food.chain.server.fcserver.validator.producer;

import com.food.chain.server.fcserver.constant.FCConst;
import com.food.chain.server.fcserver.domain.Producer;
import com.food.chain.server.fcserver.repository.ProducerRepository;

import java.util.HashMap;
import java.util.Map;

public class ProducerValidator {

    ProducerRepository producerRepository;
    ProducerEmptyFieldsValidator emptyFieldsValidator;

    public ProducerValidator(ProducerRepository producerRepository) {
        this.producerRepository = producerRepository;
    }

    public Map<String, String> validateProducer(Producer producer) {
        Map<String, String> errors = new HashMap<>();
        emptyFieldsValidator = new ProducerEmptyFieldsValidator();
        emptyFieldsValidator.validateProducerEmptyFields(producer, errors);

        validateAlreadyExists(producer, errors);

        return errors;
    }


    private void validateAlreadyExists(Producer producer, Map<String, String> errors) {
        if (producerRepository.findProducerByProducerName(producer.getProducerName()).isPresent())
            putAlreadyExistsError(producer.getProducerName(), FCConst.PRODUCER_NAME, errors);
        if (producerRepository.findProducerByUsername(producer.getUsername()).isPresent())
            putAlreadyExistsError(producer.getUsername(), FCConst.PRODUCER_USERNAME, errors);
        if (producerRepository.findProducerByLicenceNumber(producer.getLicenceNumber()).isPresent())
            putAlreadyExistsError(producer.getLicenceNumber(), FCConst.PRODUCER_LICENCE_NUMBER, errors);
        if (producerRepository.findProducerByEthereumAccount(producer.getEthereumAccount()).isPresent())
            putAlreadyExistsError(producer.getEthereumAccount(), FCConst.PRODUCER_ETHEREUM_ACCOUNT, errors);
        if (producerRepository.findProducerByUrl(producer.getUrl()).isPresent())
            putAlreadyExistsError(producer.getUrl(), FCConst.PRODUCER_URL, errors);
    }

    private void putAlreadyExistsError(String fieldValue, String fieldName, Map<String, String> errors) {
        errors.put(fieldName, "alredy exists with the value: " + fieldValue);
    }
}
