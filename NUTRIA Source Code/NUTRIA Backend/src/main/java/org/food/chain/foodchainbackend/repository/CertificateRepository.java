package org.food.chain.foodchainbackend.repository;

import org.food.chain.foodchainbackend.domain.Certificate;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CertificateRepository extends CrudRepository<Certificate, Long> {
    Optional<Certificate> findCertificateByCertificateName(String certificateName);

    Optional<Certificate> findCertificateByCertificateNameAndCertificateIdNot(String certificateName, Long certificateId);
}
