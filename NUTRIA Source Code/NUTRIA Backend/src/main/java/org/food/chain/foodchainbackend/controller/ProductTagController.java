package org.food.chain.foodchainbackend.controller;

import org.food.chain.foodchainbackend.domain.ProductTag;
import org.food.chain.foodchainbackend.service.ProductTagService;
import org.food.chain.foodchainbackend.wsdto.ProductTagWsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/productTags")
public class ProductTagController {

    private final ProductTagService productTagService;

    public ProductTagController(ProductTagService productTagService) {
        this.productTagService = productTagService;
    }

    @GetMapping
    public Set<ProductTag> getAllProductTags() {
        return productTagService.getAllProductTags();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductTag> getProductTagById(@PathVariable String id) {
        return ResponseEntity.ok().body(productTagService.getProductTagById(Long.valueOf(id)));
    }

    @CrossOrigin(origins = {"http://localhost:3000", "https://itdany.github.io"})
    @GetMapping("/hash/{hash}")
    public ResponseEntity<Set<ProductTagWsDTO>> getProductTagByHash(@PathVariable String hash) {
        return ResponseEntity.ok().body(productTagService.getProductTagsByHash(hash));
    }

    @PostMapping
    public ResponseEntity<?> addProductTag(@RequestBody ProductTag productTag) {
        return productTagService.addCheckPoint(productTag);
    }

    @GetMapping("/producer/{producer_id}")
    public Set<ProductTagWsDTO> getProductTagsForProducer(@PathVariable String producer_id) {
        return productTagService.getProductTagsForProducer(Long.valueOf(producer_id));
    }

}
