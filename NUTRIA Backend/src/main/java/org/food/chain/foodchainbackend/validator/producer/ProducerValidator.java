package org.food.chain.foodchainbackend.validator.producer;

import org.food.chain.foodchainbackend.constant.FCConst;
import org.food.chain.foodchainbackend.domain.Producer;
import org.food.chain.foodchainbackend.repository.ProducerRepository;

import java.util.HashMap;
import java.util.Map;

public class ProducerValidator {
    public Map<String, String> errors;
    ProducerRepository producerRepository;
    ProducerEmptyFieldsValidator emptyFieldsValidator;

    public ProducerValidator(ProducerRepository producerRepository) {
        this.producerRepository = producerRepository;
        this.errors = new HashMap<>();
    }

    public Map<String, String> validateProducer(Producer producer) {
        emptyFieldsValidator = new ProducerEmptyFieldsValidator();
        emptyFieldsValidator.validateProducerEmptyFields(producer, getErrors());

        validateAlreadyExists(producer);

        return getErrors();
    }


    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }


    private void validateAlreadyExists(Producer producer) {
        if (producerRepository.findProducerByProducerName(producer.getProducerName()).isPresent())
            putAlreadyExistsError(producer.getProducerName(), FCConst.PRODUCER_NAME);
        if (producerRepository.findProducerByUsername(producer.getUsername()).isPresent())
            putAlreadyExistsError(producer.getUsername(), FCConst.PRODUCER_USERNAME);
        if (producerRepository.findProducerByLicenceNumber(producer.getLicenceNumber()).isPresent())
            putAlreadyExistsError(producer.getLicenceNumber(), FCConst.PRODUCER_LICENCE_NUMBER);
        if (producerRepository.findProducerByEthereumAccount(producer.getEthereumAccount()).isPresent())
            putAlreadyExistsError(producer.getEthereumAccount(), FCConst.PRODUCER_ETHEREUM_ACCOUNT);
        if (producerRepository.findProducerByUrl(producer.getUrl()).isPresent())
            putAlreadyExistsError(producer.getUrl(), FCConst.PRODUCER_URL);
    }

    private void putAlreadyExistsError(String fieldValue, String fieldName) {
        getErrors().put(fieldName, "alredy exists with the value: " + fieldValue);
    }
}
