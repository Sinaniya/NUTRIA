package com.food.chain.server.fcserver.bootstrap;

import com.food.chain.server.fcserver.domain.Action;
import com.food.chain.server.fcserver.domain.Certificate;
import com.food.chain.server.fcserver.domain.Producer;
import com.food.chain.server.fcserver.domain.ProductTag;
import com.food.chain.server.fcserver.repository.ActionRepository;
import com.food.chain.server.fcserver.repository.CertificateRepository;
import com.food.chain.server.fcserver.repository.ProducerRepository;
import com.food.chain.server.fcserver.repository.ProductTagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.food.chain.server.fcserver.constant.FCConst.GENESIS_PRODUCT_TAG_HASH_VALUE;

@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    private final ActionRepository actionRepository;
    private final CertificateRepository certificateRepository;
    private final ProductTagRepository productTagRepository;
    private final ProducerRepository producerRepository;
    private final PasswordEncoder passwordEncoder;

    public Bootstrap(ActionRepository actionRepository,
                     CertificateRepository certificateRepository,
                     ProductTagRepository productTagRepository,
                     ProducerRepository producerRepository,
                     PasswordEncoder passwordEncoder) {
        this.actionRepository = actionRepository;
        this.certificateRepository = certificateRepository;
        this.productTagRepository = productTagRepository;
        this.producerRepository = producerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if (!alreadyLoaded()) {
            loadActions();
            loadCertificates();
            loadProductTagAndProducer();
            loadProductTags();
            log.info("Data loaded.");
        }
    }

    private void loadProductTags() {
        Producer producer = producerRepository.findProducerByUsername("u1").get();

        ProductTag pt1 = saveProductTag("a", Double.valueOf(8.55), Double.valueOf(47.36667), producer,
                new HashSet<>(Arrays.asList(productTagRepository.findByProductTagHash(GENESIS_PRODUCT_TAG_HASH_VALUE).get())));


        ProductTag pt2 = saveProductTag("b", Double.valueOf(7.4474), Double.valueOf(46.9480), producer,
                new HashSet<>(Arrays.asList(productTagRepository.findByProductTagHash(GENESIS_PRODUCT_TAG_HASH_VALUE).get())));


        saveProductTag("c", Double.valueOf(7.5886), Double.valueOf(47.5596), producer,
                new HashSet<>(Arrays.asList(pt1, pt2)));
    }

    private boolean alreadyLoaded() {
        boolean loaded = false;
        if (producerRepository.count() > 0 ||
                actionRepository.count() > 0 ||
                certificateRepository.count() > 0 ||
                productTagRepository.count() > 0)
            loaded = true;
        return loaded;
    }


    private void loadActions() {
        Action action1 = new Action();
        action1.setActionName("Production raw product");

        Action action2 = new Action();
        action2.setActionName("Transport raw product");

        Action action3 = new Action();
        action3.setActionName("Processing raw product");

        Action action4 = new Action();
        action4.setActionName("Storage finished goods");

        Action action5 = new Action();
        action5.setActionName("Transport finished goods");

        Action action6 = new Action();
        action6.setActionName("Sale finished goods");

        actionRepository.saveAll(Arrays.asList(action1, action2, action3, action4, action5, action6));
    }

    private void loadCertificates() {
        Certificate certificate1 = new Certificate();
        certificate1.setCertificateName("IP");

        Certificate certificate2 = new Certificate();
        certificate2.setCertificateName("Bio");

        Certificate certificate3 = new Certificate();
        certificate3.setCertificateName("Demeter");

        Certificate certificate4 = new Certificate();
        certificate4.setCertificateName("ProSpecieRara");

        certificateRepository.saveAll(Arrays.asList(certificate1, certificate2, certificate3, certificate4));
    }

    private void loadProductTagAndProducer() {
        ProductTag productTag = ProductTag.builder()
                .productTagHash(GENESIS_PRODUCT_TAG_HASH_VALUE)
                .date(LocalDateTime.now())
                .longitude(Double.valueOf(0))
                .latitude(Double.valueOf(0))
                .productTagProducer(producerRepository.save(getProducer()))
                .build();
        productTagRepository.save(productTag);
    }

    private Producer getProducer() {
        Producer producer = new Producer();
        producer.setProducerName("Producer 1");
        producer.setLicenceNumber("Licence 1");
        producer.setUrl("URL 1");
        producer.setEthereumAccount("Account 1");
        producer.setUsername("u1");
        producer.setPassword(passwordEncoder.encode("p1"));
        return producer;
    }


    private ProductTag saveProductTag(String hash, Double longitude, Double latitude, Producer producer, Set<ProductTag> previousPTs) {

        ProductTag productTag = ProductTag.builder()
                .productTagHash(hash)
                .date(LocalDateTime.now())
                .longitude(longitude)
                .latitude(latitude)
                .productTagProducer(producer)
                .previousProductTags(previousPTs)
                .productTagActions(new HashSet<>(Arrays.asList(actionRepository.findById(1L).get())))
                .build();
        return productTagRepository.save(productTag);
    }


}
