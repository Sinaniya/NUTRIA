package org.food.chain.foodchainbackend.repository;

import org.food.chain.foodchainbackend.domain.Producer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProducerRepository extends CrudRepository<Producer, Long> {
    Optional<Producer> findProducerByProducerName(String producerName);
    Optional<Producer> findProducerByLicenceNumber(String licenceNumber);
    Optional<Producer> findProducerByEthereumAccount(String ethereumAccount);
    Optional<Producer> findProducerByUrl(String ethereumAccount);
    Optional<Producer> findProducerByUsername(String username);

    Optional<Producer> findProducerByProducerNameAndProducerIdNot(String producerName, Long producerId);

    Optional<Producer> findProducerByLicenceNumberAndProducerIdNot(String licenceNumber, Long producerId);

    Optional<Producer> findProducerByUrlAndProducerIdNot(String url, Long producerId);

    Optional<Producer> findProducerByEthereumAccountAndProducerIdNot(String ethereumAccount, Long producerId);
}
