package org.food.chain.foodchainbackend.service;

import org.food.chain.foodchainbackend.constant.FCConst;
import org.food.chain.foodchainbackend.constant.OperationType;
import org.food.chain.foodchainbackend.domain.Producer;
import org.food.chain.foodchainbackend.exception.ResourceNotFoundException;
import org.food.chain.foodchainbackend.repository.ProducerRepository;
import org.food.chain.foodchainbackend.response.ResponseBodyGenerator;
import org.food.chain.foodchainbackend.security.UserAuthenticationService;
import org.food.chain.foodchainbackend.validator.producer.ProducerValidator;
import org.food.chain.foodchainbackend.validator.producer.UpdateProducerValidator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProducerService {

    private final ProducerRepository producerRepository;
    private final CertificateService certificateService;
    private final ActionService actionService;
    private final UserAuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;

    private final ProducerValidator producerValidator;
    private final UpdateProducerValidator updateProducerValidator;

    public ProducerService(ProducerRepository producerRepository,
                           CertificateService certificateService,
                           ActionService actionService,
                           UserAuthenticationService authenticationService,
                           PasswordEncoder passwordEncoder) {
        this.producerRepository = producerRepository;
        this.certificateService = certificateService;
        this.actionService = actionService;
        this.authenticationService = authenticationService;
        this.passwordEncoder = passwordEncoder;
        producerValidator = new ProducerValidator(producerRepository);
        updateProducerValidator = new UpdateProducerValidator(producerRepository);
    }


    public List<Producer> getAllProducers() {
        List<Producer> producers = new ArrayList<>();
        producerRepository.findAll().forEach(producers::add);
        return producers;
    }

    public Producer getProducer(Long id) {
        return producerRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(FCConst.PRODUCER,
                        FCConst.PRODUCER_ID, id));
    }

    public ResponseEntity<?> registerProducer(final Producer producer) {
        Map<String, String> errors = producerValidator.validateProducer(producer);
        if (errors.isEmpty()) {
            final String password = producer.getPassword();
            producer.setPassword(passwordEncoder.encode(password));
            certificateService.saveProducersCertificates(producer.getProducerCertificates(), errors);
            actionService.saveProducersActions(producer.getProducerActions(), errors);
            producerRepository.save(producer);
            return ResponseBodyGenerator.generateSuccessResponceBody(
                    FCConst.PRODUCER,
                    producer.getProducerId(),
                    OperationType.CREATE,
                    HttpStatus.OK,
                    authenticationService.login(producer.getUsername(), password).get());
        } else {
            return ResponseBodyGenerator.generateErrorResponseBody(
                    FCConst.PRODUCER,
                    Long.MIN_VALUE,
                    OperationType.CREATE,
                    HttpStatus.BAD_REQUEST,
                    errors);
        }
    }


    public ResponseEntity<?> updateProducer(final Producer producer) {
        if (!producerRepository.findById(producer.getProducerId()).isPresent())
            throw new ResourceNotFoundException(FCConst.PRODUCER, FCConst.PRODUCER_ID, producer.getProducerId());
        Map<String, String> errors = updateProducerValidator.validateProducer(producer);
        if (errors.isEmpty()) {
            producerRepository.save(producer);
            return ResponseBodyGenerator.generateSuccessResponceBody(
                    FCConst.PRODUCER,
                    producer.getProducerId(),
                    OperationType.UPDATE,
                    HttpStatus.OK,
                    "");
        } else {
            return ResponseBodyGenerator.generateErrorResponseBody(
                    FCConst.PRODUCER,
                    producer.getProducerId(),
                    OperationType.UPDATE,
                    HttpStatus.BAD_REQUEST,
                    errors);
        }
    }

    public ResponseEntity<?> login(final String username, final String password) {

        ResponseEntity<?> responseEntity;
        Optional<String> token = authenticationService.login(username, password);

        if (!token.isPresent()) {
            Map<String, String> error = new HashMap<>();

            if (!producerRepository.findProducerByUsername(username).isPresent())
                error.put(FCConst.PRODUCER_USERNAME, String.format("there is no user with the username: %s", username));
            else error.put(FCConst.PRODUCER_PASSWORD, "wrong password");

            responseEntity = ResponseBodyGenerator.generateErrorResponseBody(
                    FCConst.PRODUCER,
                    Long.MIN_VALUE,
                    OperationType.LOGIN,
                    HttpStatus.BAD_REQUEST,
                    error);
        } else {
            responseEntity = ResponseBodyGenerator.generateSuccessResponceBody(
                    FCConst.PRODUCER,
                    producerRepository.findProducerByUsername(username).get().getProducerId(),
                    OperationType.LOGIN,
                    HttpStatus.OK,
                    token.get());
        }
        return responseEntity;
    }

    public ResponseEntity<?> logout(HttpHeaders httpHeaders, Long producerId) {

        authenticationService.logout(httpHeaders.get("authorization").get(0));

        return ResponseBodyGenerator.generateSuccessResponceBody(
                FCConst.PRODUCER,
                producerId,
                OperationType.LOGOUT,
                HttpStatus.OK,
                "");
    }
}


