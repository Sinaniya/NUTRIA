package com.food.chain.server.fcserver.controller;

import com.food.chain.server.fcserver.constant.FCConst;
import com.food.chain.server.fcserver.constant.OperationType;
import com.food.chain.server.fcserver.domain.Certificate;
import com.food.chain.server.fcserver.service.CertificateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.food.chain.server.fcserver.response.ResponseBodyGenerator.generateSuccessResponceBody;

@RestController
@RequestMapping("/api/v1/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping
    public ResponseEntity<List<Certificate>> getAllCertificates() {
        return ResponseEntity.ok().body(certificateService.getAllCertificates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCertificate(@PathVariable String id) {
        return ResponseEntity.ok().body(certificateService.getCertificate(Long.valueOf(id)));
    }

    @PostMapping
    public ResponseEntity<?> addCertificate(@RequestBody Certificate certificate) {
        certificateService.createCertificate(certificate);
        return generateSuccessResponceBody(FCConst.CERTIFICATE,
                certificate.getCertificateId(), OperationType.CREATE, HttpStatus.OK, "");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCertificate(@PathVariable String id, @RequestBody Certificate certificate) {
        certificate.setCertificateId(Long.valueOf(id));
        certificateService.updateCertificate(certificate);
        return generateSuccessResponceBody(FCConst.CERTIFICATE,
                certificate.getCertificateId(), OperationType.UPDATE, HttpStatus.OK, "");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertificate(@RequestBody @PathVariable String id) {
        certificateService.deleteCertificate(Long.valueOf(id));
        return generateSuccessResponceBody(FCConst.CERTIFICATE, Long.valueOf(id),
                OperationType.DELETE, HttpStatus.OK, "");
    }
}
