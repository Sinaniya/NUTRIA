package com.food.chain.server.fcserver.controller;

import com.food.chain.server.fcserver.domain.ProductTag;
import com.food.chain.server.fcserver.service.ProductTagService;
import com.food.chain.server.fcserver.wsdto.CreateProductTagWsDTO;
import com.food.chain.server.fcserver.wsdto.ProductTagWsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/productTags")
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

    @GetMapping("/hash/{hash}")
    public ResponseEntity<ProductTagWsDTO> getProductTagByHash(@PathVariable String hash) {
        return ResponseEntity.ok().body(productTagService.getProductTagsByHash(hash));
    }

    @PostMapping
    public ResponseEntity<?> addProductTag(@RequestBody CreateProductTagWsDTO productTag) {
        return productTagService.addProductTag(productTag);
    }

    @GetMapping("/producer/{producer_id}")
    public Set<ProductTagWsDTO> getProductTagsForProducer(@PathVariable String producer_id) {
        return productTagService.getProductTagsForProducer(Long.valueOf(producer_id));
    }

}
