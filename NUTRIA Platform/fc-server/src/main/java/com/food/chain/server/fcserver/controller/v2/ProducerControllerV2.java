package com.food.chain.server.fcserver.controller.v2;

import com.food.chain.server.fcserver.service.ProducerService;
import com.food.chain.server.fcserver.wsdto.v2.RegisterFormDataWsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/producers")
public class ProducerControllerV2 {

    private final ProducerService producerService;

    public ProducerControllerV2(ProducerService producerService) {
        this.producerService = producerService;
    }

    @GetMapping("/registerData")
    public ResponseEntity<RegisterFormDataWsDTO> getRegisterFormData() {
        return producerService.getRegisterFormData();
    }

}
