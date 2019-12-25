package org.food.chain.foodchainbackend.controller;

import org.food.chain.foodchainbackend.domain.Producer;
import org.food.chain.foodchainbackend.service.ProducerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producers")
public class ProducerController {

    private final ProducerService producerService;

    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerProducer(@RequestBody Producer producer) {
        return producerService.registerProducer(producer);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("username") final String username,
                                   @RequestParam("password") final String password) {
        return producerService.login(username, password);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader HttpHeaders httpHeaders,
                                    @RequestParam("producerId") final String producerId) {
        return producerService.logout(httpHeaders, Long.valueOf(producerId));
    }

    @GetMapping
    public ResponseEntity<List<Producer>> getAllProducers() {
        return ResponseEntity.ok().body(producerService.getAllProducers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producer> getProducer(@PathVariable String id) {
        return ResponseEntity.ok().body(producerService.getProducer(Long.valueOf(id)));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateProducer(@PathVariable String id, @RequestBody Producer producer) {
        producer.setProducerId(Long.valueOf(id));
        return producerService.updateProducer(producer);
    }

}
