package org.food.chain.foodchainbackend.service;

import org.food.chain.foodchainbackend.constant.FCConst;
import org.food.chain.foodchainbackend.domain.Certificate;
import org.food.chain.foodchainbackend.exception.EmptyFieldExceptin;
import org.food.chain.foodchainbackend.exception.ResourceAlreadyExistsException;
import org.food.chain.foodchainbackend.exception.ResourceNotFoundException;
import org.food.chain.foodchainbackend.repository.CertificateRepository;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.google.common.base.Strings.isNullOrEmpty;

@Service
public class CertificateService {

    private final CertificateRepository certificateRepository;

    public CertificateService(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    public List<Certificate> getAllCertificates() {
        List<Certificate> certificates = new ArrayList<>();
        certificateRepository.findAll().forEach(certificates::add);
        return certificates;
    }

    public Certificate getCertificate(Long id) {
        return certificateRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(FCConst.CERTIFICATE, FCConst.CERTIFICATE_ID, id));
    }

    public Certificate createCertificate(Certificate certificate) {
        if (isNullOrEmpty(certificate.getCertificateName()))
            throw new EmptyFieldExceptin(FCConst.CERTIFICATE, FCConst.CERTIFICATE_NAME);
        else if (certificateRepository.findCertificateByCertificateName(certificate.getCertificateName()).isPresent())
            throw new ResourceAlreadyExistsException(FCConst.CERTIFICATE, FCConst.CERTIFICATE_NAME, certificate.getCertificateName());
        else
            return certificateRepository.save(certificate);
    }

    public Certificate updateCertificate(Certificate certificate) {
        if (isNullOrEmpty(certificate.getCertificateName()))
            throw new EmptyFieldExceptin(FCConst.CERTIFICATE, FCConst.CERTIFICATE_NAME);
        if (!certificateRepository.findById(certificate.getCertificateId()).isPresent())
            throw new ResourceNotFoundException(FCConst.CERTIFICATE, FCConst.CERTIFICATE_ID, certificate.getCertificateId());
        if (certificateRepository.findCertificateByCertificateNameAndCertificateIdNot(certificate.getCertificateName(), certificate.getCertificateId()).isPresent())
            throw new ResourceAlreadyExistsException(FCConst.CERTIFICATE, FCConst.CERTIFICATE_NAME, certificate.getCertificateName());
        else
            return certificateRepository.save(certificate);
    }

    public void deleteCertificate(Long id) {
        if (!certificateRepository.findById(id).isPresent())
            throw new ResourceNotFoundException(FCConst.CERTIFICATE, FCConst.CERTIFICATE_ID, id);
        certificateRepository.deleteById(id);
    }


    public void saveProducersCertificates(Set<Certificate> producerCertificates, Map<String, String> errors) {
        producerCertificates.forEach(certificate -> {
            if (certificate.getCertificateId() != null) {
                if (!certificateRepository.findById(certificate.getCertificateId()).isPresent()) {
                    errors.put(FCConst.CERTIFICATE, String.format("doesn't exist with id: %s", certificate.getCertificateId()));
                }
            } else {
                if (isNullOrEmpty(certificate.getCertificateName())) {
                    errors.put(FCConst.CERTIFICATE, String.format("please provide '%s' or '%s'", FCConst.CERTIFICATE_ID, FCConst.CERTIFICATE_NAME));
                } else {
                    Optional<Certificate> optionalCertificate = certificateRepository.findCertificateByCertificateName(certificate.getCertificateName());
                    if (optionalCertificate.isPresent()) {
                        certificate.setCertificateId(optionalCertificate.get().getCertificateId());
                    } else {
                        certificate.setCertificateId(certificateRepository.save(certificate).getCertificateId());
                    }
                }
            }
        });
    }

}