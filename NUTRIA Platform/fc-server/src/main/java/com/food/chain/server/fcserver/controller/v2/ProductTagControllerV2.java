package com.food.chain.server.fcserver.controller.v2;

import com.food.chain.server.fcserver.service.v2.ProductTagServiceV2;
import com.food.chain.server.fcserver.wsdto.CreateProductTagWsDTO;
import com.food.chain.server.fcserver.wsdto.v2.ProductTagChainWsDTOV2;
import com.food.chain.server.fcserver.wsdto.v2.ProductTagWsDTOV2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v2/productTags")
public class ProductTagControllerV2 {

    private final ProductTagServiceV2 productTagService;

    public ProductTagControllerV2(ProductTagServiceV2 productTagService) {
        this.productTagService = productTagService;
    }

    @GetMapping("/hash/{hash}")
    public ResponseEntity<Set<ProductTagWsDTOV2>> getProductTagByHash(@PathVariable String hash) {
        return ResponseEntity.ok().body(productTagService.getProductTagsByHashV2(hash));
    }

    @PostMapping
    public ResponseEntity<?> addProductTag(@RequestBody CreateProductTagWsDTO productTag) {
        return productTagService.addProductTag(productTag);
    }

    @GetMapping("/producer/{producer_id}")
    public Set<ProductTagChainWsDTOV2> getProductTagsForProducer(@PathVariable String producer_id) {
        return productTagService.getProductTagsForProducer(Long.valueOf(producer_id));
    }


}
