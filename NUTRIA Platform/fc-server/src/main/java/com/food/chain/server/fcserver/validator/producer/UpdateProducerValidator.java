package com.food.chain.server.fcserver.validator.producer;

import com.food.chain.server.fcserver.constant.FCConst;
import com.food.chain.server.fcserver.domain.Producer;
import com.food.chain.server.fcserver.repository.ProducerRepository;
import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.Map;

public class UpdateProducerValidator {

    ProducerRepository producerRepository;


    public UpdateProducerValidator(ProducerRepository producerRepository) {
        this.producerRepository = producerRepository;
    }

    public Map<String, String> validateProducer(Producer producer) {
        Map<String, String> errors = new HashMap<>();
        validateProducerEmptyFields(producer, errors);
        validateAlreadyExists(producer, errors);
        return errors;
    }

    public void validateProducerEmptyFields(Producer producer, Map<String, String> errors) {
        if (Strings.isNullOrEmpty(producer.getProducerName()))
            putEmptyFieldError(FCConst.PRODUCER_NAME, errors);
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

    private void validateAlreadyExists(Producer producer, Map<String, String> errors) {
        if (producerRepository.findProducerByProducerNameAndProducerIdNot(producer.getProducerName(), producer.getProducerId()).isPresent())
            putAlreadyExistsError(producer.getProducerName(), FCConst.PRODUCER_NAME, errors);
        if (producerRepository.findProducerByLicenceNumberAndProducerIdNot(producer.getLicenceNumber(), producer.getProducerId()).isPresent())
            putAlreadyExistsError(producer.getLicenceNumber(), FCConst.PRODUCER_LICENCE_NUMBER, errors);
        if (producerRepository.findProducerByEthereumAccountAndProducerIdNot(producer.getEthereumAccount(), producer.getProducerId()).isPresent())
            putAlreadyExistsError(producer.getEthereumAccount(), FCConst.PRODUCER_ETHEREUM_ACCOUNT, errors);
        if (producerRepository.findProducerByUrlAndProducerIdNot(producer.getUrl(), producer.getProducerId()).isPresent())
            putAlreadyExistsError(producer.getUrl(), FCConst.PRODUCER_URL, errors);
    }

    private void putAlreadyExistsError(String fieldValue, String fieldName, Map<String, String> errors) {
        errors.put(fieldName, "alredy exists with the value: " + fieldValue);
    }


}
