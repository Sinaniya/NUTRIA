package org.food.chain.foodchainbackend.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.food.chain.foodchainbackend.domain.Action;
import org.food.chain.foodchainbackend.domain.Certificate;
import org.food.chain.foodchainbackend.domain.ProductTag;
import org.food.chain.foodchainbackend.repository.ActionRepository;
import org.food.chain.foodchainbackend.repository.CertificateRepository;
import org.food.chain.foodchainbackend.repository.ProductTagRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

import static org.food.chain.foodchainbackend.constant.FCConst.GENESIS_PRODUCT_TAG_HASH_VALUE;

@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    private final ActionRepository actionRepository;
    private final CertificateRepository certificateRepository;
    private final ProductTagRepository productTagRepository;

    public Bootstrap(ActionRepository actionRepository,
                     CertificateRepository certificateRepository,
                     ProductTagRepository productTagRepository) {
        this.actionRepository = actionRepository;
        this.certificateRepository = certificateRepository;
        this.productTagRepository = productTagRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!alreadyLoaded()) {
            loadActions();
            loadCertificates();
            loadProductTags();
            log.info("Data loaded.");
        }
    }

    private boolean alreadyLoaded() {
        boolean loaded = false;
        if (actionRepository.count() > 0 ||
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

    private void loadProductTags() {
        ProductTag productTag = new ProductTag();
        productTag.setProductTagId(productTagRepository.getMaxId() + 1);
        productTag.setProductTagHash(GENESIS_PRODUCT_TAG_HASH_VALUE);
        productTag.setDate(new Date());
        productTag.setLongitude(Double.valueOf(0));
        productTag.setLatitude(Double.valueOf(0));

        productTagRepository.save(productTag);
    }


}
